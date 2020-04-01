package com.cuboid.rentacabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registeration extends AppCompatActivity {

    private EditText inputEmail, inputPass, inputName, inputNumber;
    private Button registerBtn, loginBtn;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            startActivity(new Intent(registeration.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_registeration);
        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.register_email);
        inputPass = (EditText) findViewById(R.id.register_password);
        inputName = (EditText) findViewById(R.id.register_name);
        inputNumber = (EditText) findViewById((R.id.register_phone));
        registerBtn = (Button) findViewById((R.id.register_btn));
        loginBtn = (Button) findViewById(R.id.register_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registeration.this, Login.class));
            }
        });

        takeInput();

    }

    // to get user input
    public  void  takeInput()
    {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPass.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "Enter an email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(registeration.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.length() < 6)
                {
                    Toast.makeText(registeration.this, "Password too short, should contain atleast 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                Registeruser(email, password);
            }

        });
    }

    //to register the user in Firebase
    public void Registeruser(String email, String password)
    {
        final String name = inputName.getText().toString();
        final String number = inputNumber.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(registeration.this, "Regsteration failed !" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(registeration.this, "Registeration succesful", Toast.LENGTH_SHORT).show();
                            mapData(name , number);
                        }
                    }
                });

    }

    //to map the user inputs to datatbase
    public void mapData(String name , String number)
    {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("id", userId);
        hashmap.put("name", name);
        hashmap.put("number", number);

        reference.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(registeration.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "User registeration failed! PLease try again later", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
