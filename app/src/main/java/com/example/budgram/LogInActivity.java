package com.example.budgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LogInActivity";
//cd /D d:/Program Files\Nox\bin
// nox_adb.exe connect 127.0.0.1:62001
    private FirebaseAuth auth;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private TextView toogleSingUpTextView;
    private Button loginSingUpButton;

    private String pathIdUser;

    private boolean loginModeActive;

    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersDatabaseReference = database.getReference().child("users");

        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        toogleSingUpTextView = findViewById(R.id.toogleToSingUpTextView);
        loginSingUpButton = findViewById(R.id.loginSingUpButton);

        loginSingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSingUpUser(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
            }
        });
        //проверяем авторихован ли юзер, чтобы не запускать каждый раз активити с авторизацией/регистрацией
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LogInActivity.this, UserListActivity.class));
        }
    }

    private void loginSingUpUser(String email, String password) {

        if (loginModeActive){
            //режим авторизации юзера
            //проверки введенных данных
            if (passwordEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            } else {
                //начало авторизации
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    //updateUI(user);
                                    Intent intent = new Intent(LogInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LogInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });
            }
            //режим регистрации юзера
        } else {
            //проверки введенных данных
            if (!passwordEditText.getText().toString().trim().equals(repeatPasswordEditText.getText().toString().trim())){
                Toast.makeText(this, "Passwords don`t match", Toast.LENGTH_SHORT).show();
            } else if (passwordEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            }
            else {
                //начало регистрации юзера
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    createUser(user);
                                    Intent intent = new Intent(LogInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", nameEditText.getText().toString().trim());
                                    Log.i("userName", " " + nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LogInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }

        }

    }

    private void createUser(FirebaseUser firebaseUser) {

        User user = new User();
        user.setPathIdUser(usersDatabaseReference.push().getKey());
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());
        user.setMyAvatarImageResource("Noavatar");
        //my code
        usersDatabaseReference.child(user.getPathIdUser()).setValue(user);
        // allah usersDatabaseReference.push().setValue(user);
    }

    public void toogleLoginMode(View view) {

        if (loginModeActive){
            loginModeActive = false;
            loginSingUpButton.setText("Sing Up");
            toogleSingUpTextView.setText("Or, log in");
            repeatPasswordEditText.setVisibility(View.VISIBLE);
        } else {
            loginModeActive = true;
            loginSingUpButton.setText("Log in");
            toogleSingUpTextView.setText("Or, sign up");
            repeatPasswordEditText.setVisibility(View.GONE);
        }
    }
}
