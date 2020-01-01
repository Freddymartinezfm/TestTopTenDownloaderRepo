package com.example.toptendownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = findViewById(R.id.xmlListView);
        //TODO add xmlListView to layout xml and list_item
        
        downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // TODO add feeds_menu item in layout
        // TODO create menu folder
        // TODO create feeds_menu
        this.getMenuInflater().inflate(R.menu.feeds_menu, menu);
        return true;
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // TODO create free, paid, and songs feeds_menu items in xml, mnuFree, mnuPaid, mnuSongs
        int id = item.getItemId();
        String feedUrl;

        switch (id){
            case R.id.mnuFree:
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml";
            break;

            case R.id.mnuPaid:
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml";
            break;

            case R.id.mnuSongs:
            feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml";
            break;

            default:

            return super.onOptionsItemSelected(item);

        }
        downloadUrl(feedUrl);
        return true;


    }
    
    private void downloadUrl(String feedUrl){
        Log.d(TAG, "downloadUrl(): starting AsyncTask ");
        DownloadData downloadData = new DownloadData();
        downloadData.execute(feedUrl);
        Log.d(TAG, "downloadUrl(): done ");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData ";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute():  parameter is \n" + s + "\n");
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);

            FeedAdapter adapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseApplications.getApplications());
            listApps.setAdapter(adapter);
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
