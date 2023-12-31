package com.example.chattest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.chattest.databinding.ActivityMain2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity {

    private List<MessageList> messageLists = new ArrayList<>();
    ActivityMain2Binding binding;
    private String phone,email,name;
    MessageAdapter messageAdapter ;
   // MessageList messageList = new MessageList();
   // RecyclerView messagesrecyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ProgressDialog dialog;
   // int unsenMessage = 0;
   // String lastmessage = "";
    boolean dataset = false;
    String chatKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        //get intent data from register2
       // getdata(phone,name , email);
        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

       binding.MEssagesRecyclerVIew.setHasFixedSize(true);
      //  messagesrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.MEssagesRecyclerVIew.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        messageAdapter = new MessageAdapter(messageLists,MainActivity2.this);
        binding.MEssagesRecyclerVIew.setAdapter(messageAdapter);


        waiting();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profilpicurl = snapshot.child("users").child(phone).child("profile pic").getValue(String.class);

                if(!profilpicurl.isEmpty())
                Picasso.get().load(profilpicurl).into(binding.userProilepicHome);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            String   lastmessage = "";
            int unsenMessage = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();

                chatKey = "";
                for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){


                    String getmobile = dataSnapshot.getKey();
                    dataset = false;
                    if(!getmobile.equals(phone)){
                        String getname = dataSnapshot.child("name").getValue(String.class);
                        String getprofilePic = dataSnapshot.child("profile pic").getValue(String.class);


                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts = (int) snapshot.getChildrenCount();
                                if(getChatCounts > 0 ){
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey=getKey;

                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("message")){

                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if (getUserOne.equals(getmobile) && getUserTwo.equals(phone) || getUserOne.equals(phone) && getUserTwo.equals(getmobile)) {

                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("message").getChildren()){

                                                    long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    lastmessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    SharedPreferences sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor =sharedPreferences.edit() ;
                                                    editor.putString("last message" , lastmessage);
                                                    editor.apply();
                                                 //  long getLastMessage = Long.parseLong(String.valueOf(sharedPreferences.getLong("last message",0)));





                                                    /*if(getMessageKey > getLastMessage){
                                                        unsenMessage++;
                                                    }*/

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
                        if(!dataset){
                            dataset=true;
                            MessageList messageList = new MessageList(getname,getmobile,getprofilePic,lastmessage,unsenMessage,chatKey);
                            messageLists.add(messageList);
                            messageAdapter.update(messageLists);
                        }



                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getdata(String phone , String name , String email){

        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

    }

    private void waiting(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        dialog.show();
    }
}