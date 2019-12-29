package com.example.toptendownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(): starting AsyncTask ");
        setContentView(R.layout.activity_main);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate(): done ");
    }

    private static class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData ";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostExecute():  parameter is \n" + s + "\n");
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);
//            Log.d(TAG, "onPostExecute: done");


            
            
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground(): starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null){
                Log.e(TAG, "doInBackground(): error downloading ");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath){
            Log.d(TAG, "downloadXML(): path is " + urlPath);
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML(): Response Code is " + response);
                if (response != 200) Log.d(TAG, "downloadXML(): Response Code is " + response);


                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {

                    Log.d(TAG, "downloadXML(): reading..................");
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) { // or -1 from documentation
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead)); // or inputBuffer, 0, charsRead - obj to be copied, offset, count
                    }
                }
                reader.close();
                return xmlResult.toString();
            } catch (MalformedURLException e){
                Log.e(TAG, "downloadXML(): Invalid Url " +  e.getMessage());
            } catch (IOException e){
                Log.e(TAG, "downloadXML(): Input/Output exception reading data " +  e.getMessage());
            } catch (SecurityException e){
                Log.e(TAG, "downloadXML(): Security Exception. Needs permission? " +  e.getMessage());
            }

            return null;
        }
    }
}
