package com.example.panda.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.LinkedHashMap;
import java.util.Map;

public class CreateAvailabilityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_availability);

        final Bundle extras = getIntent().getExtras();

        final EditText dateEditText = findViewById(R.id.dateText);
        final EditText timeEditText = findViewById(R.id.timeText);
        final Button create = findViewById(R.id.createButton);
        final Button homeButton = findViewById(R.id.homeButton);
        final Button searchButton = findViewById(R.id.searchButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome(extras);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearch(extras);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeToParse = timeEditText.getText().toString();
                String dateToParse = dateEditText.getText().toString();

                String time = parseTime(timeToParse);
                String date = parseDate(dateToParse);
                String dateTime = date + "T" + time + "Z";

                String username = extras.getString("username");

                createAppointment(username, dateTime);
            }
        });
    }

    private void createAppointment(String username, String time) {

        String ROOT_URL = "http://34.69.211.169/api/";
        String URL_CREATE = ROOT_URL + "appointments/create";
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new LinkedHashMap<>();

        params.put("username", username);
        params.put("time", time);

        JSONObject userInput = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_CREATE, userInput,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jObj = response;
                            System.out.println(jObj.toString());
                        } catch (Exception e) {
                            System.out.println("Exception thrown!");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private String parseTime(String time) {
        String[] timeSplit = time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        String[] minSplit = timeSplit[1].split(" ");
        String ampm = minSplit[1];

        String actualTime;

        if(ampm.equals("PM")) {
                hour += hour%12;
        }
        if(hour < 10) {
            String stringHour = "0"+ hour;
            actualTime = stringHour + ":" + minSplit[0] + ":00";
        } else {
            actualTime = hour + ":" + minSplit[0] + ":00";
        }
        return actualTime;
    }

    private String parseDate(String date) {
        String[] dateSplit = date.split("/");
        return dateSplit[2] + "-" + dateSplit[0] + "-" + dateSplit[1];
    }

    private void goToSearch(Bundle extras) {
        String firstname = extras.getString("firstName");
        String lastname = extras.getString("lastName");
        String bio = extras.getString("bio");
        String rating = extras.getString("rating");
        String username = extras.getString("username");

        // Use Intent to send the user to the next activity.
        // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
        Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
        searchActivity.putExtra("firstName", firstname);
        searchActivity.putExtra("lastName", lastname);
        searchActivity.putExtra("bio", bio);
        searchActivity.putExtra("rating", rating);
        searchActivity.putExtra("username", username);

        // Start that activity.
        startActivity(searchActivity);
    }

    private void goHome(Bundle extras) {
        String firstname = extras.getString("firstName");
        String lastname = extras.getString("lastName");
        String bio = extras.getString("bio");
        String rating = extras.getString("rating");
        String username = extras.getString("username");

        // Use Intent to send the user to the next activity.
        // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
        Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
        userActivity.putExtra("firstName", firstname);
        userActivity.putExtra("lastName", lastname);
        userActivity.putExtra("bio", bio);
        userActivity.putExtra("rating", rating);
        userActivity.putExtra("username", username);

        // Start that activity.
        startActivity(userActivity);
    }
}
