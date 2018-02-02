package com.example.hpm.Coer_e_Outpass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StudentLoginActivity extends AppCompatActivity {
    private AdView mAdView;
    static  String myurl,num;
    EditText email,password,id;
    Button loginbutton;
    TextView tvregister;
    FirebaseAuth firebaseAuth;
    static  String c_id;
    DatabaseReference databaseReference;
    String em,pw,coerid;
    static String resEmail,resPass;
    ProgressDialog progressDialog1,pd;

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                    firebaseAuth.signOut();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);
        id=(EditText)findViewById(R.id.etlid);
        email=(EditText)findViewById(R.id.etlemail);
        password=(EditText)findViewById(R.id.etlpass);
        tvregister=(TextView)findViewById(R.id.tvlregister) ;
        firebaseAuth=FirebaseAuth.getInstance();
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentLoginActivity.this,RegisterActivity.class));
            }
        });

        progressDialog1=new ProgressDialog(this);
        pd=new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Students");

        loginbutton=(Button)findViewById(R.id.loginbutton);

        loginbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog1.setTitle("Logging in");
                progressDialog1.show();
                resEmail = email.getText().toString().trim();
                resPass = password.getText().toString().trim();
                c_id = id.getText().toString().trim();


                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(c_id)) {
                            em = dataSnapshot.child(c_id).getValue(Student.class).getEmail();
                            pw=dataSnapshot.child(c_id).getValue(Student.class).getPasswd();
                            coerid=String.valueOf( dataSnapshot.child(c_id).getValue(Student.class).getStudent_id());
                            Toast.makeText(StudentLoginActivity.this, "email from db: "+em , Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                        if (resEmail.equals(em)&&resPass.equals(pw)&&c_id.equals(coerid)) {

                            pd.setTitle("Please wait");
                            pd.show();
                            firebaseAuth.signInWithEmailAndPassword(resEmail, resPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pd.dismiss();

                                    if (task.isSuccessful()) {
                                        progressDialog1.dismiss();
                                        Toast.makeText(StudentLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor=getSharedPreferences("sharedprefstudentinfo", Context.MODE_PRIVATE).edit();
                                        editor.clear();
                                        editor.putString("email",resEmail);
                                        editor.putString("password",resPass);
                                        editor.putString("id",c_id);
                                        editor.apply();
                                        startActivity(new Intent(StudentLoginActivity.this, WelcomePage.class));
                                        finish();

                                    } else {
                                        progressDialog1.dismiss();
                                        Toast.makeText(StudentLoginActivity.this, "email not registered" , Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        } else {
                            progressDialog1.dismiss();
                            id.setError("Please enter valid details");
                            email.setError("Please enter valid details");
                            password.setError("Please enter valid details");

                        }

                    }
        });
    }
}

    /*class Login extends AsyncTask<Void,Void,Void> {



        /*String res="";
        ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url=new URL(myurl);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                InputStream stream=connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
                String txt="";
                while((txt=reader.readLine())!=null){
                    res+=txt;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(StudentLoginActivity.this);
            dialog.setTitle(" logging  in Please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject object=new JSONObject(res);
                int success= object.getInt("success");
                String nam=object.getString("name");
                if (success==1){
                    Toast.makeText(StudentLoginActivity.this,"Login successful "+nam,Toast.LENGTH_LONG).show();
                    Intent i=new Intent(StudentLoginActivity.this,WelcomePage.class);
                    startActivity(i);


                }
                else if(success==0) {
                    dialog.dismiss();
                    Toast.makeText(StudentLoginActivity.this,"this Id is not registered",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(StudentLoginActivity.this,RegisterActivity.class);
                    startActivity(i);

                }
                  dialog.dismiss();



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }*/

