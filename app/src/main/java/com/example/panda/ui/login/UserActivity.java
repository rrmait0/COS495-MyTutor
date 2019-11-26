package com.example.panda.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.panda.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {

    private static final String ROOT_URL = "http://34.69.211.169/api/";
    private String URL_PROFILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle extras = getIntent().getExtras();
        TextView firstNameView = findViewById(R.id.firstName);
        TextView lastNameView = findViewById(R.id.lastName);
        TextView bioView = findViewById(R.id.bio);
        Button searchButton = findViewById(R.id.searchButton);
        Button appointmentButton = findViewById(R.id.appointmentButton);

        String username;
        if(extras != null) {
            username = extras.getString("username");
        } else {
            username = "Couldn't get username";
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use Intent to send the user to the next activity.
                // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);

                // Start that activity.
                startActivity(searchActivity);
            }
        });

        /*appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use Intent to send the user to the next activity.
                // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
                Intent appointmentActivity = new Intent(getApplicationContext(), AppointmentActivity.class);

                // Start that activity.
                startActivity(appointmentActivity);
            }
        });*/
    }
}
