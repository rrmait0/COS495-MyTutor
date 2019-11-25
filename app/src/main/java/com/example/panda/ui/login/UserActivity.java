package com.example.panda.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
        Button homeButton = findViewById(R.id.homeButton);
        Button searchButton = findViewById(R.id.searchButton);

        String username;
        if(extras != null) {
            username = extras.getString("username");
        } else {
            username = "Couldn't get username";
        }

        System.out.println(username);
        URL_PROFILE = ROOT_URL + "users/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_PROFILE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        JSONObject jObj = response;
                        //TODO: Damn Daniel... back at it again with the strategy to store individual elements of this JSON object.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
    }
}
