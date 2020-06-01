package com.example.bloodapp;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {



    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;
    private TextView userName, userLastSeen;
    private CircleImageView userImage;
    private Toolbar chatToolbar;
    private ImageButton SendMessageButton;
    private EditText MessageInputText;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private RecyclerView userMessagesList;

    private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessaageAdaptor messageAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mAuth=FirebaseAuth.getInstance();
        messageSenderID=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();




        messageReceiverID=getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName=getIntent().getExtras().get("visit_user_name").toString();



        initilizeController();


        userName.setText(messageReceiverName);



        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendMessage();

            }
        });
    }

    private void initilizeController()
    {
        chatToolbar=(Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(chatToolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        // yaha per ham custom chat layout ko access karke include karte hy
        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView =layoutInflater.inflate(R.layout.custom_chat_layout,null);
        actionBar.setCustomView(actionBarView);



        userName =(TextView) findViewById(R.id.custom_profile_name);


        SendMessageButton=(ImageButton) findViewById(R.id.send_message_btn);
        MessageInputText=(EditText) findViewById(R.id.input_message);



        messageAdaptor =  new MessaageAdaptor(messagesList);
        userMessagesList=(RecyclerView) findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager=new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdaptor);
        DisplayLastSeen();
    }



    private void DisplayLastSeen()
    {
        RootRef.child("Users").child(messageSenderID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }






    @Override
    protected void onStart()

    {
        super.onStart();

        RootRef.child("messages").child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        Messages messages=dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);

                        messageAdaptor.notifyDataSetChanged();

                        userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s)
                    {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot)
                    {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s)
                    {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

    }





    private void SendMessage()
    {
        String messageText=MessageInputText.getText().toString();
        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(ChatActivity.this, "Please Type a text", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef ="messages/" +messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef ="messages/" +messageReceiverID + "/" + messageSenderID;


            DatabaseReference userMessageKeyRef=RootRef.child("messages")
                    .child(messageSenderID).child(messageReceiverID).push();
            //ham ne message ka key banaya
            String messagePushID=userMessageKeyRef.getKey();   // yaha per key ko messagePushID me save kya

            Map messageTextBody =new HashMap();
            messageTextBody.put("message",messageText);
            messageTextBody.put("type", "text"); // show thetext typp
            messageTextBody.put("from",messageSenderID);


            Map messageBodyDetail=new HashMap();
            messageBodyDetail.put(messageSenderRef + "/" + messagePushID ,messageTextBody);
            messageBodyDetail.put(messageReceiverRef + "/" + messagePushID ,messageTextBody);


            RootRef.updateChildren(messageBodyDetail).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {   if(task.isSuccessful())
                {
                    Toast.makeText(ChatActivity.this, "Message has been sent", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ChatActivity.this, "Message Not sent", Toast.LENGTH_SHORT).show();
                }

                    MessageInputText.setText("");

                }


            });


        }
    }
}
