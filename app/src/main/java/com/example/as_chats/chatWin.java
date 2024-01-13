package com.example.as_chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    String receiverimg, receiveruid, receivername, senderuid;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    CircleImageView profile;
    TextView receiverNName;
    CardView sendbtn;
    EditText textmsg;
    public static String senderImg;
    public static String receiverIImg;

    String senderRoom, receiverRoom;
    RecyclerView mmsngerAdp;
    ArrayList<msgModelClass> messagesArrayList;
    messagesAdapter messagesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        mmsngerAdp = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmsngerAdp.setLayoutManager(linearLayoutManager);
        messagesAdapter = new messagesAdapter(chatWin.this, messagesArrayList);
        mmsngerAdp.setAdapter(messagesAdapter);

        receivername = getIntent().getStringExtra("nameee");
        receiverimg = getIntent().getStringExtra("receiverimg");
        receiveruid = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.sendButton);
        textmsg = findViewById(R.id.textmsg);

        profile = findViewById(R.id.profileimgg);
        receiverNName = findViewById(R.id.recivername);

        Picasso.get().load(receiverimg).into(profile);
        receiverNName.setText(""+receivername);

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("user").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    msgModelClass messages = dataSnapshot.getValue(msgModelClass.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString();
                receiverIImg = receiverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        senderuid = firebaseAuth.getUid();
        senderRoom = senderuid + receiveruid;
        receiverRoom = receiveruid + senderuid;
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textmsg.getText().toString();
                if(message.isEmpty())
                {
                    Toast.makeText(chatWin.this, "Enter some text in TextBox", Toast.LENGTH_SHORT).show();
                }
                textmsg.setText("");
                Date date = new Date();
                msgModelClass messages = new msgModelClass(message, senderuid, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child("senderRoom").child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats").child("receiverRoom").child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
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