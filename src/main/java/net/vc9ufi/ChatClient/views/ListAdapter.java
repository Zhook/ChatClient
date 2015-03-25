package net.vc9ufi.ChatClient.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.vc9ufi.ChatClient.R;
import net.vc9ufi.ChatClient.json_classes.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListAdapter extends BaseAdapter {

    private ArrayList<Message> mMessages;
    private SimpleDateFormat mDateFormat;

    private static LayoutInflater inflater = null;

    public ListAdapter(Context context, ArrayList<Message> messages) {
        if (messages == null) {
            mMessages = new ArrayList<Message>();
        } else {
            mMessages = messages;
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(R.layout.list_view_item, null);

        Message message = mMessages.get(position);

        TextView timeView = (TextView) view.findViewById(R.id.list_view_item_date);
        Date date = new Date(message.getTime());
        String time = mDateFormat.format(date);
        timeView.setText(time);

        TextView nameView = (TextView) view.findViewById(R.id.list_view_item_name);
        nameView.setText(message.getUser());

        TextView messageView = (TextView) view.findViewById(R.id.list_view_item_message);
        messageView.setText(message.getMsg());

        return view;
    }

    public void add(ArrayList<Message> messages) {
        if (messages == null) return;

        mMessages.addAll(messages);
    }
}
