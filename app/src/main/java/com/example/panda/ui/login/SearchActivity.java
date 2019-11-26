package com.example.panda.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.panda.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button searchButton = findViewById(R.id.search);
        Button homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use Intent to send the user to the next activity.
                // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
                Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);

                // Start that activity.
                startActivity(userActivity);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: POST search JSON Object
                //TODO: Handle response
                //TODO: Populate search
            }
        });
    }
}
