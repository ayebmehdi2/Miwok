package com.example.android.datafrominternet.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkUtils {

    final static String GITHUB_BASE_URL =
            "https://api.github.com/search/repositories";

    final static String PARAM_QUERY = "q";


    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";


    public static URL buildUrl(String githubSearchQuery) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {

        String output = "";

        if (url == null){
            return output;
        }

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                output = readStream(inputStream);
            } else { Log.e("MEHDI: ", "ERROE RESPONSE CODE: "+ urlConnection.getResponseCode()); }
        } catch (IOException e){
            Log.e("MEHDI: ", "Problem JSON", e);
        }
        finally {
            if (urlConnection != null){ urlConnection.disconnect(); }

            if (inputStream != null){
                inputStream.close();
            }
        }
        return output;


    }
    private static String readStream(InputStream in) throws IOException {

        StringBuilder out = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = bufferedReader.readLine();
        while (line != null){
            out.append(line);
            line = bufferedReader.readLine();
        }

        return out.toString();

    }

    public static class AsyncGit extends AsyncTaskLoader<String>{

        String aa;
        public AsyncGit(Context context, String a) {
            super(context);
            this.aa = a;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public String loadInBackground() {
            String out = "";
            URL url = buildUrl(aa);
            try {
                out = getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out;
        }
    }

}


