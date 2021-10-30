package com.example.bnja;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class openDialog extends DialogFragment {

    public DialogListener listener;
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder request = new AlertDialog.Builder(getActivity());
        final EditText groupInput = new EditText(getContext());
        request.setView(groupInput);

        request.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reqBloodGroup = groupInput.getText().toString().trim().toUpperCase();
                listener.applyTexts(reqBloodGroup);
                Intent intent = new Intent(getContext(), SearchView.class);
                intent.putExtra("reqGroup", reqBloodGroup);
                startActivity(intent);
            }
        });
        request.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return request.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (DialogListener) context;
    }

    public interface DialogListener{
        default void applyTexts(String bloodGroup){
        }
    }


}
