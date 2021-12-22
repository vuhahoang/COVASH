package com.example.covash_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.covash_demo.Modle.UserHelperClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class  registerform extends AppCompatActivity {
    private Button signin;
    private Context context;
    private Button signup;
    EditText EdName;
    EditText EdUsername;
    EditText EdEmail;
    EditText EdPassword;
    CheckBox checkBox;
    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton rm,rfm,ro;


    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerform);
        context = this;
        signin =(Button) findViewById(R.id.btsigninb);
        signup =(Button) findViewById(R.id.btsignupb) ;
        EdName = (EditText) findViewById(R.id.edname);
        EdUsername =(EditText) findViewById(R.id.edusername);
        EdEmail = (EditText) findViewById(R.id.edemail);
        EdPassword = (EditText) findViewById(R.id.edpassword);
        checkBox = (CheckBox) findViewById(R.id.checkboxsu);
        radioGroup = findViewById(R.id.radiogroupreg);
        rm = findViewById(R.id.rmale);
        rfm = findViewById(R.id.rfemale);
        ro = findViewById(R.id.rother);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i = new Intent(registerform.this, loginform.class);
                startActivity(i);
            }
        });




    }

    private Boolean validateName() {
        String val = EdName.getText().toString().trim();

        if (val.isEmpty()) {
            EdName.setError("Field cannot be empty");
            return false;
        }
        else {
            EdName.setError(null);

            return true;
        }
    }

    private Boolean validateUsername() {
        String val = EdUsername.getText().toString().trim();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            EdUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            EdUsername.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            EdUsername.setError("White Spaces are not allowed");
            return false;
        } else {
            EdUsername.setError(null);

            return true;
        }
    }

    private Boolean validateEmail() {
        String val = EdEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            EdEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            EdEmail.setError("Invalid email address");
            return false;
        } else {
            EdEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = EdPassword.getText().toString().trim();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter

                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            EdPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            EdPassword.setError("Password is too weak");
            return false;
        } else {
            EdPassword.setError(null);
            return true;
        }
    }

    private  Boolean dieukhoan(){
        if(!checkBox.isChecked()){
            Toast.makeText(this,"How about Rule guy",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private Boolean valiSex(){
        if(!rm.isChecked() && !rfm.isChecked() && !ro.isChecked()){
            Toast.makeText(this,"Chose Sex Guy", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }


    public void signupclick(View view) {

            if(!validateName() |!validatePassword()  | !validateEmail() | !validateUsername() | !dieukhoan() | !valiSex())
            {
                return;
            }


           else {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("User");
                String name = EdName.getText().toString().trim();
                String username = EdUsername.getText().toString().trim();
                String email = EdEmail.getText().toString().trim();
                String password = EdPassword.getText().toString().trim();
                int idradio = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(idradio);
                String sex = radioButton.getText().toString();
                UserHelperClass user = new UserHelperClass(name, username, email, password, sex);
                reference.child(username).setValue(user);
                Intent i = new Intent(registerform.this, loginform.class);
                startActivity(i);
                Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("taikhoan", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("taikhoan");
                editor.remove("matkhau");
                editor.remove("check");
                editor.apply();
                editor.putString("taikhoan",username);
                editor.putString("matkhau",password);
                editor.commit();
            }
    }
}