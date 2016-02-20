package com.example.lukasz.androidnumbersapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lukasz on 06.02.2016.
 */
public class SecondActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        imageView = (ImageView) findViewById(R.id.imageViewSecond_id);
        textView = (TextView) findViewById(R.id.textView_id);


        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("json"));
            Picasso.with(this).load(jsonObject.getString("image")).into(imageView);
            textView.setText(jsonObject.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
