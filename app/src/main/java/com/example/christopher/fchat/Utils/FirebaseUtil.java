package com.example.christopher.fchat.Utils;

import android.util.Log;

import com.example.christopher.fchat.Models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtil {
    private static String userName;
    private static String uuid = FirebaseAuth.getInstance().getUid();
    private static DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private static DatabaseReference messages = root.child("messages");
    private static DatabaseReference user = root.child("users");
    private static DatabaseReference userProperties = root.child("user_properties");

    private static final String TAG = "FirebaseUtil";

    public static String getUUID(){
        return uuid;
    }

    public static String getUserName(){

        root.child("users").child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // UserInformation uu = dataSnapshot.getValue(UserInformation.class);
                userName = dataSnapshot.child("name").getValue(String.class);
            /*    u.setAdmin_Name(uu.getName());
                userName = uu.getName();
                Log.e(TAG, "onDataChange: " + uu.getName() );*/
                Log.e(TAG, "onDataChange: " + userName );


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      return userName;
    }

    public static DatabaseReference getMessagesNode(){
        return messages;
    }

    public static DatabaseReference getRoot(){
        return root;
    }

    public static DatabaseReference getUserPropertiesNode() {
        return userProperties;
    }


    public static DatabaseReference getUserNode() {
        return user;
    }
}


