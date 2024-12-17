package com.example.loginsignupdashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText firstNameField, lastNameField, emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        firstNameField = findViewById(R.id.firstName);
        lastNameField = findViewById(R.id.lastName);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        TextView goToLogin = findViewById(R.id.goToLogin);
        Button signupButton = findViewById(R.id.signupButton);

        // Navigate to Login Activity
        goToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Handle Signup and Firestore Data Insertion
        signupButton.setOnClickListener(view -> {
            // Retrieve user input
            String firstName = firstNameField.getText().toString().trim();
            String lastName = lastNameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Validate input
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create user data map
            Map<String, Object> users = new HashMap<>();
            users.put("firstName", firstName);
            users.put("lastName", lastName);
            users.put("email", email);
            users.put("password", password);

            // Add user data to Firestore
            firestore.collection("users")
                    .add(users)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();

                            // Navigate to Dashboard Activity
                            Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Sign Up Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
