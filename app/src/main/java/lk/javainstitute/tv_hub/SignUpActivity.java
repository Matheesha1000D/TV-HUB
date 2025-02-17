package lk.javainstitute.tv_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import lk.javainstitute.tv_hub.models.UserModel;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    Button signUp;
    EditText name, email, password, mobile;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        signUp = findViewById(R.id.buttonSign);
        name = findViewById(R.id.name);
        mobile= findViewById(R.id.mobilesignup);
        email = findViewById(R.id.emailsignup);
        password = findViewById(R.id.password);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,LogoActivity.class);
                startActivity(i);
            }
        });

        TextView textView = findViewById(R.id.backTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,LogoActivity.class);
                startActivity(i);
            }
        });
    }

    private void createUser() {
    String userEmail = email.getText().toString();
    String userPassword = password.getText().toString();
    String userName = name.getText().toString();
    String userMobile = mobile.getText().toString();


        if (userName.isEmpty()) {
        Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        return;
        }

        if (userMobile.isEmpty()) {
            Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userMobile.length() != 10) {
            Toast.makeText(this, "Mobile number must be exactly 10 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userEmail.isEmpty()) {
        Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        return;
        }

        if (userPassword.isEmpty()) {
        Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        return;
        }

        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        UserModel userModel = new UserModel(userName, userMobile, userEmail, userPassword);
                        String id = task.getResult().getUser().getUid();
                        database.getReference().child("Users").child(id).setValue(userModel);

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(SignUpActivity.this, "User registration successfully.", Toast.LENGTH_SHORT).show();
                        //clear fields
                        name.setText("");
                        mobile.setText("");
                        email.setText("");
                        password.setText("");
                        Intent i = new Intent(SignUpActivity.this, LogoActivity.class);
                        startActivity(i);

                    } else {
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(SignUpActivity.this, "Unsuccessfully.", Toast.LENGTH_SHORT).show();
                    }
                }
            } );

    }

}