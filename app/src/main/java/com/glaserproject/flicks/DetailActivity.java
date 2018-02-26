package com.glaserproject.flicks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.glaserproject.flicks.Utils.ConstantsClass;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        int movieID = intent.getExtras().getInt(ConstantsClass.MOVIE_ID_INTENT_EXTRA_KEY, 0);

        TextView textDetail = findViewById(R.id.textDetail);
        textDetail.setText(Integer.toString(movieID));


    }
}
