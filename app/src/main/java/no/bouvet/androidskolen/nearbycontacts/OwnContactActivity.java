package no.bouvet.androidskolen.nearbycontacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import no.bouvet.androidskolen.nearbycontacts.models.OwnContactViewModel;
import no.bouvet.androidskolen.nearbycontacts.models.Contact;

public class OwnContactActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String PREFERENCE_FILE = "OwnContactInfo";
    private final static String PREFERENCE_NAME = "OwnContactName";
    private final static String PREFERENCE_EMAIL = "OwnContactEmail";
    private final static String PREFERENCE_TELEPHONE = "OwnContactTelephone";

    private EditText userNameEditText;
    private EditText userEmailEditText;
    private EditText userTelephoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_own_contact);

        Button startNearbyActivityButton = (Button) findViewById(R.id.start_nearby_activity_button);
        startNearbyActivityButton.setOnClickListener(this);

        userNameEditText = (EditText) findViewById(R.id.user_name_editText);
        userEmailEditText = (EditText) findViewById(R.id.user_email_editText);
        userTelephoneEditText = (EditText) findViewById(R.id.user_telephone_editText);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Contact contact = createContactFromPreferences();
        if (contact != null) {
            userNameEditText.setText(contact.getName());
            userEmailEditText.setText(contact.getEmail());
            userTelephoneEditText.setText(contact.getTelephone());
        }
    }

    private Contact createContactFromPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILE, 0);

        String name = preferences.getString(PREFERENCE_NAME, "");
        String email = preferences.getString(PREFERENCE_EMAIL, "");
        String telephone = preferences.getString(PREFERENCE_TELEPHONE, "");

        return new Contact(name, email, telephone);
    }

    @Override
    public void onClick(View view) {
        Contact contact = createContactFromInput();
        saveOwnContactInfo(contact);
        OwnContactViewModel.INSTANCE.setContact(contact);

        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }

    private void saveOwnContactInfo(Contact contact) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILE, 0);

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(PREFERENCE_NAME, contact.getName());
        edit.putString(PREFERENCE_EMAIL, contact.getEmail());
        edit.putString(PREFERENCE_TELEPHONE, contact.getTelephone());
        edit.apply();

    }

    private Contact createContactFromInput() {
        String name = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String telephone = userTelephoneEditText.getText().toString();

        return new Contact(name, email, telephone);
    }
}
