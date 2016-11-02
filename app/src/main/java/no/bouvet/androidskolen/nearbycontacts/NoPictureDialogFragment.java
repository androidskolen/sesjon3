package no.bouvet.androidskolen.nearbycontacts;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class NoPictureDialogFragment extends DialogFragment {

    public static NoPictureDialogFragment newInstance() {
        return new NoPictureDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

       // TODO Oppgave 3.5 Opprette en AlertDialog og returner denne. Dersom negative respons kaller
       // man publishContachWithoutImage(), dersom positive kaller man startImageCaptureActivityForResult
    }

    private void publishContactWithoutImage() {
        ((OwnContactActivity)getActivity()).saveContact();
        ((OwnContactActivity)getActivity()).startNearbyActivity();
    }

    private void startImageCaptureActivityForResult() {
        ((OwnContactActivity)getActivity()).startImageCaptureActivityForResult();
    }
}
