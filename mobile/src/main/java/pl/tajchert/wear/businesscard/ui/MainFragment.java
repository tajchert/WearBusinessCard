package pl.tajchert.wear.businesscard.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iangclifton.android.floatlabel.FloatLabel;

import pl.tajchert.businesscardwear.R;
import pl.tajchert.wear.businesscard.UpdateService;
import pl.tajchert.wear.businesscard.util.PreferencesSaver;
import pl.tajchert.wear.businesscard.util.ValuesCons;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MainFragment extends android.support.v4.app.Fragment {
    private FloatLabel textName;
    private FloatLabel textSurname;
    private FloatLabel textPhone;
    private FloatLabel textEmail;
    private FloatLabel textWebsite;
    private FloatLabel textAddress;
    private PreferencesSaver prefs;


    public static final String EXTRA_CONTACT_URI ="pl.tajchert.wear.businesscard.ui.EXTRA_CONTACT_URI";

    public static MainFragment newInstance() {
        final MainFragment fragment = new MainFragment();
        /*if(contactUri != null) {
            final Bundle args = new Bundle();
            args.putParcelable(EXTRA_CONTACT_URI, contactUri);
            fragment.setArguments(args);
        }*/
        return fragment;
    }

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textSurname = (FloatLabel) view.findViewById(R.id.text_contact_surname);
        textName = (FloatLabel) view.findViewById(R.id.text_contact_name);
        textPhone = (FloatLabel) view.findViewById(R.id.text_contact_phone);
        textEmail = (FloatLabel) view.findViewById(R.id.text_contact_email);
        textAddress = (FloatLabel) view.findViewById(R.id.text_contact_address);
        textWebsite = (FloatLabel) view.findViewById(R.id.text_contact_website);

        prefs = new PreferencesSaver(getActivity());
        return view;
    }

    @Override
    public void onPause() {
        saveToSharedPrefs();
        getActivity().startService(new Intent(getActivity(),UpdateService.class));
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        getSharedPrefs();
    }

    private void getSharedPrefs(){
        if(prefs==null){
            prefs= new PreferencesSaver(getActivity());
        }
        textAddress.getEditText().setText(prefs.getStringValue(ValuesCons.PREFS_ADDRESS));
        textName.getEditText().setText(prefs.getStringValue(ValuesCons.PREFS_NAME));
        textSurname.getEditText().setText(prefs.getStringValue(ValuesCons.PREFS_SURNAME));
        textWebsite.getEditText().setText(prefs.getStringValue(ValuesCons.PREFS_WEBSITE));
        textEmail.getEditText().setText(prefs.getStringValue(ValuesCons.PREFS_EMAIL));
        textPhone.getEditText().setText(prefs.getStringValue(ValuesCons.PREFS_PHONE));

    }

    private void saveToSharedPrefs(){
        String name = textName.getEditText().getText().toString();
        String surname = textSurname.getEditText().getText().toString();
        String phoneS = textPhone.getEditText().getText().toString();
        String emailS = textEmail.getEditText().getText().toString();
        String websiteS = textWebsite.getEditText().getText().toString();
        String address = textAddress.getEditText().getText().toString();


        prefs.saveStringValue(ValuesCons.PREFS_NAME, name);
        prefs.saveStringValue(ValuesCons.PREFS_SURNAME, surname);
        prefs.saveStringValue(ValuesCons.PREFS_PHONE, phoneS);
        prefs.saveStringValue(ValuesCons.PREFS_EMAIL, emailS);
        prefs.saveStringValue(ValuesCons.PREFS_WEBSITE, websiteS);
        prefs.saveStringValue(ValuesCons.PREFS_ADDRESS, address);

        prefs.saveStringValue(ValuesCons.PREFS_QR_CONTENT, setCodeContent());
    }


    private String setCodeContent(){
        String name = textName.getEditText().getText().toString();
        String surname = textSurname.getEditText().getText().toString();
        String phoneS = textPhone.getEditText().getText().toString();
        String emailS = textEmail.getEditText().getText().toString();
        String websiteS = textWebsite.getEditText().getText().toString();
        String address = textAddress.getEditText().getText().toString();

        String content = "MECARD:";
        content += "N:"+ surname + ", "+ name + ";";
        if(phoneS.length()>0){
            content += "TEL:" + phoneS+ ";";
        }
        if(emailS.length()>0){
            content += "EMAIL:" + emailS + ";";
        }
        if(websiteS.length()>0){
            content += "URL:" + websiteS + ";";
        }
        if(address.length()>0){
            content += "ADR:" + address + ";";
        }
        content +=";";
        return content;
    }
}
