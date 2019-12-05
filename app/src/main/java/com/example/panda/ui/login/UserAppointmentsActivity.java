package com.example.panda.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.panda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserAppointmentsActivity extends AppCompatActivity {

    private static final String ROOT_URL = "http://34.69.211.169/api/";
    private JSONArray appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointments);
        loadAppointments();

    }

    private void loadAppointments() {
        final Bundle extras = getIntent().getExtras();
        final String username = extras.getString("username");

        Button searchButton = findViewById(R.id.searchButton);
        Button homeButton = findViewById(R.id.homeButton);
        Button createAppointmentButton = findViewById(R.id.createAppointment);

        String URL_APPTS = ROOT_URL + "appointments/all/" + username + "/";

        System.out.println(URL_APPTS);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_APPTS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jObj = response;
                            System.out.println(jObj.toString());
                            appointments = jObj.getJSONArray("results");
                            System.out.println(appointments);
                            if(appointments != null) {
                                populateAppointments(appointments, extras);
                            }
                        } catch (Exception e)  {
                            System.out.println("In the appointments request, an exception was thrown.");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        queue.add(jsonObjectRequest);

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

        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment(extras, username);
            }
        });
    }

    private void createAppointment(Bundle extras, String username) {
        String firstname = extras.getString("firstName");
        String lastname = extras.getString("lastName");
        String bio = extras.getString("bio");
        String rating = extras.getString("rating");

        // Use Intent to send the user to the next activity.
        // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
        Intent createAvailabilityActivity = new Intent(getApplicationContext(), CreateAvailabilityActivity.class);
        createAvailabilityActivity.putExtra("firstName", firstname);
        createAvailabilityActivity.putExtra("lastName", lastname);
        createAvailabilityActivity.putExtra("bio", bio);
        createAvailabilityActivity.putExtra("rating", rating);
        createAvailabilityActivity.putExtra("username", username);

        // Start that activity.
        startActivity(createAvailabilityActivity);

    }

    private void populateAppointments(JSONArray results, Bundle extras) {
        LinearLayout layout = findViewById(R.id.appointmentsLayout);

        int numResults = results.length();
        Button[] buttons = new Button[numResults];

        // For each user recovered from search_results we populate the scrollview with buttons.
        for(int i = 0; i < numResults; i++) {
            try {
                JSONObject appointment = results.getJSONObject(i);

                // Fill the Button content with user information.
                buttons[i] = createButton(appointment, extras);

                // Style the button programmatically
                buttons[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                Typeface typeface = ResourcesCompat.getFont(this, R.font.manjari_bold);
                buttons[i].setTypeface(typeface);
                LinearLayout.LayoutParams buttonparam = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                buttonparam.gravity = Gravity.CENTER;
                buttons[i].setLayoutParams(buttonparam);

                // Show it.
                layout.addView(buttons[i]);
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Exception thrown at object " + i);
            }
        }

    }

    private Button createButton(final JSONObject appointment, final Bundle extras) {
        Button newButton = new Button(this);
        try {
            //String mainText = user.getString("appointment_string");
            final String timeString = appointment.getString("time");
            String[] dateTime = timeString.split("T");
            String date = dateTime[0];
            String[] timeSplit = dateTime[1].split("Z");
            String time = timeSplit[0];

            String actualTime = parseTime(time);
            String actualDate = parseDate(date);

            String appointee = appointment.getString("appointee");

            System.out.println(date + " " + time);

            String buttonText = appointment.getString("appointment_string");
            /*if(appointee.equals("null")) {
                buttonText = "Availability" + "\nAt " + actualTime + " on " + actualDate;
            } else {
                buttonText = "Session with " + appointee + "\nAt " + actualTime + " on " + actualDate;
            }*/
            newButton.setText(buttonText);

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Exception thrown while creating button.");
        }
        return newButton;
    }

    private String parseTime(String time) {
        String[] timeSplit = time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        String ampm;
        if(hour > 11) {
            ampm = "PM";
            if(hour > 12) {
                hour = hour%12;
            }
        } else {
            ampm = "AM";
        }
        String actualTime = hour + ":" + timeSplit[1] + " " + ampm;
        return actualTime;
    }

    private String parseDate(String date) {
        String[] dateSplit = date.split("-");
        return dateSplit[1] + "/" + dateSplit[2] + "/" + dateSplit[0];
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
