package net.vc9ufi.ChatClient;

import android.widget.Toast;
import net.vc9ufi.ChatClient.json_classes.BigAnswer;
import net.vc9ufi.ChatClient.views.MyActivity;

import java.util.LinkedList;

public class RestThread extends Thread {
    private static final String HOME = "/chat";
    private static final int PORT = 8080;

    private static class Request {
        final RestController.REQUEST request;
        final String value;


        public Request(RestController.REQUEST request, String value) {
            this.request = request;
            this.value = value;
        }
    }

    static final Request GET_NEW = new Request(RestController.REQUEST.GET_NEW, "");

    private volatile boolean terminate = false;
    private volatile String mAddress = "http://192.168.1.51";

    private final RestController mRestController;
    private final MyActivity mainActivity;


    private volatile LinkedList<Request> requests = new LinkedList<Request>();

    private String getURL() {
        return mAddress + ":" + String.valueOf(PORT) + HOME;
    }

    public RestThread(MyActivity activity) {
        this.mainActivity = activity;
        mRestController = new RestController();
        requests.add(new Request(
                RestController.REQUEST.LOGIN,
                Setting.getInstance().getString(R.string.preference_key_name, "Vasya")));
    }

    public void terminate() {
        this.terminate = true;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }


    @Override
    public void run() {
        Request request;
        String url;

        while (!terminate || requests.size() > 0) {
            url = getURL();

            while (requests.size() > 0) {
                request = requests.getFirst();
                requests.removeFirst();
                sendRequest(request.request, url, request.value);
            }

            request = GET_NEW;
            sendRequest(request.request, url, request.value);

            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void addRequest(RestController.REQUEST request, String value) {
        requests.add(new Request(request, value));
    }


    private void sendRequest(RestController.REQUEST request, String url, String value) {
        RestController.RESPONSE response;
        switch (request) {
            case LOGIN:
                checkResponse(mRestController.login(url, value));
                break;
            case POST:
                checkResponse(mRestController.post(url, value));
                break;
            case GET_NEW:
                BigAnswer answer = mRestController.getNew(url);
                response = answer.getStatus();
                if (response == RestController.RESPONSE.OK)
                    mainActivity.readBigAnswer(answer);
                checkResponse(response);
                break;
            case LOGOUT:
                checkResponse(mRestController.logout(url));
                break;
            case CHECK_SERVER:
                checkResponse(mRestController.checkServer(url));
                break;
        }
    }

    private void checkResponse(RestController.RESPONSE response) {
        switch (response) {
            case OK:
                break;
            case ERROR_SHORT_LOGIN:
                System.out.println("myout: " + "login - short name");
                break;
            case ERROR_NOT_LOGGED:
                requests.add(new Request(
                        RestController.REQUEST.LOGIN,
                        Setting.getInstance().getString(R.string.preference_key_name, "Vasya")));
                break;
            case ERROR_UNKNOWN:
                System.out.println("myout: " + "response un");
                break;
            case RESPONSE_EXC:
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity, "connection problem", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

}
