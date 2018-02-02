package com.example.hpm.Coer_e_Outpass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText id,password,mobile,email,name;
    Button registerButton;
    String myurl,repurl;
    TextView logintext;
    Spinner bhawans;
    RadioGroup genderGroup;
    RadioButton male,female;
    String bhawan,gend;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;





    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");
        name=(EditText)findViewById(R.id.etrname);
        password=(EditText)findViewById(R.id.etrpass);
        mobile=(EditText)findViewById(R.id.etrmobile);
        email=(EditText)findViewById(R.id.etremail);
        id=(EditText)findViewById(R.id.etrid);
        bhawans=(Spinner)findViewById(R.id.bhawanspinner);
        genderGroup=(RadioGroup)findViewById(R.id.radiogroup);
        male=(RadioButton) findViewById(R.id.male);
        female=(RadioButton) findViewById(R.id.female);
        email.requestFocus();
        bhawans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ( i){
                    case 0:
                        male.setChecked(true);
                        gend=male.getText().toString();
                        bhawan=(String)bhawans.getItemAtPosition(0);
                        break;
                    case 1:
                        male.setChecked(true);
                        gend=male.getText().toString();

                        bhawan=(String)bhawans.getItemAtPosition(1);
                        break;
                    case 2:
                        male.setChecked(true);
                        gend=male.getText().toString();

                        bhawan=(String)bhawans.getItemAtPosition(2);
                        break;
                    case 3:
                        male.setChecked(true);
                        gend=male.getText().toString();

                        bhawan=(String)bhawans.getItemAtPosition(3);
                        break;
                    case 4:
                        male.setChecked(true);
                        gend=male.getText().toString();

                        bhawan=(String)bhawans.getItemAtPosition(4);


                        break;
                    case 5:
                        female.setChecked(true);
                        gend=female.getText().toString();

                        bhawan=(String)bhawans.getItemAtPosition(5);

                        break;
                    case 6:
                        female.setChecked(true);
                        gend=female.getText().toString();

                        bhawan=(String)bhawans.getItemAtPosition(6);


                        break;
                    case 7:
                        female.setChecked(true);
                        gend=female.getText().toString();

                        bhawan=(String)bhawans.getItemAtPosition(7);
                        break;


                }
             }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        registerButton=(Button)findViewById(R.id.registerbutton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().length()<=0) {
                    id.setError("Please provide a valid id");
                }


                   if(password.getText().toString().length()<=0)

            {
                password.setError("Please provide a valid password");
            }
                       if(mobile.getText().toString().length()<=0)

            {
                if(mobile.getText().toString().length()<10)
                mobile.setError("Please provide a valid mobile");
            }
                Pattern pattern= Patterns.EMAIL_ADDRESS;
            if(!pattern.matcher(email.getText().toString().trim()).matches())
            {
                email.setError("Please provide a valid email");
            }
                if(email.getText().toString().length()==0){
                    email.setError("Please provide a valid email");
                }
                if(name.getText().toString().length()<=0)
                {
                    name.setError("Please provide a valid name");
                }

              else{

              registerUser();
                }



               /* myurl="http://192.168.43.109/opmgmt/insertvalues.php?id="+
                        id.getText().toString()+"&password="+password.getText().toString()
                        +"&email="+email.getText().toString()+"&mobile="+mobile.getText().toString()+
                        "&name="+name.getText().toString()+"&bhawan="+bhawan+"&gender="+gend;
                if(mobile.getText().toString().isEmpty()){
                    mobile.setError("Please fill in your mobile no.");
                }
                if(mobile.getText().toString().length()<10||mobile.getText().toString().length()>10){
                    mobile.setError("Please enter valid mobile number");
                }

                else if(!mobile.getText().toString().isEmpty()&&mobile.getText().toString().length()==10){
                repurl=myurl.replace(" ","%20");
                   new Register().execute();
                }*/
            }
        });
        logintext=(TextView)findViewById(R.id.logintext);
        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this,StartPageActivity.class);
                startActivity(i);

            }
        });



    }
    private void registerUser() {
        final long id1 = Long.parseLong(id.getText().toString());
        final long mobile1 = Long.parseLong(mobile.getText().toString());
        final String name1 = name.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Registering you");
        progressDialog.show();
        final String email1 = email.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(String.valueOf(id1))){
                    mAuth.createUserWithEmailAndPassword(email1,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                String key = databaseReference.push().getKey();
                                Student student = new Student(key, id1, name1, mobile1, email1, pwd, bhawan, gend);
                                databaseReference.child(String.valueOf(id1)).setValue(student);
                                Toast.makeText(RegisterActivity.this, "Student added", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this,StudentLoginActivity.class));
                            } else if (!task.isSuccessful())
                                Toast.makeText(RegisterActivity.this, "An error occured during Registration ", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else if(dataSnapshot.hasChild(String.valueOf(id1))){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "the coer-id is already registered with other email", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    class Register extends AsyncTask<Void,Void,Void>{
        String res="";
        ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url=new URL(repurl);
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
            dialog=new ProgressDialog(RegisterActivity.this);
            dialog.setTitle("You are being registered please wait ");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject object=new JSONObject(res);
               int success= object.getInt("success");
                if (success==1){
                    Toast.makeText(RegisterActivity.this,"You are registered",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(RegisterActivity.this,StudentLoginActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Registration unsuccessful",Toast.LENGTH_LONG).show();

                }
                dialog.dismiss();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
