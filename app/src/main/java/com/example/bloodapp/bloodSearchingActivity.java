package com.example.bloodapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class bloodSearchingActivity extends AppCompatActivity {


    private Spinner findBloodSpinner;
    private EditText findBloodInbox;
    private ImageButton findBloodImageButton;
    private String selectFindBloodSpinner, findBloodInboxText;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_searching);

        findBloodSpinnerInnitilization();
        findBloodInbox=(EditText)findViewById(R.id.findBloodInbox);
        findBloodImageButton=(ImageButton)findViewById(R.id.search_icon_image);



        findBloodImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findBloodInboxText=findBloodInbox.getText().toString();
                if(TextUtils.isEmpty(findBloodInboxText))
                {
                    Toast.makeText(bloodSearchingActivity.this, "Please Enter City Name...", Toast.LENGTH_LONG).show();
                }
                else if(selectFindBloodSpinner.equals("Select Blood Group"))
                {
                    Toast.makeText(bloodSearchingActivity.this, "Please select blood group...", Toast.LENGTH_LONG).show();
                }

                else
                {
                    Intent findBlood=new Intent(bloodSearchingActivity.this, FindBloodActivity.class);
                    findBlood.putExtra("find_blood_city",findBloodInboxText);
                    findBlood.putExtra("find_blood_group",selectFindBloodSpinner);

                    startActivity(findBlood);
                }

            }
        });




    }

    private void findBloodSpinnerInnitilization()
    {
        findBloodSpinner=(Spinner) findViewById(R.id.findBloodSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.blood_group, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        findBloodSpinner.setAdapter(adapter);


        findBloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectFindBloodSpinner= (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
