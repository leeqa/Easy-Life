package com.example.easyapplication.LogIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easyapplication.R;
import com.example.easyapplication.SignUp.UserSignUp;
import com.example.easyapplication.Main.Home.MainActivity;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {
    EditText et1;
    EditText et2;
    Button bt5;
    Button bt6;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    SharedPreferencesGetSet sharedPreferencesGetSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et1 = findViewById(R.id.uemail);
        et2 = findViewById(R.id.upass);
        bt5 = findViewById(R.id.b5);
        bt6 = findViewById(R.id.b6);
        sharedPreferencesGetSet = new SharedPreferencesGetSet();
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ho = new Intent(UserLogin.this, UserSignUp.class);
                startActivity(Ho);

            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
            }
        });

    }

    private void CheckValidation() {
        String Email = et1.getText().toString().trim();
        String Password = et2.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            et1.setError("Please enter Email");
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(Email).matches())) {
            et1.setError("Please enter valid Email");

        } else if (TextUtils.isEmpty(Password)) {
            et2.setError("Please enter Password");

        } else {
            PerformAuthentication(Email, Password);
        }

    }

    private void PerformAuthentication(String email, String password) {
        progressDialog.setMessage("Please wait ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sharedPreferencesGetSet.setUserIDSP(auth.getUid(), getApplicationContext());
                            Intent Home = new Intent(UserLogin.this, MainActivity.class);
                            startActivity(Home);
                            finish();

                        } else {
                            progressDialog.dismiss();
                            et1.setText(null);
                            et2.setText(null);
                            Toast.makeText(UserLogin.this, "Not Authentic", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}