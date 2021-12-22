package com.example.covash_demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class infoFragment extends Fragment {
    LinearLayout linfo,lsetting,llogout;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    ImageView imguser;
    TextView tvuser;
    ProgressBar load;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        linfo = view.findViewById(R.id.layoutpi);
        lsetting = view.findViewById(R.id.layoutsettings);
        llogout = view.findViewById(R.id.layoutLogout);
        imguser = view.findViewById(R.id.imguser);
        tvuser = view.findViewById(R.id.tvusername);
        load = view.findViewById(R.id.infoPBLoading);
        getInfo();
        linfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),PersonalInformation.class);
                startActivity(i);
            }
        });

        lsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Settings.class);
                startActivity(i);
            }
        });

        llogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you sure about that ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("taikhoan", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("taikhoan");
                                editor.remove("matkhau");
                                editor.remove("check");
                                editor.apply();
                                Intent i = new Intent(getContext(),loginform.class);
                                startActivity(i);
                            }
                        }).setNegativeButton("No", null);
                builder.show();
            }
        });
        return view;
    }
    public void getInfo(){
        load.setVisibility(View.VISIBLE);
        sharedPreferences = getActivity().getSharedPreferences("taikhoan", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("taikhoan","taikhoan");
        tvuser.setText(username);



        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + username + ".jpg");

        try {
            final File localFile = File.createTempFile(username,"jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imguser.setImageBitmap(bitmap);
                            load.setVisibility(View.GONE);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            load.setVisibility(View.GONE);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        getInfo();
    }
}