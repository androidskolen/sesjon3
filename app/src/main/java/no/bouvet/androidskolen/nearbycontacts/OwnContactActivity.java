package no.bouvet.androidskolen.nearbycontacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import no.bouvet.androidskolen.nearbycontacts.models.Contact;
import no.bouvet.androidskolen.nearbycontacts.models.OwnContactViewModel;

public class OwnContactActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText userNameEditText;
    private EditText userEmailEditText;
    private EditText userTelephoneEditText;
    private ImageView userPicture;
    private Preferences preferences;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_own_contact);

        Button startNearbyActivityButton = (Button) findViewById(R.id.start_nearby_activity_button);
        startNearbyActivityButton.setOnClickListener(this);

        Button takePictureButton = (Button) findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(this);

        Button removePictureButton = (Button) findViewById(R.id.remove_picture_button);
        removePictureButton.setOnClickListener(this);

        userNameEditText = (EditText) findViewById(R.id.user_name_editText);
        userEmailEditText = (EditText) findViewById(R.id.user_email_editText);
        userTelephoneEditText = (EditText) findViewById(R.id.user_telephone_editText);
        userPicture = (ImageView) findViewById(R.id.user_picture_imageView);

        preferences = new Preferences();
    }

    @Override
    protected void onStart() {
        super.onStart();

        contact = preferences.createContactFromPreferences(getApplicationContext());
        if (contact != null) {
            userNameEditText.setText(contact.getName());
            userEmailEditText.setText(contact.getEmail());
            userTelephoneEditText.setText(contact.getTelephone());
            if (contact.getPicture() != null) {
                userPicture.setImageBitmap(contact.getPicture());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Contact contact = createContactFromInput();
        preferences.saveContactToPreferences(contact, getApplicationContext());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_nearby_activity_button:
                saveContact();
                startNearbyActivity();
                break;
            case R.id.take_picture_button:
                takePicture();
                break;
            case R.id.remove_picture_button:
                removePicture();
                break;
        }
    }

    private void removePicture() {
        Log.i("Nearby-by-Bouvet", "Removing the picture ");
        userPicture.setImageBitmap(null);
        contact.setPicture(null);
        userPicture.refreshDrawableState();
        saveContact();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            userPicture.setImageBitmap(createThumbnail((Bitmap) extras.get("data")));
        }
    }

    private Bitmap createThumbnail(Bitmap bitmap) {
        int dimension = getSquareCropDimensionForBitmap(bitmap);
        Log.i("Nearby-by-Bouvet", "Creating Thumbnail with dimension " + dimension);
        return ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void startNearbyActivity() {
        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }

    private void saveContact() {
        Log.i("Nearby-by-Bouvet", "Saving the Contact");
        Contact contact = createContactFromInput();
        preferences.saveContactToPreferences(contact, getApplicationContext());
        OwnContactViewModel.INSTANCE.setContact(contact);
    }

    private Contact createContactFromInput() {
        String name = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String telephone = userTelephoneEditText.getText().toString();
        String picture = getEncodedPicutre();
        Contact newContact = new Contact(name, email, telephone, picture);
        Log.i("Nearby-by-Bouvet", "Created Contact from Input" + newContact.toJson());
        return newContact;
    }

    private String getEncodedPicutre() {
        if (userPicture.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) userPicture.getDrawable()).getBitmap();

            return encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 10);
        } else {
            return null;
        }
    }

    public int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        if (image != null) {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(compressFormat, quality, byteArrayOS);
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        } else {
            return "";
        }

    }
}
