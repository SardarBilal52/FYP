package com.example.bloodapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {


    private EditText profileAge, profileGender, profileBloodGroup, profileLocation, profilePhonNumber;
    private TextView profileName;
    private Button updateButton;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference RootRef;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        mAuth = FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();



        profileAge=(EditText) findViewById(R.id.my_profile_age);
        profileGender=(EditText) findViewById(R.id.my_profile_gender);
        profileBloodGroup=(EditText) findViewById(R.id.my_profile_blood_group);
        profileLocation=(EditText) findViewById(R.id.my_profile_location);

        profileName=(TextView)findViewById(R.id.my_profile_name);
        profilePhonNumber=(EditText) findViewById(R.id.my_profile_phone_number);




        retrieveUserInfo();
        makeAllTextFieldUnEditible();


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        // retrieving of data from sharedPreference

        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String UserPName = preferences.getString("RUserName", null);
        String UserPCountry = preferences.getString("RUserCountry",null );
        String UserPBloodGroup = preferences.getString("RUserBloodGroup", null);
        String UserPAge = preferences.getString("RUserAge",null );
        String UserPGender = preferences.getString("RUserGender", null);
        String UserPPhoneNumber = preferences.getString("RUserPhoneNumber",null );




       // Showing data in fields from shared prefrence

        profileName.setText(UserPName);
        profileBloodGroup.setText(UserPBloodGroup);
        profileLocation.setText(UserPCountry);
        profileAge.setText(UserPAge);
        profileGender.setText(UserPGender);
        profilePhonNumber.setText(UserPPhoneNumber);









    }



    private void retrieveUserInfo()
    {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("city")) &&(dataSnapshot.hasChild("blood")) &&(dataSnapshot.hasChild("age")) && dataSnapshot.hasChild("gender"))
                        {
                            String retriveUserName=dataSnapshot.child("name").getValue().toString();
                            String retriveUserCountry=dataSnapshot.child("city").getValue().toString();
                            String retriveUserBloodGroup=dataSnapshot.child("blood").getValue().toString();
                            String retriveUserAge=dataSnapshot.child("age").getValue().toString();
                            String retriveUserGender=dataSnapshot.child("gender").getValue().toString();
                            String retriveUserPhoneNumber=dataSnapshot.child("number").getValue().toString();

//                            profileName.setText(retriveUserName);
//                            profileBloodGroup.setText(retriveUserBloodGroup);
//                            profileLocation.setText(retriveUserCountry);
//                            profileAge.setText(retriveUserAge);
//                            profileGender.setText(retriveUserGender);
//                            profilePhonNumber.setText(retriveUserPhoneNumber);


                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("RUserName", retriveUserName);
                            editor.putString("RUserCountry", retriveUserCountry);
                            editor.putString("RUserBloodGroup", retriveUserBloodGroup);
                            editor.putString("RUserAge", retriveUserAge);
                            editor.putString("RUserGender", retriveUserGender);
                            editor.putString("RUserPhoneNumber", retriveUserPhoneNumber);
                            editor.apply();



                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                        {

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void makeAllTextFieldUnEditible()
    {
        profileAge.setEnabled(false);
        profileGender.setEnabled(false);
        profileBloodGroup.setEnabled(false);
        profileLocation.setEnabled(false);
        profilePhonNumber.setEnabled(false);

    }

}
