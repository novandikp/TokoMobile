package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.itbrain.aplikasitoko.R;


public class DialogPetunjukKredit extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View viewPetunjuk = getActivity().getLayoutInflater().inflate(R.layout.dialog_petunjuk_kredit, null);
        builder.setView(viewPetunjuk)
                .setNegativeButton("nanti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        watchYoutubeVideo(getActivity(), "43n90dXCfwU");
                    }
                });

        return builder.create();
    }

    void watchYoutubeVideo(Context ctx, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            ctx.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            ctx.startActivity(webIntent);
        }
    }
}
