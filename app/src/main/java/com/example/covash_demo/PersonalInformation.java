package com.example.covash_demo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class PersonalInformation extends AppCompatActivity {
    ImageView imgback;
    CardView cardViewimginfo;
    ShapeableImageView imginfo;
    private static final int IMAGE_PICK_CODE =1000;
    private static final int PERMISSION_CODE = 1001;
    DatePickerDialog.OnDateSetListener setListener;
    LinearLayout eddate;
    TextView tvdate;
    RadioGroup radioGroup;
    RadioButton radioButton,radioMale,radioFeMale,radioOther;
    EditText edfullname,ednumber,edid,edemail,edcountry;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Button btnsave;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    Uri imgUri;
    ProgressBar load;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        imgback = findViewById(R.id.imgbackpress);
        cardViewimginfo = findViewById(R.id.cardViewimginfo);
        imginfo = findViewById(R.id.imginfo);
        eddate = findViewById(R.id.edbirtday);
        tvdate = findViewById(R.id.tvbirthday);
        radioGroup = findViewById(R.id.radiogroup);
        edfullname = findViewById(R.id.edfullname);
        ednumber = findViewById(R.id.ednumber);
        edid = findViewById(R.id.edid);
        edemail = findViewById(R.id.edEmail);
        edcountry = findViewById(R.id.edCountry);
        btnsave = findViewById(R.id.btnsaveinfo);
        radioMale = findViewById(R.id.radiomale);
        radioFeMale = findViewById(R.id.radiofemale);
        radioOther = findViewById(R.id.radioother);
        load = findViewById(R.id.PinfoPBLoading);




        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        getInfo();

        eddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInformation.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth +"/" + month + "/" + year;
                tvdate.setText(date);
            }
        };




        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cardViewimginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        pickImageFromGallery();
                    }
                }else {
                    pickImageFromGallery();
                }
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInfo(tvdate.getText().toString().trim());

            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Sellect Picture"),IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK){
            imgUri = data.getData();



            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                imginfo.setImageBitmap(bitmap);
                Log.d("img", String.valueOf(imgUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void getInfo(){
        load.setVisibility(View.VISIBLE);
        sharedPreferences = getSharedPreferences("taikhoan",MODE_PRIVATE);
        String username = sharedPreferences.getString("taikhoan","taikhoan");
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("User");
        Query user = reference.orderByChild("username").equalTo(username);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String fullname = snapshot.child(username).child("fullName").getValue(String.class);
                    String number = snapshot.child(username).child("date").getValue(String.class);
                    String id = snapshot.child(username).child("id").getValue(String.class);
                    String email = snapshot.child(username).child("email").getValue(String.class);
                    String country = snapshot.child(username).child("country").getValue(String.class);
                    String date = snapshot.child(username).child("date").getValue(String.class);
                    String sex = snapshot.child(username).child("sex").getValue(String.class);
                    String male = (String) radioMale.getText();
                    String female = (String) radioFeMale.getText();
                    String other = (String) radioOther.getText();







                    edfullname.setText(fullname);
                    ednumber.setText(number);
                    edid.setText(id);
                    edemail.setText(email);
                    edcountry.setText(country);
                    tvdate.setText(date);

                    storageReference = FirebaseStorage.getInstance().getReference().child("images/" + username + ".jpg");

                    try {
                        final File localFile = File.createTempFile(username,"jpg");
                        storageReference.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imginfo.setImageBitmap(bitmap);
                                        load.setVisibility(View.GONE);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {

                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    public void addInfo(String date){
        sharedPreferences = getSharedPreferences("taikhoan",MODE_PRIVATE);
        storageReference = FirebaseStorage.getInstance().getReference("User");
        String username = sharedPreferences.getString("taikhoan","taikhoan");
        String password = sharedPreferences.getString("matkhau","matkhau");
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("User");
        String fullname = edfullname.getText().toString().trim();
        String number = ednumber.getText().toString().trim();
        String id = edid.getText().toString().trim();
        String email = edemail.getText().toString().trim();
        String country = edcountry.getText().toString().trim();
        String date1 = date;
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String sex = (String) radioButton.getText();
        UserHelperClass user = new UserHelperClass(username,email,fullname,number,id,country,sex,date,password);
        reference.child(username).setValue(user);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Succecfull").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.show();




    }




}