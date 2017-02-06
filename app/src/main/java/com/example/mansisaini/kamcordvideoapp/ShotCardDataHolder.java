package com.example.mansisaini.kamcordvideoapp;

import java.util.ArrayList;
import java.util.List;

public class ShotCardDataHolder
{
    List<ShotCardData> datas;

    public ShotCardDataHolder()
    {
        datas = new ArrayList<>();
    }

    public void addData(ShotCardData data) {
        datas.add(data);
    }

    static class ShotCardData {
        //TODO you can create a constructor and getters and setters for these if u want
        public String shotThumbnail;
        public String mp4Link;
        public String heartCount;
        //getters and setters:


    }
}