package com.example.bloodapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;

public class contact_us_activity extends AppCompatActivity {

    EditText editTextMessage;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_activity);

        editTextMessage=(EditText)findViewById(R.id.editText1);

        send=(Button)findViewById(R.id.buttonSend);

        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                String messageText=editTextMessage.getText().toString();

                if(messageText.equals(""))
                {
                    Toast.makeText(contact_us_activity.this, "Please write some Text message", Toast.LENGTH_SHORT).show();
                    return;
                }

                new TTFancyGifDialog.Builder(contact_us_activity.this)
                        .setTitle("Contact With Admin")
                        .setMessage("Are you sure to send email ????  For contacting plz click on Gmail.")
                        .setPositiveBtnText("Ok")
                        .setPositiveBtnBackground("#22b573")
                        .setNegativeBtnText("Cancel")
                        .setNegativeBtnBackground("#c1272d")
                        .setGifResource(R.drawable.contactus)      //pass your gif, png or jpg
                        .isCancellable(true)
                        .OnPositiveClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {

                                String to="salarzaiflower@gmail.com";
                                String subject="Blood Donation App";
                                String message=editTextMessage.getText().toString();

                                if(message.equals(""))
                                {
                                    Toast.makeText(contact_us_activity.this, "Please write some message", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                                email.putExtra(Intent.EXTRA_TEXT, message);

                                //need this to prompts email client only
                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email, "Choose an Email client :"));

                            }
                        })
                        .OnNegativeClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(contact_us_activity.this,"Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();


            }

        });




    }
}
