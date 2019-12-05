package com.example.panda.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.panda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String ROOT_URL = "http://34.69.211.169/api/";
    private static final String URL_SEARCH = ROOT_URL + "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final SearchView search = findViewById(R.id.searchBar);
        Button searchButton = findViewById(R.id.search);
        Button homeButton = findViewById(R.id.homeButton);

        final Bundle extras = getIntent().getExtras();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome(extras);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = search.getQuery().toString();
                searchPost(searchString, extras);
            }
        });
    }

    private void searchPost(String searchString, final Bundle extras) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new LinkedHashMap<>();
        params.put("search", searchString);

        JSONObject searchObj = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_SEARCH, searchObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jObj = response;
                            String result = jObj.getString("search_results");

                            if(result.equals("error")) {
                                throw new Exception();
                            }
                            JSONArray results = jObj.getJSONArray("search_results");

                            populateSearch(results, extras);
                        } catch(Exception e) {
                            System.out.println("Exception Thrown!");
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

    private void populateSearch(JSONArray results, Bundle extras) {
        LinearLayout layout = findViewById(R.id.searchResultsLayout);
        layout.removeAllViewsInLayout();

        int numResults = results.length();
        Button[] buttons = new Button[numResults];

        // For each user recovered from search_results we populate the scrollview with buttons.
        for(int i = 0; i < numResults; i++) {
            try {
                JSONObject user = results.getJSONObject(i);

                // Fill the Button content with user information.
                buttons[i] = createButton(user, extras);

                // Style the button programmatically
                buttons[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                Typeface typeface = ResourcesCompat.getFont(this, R.font.manjari_regular);
                buttons[i].setTypeface(typeface);
                LinearLayout.LayoutParams buttonparam = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                buttons[i].setLayoutParams(buttonparam);

                // Show it.
                layout.addView(buttons[i]);
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Exception thrown at object " + i);
            }
        }

    }

    private Button createButton(final JSONObject user, final Bundle extras) {
        Button newButton = new Button(this);
        try {
            String firstName = user.getString("first_name").toString();
            String lastName = user.getString("last_name").toString();
            String buttonText = firstName + " " + lastName;
            newButton.setText(buttonText);

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Exception thrown while creating button.");
        }

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = user.getString("username");
                    getProfile(username, extras);

                } catch(Exception e ) {
                    e.printStackTrace();
                    System.out.println("Exception thrown trying to get profile.");
                }

            }
        });
        return newButton;
    }

    private void getProfile(final String username, final Bundle extras) {
        String URL_PROFILE = ROOT_URL + "users/" + username;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_PROFILE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jObj = response;
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    SearchResultActivity.class);

                            // The response contains a user object which contains first_name,
                            // last_name, username, email, password. The other elements you can get
                            // directly from the response by element.

                            // Cache the values.
                            JSONObject user = jObj.getJSONObject("user");
                            String srfirstName = user.getString("first_name");
                            String srlastName = user.getString("last_name");
                            String srbio = jObj.getString("bio");
                            String srrating = jObj.getString("rating");
                            String srusername = user.getString("username");
                            System.out.println(srrating);

                            // Use intent to carry them over to the UserActivity
                            intent.putExtra("srfirstName", srfirstName);
                            intent.putExtra("srlastName", srlastName);
                            intent.putExtra("srbio", srbio);
                            intent.putExtra("srrating", srrating);

                            // Also send the OG user info so that we can come back home from inside
                            // the search result view.
                            String firstname = extras.getString("firstName");
                            String lastname = extras.getString("lastName");
                            String bio = extras.getString("bio");
                            String rating = extras.getString("rating");
                            String username = extras.getString("username");

                            intent.putExtra("firstName", firstname);
                            intent.putExtra("lastName", lastname);
                            intent.putExtra("bio", bio);
                            intent.putExtra("rating", rating);
                            intent.putExtra("username", username);
                            intent.putExtra("srusername", srusername);

                            startActivity(intent);
                        } catch (Exception e)  {
                            System.out.println("In getProfile, exception was thrown.");
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
