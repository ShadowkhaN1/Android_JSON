package com.example.lukasz.androidnumbersapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class MainActivity extends AppCompatActivity {


    private TableLayout tableLayout;
    private boolean isTablet;
    private int isPortraitOrientation = 1;
    private String urlJSON = "http://dev.tapptic.com/test/json.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tableLayout = (TableLayout) findViewById(R.id.tableLayout1);


        setOrientation(getResources().getConfiguration().orientation);


        setJSONProcess(urlJSON);


    }


    public void setIsTablet(boolean isTablet) {
        this.isTablet = isTablet;
    }

    public void setOrientation(int orientation) {

        isPortraitOrientation = orientation;
    }

    public void setJSONProcess(String url) {

        new JSONProcess(this).execute(url);
    }

    public void setNewIntent(JSONObject jsonObject) {
        Intent intent = new Intent("com.example.lukasz.androidnumbersapplication.SecondActivity");

        intent.putExtra("json", jsonObject.toString());

        startActivity(intent);
    }

    public void showTextAndImage(JSONArray jsonArray) {


        for (int i = 0; i <= jsonArray.length() - 1; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // create text and image in table
            for (int j = 0; j <= 1; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setPadding(5, 5, 5, 5);
                tv.setTextColor(Color.BLACK);

                ImageView imageViewJSON = new ImageView(this);
                imageViewJSON.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                imageViewJSON.setPadding(5, 5, 5, 5);
                imageViewJSON.setBackgroundColor(Color.TRANSPARENT);


                try {
                    if (j == 0) {
                        Picasso.with(this).load(jsonArray.getJSONObject(i).getString("image")).into(imageViewJSON);

                    } else
                        tv.setText(jsonArray.getJSONObject(i).getString("name"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                row.addView(imageViewJSON);
                row.addView(tv);
            }

            tableLayout.addView(row);

        }
        onClickJsonData();
    }


    public void onClickJsonData() {

        final String url = "http://dev.tapptic.com/test/json.php?name=";


        for (int i = 0; i < tableLayout.getChildCount(); i++) {

            tableLayout.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                private int count;

                public View.OnTouchListener setCount(int i) {
                    this.count = i;
                    return this;
                }

                @Override
                public boolean onTouch(View v, MotionEvent event) {


                    TableRow tableRow = (TableRow) tableLayout.getChildAt(count);
                    TextView textView = (TextView) tableRow.getChildAt(3);


                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {

                        tableRow.setBackgroundColor(Color.TRANSPARENT);
                        textView.setTextColor(Color.BLACK);

                    } else {

                        tableRow.setBackgroundColor(Color.BLUE);
                        textView.setTextColor(Color.WHITE);
                    }

                    return false;
                }
            }.setCount(i));


            tableLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {

                private int count;

                private View.OnClickListener init(int i) {
                    count = i;
                    return this;
                }

                @Override
                public void onClick(View v) {


                    TableRow tableRow = (TableRow) tableLayout.getChildAt(count);

                    //get name json which is clicked
                    TextView textView = (TextView) tableRow.getChildAt(3);


                    setJSONProcess(url + textView.getText().toString());
                }


            }.init(i));
        }

    }

    private class JSONProcess extends AsyncTask<String, Void, String> {

        private JSONArray jsonArray;
        private JSONObject jsonObject;
        private MainActivity mainActivity;

        public JSONProcess(MainActivity mainActivity) {

            this.mainActivity = mainActivity;
            jsonObject = new JSONObject();

        }


        protected String doInBackground(String... strings) {
            String dataFromUrl;
            String urlString = strings[0];

            HTTPData httpData = new HTTPData();
            dataFromUrl = httpData.GetHTTPData(urlString);


            return dataFromUrl;
        }

        protected void onPostExecute(String dataFromUrl) {


            if (dataFromUrl != null) {
                try {

                    Object object = new JSONTokener(dataFromUrl).nextValue();

                    if (object instanceof JSONArray) {
                        jsonArray = new JSONArray(dataFromUrl);
                        mainActivity.showTextAndImage(jsonArray);

                    } else if (object instanceof JSONObject) {

                        jsonObject = new JSONObject(dataFromUrl);
                        setNewIntent(jsonObject);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

