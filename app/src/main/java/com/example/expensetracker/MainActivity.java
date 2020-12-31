package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button new_userTrans;
    Button goHome;
    Button admin_login;
    ImageView image;
    TextView logoText,forgotPassword;
    EditText password, email;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new_userTrans = findViewById(R.id.new_user);

        logoText = findViewById(R.id.sinUp);
        forgotPassword=findViewById(R.id.forgot);
        password =  findViewById(R.id.password);
        goHome = findViewById(R.id.go);

        email =  findViewById(R.id.email);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        }
        new_userTrans.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


                Intent i = new Intent(MainActivity.this, SinUp.class);

                startActivity(i);
               finish();
            }


        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = email.getText().toString().trim();

                String val2 = password.getText().toString().trim();

                if (val.isEmpty()) {
                    email.setError("Field cannot be Empty");
                    return;
                }   else {
                    email.setError(null);

                    //return;
                }


                if (val2.isEmpty()) {
                    password.setError("Field cannot be Empty");
                    return;
                }  else {
                    password.setError(null);


                }
                //firebase Authentication proccess----//
                firebaseAuth.signInWithEmailAndPassword(val, val2)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent i=new Intent(MainActivity.this,HomePage.class);
                                    startActivity(i);
                                } else {

                                    Toast.makeText(MainActivity.this,"Invalid User Recheck!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}