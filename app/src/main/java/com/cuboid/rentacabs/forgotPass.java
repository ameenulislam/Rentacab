package com.cuboid.rentacabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPass extends AppCompatActivity {

    private EditText inputEmail;
    private Button resetBtn,backbtn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.reset_email);
        resetBtn = (Button) findViewById(R.id.btn_reset);
        backbtn = (Button) findViewById(R.id.go_back);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgotPass.this, Login.class));
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(forgotPass.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(), "Password Reset Failed, Try again later !", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(forgotPass.this, "We have sent the instructions to your email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
