package com.example.bloodapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SignupPhoneActivity extends AppCompatActivity {


    private Button SendVerificationCodeButton, VerifyButton;
    private EditText InputPhoneNumber, InputVerificationCode, signupName, signupCoutryName , signupAge;
    private Spinner signupBloodGroup,signupGender;
    private String selectBloodGrup ,selectGender;
    private String phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private ProgressDialog loadingBar;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private TextView simpleText, alreadyHaveAccount;
    private String currentUserID;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_phone);


        loadingBar = new ProgressDialog(SignupPhoneActivity.this);


        mAuth = FirebaseAuth.getInstance();


        SendVerificationCodeButton = (Button) findViewById(R.id.send_ver_code_button);
        VerifyButton = (Button) findViewById(R.id.verify_button);
        InputPhoneNumber = (EditText) findViewById(R.id.phone_number_input);
        InputVerificationCode = (EditText) findViewById(R.id.verification_code_input);
        simpleText=(TextView) findViewById(R.id.simple_text_show);
        signupName=(EditText) findViewById(R.id.signup_name);
        signupCoutryName=(EditText) findViewById(R.id.signup_country_name);
        signupAge=(EditText) findViewById(R.id.signup_age);

        alreadyHaveAccount=(TextView)findViewById(R.id.alredy_have_account);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity=new Intent(SignupPhoneActivity.this, login_activity.class);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginActivity);
                finish();
            }
        });

        spinnerInitilization();
        maleFemalespinnerInitilization();



        VerifyButton.setEnabled(false);
        InputVerificationCode.setEnabled(false);

        SendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = InputPhoneNumber.getText().toString();
                String name=signupName.getText().toString();
                String countryName=signupCoutryName.getText().toString();
                String age=signupAge.getText().toString();

                    if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(SignupPhoneActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else if(selectBloodGrup.equals("Select Blood Group"))
                {
                    Toast.makeText(SignupPhoneActivity.this, "Please Select blood group", Toast.LENGTH_SHORT).show();
                }

                else if(selectGender.equals("Please Select Gender"))
                {
                    Toast.makeText(SignupPhoneActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(age))
                {
                    Toast.makeText(SignupPhoneActivity.this, "Please Enter Your Age", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(countryName))
                {
                    Toast.makeText(SignupPhoneActivity.this, "Please Enter Your City Name", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(SignupPhoneActivity.this, "please Enter Phone Number ", Toast.LENGTH_SHORT).show();
                }





                else {
                    loadingBar.setTitle("Phone Verification");
                    loadingBar.setMessage("please wait, while we are authanticating your phone...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            SignupPhoneActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks

                }
            }


        });


        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendVerificationCodeButton.setEnabled(false);
                InputPhoneNumber.setEnabled(false);
                String varificationCode = InputVerificationCode.getText().toString();

                if (TextUtils.isEmpty(varificationCode)) {
                    Toast.makeText(SignupPhoneActivity.this, "please enter verification Code first...", Toast.LENGTH_SHORT).show();
                } else {


                    loadingBar.setTitle("Verification Code");
                    loadingBar.setMessage("please wait, while we are verifying your code...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, varificationCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();

                Toast.makeText(SignupPhoneActivity.this, "Inviled number, please enter correct number with your country code and also sure your internet connection", Toast.LENGTH_SHORT).show();
                SendVerificationCodeButton.setEnabled(true);
                InputPhoneNumber.setEnabled(true);

                VerifyButton.setEnabled(false);
                InputVerificationCode.setEnabled(false);

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

                Toast.makeText(SignupPhoneActivity.this, "Code has been send, Please check your phone", Toast.LENGTH_LONG).show();
                SendVerificationCodeButton.setEnabled(false);
                InputPhoneNumber.setEnabled(false);
                signupName.setEnabled(false);
                signupCoutryName.setEnabled(false);

                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                //InputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);
                InputVerificationCode.setEnabled(true);
                VerifyButton.setEnabled(true);
                alreadyHaveAccount.setVisibility(View.INVISIBLE);

                simpleText.setText("Check your mobile & put verification code here");
                simpleText.setTextSize(15);

            }
        };



    }





    private void maleFemalespinnerInitilization()
    {
        signupGender=(Spinner) findViewById(R.id.signup_gender);
        ArrayAdapter<CharSequence> maleFemaleadapter = ArrayAdapter.createFromResource(this, R.array.male_female, android.R.layout.simple_spinner_item);
        maleFemaleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signupGender.setAdapter(maleFemaleadapter);


        signupGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectGender= (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    private void spinnerInitilization()
    {
        signupBloodGroup=(Spinner) findViewById(R.id.signup_blood);
        // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.blood_group, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        signupBloodGroup.setAdapter(adapter);


        signupBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
               selectBloodGrup= (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }









    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    String nameV=signupName.getText().toString();
                    String countryNameV=signupCoutryName.getText().toString().toUpperCase();
                    String ageV=signupAge.getText().toString();
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            loadingBar.dismiss();
                            HashMap<String , Object> profileMap=new HashMap<>();
                            profileMap.put("uid", currentUserID);
                            profileMap.put("name" ,nameV);
                            profileMap.put("city" ,countryNameV);
                            profileMap.put("blood", selectBloodGrup);
                            profileMap.put("age", ageV);
                            profileMap.put("gender",selectGender);
                            profileMap.put("number",phoneNumber);
                            currentUserID=mAuth.getCurrentUser().getUid();
                            RootRef= FirebaseDatabase.getInstance().getReference();
                            RootRef.child("Users").child(currentUserID).updateChildren(profileMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                SendUserToMainActivity();
                                                Toast.makeText(SignupPhoneActivity.this, "You Login Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(SignupPhoneActivity.this, "Error occured...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            // when code is verifyied then we send user to main activity


                        } else {
                            // Sign in failed, display a message and update the UI

                            String message = task.getException().toString();
                            Toast.makeText(SignupPhoneActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SignupPhoneActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
