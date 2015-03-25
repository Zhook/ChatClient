package net.vc9ufi.ChatClient;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting {
    private static final Setting SETTING = new Setting();

    private SharedPreferences preferences;
    private Context context;


    private Setting() {
    }

    public static Setting getInstance() {
        return SETTING;
    }

    public void setApp(SharedPreferences preferences, Context context) {
        this.preferences = preferences;
        this.context = context;
    }


    public String getString(int key, String defString) {
        return preferences.getString(context.getString(key), defString);
    }

}
