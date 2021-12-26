package com.example.hackathon_ai_v1;

import android.app.DownloadManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

public class ApiCaller
{
    private String inputWord;
    private final Context context;
    private String intervalURL;
    private final String MAIN_URL;

    public ApiCaller(String inputWord, Context context)
    {
        this.inputWord = inputWord;
        this.context = context;
        intervalURL = "https://internal.nutq.uz/api/v1/cabinet/synthesize/?t=" + inputWord;
        MAIN_URL = intervalURL;
    }

    public void startEngine()
    {
        new ApiEngine().execute(MAIN_URL);
    }

    //API engine
    class ApiEngine extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... strings)
        {
            try
            {
                MediaPlayer audioPlayer = new MediaPlayer();
                audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                audioPlayer.setDataSource(context, Uri.parse(MAIN_URL));
                audioPlayer.prepare();
                audioPlayer.start();
                //Log.d("MyLog","NiceTry");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
