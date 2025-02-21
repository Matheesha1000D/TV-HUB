package lk.javainstitute.tv_hub;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LogoActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email, password;
    ProgressBar progressBar;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        checkUserAuthentication();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        TextView textView = findViewById(R.id.backText1);
        textView.setOnClickListener(view -> {
            Intent i = new Intent(LogoActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        Button button = findViewById(R.id.buttonSign);
        button.setOnClickListener(view -> {
            loginUser();
            progressBar.setVisibility(View.VISIBLE);
        });

        // Initialize Biometric Authentication
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LogoActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for TV Hub")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt for biometric authentication
        biometricPrompt.authenticate(promptInfo);
    }

    private void loginUser() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if (emailText.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordText.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LogoActivity.this, StartActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserAuthentication() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "User is signed in. Getting ID Token...");
            currentUser.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    Log.d(TAG, "ID token: " + idToken);
                    Log.d(TAG, "User ID: " + currentUser.getUid());
                    Intent intent = new Intent(LogoActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Log.e(TAG, "Error getting ID token", task.getException());
                    runOnUiThread(() -> {
                        Toast.makeText(LogoActivity.this, "Authentication error.", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(LogoActivity.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            });
        } else {
            Log.d(TAG, "No user signed in. Navigating to GetStartedActivity.");
        }
    }
}