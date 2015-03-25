package net.vc9ufi.ChatClient.views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import net.vc9ufi.ChatClient.R;
import net.vc9ufi.ChatClient.RestController;
import net.vc9ufi.ChatClient.RestThread;
import net.vc9ufi.ChatClient.Setting;
import net.vc9ufi.ChatClient.json_classes.BigAnswer;
import net.vc9ufi.ChatClient.json_classes.Message;
import net.vc9ufi.ChatClient.json_classes.User;

import java.util.ArrayList;

public class MyActivity extends Activity {

    EditText mMessageEditText;
    ListView mMessagesListView;
    int mMessagesListViewDrag;
    net.vc9ufi.ChatClient.views.ListAdapter mMessagesAdapter;

    RestThread mRestThread;

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        Setting.getInstance().setApp(preferences, this);

        mRestThread = new RestThread(this);
        mRestThread.setAddress(preferences.getString(getString(R.string.preference_key_address), "http://192.168.1.51"));

        ImageButton send = (ImageButton) findViewById(R.id.button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mMessageEditText.getText().toString();
                if (text.length() > 0) {
                    mRestThread.addRequest(RestController.REQUEST.POST, text);
                    mMessageEditText.setText("");
                }
            }
        });

        mMessageEditText = (EditText) findViewById(R.id.editText_message);
        mMessagesListView = (ListView) findViewById(R.id.listView_messages);
        mMessagesAdapter = new net.vc9ufi.ChatClient.views.ListAdapter(this, null);
        mMessagesListView.setAdapter(mMessagesAdapter);
        mMessagesListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mMessagesListViewDrag++;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mMessagesListViewDrag--;
                }
                return false;
            }
        });

        mRestThread.start();
        if(preferences.getBoolean(getString(R.string.preference_key_first_launch), true)){
            showNameInputDialog();
            SharedPreferences.Editor editPref = preferences.edit();
            editPref.putBoolean(getString(R.string.preference_key_first_launch), false);
            editPref.apply();
        }
    }


    private void showNameInputDialog() {
        String name = preferences.getString(getString(R.string.preference_key_name), "Player1");
        EditTextDialog nameDialog = new EditTextDialog(this, "Name", name) {
            @Override
            protected boolean onPositiveClick(String name) {
                if (name.length() < 3) {
                    this.setMsg(getString(R.string.too_short_msg));
                    return false;
                }
                SharedPreferences.Editor editPref = preferences.edit();
                editPref.putString(getString(R.string.preference_key_name), name);
                editPref.apply();
                return true;
            }
        };
        nameDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.item_menu_address):
                String address = preferences.getString(getString(R.string.preference_key_address), "http://192.168.1.51");
                EditTextDialog addressDialog = new EditTextDialog(this, "Address", address) {
                    @Override
                    protected boolean onPositiveClick(String newAddress) {
                        SharedPreferences.Editor editPref = preferences.edit();
                        editPref.putString(getString(R.string.preference_key_address), newAddress);
                        editPref.apply();
                        return true;
                    }
                };
                addressDialog.show();
                return true;
            case (R.id.item_menu_name):
                showNameInputDialog();
                return true;
            case (R.id.item_menu_exit):
                mRestThread.addRequest(RestController.REQUEST.LOGOUT, "");
                mRestThread.terminate();
                MyActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void readBigAnswer(BigAnswer answer) {


        ArrayList<Message> messages = answer.getNewMessages();
        if (messages == null) messages = new ArrayList<Message>();


        ArrayList<User> users = answer.getNewUsers();
        if (users != null) {
            for (User user : users) {
                messages.add(
                        new Message(
                                user.getName(),
                                getString(R.string.messages_user_in)));
            }
        }

        users = answer.getDeletedUsers();
        if (users != null) {
            for (User user : users) {
                messages.add(
                        new Message(
                                user.getName(),
                                getString(R.string.messages_user_out)));
            }
        }

        if (messages.size() > 0) {
            final ArrayList<Message> finalMessages = messages;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMessagesAdapter.add(finalMessages);
                    if (mMessagesListViewDrag == 0)
                        mMessagesListView.setSelection(mMessagesAdapter.getCount() - 1);
                }
            });
        }
    }



    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.preference_key_name))) {
                mRestThread.addRequest(RestController.REQUEST.LOGIN, preferences.getString(key, "name"));
            }

            if (key.equals(getString(R.string.preference_key_address))) {
                mRestThread.setAddress(preferences.getString(key, "http://192.168.1.51"));
            }
        }
    };
}
