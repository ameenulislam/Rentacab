package com.cuboid.rentacabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button loginBtn, forgotPwd, backBtn;
    public boolean isLoggedIn;
    Toolbar toolbar;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            startActivity(new Intent(Login.this, MainActivity.class));
            isLoggedIn = true;
            finish();
        }
        setContentView(R.layout.activity_login);
        getIntent();

        inputEmail = (EditText) findViewById(R.id.login_email);
        inputPassword = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_loginBtn);
        forgotPwd = (Button) findViewById(R.id.login_forgot_pwd);
        backBtn = (Button) findViewById(R.id.go_back);

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, forgotPass.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, registeration.class));
            }
        });

        loginInput();;
    }

    //input for login
    public void loginInput()
    {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "Enter a valid email address !", Toast.LENGTH_SHORT).show();
                    return;
                }
                else  if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(), "Enter a password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.length() < 6)
                {
                    Toast.makeText(Login.this, "Password contains 6 charatcers at least !", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    loginUser(email, password);
                }
            }
        });
    }
    // to retrive email and passwrod from the dataBase and login
    public void loginUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(Login.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }
}
