package com.example.panda.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
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

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String ROOT_URL = "http://34.69.211.169/api/";
    private static final String URL_REGISTER = ROOT_URL + "users/create/new";

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

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
                final Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Input error.");
                dialog.hide();

                boolean isLegal = true;

                username = usernameEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                firstName = firstNameEditText.getText().toString();
                lastName = lastNameEditText.getText().toString();

                TextView text = dialog.findViewById(R.id.text);

                if(username.matches("")) {
                    text.append("Username is empty");
                    isLegal = false;
                }

                if(!isValidEmail(email)) {
                    if(!isLegal) {
                        text.append("\n");
                    }
                    text.append("Not a valid e-mail");
                    isLegal = false;
                }
                if(!isValidPassword(password)) {
                    if(!isLegal) {
                        text.append("\n");
                    }
                    text.append("Not a valid password\n(At least 8 characters, One Number, One Uppercase)");
                    isLegal = false;
                }

                if(firstName.matches("")) {
                    if(!isLegal) {
                        text.append("\n");
                    }
                    text.append("First name is empty");
                    isLegal = false;
                }

                if(lastName.matches("")) {
                    if(!isLegal) {
                        text.append("\n");
                    }
                    text.append("Last name is empty");
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
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    registerUser(firstName, lastName,
                            username, email,
                            password);
                }
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    private void registerUser(String firstName, String lastName, final String username, String email, String password) {

        final String defaultBio = "Hi, I'm " + firstName + "!";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new LinkedHashMap<>();

        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("bio", defaultBio);
        System.out.println(params.toString());

        JSONObject userInput = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_REGISTER, userInput,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jObj = response;
                            String error = jObj.getString("status");

                            if (error.equals("success")) {
                                getProfile(username);
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
                        try {
                            JSONObject jObj = response;
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    UserActivity.class);

                            // The response contains a user object which contains first_name,
                            // last_name, username, email, password. The other elements you can get
                            // directly from the response by element.

                            // Cache the values.
                            JSONObject user = jObj.getJSONObject("user");
                            String firstName = user.getString("first_name");
                            String lastName = user.getString("last_name");
                            String bio = jObj.getString("bio");
                            String rating = jObj.getString("rating");

                            // Use intent to carry them over to the UserActivity
                            intent.putExtra("firstName", firstName);
                            intent.putExtra("lastName", lastName);
                            intent.putExtra("bio", bio);
                            intent.putExtra("rating", rating);

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

}
