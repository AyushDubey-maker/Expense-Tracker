package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SinUp extends AppCompatActivity {
    EditText regName,  regEmail, regConfirmPassword, regPassword;
    Button regButton;
    FirebaseAuth rootNode;
    ProgressBar progressBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sinup_main);
    regName = findViewById(R.id.full_name);
    regEmail = findViewById(R.id.email1);
    regPassword = findViewById(R.id.password);
    regConfirmPassword = findViewById(R.id.confirmPassword);
    regButton = findViewById(R.id.reg_btn);
    progressBar=findViewById(R.id.progress_bar);
    rootNode = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);
        regButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email=regEmail.getText().toString().trim();
            String password=regPassword.getText().toString().trim();
            String fullName=regName.getText().toString().trim();
            String confirmPassword=regConfirmPassword.getText().toString().trim();
            if(fullName.isEmpty()){
                Toast.makeText(SinUp.this, "Please Enter Your FullName", Toast.LENGTH_SHORT).show();

                return;

            }
            if(email.isEmpty()){
                Toast.makeText(SinUp.this,"Please Enter Email",Toast.LENGTH_SHORT).show();

                return;
            }
            else if(password.length()<=6){
                Toast.makeText(SinUp.this, "Password Must Be Greater than 6 Characters", Toast.LENGTH_SHORT).show();
            }
            if(password.isEmpty()){
                Toast.makeText(SinUp.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

                return;
            }
            if(confirmPassword.isEmpty()){
                Toast.makeText(SinUp.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();

                return;
            }
            // This process will only set-up if password==confirmPassword---//
            if (password.equals(confirmPassword)) {


                rootNode.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SinUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SinUp.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SinUp.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(SinUp.this, "Authentication Failed..Password Weak", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    });
}

}
