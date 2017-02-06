package com.example.mansisaini.kamcordvideoapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity
{
    String heartCount = null;
    String thumbnail = null;
    String link = null;
    ListView videoListView;
    VideoHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new VideoHandler(this);
        videoListView = (ListView)findViewById(R.id.VideoListView);
        videoListView.setAdapter(handler);
        new LongRunningGetIO().execute();
    }

    public class LongRunningGetIO extends AsyncTask<Void, Void, String> {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();

            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);

                if (n > 0) out.append(new String(b, 0, n));
            }

            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            //Gets the data from the Rest Api using correct headers:
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new
                    HttpGet("https://api.kamcord.com/v1/feed/set/featuredShots?count=20");
            String text = null;
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("accept-language", "en");
            httpGet.setHeader("device-token", "abc123");
            httpGet.setHeader("client-name", "android");

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);

                HttpEntity entity = response.getEntity();

                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }

        //the string thats being passed in is the data from the api:
        protected void onPostExecute(String results) {
            int count = 0;
            try {
                //captures API data into a JSON object:
                JSONObject data = new JSONObject(results);

                //gets groups json array object:
                JSONArray groupArray = data.getJSONArray("groups");

                for (int i = 0; i < groupArray.length(); i++)
                {
                    //gets cards json array:
                    JSONArray cardsArray = new JSONArray(groupArray.getJSONObject(i).getString("cards"));

                    ShotCardDataHolder row = new ShotCardDataHolder();

                    while (count < cardsArray.length())
                    {
                        JSONObject shotCardData = new JSONObject(cardsArray.getJSONObject(count).getString("shotCardData"));
                        JSONObject play = (shotCardData.optJSONObject("play"));
                        JSONObject shotThumbnail = (shotCardData.optJSONObject("shotThumbnail"));
                        heartCount = shotCardData.optString("heartCount");
                        link = play.optString("mp4");
                        thumbnail = shotThumbnail.optString("medium");

                        ShotCardDataHolder.ShotCardData cardData = new ShotCardDataHolder.ShotCardData();
                        cardData.mp4Link = link;
                        cardData.heartCount = heartCount;
                        cardData.shotThumbnail = thumbnail;

                        row.addData(cardData);
                        count++;
                        if (count % 3 == 0) {
                            handler.add(row);
                            row = new ShotCardDataHolder();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}










