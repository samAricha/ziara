package com.CamperGlobe.CampZiara.activities.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;


import com.CamperGlobe.CampZiara.R;
import com.CamperGlobe.CampZiara.adapters.UserListAdapter;
import com.CamperGlobe.CampZiara.utils.CountryToPhonePrefix;
import com.CamperGlobe.CampZiara.models.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView friendsListRecyclerView;
    private RecyclerView.LayoutManager friendsListLayoutManager;
    private RecyclerView.Adapter friendsListAdapter;

    ArrayList<UserObject> friendsList, userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);


        friendsList = new ArrayList<>();
        userList = new ArrayList<>();
        initializeRecyclerview();
        getContactList();


    }

    private void getContactList(){

        String isoPrefix = getCountryIso();

        Cursor phoneNums = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        while (phoneNums.moveToNext()){
            String name = phoneNums.getString(phoneNums.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = phoneNums.getString(phoneNums.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            number = number.replace(" ", "");
            number = number.replace("-", "");
            number = number.replace(")", "");
            number = number.replace("(", "");
            number = number.trim();

            if(!String.valueOf(number.charAt(0)).equals("+")){
                number = number.substring(1);
                number = isoPrefix + number;
            }

            UserObject mContact = new UserObject("", name, number);
            friendsList.add(mContact);

            getUsersDetails(mContact);
        }
    }

    private void getUsersDetails(final UserObject mContact) {


        DatabaseReference userDBRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = userDBRef.orderByChild("phone").equalTo(mContact.getPhoneNum());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String phone = "", name = "";

                    for(DataSnapshot childSnapShot: snapshot.getChildren()){
                            if (childSnapShot.child("phone").getValue() != null) {
                                phone = childSnapShot.child("phone").getValue().toString();
                            }
                            if (childSnapShot.child("name").getValue() != null) {
                                name = childSnapShot.child("name").getValue().toString();
                            }

                            UserObject mUser = new UserObject(childSnapShot.getKey(), name, phone);

                            if (name.equals(phone)) {

                                for (UserObject contactIterator : friendsList) {
                                    if (contactIterator.getPhoneNum().equals(phone)) {
                                        mUser.setName(contactIterator.getName());
                                    }
                                }
                            }

                            userList.add(mUser);
                            friendsListAdapter.notifyDataSetChanged();

                            return;
                    }

                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initializeRecyclerview() {
        friendsListRecyclerView = findViewById(R.id.friendsList);
        friendsListRecyclerView.setHasFixedSize(false);
        friendsListRecyclerView.setNestedScrollingEnabled(false);

        friendsListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        friendsListRecyclerView.setLayoutManager(friendsListLayoutManager);

        friendsListAdapter = new UserListAdapter(userList);//to display only the registered contacts not the whole contact
        friendsListRecyclerView.setAdapter(friendsListAdapter);

    }

    private String getCountryIso(){
        String iso = null;

        TelephonyManager telephonyManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso() != null){
            if(!telephonyManager.getNetworkCountryIso().equals("")){
                iso = telephonyManager.getNetworkCountryIso();
            }
        }

        return CountryToPhonePrefix.getPhone(iso);
    }
}