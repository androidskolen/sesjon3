package no.bouvet.androidskolen.nearbycontacts;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import no.bouvet.androidskolen.nearbycontacts.models.Contact;
import no.bouvet.androidskolen.nearbycontacts.models.OwnContactViewModel;

public class OwnContactActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText userNameEditText;
    private EditText userEmailEditText;
    private EditText userTelephoneEditText;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_own_contact);

        Button startNearbyActivityButton = (Button) findViewById(R.id.start_nearby_activity_button);
        startNearbyActivityButton.setOnClickListener(this);

        Button takePictureButton = (Button) findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(this);

        // TODO 3.2. Sett onClickListener på remove knapp som er lagt til i layout

        userNameEditText = (EditText) findViewById(R.id.user_name_editText);
        userEmailEditText = (EditText) findViewById(R.id.user_email_editText);
        userTelephoneEditText = (EditText) findViewById(R.id.user_telephone_editText);

        preferences = new Preferences();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Contact contact = preferences.createContactFromPreferences(getApplicationContext());
        if (contact != null) {
            userNameEditText.setText(contact.getName());
            userEmailEditText.setText(contact.getEmail());
            userTelephoneEditText.setText(contact.getTelephone());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        preferences.saveContactToPreferences(createContactFromInput(), getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_nearby_activity_button:
                handlePublish();
                break;
            case R.id.take_picture_button:
                startImageCaptureActivityForResult();
                break;
            // TODO Oppgave 3.2. Håndter klikk-event for remove picture knapp.
        }
    }

    private void handlePublish() {
        // Oppgave 3.5. Lag en sjekk på at bilde er satt. Dersom ikke, vis en dialog ved å starte NoPictureDialogFragment.
        saveContact();
        startNearbyActivity();

    }

    private void startNoPictureDialogFragment() {
        DialogFragment dialog = NoPictureDialogFragment.newInstance();
        dialog.show(getFragmentManager(), null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // TODO Oppgave 3.1

            // Siden vi skal sende bilde i en kanal med begrenset plass bruker vi
            // en thumbnail. Et alternativ er å ha to varianter av bilde, et som man viser
            // og et som man sender. Men vi holder det enkelt.
            // Velger også å croppe bildet til kvadratisk form for at det skal se bedre ut.

        }
    }


    // Starter en Camera Activity for å ta bilde med resultatet returnert i et onActivityResult
    // callback.
    void startImageCaptureActivityForResult() {
        // TODO Oppgave 3.1. Husk startActivityForResult med request kode REQUEST_IMAGE_CAPTURE

    }

    void startNearbyActivity() {
        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }

    void saveContact() {
        Contact contact = createContactFromInput();
        preferences.saveContactToPreferences(contact, getApplicationContext());
        OwnContactViewModel.INSTANCE.setContact(contact);
    }

    private Contact createContactFromInput() {
        String name = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String telephone = userTelephoneEditText.getText().toString();
        String picture = getEncodedPicture();
        return new Contact(name, email, telephone, picture);
    }

    private String getEncodedPicture() {
        // TODO oppgave 3.4. Hent base64 encodet bilde fra View i layout.
        return "";
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        if (image != null) {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(compressFormat, quality, byteArrayOS);
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        } else {
            return "";
        }
    }
}
