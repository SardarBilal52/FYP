package com.example.bloodapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SendingRequestActivity extends AppCompatActivity {


    private String reciverUserrID, senderUseID ,Current_State;;
    private DatabaseReference UserRef,BloodRequestRef, RequestUserData;
    private FirebaseAuth mAuth;
    private TextView requested_user_profile_name;
    private Button send_blood_request_button;
    private String RequestedUserName;
    String userBloodGroup;
    private EditText requested_blood_location,requested_blood_quantity,requested_hospital,requested_contact_number;
    private EditText requestedUserBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_request);


        requested_user_profile_name=(TextView)findViewById(R.id.user_profile_name);
        send_blood_request_button=(Button)findViewById(R.id.send_blood_request_button);

        requested_blood_location=(EditText)findViewById(R.id.blood_request_location);
        requested_blood_quantity=(EditText)findViewById(R.id.blood_request_blood_quantity);
        requested_hospital=(EditText)findViewById(R.id.blood_request_required_hospital);
        requested_contact_number=(EditText)findViewById(R.id.blood_request_contact_number);
        requestedUserBloodGroup=(EditText)findViewById(R.id.requsted_blood);
        requestedUserBloodGroup.setEnabled(false);


        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        reciverUserrID=getIntent().getExtras().get("visit_user_id").toString();
        BloodRequestRef= FirebaseDatabase.getInstance().getReference().child("Blood Requests");
        RequestUserData=FirebaseDatabase.getInstance().getReference().child("Request UserData");
        mAuth=FirebaseAuth.getInstance();
        senderUseID=mAuth.getCurrentUser().getUid();
        Current_State="new";


        RetrieveUserInfo();




        send_blood_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                




                if(TextUtils.isEmpty(requiredUserLocation))
                {
                    Toast.makeText(SendingRequestActivity.this, "Please Your location", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(requiredBloodQuantity))
                {
                    Toast.makeText(SendingRequestActivity.this, "Please required blood quantity", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(requiredHospital))
                {
                    Toast.makeText(SendingRequestActivity.this, "Please Enter Hospital name", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(requiredBloodContactNumber))
                {
                    Toast.makeText(SendingRequestActivity.this, "Please Enter Contact number", Toast.LENGTH_SHORT).show();
                }

                else {

                    new TTFancyGifDialog.Builder(SendingRequestActivity.this)
                            .setTitle("Sending Blood Request")
                            .setMessage("Are you sure to send a blood request ???")
                            .setPositiveBtnText("Send Req")
                            .setPositiveBtnBackground("#22b573")
                            .setNegativeBtnText("Cancel Req")
                            .setNegativeBtnBackground("#c1272d")
                            .setGifResource(R.drawable.sendingrequest)      //pass your gif, png or jpg
                            .isCancellable(true)
                            .OnPositiveClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    BloodRequestRef.child(senderUseID).child(reciverUserrID)
                                            .child("request_type").setValue("sent")
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {

                                                    if(task.isSuccessful())
                                                    {
                                                        BloodRequestRef.child(reciverUserrID).child(senderUseID)
                                                                .child("request_type").setValue("received")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                    {
                                                                        if(task.isSuccessful())
                                                                        {
                                                                            // yaha per ham aik new node banate hy notification kele

                                                                            HashMap<String , Object> profileMap=new HashMap<>();
                                                                            profileMap.put("required_location" ,requiredUserLocation);
                                                                            profileMap.put("required_blood",requiredBloodGroup);
                                                                            profileMap.put("required_quantity",requiredBloodQuantity);
                                                                            profileMap.put("required_hospital",requiredHospital);
                                                                            profileMap.put("required_number",requiredBloodContactNumber);



                                                                            RequestUserData.child(reciverUserrID).child(senderUseID)
                                                                                    .updateChildren(profileMap)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                                        {

                                                                                            if(task.isSuccessful())
                                                                                            {
                                                                                                Toast.makeText(SendingRequestActivity.this, "you sent a blood request", Toast.LENGTH_SHORT).show();

                                                                                            }

                                                                                        }
                                                                                    });



                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            });

                                    Intent backToSearchinActivity=new Intent(SendingRequestActivity.this,RequestForBloodActivity.class);

                                    backToSearchinActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(backToSearchinActivity);
                                    finish();
                                    Toast.makeText(SendingRequestActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .OnNegativeClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(SendingRequestActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();





//                    BloodRequestRef.child(senderUseID).child(reciverUserrID)
//                            .child("request_type").setValue("sent")
//                            .addOnCompleteListener(new OnCompleteListener<Void>()
//                            {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task)
//                                {
//
//                                    if(task.isSuccessful())
//                                    {
//                                        BloodRequestRef.child(reciverUserrID).child(senderUseID)
//                                                .child("request_type").setValue("received")
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task)
//                                                    {
//                                                        if(task.isSuccessful())
//                                                        {
//                                                            // yaha per ham aik new node banate hy notification kele
//
//                                                            HashMap<String , Object> profileMap=new HashMap<>();
//                                                            profileMap.put("required_location" ,requiredUserLocation);
//                                                            profileMap.put("required_blood",requiredBloodGroup);
//                                                            profileMap.put("required_quantity",requiredBloodQuantity);
//                                                            profileMap.put("required_hospital",requiredHospital);
//                                                            profileMap.put("required_number",requiredBloodContactNumber);
//
//
//
//                                                            RequestUserData.child(reciverUserrID).child(senderUseID)
//                                                                    .updateChildren(profileMap)
//                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<Void> task)
//                                                                        {
//
//                                                                            if(task.isSuccessful())
//                                                                            {
//                                                                                Toast.makeText(SendingRequestActivity.this, "you sent a blood request", Toast.LENGTH_SHORT).show();
//
//                                                                            }
//
//                                                                        }
//                                                                    });
//
//
//
//                                                        }
//                                                    }
//                                                });
//
//                                    }
//                                }
//                            });
//
//                    Intent backToSearchinActivity=new Intent(SendingRequestActivity.this,RequestForBloodActivity.class);
//
//                    backToSearchinActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(backToSearchinActivity);
//                    finish();
                }
            }
        });

    }


    private void RetrieveUserInfo()
    {

        UserRef.child(reciverUserrID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {// agr profile image exist ho tu ye load karo
                   RequestedUserName=dataSnapshot.child("name").getValue().toString();
                    userBloodGroup=dataSnapshot.child("blood").getValue().toString();



                    requested_user_profile_name.setText(RequestedUserName);
                    requestedUserBloodGroup.setText( userBloodGroup);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
