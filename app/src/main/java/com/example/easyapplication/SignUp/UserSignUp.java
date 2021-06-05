package com.example.easyapplication.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easyapplication.LogIn.UserLogin;
import com.example.easyapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {
    EditText et1,et2,et3,et4,et5;
    Button bt5;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        et1=findViewById(R.id.uName);
        et2=findViewById(R.id.uemail);
        et3=findViewById(R.id.upass);
        et4=findViewById(R.id.ucpass);
        et5=findViewById(R.id.ucontact);
        bt5=findViewById(R.id.b5);
        auth=FirebaseAuth.getInstance();

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();

            }
        });
    }

    private void Validation() {
        String Name  = et1.getText().toString().trim();
        String Email= et2.getText().toString().trim();
        String Password = et3.getText().toString().trim();
        String CnfPasswrd = et4.getText().toString().trim();
        String Phone = et5.getText().toString().trim();

        if (TextUtils.isEmpty(Name)) {
            et1.setError("Please enter Name");
        }

        else if (TextUtils.isEmpty(Email)){
            et2.setError("Please enter Email");
        }
        else if (!(Patterns.EMAIL_ADDRESS.matcher(Email).matches())){
            et2.setError("Please enter valid Email");

        }
        else if (TextUtils.isEmpty(Password)){
            et3.setError("Please enter Password");

        }

        else if (TextUtils.isEmpty(CnfPasswrd)){
            et4.setError("Please enter Correct Password");

        }
        else if (TextUtils.isEmpty(Phone)){
            et5.setError("Please enter Phone number");

        }
        else if (!(Password.equals(CnfPasswrd))){
            Toast.makeText(this,"Password Not Match",Toast.LENGTH_SHORT).show();
        }
        else{
            CreateUser(Name,Email,Password,CnfPasswrd,Phone);

        }

    }

    private void CreateUser( final String name,final String email,final String password, String cnfPasswrd,final String phone) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(UserSignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserSignUp.this, "User Created", Toast.LENGTH_SHORT).show();
                    InsertDatabase(name,email,password,phone);
                }
                else
                {
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("fail",""+e);
                        }
                    });
                    Toast.makeText(UserSignUp.this, "User Not Created", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void InsertDatabase(String name, String email, String password, String phone) {
        UserModel xyz= new UserModel(name,email,password,phone);
        FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid()).child("Info").setValue(xyz).addOnCompleteListener(UserSignUp.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserSignUp.this, "User data Stored", Toast.LENGTH_SHORT).show();
                    Intent Home= new Intent(UserSignUp.this, UserLogin.class);
                    startActivity(Home);
                    finish();

                }
                else
                {
                    Toast.makeText(UserSignUp.this, "User data Not Created", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}