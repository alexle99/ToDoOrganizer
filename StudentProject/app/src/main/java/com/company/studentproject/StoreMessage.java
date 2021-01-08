package com.company.studentproject;

public class StoreMessage {
    private String messageId;
    private String messageString;
    private int messageVotes;

    public StoreMessage(){

    }

    public StoreMessage(String messageId, String messageString) {
        this.messageId = messageId;
        this.messageString = messageString;
        this.messageVotes = 0;
    }

    @Override
    public String toString(){
        return "ID: " + messageId + " VOTES: " + messageVotes + " MESSAGE: "+ messageString;// + " Votes: " + messageVotes;
    }


    public String getMessageId() {
        return messageId;
    }

    public String getMessageString() {
        return messageString;
    }

    public int getMessageVotes(){return messageVotes;}

    public void increaseVotesByOne(){this.messageVotes++;}
}
