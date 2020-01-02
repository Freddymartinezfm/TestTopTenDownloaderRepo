package com.example.toptendownloader;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications  {
    private static final String TAG = "ParseApplications";

    private ArrayList<FeedEntry> applications;

    public ParseApplications() { this.applications = new ArrayList<>();}

    public ArrayList<FeedEntry> getApplications(){ return applications;}

    public boolean parse(String xmlData){
        Log.d(TAG, "parse: ");
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            Log.d(TAG, "parse: ======================");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            Log.d(TAG, "parse: eventType " +  eventType);

            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        //Log.d(TAG, "Starting tag for tag name: " +  tagName);
                        if ("entry".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;
                        
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                        
                    case XmlPullParser.END_TAG:
                        //Log.d(TAG, "parse: Ending tag for " +  tagName);
                        if (inEntry){
                            if ("entry".equalsIgnoreCase(tagName)){
//                                Log.d(TAG, "parse: entry added");
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)){
//                                Log.d(TAG, "parse: name set");
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)){
//                                Log.d(TAG, "parse: artist set");
                                currentRecord.setArtist(textValue);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)){
//                                Log.d(TAG, "parse: releaseDate set");
                                currentRecord.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(tagName)){
//                                Log.d(TAG, "parse: syummary set");
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(tagName)){
                                currentRecord.setImageUrl(textValue);
                            }
                        }
                    break;

                    default:
                    break;
                }
                eventType = xpp.next();
            }

            
//            for (FeedEntry app : applications) {
//                Log.d(TAG, "**********");
//                Log.d(TAG, app.toString());
//            }


        } catch (Exception e){
            status = false;
            Log.e(TAG, "parse: Xml Parse Issue " +  e.getMessage());
            e.printStackTrace();
        }

        return status;

    }

}
