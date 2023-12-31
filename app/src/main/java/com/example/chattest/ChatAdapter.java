package com.example.chattest;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewHolder> {

    private List<ChatList> chatLists;
    private Context context;
    private String userMobile;
//    SharedPreferences sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE);

    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
        this.userMobile = sharedPreferences.getString("phonee", "no mobile");
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ChatList list2 = chatLists.get(position);

        if (list2.getMobile().equals(userMobile)) {

            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.recicveLayout.setVisibility(View.GONE);
            holder.sentMessage.setText(list2.getMessage());
            holder.sentTime.setText(list2.getDate() + "  " + list2.getTime());

        } else {
            holder.sendLayout.setVisibility(View.GONE);
            holder.recicveLayout.setVisibility(View.VISIBLE);
            holder.recevieMessage.setText(list2.getMessage());
            holder.recevieTime.setText(list2.getDate() + "  " + list2.getTime());

        }

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists) {
        this.chatLists = chatLists;
        notifyDataSetChanged();

    }


    static class viewHolder extends RecyclerView.ViewHolder {

        LinearLayout sendLayout, recicveLayout;
        TextView sentMessage, recevieMessage;
        TextView sentTime, recevieTime;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            sendLayout = itemView.findViewById(R.id.sent_layout);
            recicveLayout = itemView.findViewById(R.id.recevie_layout);
            sentMessage = itemView.findViewById(R.id.sent_message);
            recevieMessage = itemView.findViewById(R.id.receive_message);
            sentTime = itemView.findViewById(R.id.send_messageTime);
            recevieTime = itemView.findViewById(R.id.received_messageTime);

        }
    }


}
