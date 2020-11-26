package com.example.christopher.fchat.Activitys;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.christopher.fchat.Recyclerviews.MainOverview;
import com.example.christopher.fchat.Models.Channel;
import com.example.christopher.fchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static com.example.christopher.fchat.Utils.Constants.UserID;
import static com.example.christopher.fchat.Utils.Constants.UserInformation;
import static com.example.christopher.fchat.Utils.Constants.UserName;


public class MainActivity extends AppCompatActivity {


    private ArrayList<Channel> mExampleList;

    private RecyclerView mRecyclerView;
    private MainOverview mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView nav_header;


    private DrawerLayout mDrawerLayout;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String CurrentUserID = FirebaseAuth.getInstance().getUid();
    private String CurrentUser;


    // Debug TAG
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setButtons();

        // Simple check if user is logged in
       /* if (user != FirebaseAuth.getInstance().getCurrentUser()) {*/
            Toast.makeText(MainActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Log.e(TAG, "onCreate: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GetUserInformation(dataSnapshot);
                    ReadChannels(dataSnapshot);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failing redirect", Toast.LENGTH_SHORT).show();
                }
            });



        Toolbar toolbar = findViewById(R.id.toolbar_11);
        setSupportActionBar(toolbar);


        ActionBar actionbar = getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);




        FloatingActionButton fab = findViewById(R.id.fabMessages);
        fab.bringToFront();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchUserActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public ActionBar getSupportActionBar() {
        // Small hack here so that Lint does not warn me in every single activity about null
        // action bar
        return super.getSupportActionBar();
    }

    @Override
    public void onBackPressed() {
    } // Disables back button, so the user cant access the loginscreen accidentally

    /**
     * onPause() Saves the name of the current activity.
     * When the user will open the Application later, the App will relaunch the latest activity
     */

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void GetUserInformation(DataSnapshot dataSnapshot) {

        CurrentUser = dataSnapshot.child("users").child(CurrentUserID).child("name").getValue(String.class);

        Log.e(TAG, "LoadUserInformation: " + CurrentUser);

        // Save Userinformation on device to reduce Networktraffic
        SaveUserdata(CurrentUser, CurrentUserID);


    }

    @NonNull
    private void ReadChannels(DataSnapshot dataSnapshot) {

        CurrentUserID = FirebaseAuth.getInstance().getUid();
        if (CurrentUserID != null) {
            DataSnapshot chats = dataSnapshot.child("user_properties").child(CurrentUserID);
            mExampleList = new ArrayList<>();


            if (chats.exists() || chats.hasChildren()) {
                for (DataSnapshot ds : chats.getChildren()) {
                    String DisplayName;


                    String member1 = ds.child("admin_Name").getValue(String.class);
                    String meber1ID = ds.child("admin").getValue(String.class);

                    String member2 = ds.child("member_Name").getValue(String.class);
                    String member2ID = ds.child("member").getValue(String.class);

                    String Room = ds.child("chatRoom").getValue(String.class);
                    // Get most recent message
                    String newest_path = ds.child(Room).getKey();

                    String newest = getNewestMessage(dataSnapshot, Room);
                    newest = ds.child(Room).child(newest_path).child("message").getValue(String.class);


                    //.child("message").getValue(String.class);


                    Log.e(TAG, "ReadChannels: Current User ID " + CurrentUserID);
                    Log.e(TAG, "ReadChannels: Member" + member1);
                    if (CurrentUserID.equals(member1)) {
                        DisplayName = member1;

                    } else {
                        DisplayName = member2;

                    }

                    String Latest_message = getNewestMessage(dataSnapshot, Room);
                    Channel c = new Channel(R.drawable.ic_home, DisplayName, Latest_message, Room);
                    mExampleList.add(c);
                }
            }
        }


        Log.e(TAG, "ReadChannels: " + dataSnapshot.toString());
        Log.e(TAG, Long.toString(dataSnapshot.getChildrenCount()));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MainOverview(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MainOverview.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String RoomID = mExampleList.get(position).getRoom_id();

                Log.e(TAG, "onItemClick: " + RoomID);

                Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
                Log.e(TAG, "openStartChat: " + RoomID);
                intent.putExtra("RoomID", RoomID);
                startActivity(intent);


            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

    }

    private String getNewestMessage(DataSnapshot dataSnapshot, String roomName) {
        DataSnapshot chats = dataSnapshot.child("chat_rooms").child(roomName);

        Map<Integer, String> messages = new HashMap<Integer, String>();
        for (DataSnapshot ds : chats.getChildren()) {
            int timestamp = 0;
            String tmp = ds.child("message").getValue(String.class);
            Log.e(TAG, "getNewestMessage: " + tmp);
            try {
                timestamp = Integer.parseInt(ds.child("timestamp").getValue(String.class));

                messages.put(timestamp, tmp);
            } catch (Exception e) {

            }


        }
        return maxUsingIteration(messages);


    }
    // Could not use the Java 8 aproache, because this would need API Level 24 and higher
    public <K, V extends Comparable<V>> V maxUsingIteration(Map<K, V> map) {
        Map.Entry<K, V> maxEntry = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue()
                    .compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry.getValue();
    }


    public void logout() {


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        try {
            mAuth.signOut();
            Toast.makeText(this, "Logout was sucsessfull", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            DeleteCached();
            this.finish();

        } catch (Exception e) {
            Log.e(TAG, "onClick: Exception " + e.getMessage(), e);
            Toast.makeText(MainActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
        }


    }

    private void DeleteCached() {
        SharedPreferences.Editor editor = getSharedPreferences(UserInformation, MODE_PRIVATE).edit();
        editor.putString(UserName, "");
        editor.putString(UserID, "");
        editor.apply();
    }


    public void removeItem(int position) {
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }


    private void SaveUserdata(String User, String ID) {
        SharedPreferences.Editor editor = getSharedPreferences(UserInformation, MODE_PRIVATE).edit();
        editor.putString(UserName, User);
        editor.putString(UserID, ID);
        editor.apply();


    }

    private void OpenLoginActivity() {
        Log.e(TAG, "OpenLoginActivity: Not logged in, Starting LoginActivity");
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        this.finish();
    }


}
