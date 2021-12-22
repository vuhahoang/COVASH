package com.example.covash_demo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.content.Context.MODE_PRIVATE;


public class DashboardFragment extends Fragment {

    BarChart barChart;
    TextView tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths,tvAffectedCountries;
    private Context context;
    Button btnCountrydata,dismiss,send;
    CardView cvrateus,cvshare;
    EditText feedback,email,password;
    RatingBar ratingStar;
    float myRating =0;
    String sEmail,sPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);



        tvCases = view.findViewById(R.id.tvCases);
        tvRecovered = view.findViewById(R.id.tvRecovered);
        tvCritical = view.findViewById(R.id.tvCritical);
        tvActive = view.findViewById(R.id.tvActive);
        tvTodayCases = view.findViewById(R.id.tvTodayCases);
        tvTotalDeaths = view.findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths = view.findViewById(R.id.tvTodayDeaths);
        tvAffectedCountries = view.findViewById(R.id.tvAffectedCountries);
        barChart = view.findViewById(R.id.barchart);
        btnCountrydata = view.findViewById(R.id.btnCountryData);
        cvshare = view.findViewById(R.id.cvshareus);
        cvrateus = view.findViewById(R.id.cvrateus);



       btnCountrydata.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getContext(),CountrySwitchData.class);
               startActivity(i);
           }
       });

       cvshare.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent shareIntent = new Intent(Intent.ACTION_SEND);
               shareIntent.setType("text/plain");
               String shareBody = "Dowload my app now : -https://play.google/stores/apps/Covash_App.com";
               String shareSub = "Covash App";
               shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
               shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
               startActivity(Intent.createChooser(shareIntent,"Share with"));
           }
       });

       cvrateus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Dialog dialog = new Dialog(getContext());
               dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               dialog.setContentView(R.layout.layout_dialog_feedback);

               Window window = dialog.getWindow();
               if(window == null){
                   return;
               }

               window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
               window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

               WindowManager.LayoutParams windowAttributes = window.getAttributes();
               windowAttributes.gravity = Gravity.CENTER;
               window.setAttributes(windowAttributes);
               feedback = dialog.findViewById(R.id.feedback);
               dismiss = dialog.findViewById(R.id.dismiss);
               send = dialog.findViewById(R.id.send);
               ratingStar = dialog.findViewById(R.id.ratingStar);
               dismiss.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });
               ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                   @Override
                   public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                       int rate = (int) rating;
                       String msg = null;
                       myRating = ratingBar.getRating();
                       switch (rate){
                           case 1:
                               msg = "Sorry to hear that ! :(";
                               break;
                           case 2:
                               msg = "We always accept your suggestions!";
                               break;
                           case 3:
                               msg = "Good enough!";
                               break;
                           case 4:
                               msg = "Great! Thank you!";
                               break;
                           case 5:
                               msg = "Awesome! You're the best!";
                               break;
                       }
                       Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
                   }
               });
               send.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       SharedPreferences sharedPreferences = getActivity().getSharedPreferences("taikhoan", Context.MODE_PRIVATE);
                       String username = sharedPreferences.getString("taikhoan","");

                       String eto = "vuhahoang2208@gmail.com";
                       String subject = String.valueOf(myRating);
                       String mess = feedback.getText().toString().trim();



                       Properties properties = new Properties();
                       properties.put("mail.smtp.auth","true");
                       properties.put("mail.smtp.socketFactory.port", "465");
                       properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                       properties.put("mail.smtp.host","smtp.gmail.com");
                       properties.put("mail.smtp.port","465");

                       Session session = Session.getInstance(properties, new Authenticator() {
                           @Override
                           protected PasswordAuthentication getPasswordAuthentication() {
                               return new PasswordAuthentication("nhunghongthinguyen2107@gmail.com","thecoa22");
                           }
                       });
                       try {
                           MimeMessage message = new MimeMessage(session);
                           message.setFrom(new InternetAddress("nhunghongthinguyen2107@gmail.com"));
                           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(eto));
                           message.setSubject(username + " xếp hạng : "+subject+" sao");
                           message.setText(mess);
                           new SendMail().execute(message);
                       }catch (MessagingException e){
                           e.printStackTrace();
                       }
                   }
               });
               dialog.setCancelable(true);
               dialog.show();
           }
       });








        fetchData();
        return view;



    }

    private class SendMail extends AsyncTask<Message,String,String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),"Please wait...","Sending feedback",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Success")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color = '#509324'>Success</font>"));
                builder.setMessage("Feedback send successfully ^^");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        feedback.setText("");
                    }
                });
                builder.show();
            }else {
                Toast.makeText(getContext(),"Some thing went wrong :(",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fetchData() {
        String url  = "https://disease.sh/v2/all/";



        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            tvCases.setText(jsonObject.getString("cases"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvActive.setText(jsonObject.getString("active"));
                            tvTodayCases.setText(jsonObject.getString("todayCases"));
                            tvTotalDeaths.setText(jsonObject.getString("deaths"));
                            tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                            tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));


                            barChart.addBar(new BarModel("Cases",Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
                            barChart.addBar(new BarModel("Recoverd",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                            barChart.addBar(new BarModel("Deaths",Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                            barChart.addBar(new BarModel("Active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                            barChart.startAnimation();







                        } catch (JSONException e) {
                            e.printStackTrace();


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }


}