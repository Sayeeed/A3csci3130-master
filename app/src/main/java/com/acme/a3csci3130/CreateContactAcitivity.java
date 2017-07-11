package com.acme.a3csci3130;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateContactAcitivity extends Activity {

    private Button submitButton;
        // Required business data formatting
    private EditText nameField, businessNumberField, addressField;
    private MyApplicationData appState;

    // Required business data formatting
    Spinner primaryBusiness, province;

        // Primary business: required, {Fisher, Distributor, Processor, Fish Monger}
    String[] primaryBusinessSpinner = {
            "Primary business...",
            "Fisher",
            "Distributor",
            "Processor",
            "Fish Monger"
    };
        //Province/territory: {AB, BC, MB, NB, NL, NS, NT, NU, ON, PE, QC, SK, YT, “ “}
    String[] provinceSpinner = {
            "Province/territory...",
            "AB",
            "BC",
            "MB",
            "NB",
            "NL",
            "NS",
            "NT",
            "NU",
            "ON",
            "PE",
            "QC",
            "SK",
            "YT",
            " "
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact_acitivity);
        //Get the app wide shared variables
        appState = ((MyApplicationData) getApplicationContext());

        submitButton = (Button) findViewById(R.id.submitButton);
        businessNumberField = (EditText) findViewById(R.id.businessNumber);
        nameField = (EditText) findViewById(R.id.name);
        addressField = (EditText) findViewById(R.id.address);

        /*
         * set spinners
         */
        primaryBusiness = (Spinner) findViewById(R.id.primaryBusiness);
        province = (Spinner) findViewById(R.id.province);

        ArrayAdapter<String> adapter01 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, primaryBusinessSpinner);
        ArrayAdapter<String> adapter02 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, provinceSpinner);


        primaryBusiness.setAdapter(adapter01);
        province.setAdapter(adapter02);
    }

    public void submitInfoButton(View v) {
        //each entry needs a unique ID
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
                                // add to firebase
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
}
