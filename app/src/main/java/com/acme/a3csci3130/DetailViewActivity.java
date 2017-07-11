package com.acme.a3csci3130;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailViewActivity extends Activity {

        // Required business data formatting
    private EditText nameField, businessNumberField, addressField;
    Spinner primaryBusiness, province;
    private MyApplicationData appState;
    String[] primaryBusinessSpinner = {"Primary business...", "Fisher", "Distributor", "Processor", "Fish Monger"};
    String[] provinceSpinner = {"Province/territory...", "AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YT", ""};

    Contact receivedPersonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        receivedPersonInfo = (Contact)getIntent().getSerializableExtra("Contact");

        businessNumberField = (EditText) findViewById(R.id.businessNumber);
        nameField = (EditText) findViewById(R.id.name);
        addressField = (EditText) findViewById(R.id.address);

        primaryBusiness = (Spinner) findViewById(R.id.primaryBusiness);
        province = (Spinner) findViewById(R.id.province);

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, primaryBusinessSpinner);
        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, provinceSpinner);


        primaryBusiness.setAdapter(adapter01);
        province.setAdapter(adapter02);

        if(receivedPersonInfo != null){
            nameField.setText(receivedPersonInfo.name);
            businessNumberField.setText(receivedPersonInfo.businessNumber);
            addressField.setText(receivedPersonInfo.address);
                // setting spinner using position of the primaryBusiness string from firebase
            int spinnerPosition = adapter01.getPosition(receivedPersonInfo.primaryBusiness);
            primaryBusiness.setSelection(spinnerPosition);

            spinnerPosition = adapter02.getPosition(receivedPersonInfo.province);
            province.setSelection(spinnerPosition);
        }
    }

        /*
         * update method
         * delets the child with the current data and make a new one with the updated data
         */
    public void updateContact(View v){
        //TODO: Update contact funcionality
        appState = ((MyApplicationData) getApplicationContext());
        appState.firebaseReference.child(receivedPersonInfo.uid).removeValue();

        String personID = appState.firebaseReference.push().getKey();
        String name = nameField.getText().toString();
        String businessNumber = businessNumberField.getText().toString();
        String address = addressField.getText().toString();
        String primaryBusinessSelection = primaryBusiness.getSelectedItem().toString();
        String provinceSelection = province.getSelectedItem().toString();

            // textview to display an error when it occurs
        TextView Error = (TextView) findViewById(R.id.error);
        Error.setVisibility(View.GONE);

            // nested if statments to check Required business data formatting
                // else statments to display errors
        if (businessNumber.matches("[0-9]+") && businessNumber.length() == 9) {
            if (name.length() > 1 && name.length() <49) {
                if (!primaryBusinessSelection.equals("Primary business...")) {
                    if (address.length() < 50) {
                        if (!provinceSelection.equals("Province/territory...")) {
                            Contact person = new Contact(personID, businessNumber, name, primaryBusinessSelection, address, provinceSelection);
                            appState.firebaseReference.child(personID).setValue(person);
                            finish();
                        }
                        else {
                            provinceSelection = " ";
                            Contact person = new Contact(personID, businessNumber, name, primaryBusinessSelection, address, provinceSelection);
                            appState.firebaseReference.child(personID).setValue(person);
                            finish();
                        }
                    }
                    else {
                        Error.setText("Error:\n\tAddress can not be longer than 49 charesters.");
                        Error.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Error.setText("Error:\n\tplease select primary business. It is required.");
                    Error.setVisibility(View.VISIBLE);
                }
            }
            else {
                Error.setText("Error:\n\tName length can not be less than 2 or greater than 48.");
                Error.setVisibility(View.VISIBLE);
            }
        }
        else{
            Error.setText("Error:\n\tBusiness number has to be exactly 9 digits.");
            Error.setVisibility(View.VISIBLE);
        }
    }

        // erease methode, delete the child from firebase with the currect data
    public void eraseContact(View v) {
        //TODO: Erase contact functionality
        appState = ((MyApplicationData) getApplicationContext());
        appState.firebaseReference.child(receivedPersonInfo.uid).removeValue();
        finish();
    }
}
