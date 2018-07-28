package com.localshopper.team.localshopper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.localshopper.team.localshopper.R;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivity";

    TextInputEditText usernameEdtTxt;
    TextInputEditText passwordEdtTxt;
    TextView loginBtn;
    TextView registerBtn;

    String username;
    String password;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO : Check if already logged in using shared pref

        setContentView(R.layout.activity_login);
        initViews();

        db = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

    }

    private void initViews() {
        usernameEdtTxt = findViewById(R.id.user_name_edtxt_act_log);
        passwordEdtTxt = findViewById(R.id.password_edtxt_act_log);
        loginBtn = findViewById(R.id.login_btn_act_log);
        registerBtn = findViewById(R.id.register_btn_act_log);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_act_log){

            username = usernameEdtTxt.getText().toString();
            checkLogin();
        }
        else if (view.getId() == R.id.register_btn_act_log){
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }

    private void checkLogin() {

        DocumentReference docRef = db.collection("users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Toasty.error(LoginActivity.this, "Logged in successfully.").show();
                    } else {
                        Toasty.error(LoginActivity.this, "Username or password is incorrect.").show();
                    }
                } else {
                    Toasty.error(LoginActivity.this, "You are not yet registered.").show();
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
