package com.company.studentproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageViewAdapter extends RecyclerView.Adapter{
    private ArrayList list;
    private Context context;
    private DatabaseReference databaseReference;
    private String asdf = "asdf";

    public MessageViewAdapter(Context context, ArrayList list){
        this.list = new ArrayList<StoreMessage>();
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("messages");
    }

    public void update(ArrayList newList){ // sort the list before adding it back
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void deleteMessage(int position){
        String messageId = ((StoreMessage)list.get(position)).getMessageId();
        databaseReference.child(messageId).removeValue();
        list.remove(position);
        notifyDataSetChanged();
    }

    public void upVote(int position){
        StoreMessage m = (StoreMessage)list.get(position);
        Log.d(asdf, "message at posotion " + m.toString());
        m.increaseVotesByOne();
        databaseReference.child(m.getMessageId()).setValue(m);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_viewer,parent, false);
        MessageViewHolder holder = new MessageViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String recyclerviewMessage = ((StoreMessage)list.get(position)).getMessageString();
        String recyclerviewVote = Integer.toString(((StoreMessage)list.get(position)).getMessageVotes());

        ((MessageViewHolder)holder).message.setText(recyclerviewMessage);
        ((MessageViewHolder)holder).vote.setText(recyclerviewVote);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView message, vote;
        public Button voteButton;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.textViewMessage);
            vote = (TextView) itemView.findViewById(R.id.textViewVotes); //textViewVotes is the text id in the xml file
            voteButton = (Button)itemView.findViewById(R.id.buttonVote);

            voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    upVote(position);
                }
            });

        }
    }
}
