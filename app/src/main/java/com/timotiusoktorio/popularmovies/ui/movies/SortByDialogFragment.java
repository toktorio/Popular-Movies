package com.timotiusoktorio.popularmovies.ui.movies;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.source.local.PreferencesHelper;

public class SortByDialogFragment extends DialogFragment {

    private SortByDialogFragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SortByDialogFragmentListener) {
            mListener = (SortByDialogFragmentListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int selectedSectionId = PreferencesHelper.getInstance(getActivity()).getSelectedSection();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.label_sort_by)
                .setSingleChoiceItems(R.array.sort_by_options, selectedSectionId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onSectionSelected(which);
                        }
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public interface SortByDialogFragmentListener {

        void onSectionSelected(int sectionId);
    }
}