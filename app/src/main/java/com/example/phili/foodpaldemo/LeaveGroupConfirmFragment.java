package com.example.phili.foodpaldemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by phili on 2018-02-28.
 */

public class LeaveGroupConfirmFragment extends DialogFragment {

    // cite code: developer.android.com/guide/topics/ui/dialogs.html

    public interface LeaveDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);

    }

    LeaveDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        try {
            mListener = (LeaveDialogListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement this interface");

        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.leave_group_confirm)
                // if the user is the creater: add one more message
                // add code here
                .setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // leave the group
                        mListener.onDialogPositiveClick(LeaveGroupConfirmFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // stay in the group
                        mListener.onDialogNegativeClick(LeaveGroupConfirmFragment.this);

                    }
                });
        // create the alertDialog object and return it
        return builder.create();
    }
}
