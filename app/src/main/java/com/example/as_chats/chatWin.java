package com.example.as_chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {
    String reciverimg, reciverUid,reciverName,SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public  static String senderImg;
    public  static String reciverIImg;
    CardView sendbtn;
    EditText textmsg;

    String senderRoom,reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<msgModelClass> messagesArrayList;
    messagesAdapter mmessagesAdpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);
        //getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        reciverName = getIntent().getStringExtra("nameee");
        reciverimg = getIntent().getStringExtra("receiverimg");
        reciverUid = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.sendButton);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdpter.setLayoutManager(linearLayoutManager);
        mmessagesAdpter = new messagesAdapter(chatWin.this,messagesArrayList);
        messageAdpter.setAdapter(mmessagesAdpter);


        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(""+reciverName);

        // RUNNING PERFECTLY TILL NOW

        SenderUID =  firebaseAuth.getUid();

        senderRoom = SenderUID+reciverUid;
        reciverRoom = reciverUid+SenderUID;



        DatabaseReference  reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference  chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        // RUNNING PERFECTLY TILL NOW

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    msgModelClass messages = dataSnapshot.getValue(msgModelClass.class);
                    messagesArrayList.add(messages);
                }
                mmessagesAdpter.notifyDataSetChanged();
                NotificationCompat.Builder mbuilder = (NotificationCompat.Builder)
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ajaynoti)
                                .setContentTitle("Notification")
                                .setContentText("Ajay sent you a message");

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, mbuilder.build());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // RUNNING PERFECTLY TILL NOW

        // reference.addValue.. not working
        // changed profilepic to profilePic
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg= snapshot.child("profilePic").getValue().toString();
                reciverIImg=reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(chatWin.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");
                Date date = new Date();
                msgModelClass messagess = new msgModelClass(message,SenderUID,date.getTime());

                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });

    }
}