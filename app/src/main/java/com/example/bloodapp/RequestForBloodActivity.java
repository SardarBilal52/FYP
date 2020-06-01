package com.example.bloodapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestForBloodActivity extends AppCompatActivity {


    private RecyclerView RequestedBloodRecyclerList;
    private DatabaseReference BloodRequestUserRef;

    //.......................................................

    private String reciverUserrID, senderUseID ,Current_State;
    private DatabaseReference UserRef ,ChatRequestRef;
    private FirebaseAuth mAuth;
    //........................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_blood);

        BloodRequestUserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        BloodRequestUserRef.keepSynced(true);


        RequestedBloodRecyclerList=(RecyclerView) findViewById(R.id.request_for_blood_recycler_list);
        RequestedBloodRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        //.............................................................................

        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestRef= FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        mAuth=FirebaseAuth.getInstance();
        senderUseID=mAuth.getCurrentUser().getUid();
        Current_State="new";

        //.............................................................................

    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(BloodRequestUserRef,Contacts.class)
                        .build();



        FirebaseRecyclerAdapter<Contacts, RequestedBloodViewHolder> adapter=
                new FirebaseRecyclerAdapter<Contacts, RequestForBloodActivity.RequestedBloodViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull RequestForBloodActivity.RequestedBloodViewHolder holder, final int position, @NonNull Contacts model)
                    {
                        holder.RequestBlooduserName.setText(model.getName());
                        holder.RequestBlooduserBlood.setText(model.getblood());
                        holder. RequestBlooduserCity.setText(model.getCity());


//                        holder.userRequestBloodButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                reciverUserrID=getRef(position).getKey();
//
//                                SendBloodRequest();
//
//
//
//
//
//
//
//
//
//
//                            }
//                        });


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String visit_user_id=getRef(position).getKey();

                                Intent profileIntent=new Intent(RequestForBloodActivity.this, SendingRequestActivity.class);
                                profileIntent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileIntent);

                            }
                        });

                    }





                    @NonNull
                    @Override
                    public RequestForBloodActivity.RequestedBloodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_blood_request_display,viewGroup,false);
                        RequestForBloodActivity.RequestedBloodViewHolder viewHolder =new RequestForBloodActivity.RequestedBloodViewHolder(view);

                        return viewHolder;
                    }
                };

        RequestedBloodRecyclerList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class RequestedBloodViewHolder extends RecyclerView.ViewHolder
    {

        TextView RequestBlooduserName, RequestBlooduserBlood, RequestBlooduserCity;
//      public Button userRequestBloodButton;




        public RequestedBloodViewHolder(@NonNull View itemView)
        {
            super(itemView);



            RequestBlooduserName=itemView.findViewById(R.id.user_blood_request_profile_name);
            RequestBlooduserBlood=itemView.findViewById(R.id.user_blood_request_display);
            RequestBlooduserCity=itemView.findViewById(R.id.user_blood_request_disply_city);
//            userRequestBloodButton=itemView.findViewById(R.id.user_blood_request_send_button);


        }
    }

    //.............................................................................................


    private void SendBloodRequest()
    {

        ChatRequestRef.child(senderUseID).child(reciverUserrID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {



                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        if(task.isSuccessful())
                        {
                            ChatRequestRef.child(reciverUserrID).child(senderUseID)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(RequestForBloodActivity.this, "you sent a blood request", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }


                    }
                });
    }

    private void CancelChatRequest()
    {
        ChatRequestRef.child(senderUseID).child(reciverUserrID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            ChatRequestRef.child(reciverUserrID).child(senderUseID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Current_State="new";

                                            }

                                        }
                                    });
                        }

                    }
                });
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


}
