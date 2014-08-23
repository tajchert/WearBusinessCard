package pl.tajchert.wear.businesscard.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iangclifton.android.floatlabel.FloatLabel;

import pl.tajchert.businesscardwear.R;
import pl.tajchert.wear.businesscard.util.Tools;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setCodeContent(){
        String name = textName.getEditText().getText().toString();
        String surname = textName.getEditText().getText().toString();
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
        content +=";";
        Tools.makeQRCode(content);
    }



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
        return view;
    }


}
