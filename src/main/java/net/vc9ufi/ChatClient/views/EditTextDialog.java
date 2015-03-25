package net.vc9ufi.ChatClient.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import net.vc9ufi.ChatClient.R;

public abstract class EditTextDialog {
    Context context;
    String title;
    String defName;

    TextView textViewMsg;

    public EditTextDialog(Context context, String title, String defName) {
        this.context = context;
        this.title = title;
        this.defName = defName;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.edittext_server_address, null);

        final EditText editTextName = (EditText)view.findViewById(R.id.name_dialog_edit_text);
        editTextName.setText(defName);

        textViewMsg = (TextView)view.findViewById(R.id.name_dialog_msg_textview);


        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        final AlertDialog nameDialog = builder.create();
        nameDialog.show();

        nameDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = onPositiveClick(editTextName.getText().toString());
                if (wantToCloseDialog) nameDialog.dismiss();
            }
        });
    }

    public void setMsg(String msg){
        textViewMsg.setText(msg);
    }

    protected abstract boolean onPositiveClick(String name);
}
