package com.example.bloodapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.annotation.NonNull;
import android.appcompat.app.AppCompatActivity;
import android.recyclerview.widget.LinearLayoutManager;
import android.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactSmsActivity extends AppCompatActivity {

    private RecyclerView chatsList;
    private DatabaseReference chatRef, userRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_sms);




        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        chatRef= FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");


        chatsList=(RecyclerView) findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    public void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(chatRef,Contacts.class)
                        .build();


        FirebaseRecyclerAdapter<Contacts, ChatsViewHolder> adapter=
                new FirebaseRecyclerAdapter<Contacts, ChatsViewHolder>(options) //option is a query which we have created already
                {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Contacts model)
                    {
                        final String userIDs=getRef(position).getKey();


                        userRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if(dataSnapshot.exists())
                                {

                                    final String retName=dataSnapshot.child("name").getValue().toString();
                                    final String retCity=dataSnapshot.child("city").getValue().toString();
                                    final  String retBlood=dataSnapshot.child("blood").getValue().toString();

                                    holder.chatUserName.setText(retName);
                                    holder. userUserCity.setText(retCity);
                                    holder.chatUserBlood.setText(retBlood);


                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent chatIntent=new Intent(ContactSmsActivity.this,ChatActivity.class);
                                            chatIntent.putExtra("visit_user_id", userIDs);
                                            chatIntent.putExtra("visit_user_name",retName);
                                            startActivity(chatIntent);

                                        }
                                    });



                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_user_display,viewGroup,false);

                        return new ChatsViewHolder(view);
                    }
                };
        chatsList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder
    {

        // now here we include our layout(user display layout) which  contain user name profile status
        TextView chatUserName, userUserCity, chatUserBlood;


        public ChatsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            chatUserName=itemView.findViewById(R.id.chat_user_blood_profile_name);
            userUserCity =itemView.findViewById(R.id.chat_user_blood__disply_city);
            chatUserBlood=itemView.findViewById(R.id.chat_user_blood_display);
        }
    }
}
