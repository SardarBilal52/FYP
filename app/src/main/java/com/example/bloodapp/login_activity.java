package com.example.bloodapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.annotation.NonNull;
import android.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class login_activity extends AppCompatActivity {


    private Button SendVerificationCodeButton, VerifyButton;
    private EditText InputPhoneNumber, InputVerificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private TextView createNewUser;
    private ProgressDialog loadingBar;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);


        loadingBar=new ProgressDialog(login_activity.this);


        mAuth=FirebaseAuth.getInstance();


        SendVerificationCodeButton=(Button) findViewById(R.id.login_Button);
        VerifyButton=(Button) findViewById(R.id.code_verify_button);
        InputPhoneNumber=(EditText) findViewById(R.id.phone_number_input);
        InputVerificationCode=(EditText) findViewById(R.id.verification_code_input);
        createNewUser=(TextView) findViewById(R.id.New_registration);

        createNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(login_activity.this,SignupPhoneActivity.class));
            }
        });




        VerifyButton.setEnabled(false);
        InputVerificationCode.setEnabled(false);


        SendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=InputPhoneNumber.getText().toString();


                if(TextUtils.isEmpty(phoneNumber))
                {
                    Toast.makeText(login_activity.this, "please Enter Phone Number ...", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    loadingBar.setTitle("Phone Verification");
                    loadingBar.setMessage("please wait, while we are authanticating your phone...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            login_activity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks

                }
            }



        });






        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendVerificationCodeButton.setEnabled(false);
                InputPhoneNumber.setEnabled(false);
                String varificationCode=InputVerificationCode.getText().toString();

                if(TextUtils.isEmpty(varificationCode))
                {
                    Toast.makeText(login_activity.this, "please enter verification Code first...", Toast.LENGTH_SHORT).show();
                }
                else
                {


                    loadingBar.setTitle("Verification Code");
                    loadingBar.setMessage("please wait, while we are verifying your code...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, varificationCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });








        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                if(isConnected())
                {

                    loadingBar.dismiss();

                    Toast.makeText(login_activity.this, "please enter correct number with your country code", Toast.LENGTH_LONG).show();
                    SendVerificationCodeButton.setEnabled(true);
                    InputPhoneNumber.setEnabled(true);

                    VerifyButton.setEnabled(false);
                    InputVerificationCode.setEnabled(false);


                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(login_activity.this, "network not connected !!! please make sure your internet connection", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                loadingBar.dismiss();

                Toast.makeText(login_activity.this, "Code has been send, Please check your phone", Toast.LENGTH_LONG).show();
                SendVerificationCodeButton.setEnabled(false);
                InputPhoneNumber.setEnabled(false);

                VerifyButton.setEnabled(true);
                InputVerificationCode.setEnabled(true);

                InputVerificationCode.setVisibility(VISIBLE);
                SendVerificationCodeButton.setVisibility(INVISIBLE);
                VerifyButton.setVisibility(VISIBLE);


            }
        };




    }

    private boolean isConnected()
    {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            loadingBar.dismiss();
                            Toast.makeText(login_activity.this, "You Login Successfully", Toast.LENGTH_SHORT).show();

                            SendUserToMainActivity();// when code is verifyied then we send user to main activity


                        } else {
                            // Sign in failed, display a message and update the UI

                            String message=task.getException().toString();
                            Toast.makeText(login_activity.this, "Error Occured: "+message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }





    private void SendUserToMainActivity()
    {
        Intent mainIntent=new Intent(login_activity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}
