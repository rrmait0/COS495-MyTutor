package com.example.panda.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.panda.R;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

    final Bundle extras = getIntent().getExtras();
    TextView firstNameView = findViewById(R.id.srfirstName);
    TextView lastNameView = findViewById(R.id.srlastName);
    TextView ratingView = findViewById(R.id.srrating);
    TextView bioView = findViewById(R.id.srbio);
    Button searchButton = findViewById(R.id.srsearchButton);
    Button homeButton = findViewById(R.id.srhomeButton);
    Button appointmentButton = findViewById(R.id.srappointmentButton);

    String firstname = extras.getString("srfirstName");
    String lastname = extras.getString("srlastName");
    String bio = extras.getString("srbio");
    String rating = "Rating: " + extras.getString("srrating");

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

    homeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goHome(extras);
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

    private void goHome(Bundle extras) {
        String firstname = extras.getString("firstName");
        String lastname = extras.getString("lastName");
        String bio = extras.getString("bio");
        String rating = extras.getString("rating");

        // Use Intent to send the user to the next activity.
        // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
        Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
        userActivity.putExtra("firstName", firstname);
        userActivity.putExtra("lastName", lastname);
        userActivity.putExtra("bio", bio);
        userActivity.putExtra("rating", rating);

        // Start that activity.
        startActivity(userActivity);
    }
}
