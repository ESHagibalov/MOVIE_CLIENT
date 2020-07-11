package com.example.movie_client;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movie_client.utils.DownloadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.movie_client.utils.NetworkUtils.generateUrl;
import static com.example.movie_client.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchBtn;
    private TextView result, rated_tv, year_tv, genre_tv, runtime_tv, director_tv, plot_tv, tv_plot_a, error_msg;
    private ImageView poster;
    private TableLayout tl_response;
    private ProgressBar loading_indicator;

    private void showResultTextView() {
        tl_response.setVisibility(View.VISIBLE);
        tv_plot_a.setVisibility(View.VISIBLE);
        plot_tv.setVisibility(View.VISIBLE);
        error_msg.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView() {
        tl_response.setVisibility(View.INVISIBLE);
        tv_plot_a.setVisibility(View.INVISIBLE);
        plot_tv.setVisibility(View.INVISIBLE);
        error_msg.setVisibility(View.VISIBLE);
    }

    private void showNothing() {
        tl_response.setVisibility(View.INVISIBLE);
        tv_plot_a.setVisibility(View.INVISIBLE);
        plot_tv.setVisibility(View.INVISIBLE);
        error_msg.setVisibility(View.INVISIBLE);
    }

    class MOVIEQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            showNothing();
            loading_indicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try{
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }
        @Override
        protected void onPostExecute(String response) {
            JSONObject jsonResponse = null;
            String title = null;
            String rated = null;
            String year = null;
            String poster_url = null;
            String runtime = null;
            String genre = null;
            String director = null;
            String plot = null;
            if(response != null) {
                try {
                    jsonResponse = new JSONObject(response);
                    title = (String) jsonResponse.get("Title");
                    rated = (String) jsonResponse.get("Rated");
                    year = (String) jsonResponse.get("Year");
                    poster_url = (String) jsonResponse.get("Poster");
                    runtime = (String) jsonResponse.get("Runtime");
                    genre = (String) jsonResponse.get("Genre");
                    director = (String) jsonResponse.get("Director");
                    plot = (String) jsonResponse.get("Plot");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new DownloadImageTask((ImageView) findViewById(R.id.poster))
                        .execute(poster_url);

                if(plot == null)
                    plot = "";
                result.setText(title);
                rated_tv.setText(rated);
                year_tv.setText(year);
                runtime_tv.setText(runtime);
                genre_tv.setText(genre);
                director_tv.setText(director);
                plot_tv.setText("        " + plot);

                showResultTextView();
            } else {
                showErrorTextView();
            }

            loading_indicator.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchBtn = findViewById(R.id.btn_search);
        result = findViewById(R.id.tv_result);
        poster = findViewById(R.id.poster);
        rated_tv = findViewById(R.id.tv_rated);
        year_tv = findViewById(R.id.tv_year);
        genre_tv = findViewById(R.id.tv_genre);
        runtime_tv = findViewById(R.id.tv_runtime);
        director_tv = findViewById(R.id.tv_director);
        plot_tv = findViewById(R.id.tv_plot);
        tl_response = findViewById(R.id.tl_pesponse);
        tv_plot_a = findViewById(R.id.tv_plot_a);
        loading_indicator = findViewById(R.id.pb_loading_indicator);
        error_msg = findViewById(R.id.tv_error_message);

        tl_response.setVisibility(View.INVISIBLE);
        tv_plot_a.setVisibility(View.INVISIBLE);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL generatedUrl = null;
                try {
                    generatedUrl = generateUrl(searchField.getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                 new MOVIEQueryTask().execute(generatedUrl);
            }
        });
    }
}