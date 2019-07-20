package com.example.whether;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    public class Download_Weather extends AsyncTask<String, Void, String>
    {
        String result = "";
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int i = reader.read();
                while(i != -1)
                {
                    char c = (char)i;
                    result += c;
                    i = reader.read();
                }
                }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject data = new JSONObject(result);
                String Info = data.getString("weather");
                JSONArray arr = new JSONArray(Info);
                String string = null;
                Log.i("end", "end");
                for(int i = 0; i < arr.length(); i++)
                {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    string= jsonObject.getString("description");
                }
                String temp = data.getString("main");

                TextView textView = (TextView)findViewById(R.id.Weather);

                JSONObject temp1 = new JSONObject(temp);
                double tem = Double.parseDouble(temp1.getString("temp"))-273.15;
                textView.append(string + "\n\n" + "" + (double)(int)tem + " C");
                   // InputMethodManager mgr = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    //mgr.hideSoftInputFromWindow()
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void findweather(View view)
    {
        EditText whether = (EditText)findViewById(R.id.City);
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(whether.getWindowToken(), 0);
        String city = whether.getText().toString();
        TextView textView = (TextView)findViewById(R.id.Weather);
        textView.setText(city +"\n\n");
        whether.setText("");
        Download_Weather download_weather = new Download_Weather();
        download_weather.execute("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=2faf2e6f27ab69a4388bc0eb9a932ce9");
    }
}
