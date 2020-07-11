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

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String BASE_URL = "https://www.omdbapi.com/";
    private static final String API_KEY = "apikey";
    private static final String T = "t";

    public static URL generateUrl(String movie_name) throws MalformedURLException {
        Uri buildUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY, "Place your key here")
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

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput)
                return scanner.next();
            else
                return null;
        } catch (UnknownHostException e) {
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
