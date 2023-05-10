package com.itbrain.aplikasitoko.kasir;

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

public class DialogPetunjukKasir extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View viewPetunjuk = getActivity().getLayoutInflater().inflate(R.layout.dialog_petunjuk_kasir, null);
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
                        watchYoutubeVideo(getActivity(), "ZtRlYU204-E");
                    }
                });

        return builder.create();
    }

    void watchYoutubeVideo(Context ctx, String id) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")));
    }
}
