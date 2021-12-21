package com.example.covash_demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    EditText edOldPass,edNewPass1,edNewPass2;
    Button btnsave;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edOldPass = findViewById(R.id.edmatkhaucu);
        edNewPass1 = findViewById(R.id.edmatkhaumoi);
        edNewPass2 = findViewById(R.id.edmatkhaumoi2);
        btnsave = findViewById(R.id.btnsavepassword);
        back = findViewById(R.id.imgbackc);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!validatePassword()){
                   return;
               }else {

                   Check();
               }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void Check(){
        SharedPreferences sharedPreferences = getSharedPreferences("taikhoan",MODE_PRIVATE);
        String username = sharedPreferences.getString("taikhoan","");
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("User");
        final String passwordold = edOldPass.getText().toString();
        final String passwordnew = edNewPass1.getText().toString();
        final String passwordnew2 = edNewPass2.getText().toString();
        Query checkUser = reference.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);
                    if(passwordold.equals(passwordFromDB)){
                        if(passwordnew.equals(passwordnew2)){
                            reference.child(username).child("password").setValue(passwordnew);
                            Toast.makeText(ChangePassword.this,"Change Password succes",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ChangePassword.this,homepage.class);
                            startActivity(i);
                        }else {
                            edNewPass2.setError("password does not match");
                        }
                    }else {
                        edOldPass.setError("Password is not correct");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private Boolean validatePassword() {
        String op = edOldPass.getText().toString();
        String np1 = edNewPass1.getText().toString();
        String np2 = edNewPass2.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter

                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (op.isEmpty() || np1.isEmpty() || np2.isEmpty()) {
            Toast.makeText(this,"Do not empty",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!op.matches(passwordVal) || !np1.matches(passwordVal) || !np2.matches(passwordVal)) {
            if(!op.matches(passwordVal)){
                edOldPass.setError("Not invalid");
            }else if(!np1.matches(passwordVal)){
                edNewPass1.setError("Not invalid");
            }else if (!np2.matches(passwordVal)){
                edNewPass2.setError("Not invalid");
            }
            return false;
        }else {
            edOldPass.setError(null);
            edNewPass1.setError(null);
            edNewPass2.setError(null);
            return true;
        }
    }
}