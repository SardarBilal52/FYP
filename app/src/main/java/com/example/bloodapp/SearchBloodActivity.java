package com.example.bloodapp;


import android.Manifest;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchBloodActivity extends AppCompatActivity {

    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blood);

        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef.keepSynced(true);


        FindFriendsRecyclerList=(RecyclerView) findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));






    }



    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UserRef,Contacts.class)
                        .build();



        FirebaseRecyclerAdapter<Contacts,FindFriendViewHolder> adapter=
                new FirebaseRecyclerAdapter<Contacts, FindFriendViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull Contacts model)
                    {
                        holder.userName.setText(model.getName());
                        holder.userBlood.setText(model.getblood());
                        holder.userCity.setText(model.getCity());

                       final String UserPhonNumber=model.getNumber();


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String visit_user_id=getRef(position).getKey();

                                Intent profileIntent=new Intent(SearchBloodActivity.this, SendingSmsActivity.class);
                                profileIntent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileIntent);

                            }
                        });


                        holder.userCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                new TTFancyGifDialog.Builder(SearchBloodActivity.this)
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
                                                Toast.makeText(SearchBloodActivity.this,"Calling...",Toast.LENGTH_SHORT).show();
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);

                                                callIntent.setData(Uri.parse("tel:" + UserPhonNumber));
                                                if (ActivityCompat.checkSelfPermission(SearchBloodActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

                                            }
                                        })
                                        .OnNegativeClicked(new TTFancyGifDialogListener() {
                                            @Override
                                            public void OnClick() {
                                                Toast.makeText(SearchBloodActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .build();



                            }
                        });


                        holder.userMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visit_user_id=getRef(position).getKey();

                                Intent profileIntent=new Intent(SearchBloodActivity.this, SendingSmsActivity.class);
                                profileIntent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileIntent);

                            }
                        });

                    }





                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout,viewGroup,false);
                        FindFriendViewHolder viewHolder =new FindFriendViewHolder(view);

                        return viewHolder;
                    }
                };

        FindFriendsRecyclerList.setAdapter(adapter);
        adapter.startListening();


    }


    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName, userBlood, userCity;
        Button userCall, userMessage;




        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);



            userName=itemView.findViewById(R.id.user_profile_name);
            userBlood=itemView.findViewById(R.id.user_display_blood);
            userCity=itemView.findViewById(R.id.user_disply_city);
            userCall=itemView.findViewById(R.id.user_call_btn);
            userMessage=itemView.findViewById(R.id.user_message_btn);


        }
    }
}
