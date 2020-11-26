package com.example.christopher.fchat.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.christopher.fchat.Models.RoomInfo;
import com.example.christopher.fchat.Models.UserProperties;
import com.example.christopher.fchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Random;

import static com.example.christopher.fchat.Utils.Constants.UserID;
import static com.example.christopher.fchat.Utils.Constants.UserInformation;
import static com.example.christopher.fchat.Utils.Constants.UserName;
import static com.example.christopher.fchat.Utils.Constants.no_id;
import static com.example.christopher.fchat.Utils.Constants.no_name;


public class StartChatActivity extends AppCompatActivity {

    private Button Chat_yes;
    private Button Chat_no;
    private String uid = FirebaseAuth.getInstance().getUid();



    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user_properties");
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private static final String TAG = "StartChatActivity";

    private String userID;
    private String MemberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startchat);
        Toolbar toolbar = findViewById(R.id.toolbar_11);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Start Conversation");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get parameters from Searchuser-Activity
        Intent i = getIntent();
        //RoomID = i.getStringExtra("RoomID");
        userID = i.getStringExtra("userID");
        MemberName = i.getStringExtra("userName");
        Log.e(TAG, "onCreate: "+ MemberName );



        Chat_yes = findViewById(R.id.start_chat_yes);
        Chat_no = findViewById(R.id.start_chat_no);

        Chat_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Nutzer möchte einen Chat starten, es wird überprüft ob bereits einer mit dem
                gewünschten Partner vorhanden ist. */

                check();
            }
        });

        Chat_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSearchUser();


            }
        });
    }


    private void OpenSearchUser() {
        Intent intent = new Intent(StartChatActivity.this, SearchUserActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void OpenChat(String RoomID) {

        Intent intent = new Intent(StartChatActivity.this, ChatRoomActivity.class);
        Log.e(TAG, "openStartChat: " + RoomID);
        intent.putExtra("RoomID", RoomID);
        startActivity(intent);
        this.finish();
    }

    private void OpenMain(){
        Intent intent = new Intent(StartChatActivity.this, ChatRoomActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void check() {

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.child("user_properties").child(uid).hasChildren()) {

                    for (DataSnapshot ds : dataSnapshot.child("user_properties").child(uid).getChildren()) {

                        String foundadmin = ds.child("admin").getValue(String.class);
                        String founduser = ds.child("member").getValue(String.class);
                        String foundroom = ds.child("chatRoom").getValue(String.class);

                        Log.e(TAG, "check gefunderner nutzer 1 " + foundadmin);
                        Log.e(TAG, "check gefunderner nutzer 2 " + founduser);

                        Log.e(TAG, "check: gesprächspartner " + userID);
                        // Wenn Aktueller nutzer im gefundenen Raum ist -> öffne Raum
                        if ((foundadmin.equals(userID) || founduser.equals(userID))) {
                        // Es gibt einen Chatraum mit dem ausgewählten Nutzer und dieser wird geöffnet
                            Log.e(TAG, "check: öffne raum = " + foundroom);
                            //OpenMain();
                            OpenChat(foundroom);


                        } else {
                            // Es gibt einen Chatraum, jedoch nicht mit dem ausgewählten Nutzer, deshalb wird ein neuer Genieriert
                            Log.e(TAG, "check: generiere neuen Raum ");
                            generateChatRoom(userID, MemberName);

                        }
                    }

                } else {
                    // Kein einziger Chatraum vorhanden, es wird ein neuer Genieriert
                    Log.e(TAG, "check: generiere neuen Raum ");
                    generateChatRoom(userID, MemberName);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }





    private void generateChatRoom(String MemberID, String MemberName) {


        SharedPreferences prefs = getSharedPreferences(UserInformation, MODE_PRIVATE);
        String CurrentUser = prefs.getString(UserName, no_name);//"No name defined" is the default
        String CurrentUserID = prefs.getString(UserID, no_id);
        String RoomID = generateRoomUID();


        UserProperties u = new UserProperties();
        u.setAdmin(CurrentUserID);
        u.setAdmin_Name(CurrentUser);

        u.setMember(MemberID);
        u.setMember_Name(MemberName);

        u.setChatRoom(RoomID);
        u.setChatRoom_Name("Names des Chatraumes"); // TODO: 21/05/2019 Namen des Raumes von Nutzer auswählen lassen (nur gruppe)


        RoomInfo r = new RoomInfo();
        r.setAdmin_Name(CurrentUser); // Name des eigenen gerätes
        r.setAdmin(CurrentUserID);

        r.setMember_Name(MemberName); // Name des Konversationspartners
        r.setMember(MemberID);

        r.setChatRoom(RoomID);
        root.child("chat_rooms").child(RoomID).child("RoomInfo").setValue(r);

        // Infos über den Chatroom werden bei beiden nutzern hinterlegt, sodass beiden der Pfad zum eigentlichen Chat bekannt ist.
        ref.child(CurrentUserID).child(RoomID).setValue(u); // Aktuell eingeloggter nutzer

        ref.child(MemberID).child(RoomID).setValue(u); // Konversationspartner

        OpenChat(RoomID);


    }   private String generateRoomUID() {


        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();


    }


}




