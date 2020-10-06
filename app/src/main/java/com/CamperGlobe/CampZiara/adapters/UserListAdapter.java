package com.CamperGlobe.CampZiara.adapters;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CamperGlobe.CampZiara.R;
import com.CamperGlobe.CampZiara.models.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.FriendListViewHolder> {

    ArrayList<UserObject> friendList;

    public UserListAdapter(ArrayList<UserObject> friendList){
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View friendView = inflater.inflate(R.layout.item_friend, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        friendView.setLayoutParams(layoutParams);

        return new FriendListViewHolder(friendView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendListViewHolder holder, final int position) {

        holder.name.setText(friendList.get(position).getName());
        holder.phone.setText(friendList.get(position).getPhoneNum());

        holder.checkBoxAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                friendList.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });
    }




    @Override
    public int getItemCount() {
        return friendList.size();
    }


     class FriendListViewHolder extends RecyclerView.ViewHolder{
        TextView name, phone;
        LinearLayout userItemLayout;
        CheckBox checkBoxAdd;

        FriendListViewHolder(View view){
            super(view);

            name = view.findViewById(R.id.friend_name);
            phone = view.findViewById(R.id.friend_phone);
            checkBoxAdd = view.findViewById(R.id.checkboxAdd);
            userItemLayout = view.findViewById(R.id.userItemLayout);
        }

    }
}


