package com.example.panda.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.panda.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String ROOT_URL = "http://34.69.211.169/api/";
    private static final String URL_LOGIN = ROOT_URL + "users/";
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Below we store all the values the user put on the screen and store the elements from the
        // UI as objects so we can use their established member functions. We link these by their
        // reference IDs. These are defined in the XML by "android:id="@+id/<name of element>""
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signupButton = findViewById(R.id.signup);
        loadingProgressBar = findViewById(R.id.loading);

        // Listens for when the user clicks on the login button.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the loading bar
                loadingProgressBar.setVisibility(View.VISIBLE);

                // Take what the user put in the username and password textboxes and send to the
                // function loginUser.
                loginUser(emailEditText.toString(), passwordEditText.toString());
            }
        });

        //Listens for when the user clicks on the signup button.
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use Intent to send the user to the next activity.
                // Intent constructor arguments (<Your Current Activity>, <Your Next Activity Class>)
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);

                // Start that activity.
                startActivity(registerActivity);
            }
        });
    }

    private void loginUser(final String email, final String password) {
        /*String cancelRequestTag = "login";
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Register Response: " + response);
                try {
                    JSONObject jObj = response;
                    String error = jObj.getString("status");

                    if (error != "fail") {
                        String user = jObj.getJSONObject("user").getString("first_name");
                        // Launch User activity
                        Intent intent = new Intent(
                                LoginActivity.this,
                                UserActivity.class);
                        intent.putExtra("username", user);
                        startActivity(intent);
                        Log.d(TAG, "Welcome " + user);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        // Add request to queue.
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonReq, cancelRequestTag)*/
    }
}
