package com.example.hpm.Coer_e_Outpass;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentLoginFragment extends Fragment implements RewardedVideoAdListener {
    static String myurl, num;
    EditText email, password, id;
    Button loginbutton;
    TextView tvregister;
    FirebaseAuth firebaseAuth;
    static String c_id;
    DatabaseReference databaseReference;
    String em, pw, coerid;
    static String resEmail, resPass;
    ProgressDialog progressDialog1, pd;
    ImageView imageView;
    RelativeLayout layout;
    Animation animation1;
    private AdView mAdView,adView2;
    private RewardedVideoAd mRewardedVideoAd;


    public StudentLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_student_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileAds.initialize(getContext(), "\n" +
                "ca-app-pub-6094042633128083~9689877716");
        mAdView = (AdView) getView().findViewById(R.id.adView);
        adView2=(AdView)getView().findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdColony.configure(getActivity(),"appe35a16562e1548189f","vz7bb8575fbbe4448d90");
        mAdView.loadAd(adRequest);
        adView2.loadAd(adRequest);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.loadAd("ca-app-pub-6094042633128083/4609436266",
                new AdRequest.Builder().build());
        mRewardedVideoAd.setRewardedVideoAdListener(this);




        imageView = (ImageView) getView().findViewById(R.id.imageView2);
        animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.littlerotate);
        imageView.startAnimation(animation1);
        SharedPreferences preferences = getActivity().getSharedPreferences("sharedprefstudentinfo", Context.MODE_PRIVATE);
        final String speamil = preferences.getString("email", "");
        final String sppassword = preferences.getString("password", "");
        if (!speamil.equals("") && !sppassword.equals("")) {
            startActivity(new Intent(getContext(), WelcomePage.class));

        }

        id = (EditText) getView().findViewById(R.id.etlid);
        email = (EditText) getView().findViewById(R.id.etlemail);
        email.clearFocus();
        id.clearFocus();
        password = (EditText) getView().findViewById(R.id.etlpass);
        password.clearFocus();
        tvregister = (TextView) getView().findViewById(R.id.tvlregister);
        firebaseAuth = FirebaseAuth.getInstance();
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });
        progressDialog1 = new ProgressDialog(getContext());
        pd = new ProgressDialog(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("Students");

        loginbutton = (Button) getView().findViewById(R.id.loginbutton);

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
                            pw = dataSnapshot.child(c_id).getValue(Student.class).getPasswd();
                            coerid = String.valueOf(dataSnapshot.child(c_id).getValue(Student.class).getStudent_id());
                            //Toast.makeText(getContext(), "email from db: "+em , Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (resEmail.equals(em) && resPass.equals(pw) && c_id.equals(coerid)) {
                    pd.setTitle("Please wait");
                    pd.show();

                    firebaseAuth.signInWithEmailAndPassword(resEmail, resPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                progressDialog1.dismiss();
                                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("sharedprefstudentinfo", Context.MODE_PRIVATE).edit();
                                editor.clear();
                                editor.putString("email", resEmail);
                                editor.putString("password", resPass);
                                editor.putString("id", c_id);
                                editor.apply();
                                startActivity(new Intent(getContext(), WelcomePage.class));
                                getActivity().finish();

                            } else {
                                progressDialog1.dismiss();
                                Toast.makeText(getContext(), "email not registered", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                } else {
                    pd.dismiss();
                    id.setError("Please enter valid details");
                    email.setError("Please enter valid details");
                    password.setError("Please enter valid details");

                }

            }
        });

    }

    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(getContext(), "Thank you "/* + reward.getType() + "  amount: " +
                reward.getAmount()*/, Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(getContext(), "Thanks",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(getContext(), "Ad Closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(getContext(), "Video failed to open", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(getContext(), "VideoAd Loaded    ", Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(getContext(), "VideoAd Opened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(getContext(), "Video Started", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResume() {
        mRewardedVideoAd.resume(getContext());
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(getContext());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(getContext());
        super.onDestroy();
    }
}
