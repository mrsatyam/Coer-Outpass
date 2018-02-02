package com.example.hpm.Coer_e_Outpass;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener {
    TextView tv,tvwelcome,choosedate,timefrom,timeto;
    EditText coerId,name,room_no,bhawan,place,purpose;
    Button submitbutton;
    DatabaseReference databaseReference;
    ImageButton logoutbutton;
    SharedPreferences preferences;
    TextToSpeech textToSpeech;
    String gender;
    long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);
        preferences=getSharedPreferences("sharedprefstudentinfo", Context.MODE_PRIVATE);
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.getDefault());


                //textToSpeech.setVoice();
            }

        });


        logoutbutton=(ImageButton)findViewById(R.id.logoutbutton);
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");

        timefrom=(TextView)findViewById(R.id.time_from);
        timeto=(TextView) findViewById(R.id.time_to);
        tv=(TextView)findViewById(R.id.tv);
        coerId=(EditText)findViewById(R.id.etwid);
        name=(EditText)findViewById(R.id.etwname);
        room_no=(EditText)findViewById(R.id.etwroom_no);
        bhawan=(EditText)findViewById(R.id.etwbhawan);
        place=(EditText)findViewById(R.id.etwplace);
        purpose=(EditText)findViewById(R.id.etwpurpose);
        submitbutton=(Button) findViewById(R.id.btwsubmit);
        choosedate=(TextView)findViewById(R.id.choose_date);
        tvwelcome=(TextView)findViewById(R.id.tvwelcome);
        choosedate.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        timefrom.setOnClickListener(this);
        timeto.setOnClickListener(this);
        logoutbutton.setOnClickListener(this);
        tv.setText("Welcome");
        coerId.requestFocus();
        coerId.setTextColor(Color.BLUE);
        coerId.setText("Coer ID: "+ preferences.getString("id",""));
        coerId.setEnabled(false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild( preferences.getString("id",""))){
                    String sname=dataSnapshot.child( preferences.getString("id","")).getValue(Student.class).getName();
                    name.setText(sname);
                    String[] s=sname.split(" ");
                    id=dataSnapshot.child(preferences.getString("id","")).getValue(Student.class).getStudent_id();
                    bhawan.setText(dataSnapshot.child( preferences.getString("id","")).getValue(Student.class).getBhawan());
                    gender=dataSnapshot.child( preferences.getString("id","")).getValue(Student.class).getGender();

                    if(gender.equals("Male")){
                        if(id==14041079){
                            tvwelcome.setText("Mr. Satyam (Developer)");
                            textToSpeech.speak("Hello Developer, Satyumm "+"!"+", welcome!",TextToSpeech.QUEUE_FLUSH,null,null);
                        }
                        else{
                        tvwelcome.setText("Mr. "+s[0]);
                        textToSpeech.speak("Hello mister "+s[0]+", welcome to the outpass portal",TextToSpeech.QUEUE_FLUSH,null,null);
                        }
                    }
                    else if(gender.equals("Female")) {
                        if(id==14042019){
                            tvwelcome.setText(" Developer Ms. Muskan");
                            textToSpeech.speak("Hello Developer , Muskaan" +"!"+ ", welcome!", TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                        tvwelcome.setText("Ms. "+s[0]);
                        textToSpeech.speak("Hello miss " + s[0] + ", welcome to the outpass portal", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                   // else tvwelcome.setText(s[0]);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       // new Download().execute();
    }
    protected Dialog onCreateDialog(int id){

        if(id==0){
            return new DatePickerDialog(WelcomePage.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    DateFormat dateFormat=DateFormat.getDateInstance();
                    Calendar c= Calendar.getInstance();
                    c.set(year,(monthOfYear),dayOfMonth);
                    Date d = c.getTime();
                    String date=dateFormat.format(d);
                    choosedate.setText(date);
                    choosedate.setTextColor(Color.BLUE);

                }
            }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }
        else if(id==1){
            return  new TimePickerDialog(WelcomePage.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String time=hourOfDay+" : "+minute;
                    if (minute==0){
                        time+="0";
                    }
                    if(hourOfDay>12){
                        time+="  pm";
                    }
                    else if (hourOfDay<12 )
                        time+="  am";
                    timefrom.setText(time);
                    timefrom.setTextColor(Color.BLUE);


                }
            },Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),false);
        }
        else if(id==2){
            return new TimePickerDialog(WelcomePage.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String time=hourOfDay+" : "+minute;
                    if (minute==0){
                        time+="0";
                    }
                    if(hourOfDay>12){
                        time+=" pm";
                    }
                    else if (hourOfDay<12)
                        time+=" am";
                    timeto.setText(time);
                    timeto.setTextColor(Color.BLUE);
                }
            },Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),false);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.btwsubmit:
               AlertDialog.Builder builder=new AlertDialog.Builder(WelcomePage.this);
              builder.setTitle("Fill outpass").setMessage("Do you really want to request for the outpass ?");
              builder.setPositiveButton("no", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {


                  }
              });
              builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      //code to submit outpass request to the server

                      Toast.makeText(WelcomePage.this,"Outpass Request sent to the warden",Toast.LENGTH_LONG).show();
                  }
              });
              builder.show();

              break;
          case R.id.choose_date:
              showDialog(0);
              break;
          case R.id.time_from:
              showDialog(1);
              break;
          case R.id.time_to:
              showDialog(2);
              break;
          case R.id.logoutbutton:
              SharedPreferences.Editor editor=getSharedPreferences("sharedprefstudentinfo", Context.MODE_PRIVATE).edit();
              editor.clear();
              editor.apply();
              startActivity(new Intent(WelcomePage.this,StartPageActivity.class));
              finish();
              break;

      }
    }





    class Download extends AsyncTask<Void,Void,Void>{
        String get="";
        ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url=new URL(StudentLoginActivity.myurl);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                InputStream stream=connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
                String txt="";
                while((txt=reader.readLine())!=null){
                    get+=txt;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(WelcomePage.this);
            dialog.setTitle("Please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject object=new JSONObject(get);
                int success=object.getInt("success");
                String nm=object.getString("name");
                String bhaw=object.getString("bhawan");
                String cid=object.getString("id");
                if(success==1){
                    tv.setText("Welcome");
                    tvwelcome.setText(nm);
                    name.setText(nm);
                    bhawan.setText(bhaw);
                    coerId.setText(cid);


                }
                else if(success==0){
                    Toast.makeText(WelcomePage.this,"ID  not registered!",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(WelcomePage.this,RegisterActivity.class);
                    startActivity(i);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        }
    }
}







