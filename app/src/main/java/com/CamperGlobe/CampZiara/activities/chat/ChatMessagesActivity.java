package com.CamperGlobe.CampZiara.activities.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.CamperGlobe.CampZiara.R;
import com.CamperGlobe.CampZiara.adapters.ChatMessagesAdapter;
import com.CamperGlobe.CampZiara.adapters.MediaAdapter;
import com.CamperGlobe.CampZiara.models.ChatObject;
import com.CamperGlobe.CampZiara.models.MessageObject;
import com.CamperGlobe.CampZiara.models.UserObject;
import com.CamperGlobe.CampZiara.utils.SendNotification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;

public class ChatMessagesActivity extends AppCompatActivity {

    private RecyclerView messageRV, mediaRV;
    private RecyclerView.Adapter messageRVAdapter, mediaRVAdapter;
    private RecyclerView.LayoutManager messageLayoutManager, mediaLayoutManager;

    ArrayList<MessageObject> messageList;

    ChatObject mChatObject;
    DatabaseReference chatMessagesDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        mChatObject = (ChatObject)getIntent().getSerializableExtra("ChatObject");


        chatMessagesDbRef  = FirebaseDatabase.getInstance().getReference().child("chat").child(mChatObject.getChatId());

        Button sendTxtBtn = findViewById(R.id.sendTxtBtn);
        Button addImageBtn = findViewById(R.id.addImgBtn);
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        sendTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        
        initializeMessageRecyclerView();
        initializeMediaRecyclerView();
        getChatMessages();

    }



    private void getChatMessages() {
        chatMessagesDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists()){
                    String text = "",
                            senderID= "" ;
                    ArrayList<String> mediaUrlList = new ArrayList<>();


                    if(snapshot.child("text").getValue() != null){
                        text = snapshot.child("text").getValue().toString();
                    }

                    if(snapshot.child("sender").getValue() != null){
                        senderID = snapshot.child("sender").getValue().toString();
                    }

                    if(snapshot.child("media").getChildrenCount() > 0){
                        for(DataSnapshot mediaSnapShot : snapshot.child("media").getChildren()){
                            mediaUrlList.add(mediaSnapShot.getValue().toString());
                        }
                    }


                    MessageObject messageObject = new MessageObject(snapshot.getKey(), senderID, text, mediaUrlList);
                    messageList.add(messageObject);
                    messageLayoutManager.scrollToPosition(messageList.size() - 1);
                    messageRVAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }



    int totalMediaUploaded = 0;
    ArrayList<String> mediaIdList = new ArrayList<>();
    EditText txtMessage;

    private void sendMessage() {
        txtMessage = findViewById(R.id.textMessage);
        String messageId;

        messageId = chatMessagesDbRef.push().getKey();
        final DatabaseReference newMessageRef = chatMessagesDbRef.child(messageId);

        final Map newMessageMap = new HashMap<>();

        newMessageMap.put("sender", FirebaseAuth.getInstance().getUid());
        if(!txtMessage.getText().toString().isEmpty())
            newMessageMap.put("text", txtMessage.getText().toString());


        if(!mediaUriList.isEmpty()){

            for(String mediaUri : mediaUriList){
                String mediaId = newMessageRef.child("media").push().getKey();
                mediaIdList.add(mediaId);
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("chat").child(mChatObject.getChatId()).child(messageId).child(mediaId);

                UploadTask uploadTask = filePath.putFile(Uri.parse(mediaUri));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newMessageMap.put("/media/" + mediaIdList.get(totalMediaUploaded) + "/", uri.toString());


                                totalMediaUploaded++;
                                if(totalMediaUploaded == mediaUriList.size()){
                                    updateDbasewithMessage(newMessageRef, newMessageMap);
                                    Toast.makeText(ChatMessagesActivity.this, "sent", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }

        }else {
            if(!txtMessage.getText().toString().isEmpty()) {
                updateDbasewithMessage(newMessageRef, newMessageMap);
                Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
            }
        }
        txtMessage.setText(null);

    }


    private void updateDbasewithMessage(DatabaseReference newMessageDbRef, Map newMessageMap){
        newMessageDbRef.updateChildren(newMessageMap);
        txtMessage.setText(null);
        mediaUriList.clear();
        mediaIdList.clear();
        mediaRVAdapter.notifyDataSetChanged();


        String message;

        if(newMessageMap.get("text") != null)
            message = newMessageMap.get("text").toString();
        else
            message = "sent Media";

        for(UserObject mUser : mChatObject.getUserObjectArrayList()){
            if(!mUser.getuId().equals(FirebaseAuth.getInstance().getUid())){
                new SendNotification(message, "New Message", mUser.getNotificationKey());
            }
        }
    }



    private void initializeMessageRecyclerView() {
        messageList = new ArrayList<>();
        messageRV = findViewById(R.id.messageListRV);
        messageRV.setHasFixedSize(false);
        messageRV.setNestedScrollingEnabled(false);
        messageLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        messageRV.setLayoutManager(messageLayoutManager);
        messageRVAdapter = new ChatMessagesAdapter(messageList);
        messageRV.setAdapter(messageRVAdapter);
    }

    ArrayList<String> mediaUriList = new ArrayList<>();
    int PICK_IMAGE_INTENT = 1;

    private void initializeMediaRecyclerView() {
        mediaUriList = new ArrayList<>();
        mediaRV = findViewById(R.id.mediaListRV);
        mediaRV.setHasFixedSize(false);
        mediaRV.setNestedScrollingEnabled(false);
        mediaLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mediaRV.setLayoutManager(mediaLayoutManager);
        mediaRVAdapter = new MediaAdapter(getApplicationContext(), mediaUriList);
        mediaRV.setAdapter(mediaRVAdapter);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if( Build.VERSION.SDK_INT >= 18) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image(s)"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == PICK_IMAGE_INTENT){
                if(data.getClipData() == null) {
                    mediaUriList.add(data.getData().toString());
                }else{
                    for(int i = 0; i<data.getClipData().getItemCount(); i++){
                        mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }

                mediaRVAdapter.notifyDataSetChanged();

            }
        }
    }
}