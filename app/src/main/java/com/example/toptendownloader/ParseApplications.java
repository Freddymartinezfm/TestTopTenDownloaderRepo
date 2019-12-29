package com.example.toptendownloader;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications  {
    private static final String TAG = "ParseApplications";

    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications = new ArrayList<>();

    }

    public ArrayList<FeedEntry> getApplications(){ return applications;}

    public boolean parse(String xmlData){
        Log.d(TAG, "parse: ");
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
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
                        Log.d(TAG, "Starting tag for tag name: " +  tagName);
                        if ("entry".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;
                        
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                        
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " +  tagName);
                        if (inEntry){
                            if ("entry".equalsIgnoreCase(tagName)){
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(textValue)){
                                currentRecord.setName(tagName);
                            } else if ("artist".equalsIgnoreCase(textValue)){
                                currentRecord.setArtist(tagName);
                            } else if ("releaseDate".equalsIgnoreCase(textValue)){
                                currentRecord.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(textValue)){
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(textValue)){
                                currentRecord.setImageUrl(textValue);
                            }
                            
                            
                            //switch (tagName){
                                //case "entry":
                                    //applications.add(currentRecord);
                                    //break;
                                //case "name":
                                    //currentRecord.setName(textValue);
                                    //break;
                                //case "releaseDate":
                                    //currentRecord.setReleaseDate(textValue);
                                    //break;
                                //case "artist":
                                    //currentRecord.setArtist(textValue);

                                    //break;
                                //case "summary":
                                    //currentRecord.setSummary(textValue);
                                    //break;
                                //case "image":
                                    //currentRecord.setImageUrl(textValue);
                                    //break;
                            //}
                        }
                        
                    break;
                    
                    default:
                        // nothing to do 
                }
                
                 eventType = xpp.next();

            }
            
            // loop through app list
            for (FeedEntry app : applications) {
                Log.d(TAG, "**********");
                Log.d(TAG, app.toString());
            }
            
        } catch (XmlPullParserException e){
            status = false;
            Log.e(TAG, "parse: Xml Parse Issue " +  e.getMessage());
            e.printStackTrace();
        }

        return status;

    }

}
