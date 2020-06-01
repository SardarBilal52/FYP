package com.example.bloodapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference RootRef;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UserName = "nameKey";
    public static final String UserBloodGroup = "bloodKey";

    private ImageButton profileBtn,bloodReqBtn,searchBloodBtn,inboxBtn,bloodBankBtn,compitibilityBtn,viewBloodBtn, rateUsBtn;

    private TextView userName, userBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar=(Toolbar) findViewById(R.id.main_page_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");




        drawerLayout=(DrawerLayout) findViewById(R.id.drawable_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);//it needs two strin one drwer open and drwaer close and we define it in values strings.xml
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView=(NavigationView) findViewById(R.id.navigation_view);



        View navView=navigationView.inflateHeaderView(R.layout.navigation_header);
        // here we reference navigation header and store in navView





        profileBtn=(ImageButton) findViewById(R.id.profile_btn);
        bloodReqBtn=(ImageButton) findViewById(R.id.blood_rquest_btn);
        searchBloodBtn=(ImageButton) findViewById(R.id.search_btn);
        inboxBtn=(ImageButton) findViewById(R.id.inbox_btn);
        bloodBankBtn=(ImageButton) findViewById(R.id.blood_bank_btn);
        compitibilityBtn=(ImageButton) findViewById(R.id.compitibility_btn);
        viewBloodBtn=(ImageButton) findViewById(R.id.view_request_btn);
        rateUsBtn=(ImageButton) findViewById(R.id.rate_us_btn);



///////////////////////////////////////////////
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProfile=new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(myProfile);

            }
        });

        bloodReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Request_recieve_Activity.class));

            }
        });

        searchBloodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,bloodSearchingActivity.class));
            }
        });


        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inbox=new Intent(MainActivity.this, ContactSmsActivity.class);
                startActivity(inbox);

            }
        });

        bloodBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        compitibilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup=new Intent(MainActivity.this, BloodCompitibility.class);
                startActivity(signup);

            }
        });

        viewBloodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RequestForBloodActivity.class));

            }
        });

        rateUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        ////////////////////////////////////////////////



        userName=navView.findViewById(R.id.user_profile_name);
        userBloodGroup=navView.findViewById(R.id.user_blood_group);






        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                userNavigationItemSelected(item); // we create this method for item selected
                return false;
            }
        });

    }



    private void sendUserToRequestReciveBloodActivity()
    {
        startActivity(new Intent(new Intent(MainActivity.this,Request_recieve_Activity.class)));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }








    private void userNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_profile:
                Intent myProfile=new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(myProfile);
                break;

            case R.id.menu_inbox:
                Intent inbox=new Intent(MainActivity.this, ContactSmsActivity.class);
                startActivity(inbox);
                break;


            case R.id.menu_view_blood_request:
                startActivity(new Intent(MainActivity.this,RequestForBloodActivity.class));
                break;

            case R.id.menu_my_blood_request:

                startActivity(new Intent(MainActivity.this,Request_recieve_Activity.class));
                break;

            case R.id.menu_blood_info:
                Intent signup=new Intent(MainActivity.this, BloodCompitibility.class);
                startActivity(signup);

                break;

            case R.id.menu_contact_us:

                startActivity(new Intent(MainActivity.this, contact_us_activity.class));
                break;

            case R.id.menu_logout:

//                Intent login=new Intent(MainActivity.this, SignupPhoneActivity.class);
//                startActivity(login);
                mAuth.signOut();
                sendToLoginActivity();
                Toast.makeText(this, "Log out...", Toast.LENGTH_SHORT).show();
                break;



            case R.id.menu_search_blood:

                sendUserToSearchBloodActivity();
                break;
        }
    }

    private void sendToLoginActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this, login_activity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser ==null)
        {
            sendUserToLoginActivity();
        }
        else
        {
            currentUserID=mAuth.getCurrentUser().getUid();
            RootRef= FirebaseDatabase.getInstance().getReference().child("Users");


            RootRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {
                        if(dataSnapshot.hasChild("name") && dataSnapshot.hasChild("blood"))
                        {


                            String userProfileName=dataSnapshot.child("name").getValue().toString();
                            userName.setText(userProfileName);
                            String userBlood=dataSnapshot.child("blood").getValue().toString();
                            userBloodGroup.setText("Blood Grp: "+userBlood);






                        }
                        else if(dataSnapshot.hasChild("name"))
                        {
//                                userName.setText(userProfileName);
                        }


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void  sendUserToLoginActivity()
    {
        Intent signupIntent=new Intent(MainActivity.this, SignupPhoneActivity.class);
        signupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signupIntent);
        finish();
    }


    private void sendUserToSearchBloodActivity()
    {
        Intent sendToSearchActivity=new Intent(MainActivity.this, SearchBloodActivity.class);
        startActivity(sendToSearchActivity);
    }

    private void sendUserToRequstedBloodActivity()
    {
        Intent sendToRequestedActivity=new Intent(MainActivity.this, RequestForBloodActivity.class);
        startActivity(sendToRequestedActivity);
    }
}