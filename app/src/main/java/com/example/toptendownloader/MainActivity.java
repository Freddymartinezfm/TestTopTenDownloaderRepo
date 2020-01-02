package com.example.toptendownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    private ListView listApps;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;
    public static final String STATE_LIMIT = "STATE_LIMIT";
    public static final String STATE_URL = "STATE_URL";
    public String cachedUrl = "invalid";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = findViewById(R.id.xmlListView);
        if (savedInstanceState == null){
            downloadUrl(String.format(feedUrl, feedLimit));
        } else {
            String urltest = String.format(savedInstanceState.getString(STATE_URL), savedInstanceState.getInt(STATE_LIMIT));
            Log.d(TAG, "onCreate: saved url " + urltest);
            downloadUrl(urltest);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.feeds_menu, menu);
        if (feedLimit == 10) {
            menu.findItem(R.id.mnu10).setChecked(true);
        } else {
            menu.findItem(R.id.mnu25).setChecked(true);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: saving " + feedUrl);
        Log.d(TAG, "onSaveInstanceState: saving " + feedLimit);
        outState.putString(STATE_URL, feedUrl);
        outState.putInt(STATE_LIMIT, feedLimit);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        feedLimit =  savedInstanceState.getInt(STATE_LIMIT);
        feedUrl = savedInstanceState.getString(STATE_URL);

        Log.d(TAG, "onRestoreInstanceState: restoring " + savedInstanceState.getInt(STATE_LIMIT));
        Log.d(TAG, "onRestoreInstanceState: restoring " + savedInstanceState.getString(STATE_URL));
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.mnuFree:
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
            break;

            case R.id.mnuPaid:
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
            break;

            case R.id.mnuSongs:
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
            break;

            case R.id.mnu10: case R.id.mnu25:
                if (!item.isChecked()){
                    item.setChecked(true);
                    feedLimit = 35 - feedLimit;
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " setting feedlimit to " + feedLimit);
                } else { // item already checked
                    Log.d(TAG, "onOptionsItemSelected: " +  item.getTitle() + " feedlimit not changed");
                }
                break;

            case R.id.mnuRefresh:
//                Log.d(TAG, "onOptionsItemSelected: refreshing, feedlimit is " + feedLimit);
//                Log.d(TAG, "onOptionsItemSelected: refreshing, feedurl is " + feedUrl);
                //downloadUrl(String.format(feedUrl, feedLimit));
                cachedUrl = "invalid";
//                cachedUrl = true;
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        downloadUrl(String.format(feedUrl, feedLimit));
        return true;
    }

    private void downloadUrl(String feedUrl){
        if (!feedUrl.equalsIgnoreCase(cachedUrl)){ // not "invalid" and a new url
            Log.d(TAG, "downloadUrl(): starting AsyncTask ");
            DownloadData downloadData = new DownloadData();
            downloadData.execute(feedUrl);
            cachedUrl = feedUrl;
            Log.d(TAG, "downloadUrl(): done url is " + feedUrl);
        } else {
            Log.d(TAG, "downloadUrl: URL not invalid");

        }
//        if (!cachedUrl){
//            Log.d(TAG, "downloadUrl(): starting AsyncTask ");
//            DownloadData downloadData = new DownloadData();
//            downloadData.execute(feedUrl);
////            cachedUrl = true;
//            Log.d(TAG, "downloadUrl(): done url is " + feedUrl);
//        } else {
//            Log.d(TAG, "downloadUrl: URL not changed");
//
//        }
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData ";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, "onPostExecute():  parameter is \n" + s + "\n");
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);
            FeedAdapter adapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseApplications.getApplications());
            listApps.setAdapter(adapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground(): starts with " + strings[0]);
            String rssFeed= "";
                rssFeed = downloadXML(strings[0]);
            if (rssFeed == null){
                //Log.e(TAG, "doInBackground(): error downloading ");
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
                    //Log.d(TAG, "downloadXML(): reading..................");
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
