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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final Bundle extras = getIntent().getExtras();
        TextView firstNameView = findViewById(R.id.firstName);
        TextView lastNameView = findViewById(R.id.lastName);
        TextView ratingView = findViewById(R.id.rating);
        TextView bioView = findViewById(R.id.bio);
        Button searchButton = findViewById(R.id.searchButton);
        Button appointmentButton = findViewById(R.id.appointmentButton);

        String firstname = extras.getString("firstName");
        String lastname = extras.getString("lastName");
        String bio = extras.getString("bio");
        String rating = "Rating: " + extras.getString("rating");

        firstNameView.setText(firstname);
        lastNameView.setText(lastname);
        ratingView.setText(rating);
        bioView.setText(bio);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goToSearch(extras);
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

    private void goToSearch(Bundle extras) {
        String firstname = extras.getString("firstName");
        String lastname = extras.getString("lastName");
        String bio = extras.getString("bio");
        String rating = extras.getString("rating");

        // Use Intent to send the user to the next activity.
        // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
        Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
        searchActivity.putExtra("firstName", firstname);
        searchActivity.putExtra("lastName", lastname);
        searchActivity.putExtra("bio", bio);
        searchActivity.putExtra("rating", rating);

        // Start that activity.
        startActivity(searchActivity);
    }
}
