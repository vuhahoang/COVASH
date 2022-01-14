package com.example.covash_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class loginform extends AppCompatActivity {
    private Button signup;
    private Context context;
    TextInputLayout EdUsername, EdPassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    CheckBox checkBox;
    SharedPreferences sharedpreferences;
    TextView test;
    ProgressBar load;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loginform);
        context = this;
        signup = (Button) findViewById(R.id.btsignupa);
        EdUsername = (TextInputLayout) findViewById(R.id.edusernamea);
        EdPassword = (TextInputLayout) findViewById(R.id.edpassworda);
        test = findViewById(R.id.tvtestthoi);
        checkBox = findViewById(R.id.remembercheckbox);
        load = findViewById(R.id.loginPBLoading);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(loginform.this, registerform.class);
                startActivity(i);
            }
        });

        if(isConnect(this)){
            duytridangnhap();
        }



    }

    public void loginUser(View view) {

        if(!isConnect(this)){

            showCustomDialog();
        }
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private Boolean validateUsername() {
        String val = EdUsername.getEditText().getText().toString();
        if (val.isEmpty()) {
            EdUsername.setError("Field cannot be empty");
            return false;
        } else {
            EdUsername.setError(null);
            EdUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = EdPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            EdPassword.setError("Field cannot be empty");
            return false;
        } else {
            EdPassword.setError(null);
            EdPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void isUser() {
        load.setVisibility(View.VISIBLE);
        sharedpreferences = getSharedPreferences("taikhoan",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        final String userEnteredUsername = EdUsername.getEditText().getText().toString().trim();
        final String userEnteredPassword = EdPassword.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    EdUsername.setError(null);
                    EdUsername.setErrorEnabled(false);
                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        editor.putString("taikhoan",userEnteredUsername);
                        editor.putString("matkhau",userEnteredPassword);
                        if (checkBox.isChecked()){
                            editor.putBoolean("check",true);
                        }
                        editor.putBoolean("checklogin",true);
                        editor.commit();
                        Intent j = new Intent(loginform.this,homepage.class);
                        startActivity(j);
                        load.setVisibility(View.GONE);
                    }else{
                        EdPassword.setError("Wrong password");
                        EdPassword.requestFocus();
                    }
                }else {
                    EdUsername.setError("No such User exist");
                    EdUsername.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(loginform.this, "Something wrong", Toast.LENGTH_SHORT).show();
                load.setVisibility(View.GONE);
            }
        });



    }

    private void duytridangnhap(){
        sharedpreferences = getSharedPreferences("taikhoan", MODE_PRIVATE);
        String taikhoan = sharedpreferences.getString("taikhoan", "");
        String passs = sharedpreferences.getString("matkhau", "");

        EdUsername.getEditText().setText(taikhoan);
        EdPassword.getEditText().setText(passs);
        checkBox.setChecked(sharedpreferences.getBoolean("check", false));

        if (sharedpreferences.getBoolean("check",false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isUser();
                }
            }, 1);
        }
    }

    private boolean isConnect(loginform loginform){
        ConnectivityManager connectivityManager = (ConnectivityManager) loginform.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(mobileconnect != null && mobileconnect.isConnected() || (wificonnect != null && wificonnect.isConnected())){
            return true;
        }else {return false;}
    }

    private void showCustomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(loginform.this);
        builder.setMessage("Please connect to the internet to using app").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),loginform.class));
                        finish();
                    }
                });
        builder.show();
    }


}