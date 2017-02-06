package com.example.mansisaini.kamcordvideoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mansisaini.kamcordvideoapp.ShotCardDataHolder.ShotCardData;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VideoHandler extends BaseAdapter
{
    List<ShotCardDataHolder> list = new ArrayList<>();
    Context c;

    public VideoHandler(Context context) {
        c = context;
    }

    public void add(ShotCardDataHolder object) {
        list.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final VideoAdapter videoadapter;

        //if the row is empty:
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.video_layout, parent, false);
            videoadapter = new VideoAdapter();

            //assign videoview to proper id:
            videoadapter.thumb1 = (ImageView) row.findViewById(R.id.thumb1);
            videoadapter.thumb2 = (ImageView) row.findViewById(R.id.thumb2);
            videoadapter.thumb3 = (ImageView) row.findViewById(R.id.thumb3);

            //assign videoview to proper id:
            videoadapter.video1 = (VideoView) row.findViewById(R.id.videoview1);
            videoadapter.video2 = (VideoView) row.findViewById(R.id.videoview2);
            videoadapter.video3 = (VideoView) row.findViewById(R.id.videoview3);

            //assign textview to id:
            videoadapter.text1 = (TextView)row.findViewById(R.id.heartCount1);
            videoadapter.text2 = (TextView)row.findViewById(R.id.heartCount2);
            videoadapter.text3 = (TextView)row.findViewById(R.id.heartCount3);

            row.setTag(videoadapter);

        } else {

            videoadapter = (VideoAdapter) row.getTag();

        }

        //assign data to the video and image views:

        //FOR THE FIRST ONE:
        ShotCardDataHolder datas = list.get(position);
        ShotCardData shotCardData = datas.datas.get(0);
        MediaController controller1 = new MediaController(c);
        controller1.setAnchorView(videoadapter.video1);
        controller1.setMediaPlayer(videoadapter.video1);
        Uri uri1 = Uri.parse(shotCardData.mp4Link);
        videoadapter.video1.setMediaController(controller1);
        videoadapter.video1.setVideoURI(uri1);
        videoadapter.video1.requestFocus();
        videoadapter.thumb1.setTag(shotCardData.shotThumbnail);
        videoadapter.thumb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoadapter.video1.start();
                videoadapter.thumb1.setVisibility(View.GONE);
            }
        });
        new DownloadImageTask(videoadapter.thumb1, shotCardData.shotThumbnail).execute(shotCardData.shotThumbnail);

        videoadapter.text1.setText(String.valueOf("Hearts: " + shotCardData.heartCount));

        shotCardData = datas.datas.get(1);

        //FOR THE SECOND ONE:
        MediaController controller2 = new MediaController(c);
        controller2.setAnchorView(videoadapter.video2);
        controller2.setMediaPlayer(videoadapter.video2);
        Uri uri2 = Uri.parse(shotCardData.mp4Link);
        videoadapter.video2.setMediaController(controller2);
        videoadapter.video2.setVideoURI(uri2);
        videoadapter.video2.requestFocus();
        videoadapter.thumb2.setTag(shotCardData.shotThumbnail);
        videoadapter.thumb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoadapter.video2.start();
                videoadapter.thumb2.setVisibility(View.GONE);
            }
        });

        new DownloadImageTask(videoadapter.thumb2, shotCardData.shotThumbnail).execute(shotCardData.shotThumbnail);

        videoadapter.text2.setText(String.valueOf("Hearts: " + shotCardData.heartCount));

        shotCardData = datas.datas.get(2);

        //FOR THE THIRD ONE:
        MediaController controller3 = new MediaController(c);
        controller3.setAnchorView(videoadapter.video3);
        controller3.setMediaPlayer(videoadapter.video3);
        Uri uri3 = Uri.parse(shotCardData.mp4Link);
        videoadapter.video3.setMediaController(controller3);
        videoadapter.video3.setVideoURI(uri3);
        videoadapter.video3.requestFocus();
        videoadapter.thumb3.setTag(shotCardData.shotThumbnail);
        videoadapter.thumb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoadapter.video3.start();
                videoadapter.thumb3.setVisibility(View.GONE);
            }
        });

        new DownloadImageTask(videoadapter.thumb3, shotCardData.shotThumbnail).execute(shotCardData.shotThumbnail);


        videoadapter.text3.setText(String.valueOf("Hearts: " + shotCardData.heartCount));

        return row;
    }

    public class VideoAdapter {

        ImageView thumb1, thumb2, thumb3;
        VideoView video1, video2, video3;
        TextView text1, text2, text3;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String tag;

        public DownloadImageTask(ImageView bmImage, String tag) {
            this.tag = tag;
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (tag == null || !tag.equals(bmImage.getTag())) {
                return;
            }
            bmImage.setImageBitmap(result);
        }
    }
}


