package com.example.chattest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.chattest.databinding.ActivityChatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chat extends AppCompatActivity {

    ActivityChatBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String getuserMobile = "";
    String chatKey;
    ChatAdapter chatAdapter;
    private List<ChatList> chatLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //get name and profile from messages adapter
        final String getname = getIntent().getStringExtra("name");
        final String getprofilepic = getIntent().getStringExtra("profile pic");
        chatKey = getIntent().getStringExtra("chat key");
        final String getmobile = getIntent().getStringExtra("mobile");

        SharedPreferences sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
        getuserMobile = sharedPreferences.getString("phonee", "");


        binding.userNameInsideChat.setText(getname);
        Picasso.get().load(getprofilepic).into(binding.profilePicChat);

        binding.chatRecyclerView.setHasFixedSize(true);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
        chatAdapter = new ChatAdapter(chatLists, Chat.this);
        binding.chatRecyclerView.setAdapter(chatAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatKey.isEmpty()) {
                    chatKey = "1";
                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);

                    }
                }


                if (snapshot.hasChild("chat")) {
                    if (snapshot.child("chat").child(chatKey).hasChild("message")) {
                        chatLists.clear();

                        for (DataSnapshot messageSnapchot : snapshot.child("chat").child(chatKey).child("message").getChildren()) {

                            if (messageSnapchot.hasChild("msg") && messageSnapchot.hasChild("mobile")) {

                                String messageTime = messageSnapchot.getKey();
                                String getmobile = messageSnapchot.child("mobile").getValue(String.class);
                                String getMsg = messageSnapchot.child("msg").getValue(String.class);
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy ", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                                ChatList chatList = new ChatList(getmobile, getname, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                               chatLists.add(chatList);
                                if (!getmobile.equals(getuserMobile)) {
                                   // chatLists.add(chatList);

                                    chatAdapter.updateChatList(chatLists);
                                    binding.chatRecyclerView.scrollToPosition(chatLists.size() - 1);

                                }

                            }

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getMessageText = binding.typeMessage.getText().toString();

                final String currentTime = String.valueOf(System.currentTimeMillis()).substring(0, 10);


                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getuserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getmobile);
                databaseReference.child("chat").child(chatKey).child("message").child(currentTime).child("msg").setValue(getMessageText);
                databaseReference.child("chat").child(chatKey).child("message").child(currentTime).child("mobile").setValue(getuserMobile);


                //clear edit text
                binding.typeMessage.setText("");
            }
        });


        binding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}