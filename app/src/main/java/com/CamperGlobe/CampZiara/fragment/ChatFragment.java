package com.CamperGlobe.CampZiara.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CamperGlobe.CampZiara.R;
import com.CamperGlobe.CampZiara.activities.chat.FindUserActivity;
import com.CamperGlobe.CampZiara.adapters.ChatListAdapter;
import com.CamperGlobe.CampZiara.models.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private RecyclerView chatListRV;
    private RecyclerView.Adapter chatListAdapter;
    private RecyclerView.LayoutManager chatListLayoutManager;

    ArrayList<ChatObject> chatList;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatListRV = view.findViewById(R.id.chatList);
        button = view.findViewById(R.id.findUsers);
        chatList = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindUserActivity.class);
                startActivity(intent);
            }
        });

        initializeRecyclerView();
        getChatList();
        return view;
    }


    private void getChatList(){
        DatabaseReference chatDbRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getUid()).child("chat");

        chatDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot childSnapshot : snapshot.getChildren()){
                        ChatObject mChat = new ChatObject(childSnapshot.getKey());
                        chatList.add(mChat);
                       chatListAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }


    private void initializeRecyclerView(){

        chatListRV.setNestedScrollingEnabled(false);
        chatListRV.setHasFixedSize(false);

        chatListLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        chatListRV.setLayoutManager(chatListLayoutManager);

        chatListAdapter = new ChatListAdapter(chatList);
        chatListRV.setAdapter(chatListAdapter);
    }


}