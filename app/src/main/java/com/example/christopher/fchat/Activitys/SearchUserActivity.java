package com.example.christopher.fchat.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.example.christopher.fchat.Recyclerviews.UserSearch;
import com.example.christopher.fchat.Models.UserInformation;
import com.example.christopher.fchat.Models.UserProperties;
import com.example.christopher.fchat.R;
import static com.example.christopher.fchat.Utils.Constants.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.christopher.fchat.Utils.Constants.UserName;
import static com.example.christopher.fchat.Utils.Constants.no_name;
import static com.example.christopher.fchat.Utils.FirebaseUtil.getUUID;

import static com.example.christopher.fchat.Utils.FirebaseUtil.getUserNode;

public class SearchUserActivity extends AppCompatActivity {
    private static final String TAG = "SearchUserActivity";

    private String[] Contactlist = new String[10];
    private volatile UserProperties u = new UserProperties();

    private String uid = FirebaseAuth.getInstance().getUid();
    public  String userName;
    private String userID;

    private String DisplayUserName;
    private String RoomID;


    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user_properties");
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();


    private UserSearch adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        setupToolbar();
        DatabaseReference ref = getUserNode();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ReadDB(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void ReadDB(DataSnapshot dataSnapshot) {
        Log.e(TAG, "ReadDB: dataSnapshot  " + dataSnapshot.toString() );
        String uid = getUUID();

        final ArrayList<UserInformation> Users = new ArrayList<>();

        UserInformation u = dataSnapshot.child(uid).getValue(UserInformation.class);
        Log.e("Benutzername = ", u.getName());
        int foo = 0;

        for (DataSnapshot ds : dataSnapshot.getChildren()) {


            String founduser = ds.child("name").getValue(String.class);
            String foundUID = ds.child("uid").getValue(String.class);


            UserInformation i = new UserInformation();

            i.setUID(foundUID);
            i.setName(founduser);

            Log.e("Gefundene Nutzer=", founduser);
            Log.e("Gefundene UID", foundUID);


            if (i.getUID().equals(uid)) {
                // if(!foundUID.equals(uid) funktionierte nicht, deshalb "falsch" herum.
            } else {
                Users.add(i);
                Contactlist[foo] = foundUID + " " + founduser;
                Log.e(TAG, "Gefundener nutzer: " + Contactlist[foo] + " in reihe: " + i);
                foo++;
            }

        }

        RecyclerView recyclerView = findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserSearch(this, Users);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new UserSearch.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                com.example.christopher.fchat.Models.UserInformation u = Users.get(position);

                openStartChat(u);
            }
        });


    }


    private void setupToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar_11);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Search for new user");
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }



    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserInformation, MODE_PRIVATE);
        DisplayUserName = sharedPreferences.getString(UserName, no_name);
        Log.e(TAG, DisplayUserName);
    }


    private void openStartChat(UserInformation u) {
        Intent intent = new Intent(SearchUserActivity.this, StartChatActivity.class);
        Log.e(TAG, "openStartChat: " + RoomID);
        Log.e(TAG, "openStartChat: " + userID);
        Log.e(TAG, "openStartChat: " + userName );
        intent.putExtra("RoomID", RoomID);
        intent.putExtra("userID", u.getUID());
        intent.putExtra("userName", u.getName());
        startActivity(intent);
        this.finishAffinity();
    }
}

