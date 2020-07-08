package com.example.movie_client.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.Scanner;

public class NetworkUtils {

    private static final String BASE_URL = "http://www.omdbapi.com/";
    private static final String API_KEY = "apikey";
    private static final String T = "t";

    public static URL generateUrl(String movie_name) throws MalformedURLException {
        Uri buildUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY, "")
                .appendQueryParameter(T, movie_name)
                .build();
        URL url = null;

        try{
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput)
                return scanner.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }


    }
}
