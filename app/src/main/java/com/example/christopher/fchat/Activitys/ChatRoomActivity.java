package com.example.christopher.fchat.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.christopher.fchat.Recyclerviews.ChatRoom;
import com.example.christopher.fchat.Models.ChatMessage;
import com.example.christopher.fchat.Models.UserInformation;
import com.example.christopher.fchat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import static com.example.christopher.fchat.Utils.Constants.UserID;
import static com.example.christopher.fchat.Utils.Constants.UserInformation;
import static com.example.christopher.fchat.Utils.Constants.UserName;
import static com.example.christopher.fchat.Utils.Constants.no_id;
import static com.example.christopher.fchat.Utils.Constants.no_name;
import static com.example.christopher.fchat.Utils.FirebaseUtil.getRoot;
import static com.example.christopher.fchat.Utils.FirebaseUtil.getUUID;
import static com.example.christopher.fchat.Utils.FirebaseUtil.getUserName;

public class ChatRoomActivity extends AppCompatActivity {


    private String CurrentUserID;
    private String CurrentUserName;
    private String CurrentRoom;
    private DatabaseReference ref;


    private ChatRoom adapter;

    private EditText NewMessage;


    // Debug Tag
    private String TAG = "ChatRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Log.e(TAG, "onCreate: Load Activity");


        Intent i = getIntent();
        CurrentRoom = i.getStringExtra("RoomID");


        ref = getRoot().child("chat_rooms").child(CurrentRoom);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ReadDB(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        NewMessage = findViewById(R.id.newMessage);


        Button send = findViewById(R.id.sendMessage);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage();
            }
        });


    }
    

    private void setupToolbar(String member1, String member2) {
        LoadPref();
        Toolbar toolbar = findViewById(R.id.toolbar_11);
        setSupportActionBar(toolbar);
        Log.e(TAG, "setupToolbar: "+ CurrentUserName + " " + member1 + " " + member2);
        if (member1.equals(CurrentUserName)) {
            getSupportActionBar().setTitle(member2);
        } else {
            getSupportActionBar().setTitle(member1);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private UserInformation LoadPref() {
        SharedPreferences prefs = getSharedPreferences(UserInformation, MODE_PRIVATE);
        CurrentUserName = prefs.getString(UserName, no_name);//"No name defined" is the default
        CurrentUserID = prefs.getString(UserID, no_id);
        com.example.christopher.fchat.Models.UserInformation u = new UserInformation();
        u.setName(CurrentUserName);
        u.setUID(CurrentUserID);
        return u;
    }


    private void ReadDB(DataSnapshot dataSnapshot) {


        ArrayList<ChatMessage> messages = new ArrayList<>();
        messages.clear();

            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                String message = ds.child("message").getValue(String.class);
                String author = ds.child("author").getValue(String.class);
                String authorUID = ds.child("authorUID").getValue(String.class);
                String Unixtime = ds.child("timestamp").getValue(String.class);

                String timestamp = UnixTimeToString(Unixtime);
                if (timestamp != null) {
                    messages.add(new ChatMessage(author, authorUID, timestamp, message));
                } else {
                    String member1 = ds.child("admin_Name").getValue(String.class);
                    String member2 = ds.child("member_Name").getValue(String.class);
                    setupToolbar(member1, member2);
                }


            }



        RecyclerView recyclerView = findViewById(R.id.message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRoom(messages, getApplicationContext());

        // Es wird Automatisch zu dem Element n-1 gescrollt, sodass die neuste Nachricht immer zu sehen ist
        recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private String UnixTimeToString(String UnixTime) {
        if (UnixTime == null) {
          return null;
        }else {
            long dv = Long.valueOf(UnixTime) * 1000;// its need to be in milisecond
            Date df = new java.util.Date(dv);
            String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
            return vv;
        }

    }


    private void writeMessage() {
        String uid = getUUID();

        // leere Nachrichten können nicht gesendet werden
        if (!NewMessage.getText().toString().equals("")) {
            ChatMessage c = new ChatMessage();
            c.setMessage(NewMessage.getText().toString());
            c.setAuthor(LoadPref().getName());
            c.setAuthorUID(LoadPref().getUID());
            Log.e(TAG, "writeMessage: Author Name = " + getUserName());
            long unixTime = System.currentTimeMillis() / 1000;
            c.setTimestamp(String.valueOf(unixTime));
            c.setAuthorUID(uid);
            ref.push().setValue(c);


            // Clears the input field to avoid spam
            NewMessage.getText().clear();

        }
    }


    private String GetTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(ChatRoomActivity.this, MainActivity.class);
        startActivity(i);
        super.onBackPressed();  // optional depending on your needs
    }


}
