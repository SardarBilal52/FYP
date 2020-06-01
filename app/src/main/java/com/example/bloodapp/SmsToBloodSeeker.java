package com.example.bloodapp;


import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SmsToBloodSeeker extends AppCompatActivity {

    private String SMSTOBLOODSEEKER;
    SmsManager smsToBS;
    private Button sendButton;
    private EditText editTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_to_blood_seeker);

        SMSTOBLOODSEEKER=getIntent().getExtras().get("SmsToBloodSEEKER").toString();



        smsToBS=SmsManager.getDefault();

        sendButton=(Button)findViewById(R.id.sendbutton);
        editTextMessage=(EditText)findViewById(R.id.sms_To_Blood_Seeker);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendingNumber = String.valueOf(SMSTOBLOODSEEKER);
                String message = editTextMessage.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(sendingNumber, null, message, null, null);

                Toast.makeText(SmsToBloodSeeker.this, "Message Has been Send to  "+sendingNumber, Toast.LENGTH_SHORT).show();



                Intent backToSearchinActivity=new Intent(SmsToBloodSeeker.this,MainActivity.class);

                backToSearchinActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(backToSearchinActivity);
                finish();
            }
        });

    }
}
