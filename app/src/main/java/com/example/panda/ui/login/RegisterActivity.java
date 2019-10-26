package com.example.panda.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.panda.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String ROOT_URL = "http://34.69.211.169/api/";
    private static final String URL_REGISTER = ROOT_URL + "users/create/new";
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText schoolEditText;
    private Button signUpButton;
    private ProgressBar loadingProgressBar;
    private Spinner yearInSchool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Below we store all the values the user put on the screen and store the elements from the
        // UI as objects so we can use their established member functions. We link these by their
        // reference IDs. These are defined in the XML by "android:id="@+id/<name of element>""
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        schoolEditText = findViewById(R.id.school);
        yearInSchool = findViewById(R.id.yearInSchool);
        signUpButton = findViewById(R.id.signup);
        loadingProgressBar = findViewById(R.id.loading);


        // Here we create the spinner on the screen and give it the string array of classifications
        // from res/values/strings.xml to display upon clicking.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classifications, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearInSchool.setAdapter(adapter);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerUser(firstNameEditText.toString(), lastNameEditText.toString(),
                        usernameEditText.toString(), emailEditText.toString(),
                        passwordEditText.toString());
            }
        });
    }



    private void registerUser(final String firstName, final String lastName, final String username, final String email, final String password) {
        /*
        final String defaultBio = "Hi, I'm " + firstName + "!";
        String cancelRequestTag = "register";

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<JSONObject>() {
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
                                RegisterActivity.this,
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                params.put("bio", defaultBio);
                return params;
            }
        };
        // Add request to queue.
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonReq, cancelRequestTag);*/
    }

    private void showRegistrationFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
