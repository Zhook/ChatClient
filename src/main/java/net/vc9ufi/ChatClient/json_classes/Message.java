package net.vc9ufi.ChatClient.json_classes;

public class Message {
    private long time;
    private String user;
    private String msg;

    public Message( String user, String msg) {
        this.time = System.currentTimeMillis();
        this.msg = msg;
        this.user = user;
    }

    public Message(long time, String user, String msg) {
        this.msg = msg;
        this.time = time;
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
