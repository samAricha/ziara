package com.CamperGlobe.CampZiara.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CamperGlobe.CampZiara.R;
import com.CamperGlobe.CampZiara.models.ChatObject;
import com.CamperGlobe.CampZiara.models.MessageObject;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder> {

    ArrayList<MessageObject> messageList;

    public ChatMessagesAdapter(ArrayList<MessageObject> messageList){
        this.messageList = messageList;
    }


    @NonNull
    @Override
    public ChatMessagesAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layoutView = inflater.inflate(R.layout.item_message, null, false);
        RecyclerView.LayoutParams rLp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(rLp);

        MessageViewHolder messageViewHolder = new MessageViewHolder(layoutView);
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatMessagesAdapter.MessageViewHolder holder, int position) {
        holder.messageTxt.setText(messageList.get(position).getMessage());
        holder.messageSender.setText(messageList.get(position).getSenderId());

        if(messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty())
            holder.viewMediaBtn.setVisibility(View.GONE);

        holder.viewMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                        .setStartPosition(0)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

   class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageTxt, messageSender;
        LinearLayout messageLayout;
        Button viewMediaBtn;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLayout = itemView.findViewById(R.id.messageLayout);
            messageTxt = itemView.findViewById(R.id.messageContent);
            messageSender = itemView.findViewById(R.id.sender);
            viewMediaBtn = itemView.findViewById(R.id.viewMediaBtn);
        }
    }
}
