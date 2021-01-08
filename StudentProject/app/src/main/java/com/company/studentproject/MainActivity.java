package com.company.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText messageText;
    private Button postButton;
    private ArrayList<StoreMessage>messageList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String asdf = "asdf"; //used for printing to console with asdf TAG

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("messages");
        messageList = new ArrayList<StoreMessage>();
        messageText = (EditText)findViewById(R.id.editTextMessage);
        postButton = (Button)findViewById(R.id.buttonPostMessage);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageViewAdapter(this, messageList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDelete((MessageViewAdapter)adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        postButton.setOnClickListener(new View.OnClickListener() { //clicking post
            @Override
            public void onClick(View v) {
                addMessage(v);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() { //something changed in the database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    StoreMessage m = d.getValue(StoreMessage.class);
                    messageList.add(m);
                }
                ((MessageViewAdapter)adapter).update(messageList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(asdf, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void addMessage(View view){ // add to the database not the messageList
        String message = messageText.getText().toString();

        if (message.length() > 0){
            String id = databaseReference.push().getKey();
            StoreMessage m = new StoreMessage(id, message);

            databaseReference.child(id).setValue(m);
            messageText.setText("");
        }
    }
}
