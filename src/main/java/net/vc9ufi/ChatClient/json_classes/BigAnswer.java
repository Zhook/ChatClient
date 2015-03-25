package net.vc9ufi.ChatClient.json_classes;

import net.vc9ufi.ChatClient.RestController;

import java.util.ArrayList;

public class BigAnswer {

    private RestController.RESPONSE status= RestController.RESPONSE.ERROR_UNKNOWN;
    private ArrayList<User> newUsers = new ArrayList<User>();
    private ArrayList<User> deletedUsers = new ArrayList<User>();
    private ArrayList<Message> newMessages = new ArrayList<Message>();

    public BigAnswer() {
    }

    public BigAnswer(RestController.RESPONSE status) {
        this.status = status;
    }


    public RestController.RESPONSE getStatus() {
        return status;
    }

    public void setStatus(RestController.RESPONSE status) {
        this.status = status;
    }

    public ArrayList<User> getDeletedUsers() {
        return deletedUsers;
    }

    public void setDeletedUsers(ArrayList<User> deletedUsers) {
        this.deletedUsers = deletedUsers;
    }

    public ArrayList<Message> getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(ArrayList<Message> newMessages) {
        this.newMessages = newMessages;
    }

    public ArrayList<User> getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(ArrayList<User> newUsers) {
        this.newUsers = newUsers;
    }


}
