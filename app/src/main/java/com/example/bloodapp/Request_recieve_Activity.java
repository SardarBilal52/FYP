package com.example.bloodapp;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Request_recieve_Activity extends AppCompatActivity {

    private RecyclerView RecieveRequestedBloodRecyclerList;

    private DatabaseReference ChatRequestRef ,UserRef ,ContactsRef, requesterId;
    private FirebaseAuth mAuth;
    String currentUserID;

    private String contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_recieve_);

        //###########################################################################################


        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        requesterId= FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef= FirebaseDatabase.getInstance().getReference().child("Request UserData");
        ChatRequestRef= FirebaseDatabase.getInstance().getReference().child("Blood Requests");
        ContactsRef=FirebaseDatabase.getInstance().getReference().child("Contacts");



        //############################################################################################



        RecieveRequestedBloodRecyclerList=(RecyclerView) findViewById(R.id.recieve_blood_requst);
        RecieveRequestedBloodRecyclerList.setLayoutManager(new LinearLayoutManager(this));


    }
//#################################################################################################################






    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(ChatRequestRef.child(currentUserID),Contacts.class) // current user Id rawakhla ao contact class na user name status etc
                        .build();


        FirebaseRecyclerAdapter<Contacts,RequestedBloodViewHolder> adapter=
                new FirebaseRecyclerAdapter<Contacts, RequestedBloodViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestedBloodViewHolder holder, final int position, @NonNull Contacts model)
                    {


                        final String list_user_id=getRef(position).getKey();

                        DatabaseReference getTypeRef=getRef(position).child("request_type").getRef();


                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {
                                   final String type=dataSnapshot.getValue().toString();

                                    if(type.equals("received"))// hm sirf received request ko retrive karke requst wala portion me show karte hy
                                    {
                                        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot)
                                            { for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {


                                                String hospital,location,quantity, blood;

                                                hospital=item_snapshot.child("required_hospital").getValue().toString();
                                               contact=item_snapshot.child("required_number").getValue().toString();
                                                location=item_snapshot.child("required_location").getValue().toString();
                                                quantity=item_snapshot.child("required_quantity").getValue().toString();
                                                blood=item_snapshot.child("required_blood").getValue().toString();

                                                holder.RequestBlooduserMessage.setText( blood+ " Blood  is Urgently Req at "+hospital+ " ("+location+")"  );
//
                                            }


                                                requesterId.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot)
                                                    {
                                                        final String requestedNamee=dataSnapshot.child("name").getValue().toString();
                                                        holder.RequestBlooduserName.setText(requestedNamee);

                                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v)
                                                            {
                                                                CharSequence options[]=new CharSequence[]
                                                                        {
                                                                                "Accept",
                                                                                "Cancel",
                                                                                "call ?",
                                                                                "Message ?"
                                                                        };

                                                                final AlertDialog.Builder builder=new AlertDialog.Builder(Request_recieve_Activity.this);
                                                                builder.setTitle(requestedNamee);

                                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i)
                                                                    {
                                                                        if(i==0)
                                                                        {// ye reciever side kele hy
                                                                            ContactsRef.child(currentUserID).child(list_user_id).child("Contact")
                                                                                    .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    if(task.isSuccessful())
                                                                                    { // aur ye sender side kele hy
                                                                                        ContactsRef.child(list_user_id).child(currentUserID).child("Contact")
                                                                                                .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task)
                                                                                            {
                                                                                                if(task.isSuccessful())
                                                                                                { // yaha per ham resceiver ke requestchat se requset remove karte hy kynke requset accept hogai hy
                                                                                                    ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                                                            .removeValue()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                                                {
                                                                                                                    if (task.isSuccessful())
                                                                                                                    {// yaha per ham sender ke side se requset remove karte hy kynke requset accept hogai hy
                                                                                                                        ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                                                                .removeValue()
                                                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                                                                    {
                                                                                                                                        if (task.isSuccessful())
                                                                                                                                        {
                                                                                                                                            Toast.makeText(Request_recieve_Activity.this, "New Contact Added", Toast.LENGTH_SHORT).show();

                                                                                                                                            startActivity(new Intent(Request_recieve_Activity.this,ContactSmsActivity.class));
                                                                                                                                        }

                                                                                                                                    }
                                                                                                                                });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                }

                                                                                            }
                                                                                        });
                                                                                    }


                                                                                }
                                                                            });
                                                                        }

                                                                        // ye cancel kele hy
                                                                        if(i==1)
                                                                        {

                                                                            ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                                        {
                                                                                            if (task.isSuccessful())
                                                                                            {// yaha per ham sender ke side se requset remove karte hy kynke requset accept hogai hy
                                                                                                ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                                        .removeValue()
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task)
                                                                                                            {
                                                                                                                if (task.isSuccessful())
                                                                                                                {
                                                                                                                    Toast.makeText(Request_recieve_Activity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                                                                                                                }

                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        }
                                                                                    });

                                                                        }

                                                                        if(i==2)
                                                                        {

                                                                            String callNo=contact;
                                                                            Intent callNoIntent = new Intent(Intent.ACTION_CALL);

                                                                            callNoIntent.setData(Uri.parse("tel:" + callNo));
                                                                            if (ActivityCompat.checkSelfPermission(Request_recieve_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                                                // TODO: Consider calling
                                                                                //    ActivityCompat#requestPermissions
                                                                                // here to request the missing permissions, and then overriding
                                                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                                                //                                          int[] grantResults)
                                                                                // to handle the case where the user grants the permission. See the documentation
                                                                                // for ActivityCompat#requestPermissions for more details.
                                                                                return;
                                                                            }
                                                                            startActivity(callNoIntent);

                                                                        }

                                                                        if(i==3)
                                                                        {
                                                                            Intent smsToBLOODSEEKER=new Intent(Request_recieve_Activity.this,SmsToBloodSeeker.class);
                                                                            smsToBLOODSEEKER.putExtra("SmsToBloodSEEKER",contact);
                                                                            smsToBLOODSEEKER.putExtra("message_contact","message_contact");

                                                                            startActivity(smsToBLOODSEEKER);
                                                                        }


                                                                    }
                                                                });
                                                                builder.show();


                                                            }
                                                        });





                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError)
                                            {

                                            }
                                        });

                                    }










                                    /////////////////////


                                    else if(type.equals("sent"))
                                    {
                                        requesterId.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot)
                                            {

                                                final String requestUserName=dataSnapshot.child("name").getValue().toString();

                                                holder.RequestBlooduserName.setText(requestUserName);
                                                holder.RequestBlooduserMessage.setText("You Sent a Blood Request to "+requestUserName);

                                                // here we set on click listener on coming request
                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        CharSequence options[]=new CharSequence[]
                                                                {
                                                                        "Cancel chat request"
                                                                };

                                                        final AlertDialog.Builder builder=new AlertDialog.Builder(Request_recieve_Activity.this);
                                                        builder.setTitle("Already Sent Blood Request");

                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i)
                                                            {
                                                                // ye cancel kele hy
                                                                if(i==0)
                                                                {

                                                                    ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    if (task.isSuccessful())
                                                                                    {// yaha per ham sender ke side se requset remove karte hy kynke requset accept hogai hy
                                                                                        ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                                    {
                                                                                                        if (task.isSuccessful())
                                                                                                        {
                                                                                                            Toast.makeText(Request_recieve_Activity.this, "You have cancelled the chat request", Toast.LENGTH_SHORT).show();
                                                                                                        }

                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });

                                                                }


                                                            }
                                                        });
                                                        builder.show();


                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError)
                                            {

                                            }
                                        });

                                    }




                                    ///////////////////////

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
                    public RequestedBloodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recieve_request_display,viewGroup,false);
                        //es ke zareye ham user display layout ko access karte hy

                        RequestedBloodViewHolder holder=new RequestedBloodViewHolder(view);

                        return holder;
                    }
                };

        RecieveRequestedBloodRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }












//*******************************************************************************************************************************
    public static class RequestedBloodViewHolder extends RecyclerView.ViewHolder
    {

       TextView RequestBlooduserName,RequestBlooduserMessage ;
        private Button userBloodRequestAccept, userBloodRequestReject;




        public RequestedBloodViewHolder(@NonNull View itemView)
        {
            super(itemView);



            RequestBlooduserName=itemView.findViewById(R.id.user_blood_request_profile_name);
            RequestBlooduserMessage=itemView.findViewById(R.id.user_blood_request_display_msg);


        }
    }
}
