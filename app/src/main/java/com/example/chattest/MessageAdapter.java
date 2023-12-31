package com.example.chattest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {

    private List <MessageList> messageList;
    private Context context;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public MessageAdapter(List<MessageList> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=  (LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout,parent,false));
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        MessageList list2 = messageList.get(position);

        if(!list2.getProfilepic().isEmpty())
            Picasso.get().load(list2.getProfilepic()).into(holder.profilePic);

        holder.name.setText(list2.getName());
        SharedPreferences sharedPreferences =context.getSharedPreferences("shared", Context.MODE_PRIVATE);
        String lastmsg = sharedPreferences.getString("last message" , "last message");

        holder.lastMeassage.setText(lastmsg);

        if (list2.getUnSeenMessage() == 0){
            holder.unseenmessages.setVisibility(View.GONE);


        }
        else{
            holder.unseenmessages.setVisibility(View.VISIBLE);
            holder.unseenmessages.setText(list2.getUnSeenMessage()+"");
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Chat.class);
                intent.putExtra("mobile" , list2.getPhone());
                intent.putExtra("name" , list2.getName());
                intent.putExtra("profile pic",list2.getProfilepic());
                intent.putExtra("chat key" , list2.getChatKey());

                context.startActivity(intent);

            }
        });

    }

    public  void update(List <MessageList> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static  class viewHolder extends RecyclerView.ViewHolder{
        private ImageView profilePic;
        private TextView name,lastMeassage,unseenmessages;
        private LinearLayout linearLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic);
            name=itemView.findViewById(R.id.user_name_in_mesaages);
            lastMeassage = itemView.findViewById(R.id.last_message);
            unseenmessages = itemView.findViewById(R.id.unseen_message);
            linearLayout = itemView.findViewById(R.id.liner_root);
        }
    }

}
