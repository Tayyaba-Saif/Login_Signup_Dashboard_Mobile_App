package com.example.loginsignupdashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Link UI elements
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        TextView signupText = findViewById(R.id.signupText);
        Button loginButton = findViewById(R.id.loginButton);

        // Navigate to Signup Activity
        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Handle Login
        loginButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Query Firestore for user credentials
            firestore.collection("users")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Login successful
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                                // Navigate to Dashboard Activity
                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Invalid credentials
                                Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Query failed
                            Toast.makeText(getApplicationContext(), "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
