package com.example.panda.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String ROOT_URL = "http://34.69.211.169/api/";
    private static final String URL_LOGIN = ROOT_URL + "users/login/auth";
    private EditText usernamelEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private ProgressBar loadingProgressBar;
    private String username;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Below we store all the values the user put on the screen and store the elements from the
        // UI as objects so we can use their established member functions. We link these by their
        // reference IDs. These are defined in the XML by "android:id="@+id/<name of element>""
        usernamelEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signupButton = findViewById(R.id.signup);
        loadingProgressBar = findViewById(R.id.loading);

        // Listens for when the user clicks on the login button.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Input error.");
                dialog.hide();

                username = usernamelEditText.getText().toString();
                password = passwordEditText.getText().toString();

                boolean isLegal = true;

                TextView text = dialog.findViewById(R.id.text);

                if(username.matches("")) {
                    text.append("Username is empty");
                    isLegal = false;
                }

                if(!isValidPassword(password)) {
                    if(!isLegal) {
                        text.append("\n");
                    }
                    text.append("Not a valid password\n(At least 8 characters, One Number, One Uppercase)");
                    isLegal = false;
                }

                if(!isLegal) {
                    Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                    dialog.show();
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }

                if(isLegal) {
                    // Show the loading bar
                    loadingProgressBar.setVisibility(View.VISIBLE);

                    // Take what the user put in the username and password textboxes and send to the
                    // function loginUser.
                    loginUser(username, password);
                }
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

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$";

        Pattern pattern = Pattern.compile(passwordRegex);
        if(password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }

    private void loginUser(final String username, final String password) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new LinkedHashMap<>();

        System.out.println(username + " " + password);

        params.put("username", username);
        params.put("password", password);

        JSONObject userInput = new JSONObject(params);

        System.out.println(userInput.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_LOGIN, userInput,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jObj = response;
                            String error = jObj.getString("status");
                            System.out.println(error);

                            if (error.equals("success")) {
                                // Launch User activity
                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        UserActivity.class);
                                getProfile(username);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            } else {

                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
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

    private void getProfile(final String username) {
        String URL_PROFILE = ROOT_URL + "users/" + username;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_PROFILE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jObj = response;


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        queue.add(jsonObjectRequest);
    }
}
