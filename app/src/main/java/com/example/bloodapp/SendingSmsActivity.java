package com.example.bloodapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendingSmsActivity extends AppCompatActivity {

    private String reciverUserrID;
    private DatabaseReference UserRef;
    private FirebaseAuth mAuth;
    private TextView send_sms_profile_name;
    private String userMobileNumber;
    private Button callButton, smsOnNumberButton;
    SmsManager sm;
    private String selectSendingBloodGrup;
    private String UserName;
    String userBloodGroup;
    private EditText sending_sms_location_user,sms_sending_blood_quantity_user,sms_required_hospital_user,sms_sending_contact_number_user;
    private EditText sendingSmsUserBloodGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_sms);

        send_sms_profile_name=(TextView)findViewById(R.id.my_profile_name);
        callButton=(Button)findViewById(R.id.call_on_number);
        smsOnNumberButton=(Button)findViewById(R.id.message_on_number);

        sending_sms_location_user=(EditText)findViewById(R.id.sending_sms_location);
        sms_sending_blood_quantity_user=(EditText)findViewById(R.id.sms_sending_blood_quantity);
        sms_required_hospital_user=(EditText)findViewById(R.id.sms_required_hospital);
        sms_sending_contact_number_user=(EditText)findViewById(R.id.sms_sending_contact_number);
        sendingSmsUserBloodGroup=(EditText)findViewById(R.id.sms_sending_blood);
        sendingSmsUserBloodGroup.setEnabled(false);


        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        reciverUserrID=getIntent().getExtras().get("visit_user_id").toString();

        sm=SmsManager.getDefault();

        RetrieveUserInfo();






        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TTFancyGifDialog.Builder(SendingSmsActivity.this)
                        .setTitle("Making Call")
                        .setMessage("Are you sure to make a call???")
                        .setPositiveBtnText("Make Call")
                        .setPositiveBtnBackground("#22b573")
                        .setNegativeBtnText("Cancel")
                        .setNegativeBtnBackground("#c1272d")
                        .setGifResource(R.drawable.callgif)      //pass your gif, png or jpg
                        .isCancellable(true)
                        .OnPositiveClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {

                                String callNumber=userMobileNumber;
                                Intent callIntent = new Intent(Intent.ACTION_CALL);

                                callIntent.setData(Uri.parse("tel:" + callNumber));
                                if (ActivityCompat.checkSelfPermission(SendingSmsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(callIntent);
                                Toast.makeText(SendingSmsActivity.this,"Calling...",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .OnNegativeClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(SendingSmsActivity.this,"Cancel...",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();



            }
        });




        smsOnNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String requiredBloodNumber=sms_sending_contact_number_user.getText().toString();
                final String requiredHospital=sms_required_hospital_user.getText().toString();
                final String requiredBloodQuantity=sms_sending_blood_quantity_user.getText().toString();
                final String requiredUserLocation=sending_sms_location_user.getText().toString();




                if(TextUtils.isEmpty(requiredUserLocation))
                {
                    Toast.makeText(SendingSmsActivity.this, "Please Your location", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(requiredBloodQuantity))
                {
                    Toast.makeText(SendingSmsActivity.this, "Please required blood quantity", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(requiredHospital))
                {
                    Toast.makeText(SendingSmsActivity.this, "Please Enter Hospital name", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(requiredBloodNumber))
                {
                    Toast.makeText(SendingSmsActivity.this, "Please Enter Contact number", Toast.LENGTH_SHORT).show();
                }

else {


                    new TTFancyGifDialog.Builder(SendingSmsActivity.this)
                            .setTitle("Sending Sms")
                            .setMessage("Are you sure to send Sms ???.")
                            .setPositiveBtnText("Send sms")
                            .setPositiveBtnBackground("#22b573")
                            .setNegativeBtnText("Cancel")
                            .setNegativeBtnBackground("#c1272d")
                            .setGifResource(R.drawable.sendingsms)      //pass your gif, png or jpg
                            .isCancellable(true)
                            .OnPositiveClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {


                                    String phoneNumber = String.valueOf(userMobileNumber);
                                    String message = "AOA  " +userBloodGroup+ " Blood  "+ requiredBloodQuantity+ " in Quantity is Urgently required at "+requiredHospital+ ". "+
                                            "("+  requiredUserLocation+" )"+ "   Kindly contact at  "+requiredBloodNumber +". Thanks."   +"         " +
                                            "      (BLOOD APP COMMUNITY)";
                                    SmsManager smsManager = SmsManager.getDefault();
                                    ArrayList<String> parts = smsManager.divideMessage(message);
                                    smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);

                                    Toast.makeText(SendingSmsActivity.this, "Message Has been Send to  "+UserName, Toast.LENGTH_SHORT).show();



                                    Intent backToSearchinActivity=new Intent(SendingSmsActivity.this,SearchBloodActivity.class);

                                    backToSearchinActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(backToSearchinActivity);
                                    finish();
                                    Toast.makeText(SendingSmsActivity.this,"Sending Sms...",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .OnNegativeClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(SendingSmsActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();

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
                    UserName=dataSnapshot.child("name").getValue().toString();
                    userMobileNumber=dataSnapshot.child("number").getValue().toString();
                   userBloodGroup=dataSnapshot.child("blood").getValue().toString();



                    send_sms_profile_name.setText(UserName);
                    sendingSmsUserBloodGroup.setText( userBloodGroup);

                    // userProfileStatus.setText(UserStatus);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
