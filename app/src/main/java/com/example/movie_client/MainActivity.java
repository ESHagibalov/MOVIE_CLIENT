package com.example.movie_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.movie_client.utils.NetworkUtils.generateUrl;
import static com.example.movie_client.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchBtn;
    private TextView result;


    class MOVIEQueryTask extends AsyncTask<URL, Void, String> {

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
            result.setText(response);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchBtn = findViewById(R.id.btn_search);
        result = findViewById(R.id.tv_result);

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