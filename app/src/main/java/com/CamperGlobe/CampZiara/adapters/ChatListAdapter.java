package com.CamperGlobe.CampZiara.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CamperGlobe.CampZiara.R;
import com.CamperGlobe.CampZiara.models.ChatObject;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.chatListViewHolder> {

    ArrayList<ChatObject> chatList;


    public ChatListAdapter(ArrayList<ChatObject> chatList){
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public chatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View chatView = inflater.inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        chatView.setLayoutParams(layoutParams);

        return new chatListViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull chatListViewHolder holder, final int position) {

        holder.chatTitle.setText(chatList.get(position).getChatId());

        holder.chatItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }




    public class chatListViewHolder extends RecyclerView.ViewHolder{
        TextView chatTitle;
        LinearLayout chatItemLayout;

        chatListViewHolder(View view){
            super(view);

            chatTitle = view.findViewById(R.id.chatTitle);
            chatItemLayout = view.findViewById(R.id.chatLayout);
        }

    }
}

