package com.example.chetan.n0_dues;


import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Collection;

public class AddDuesFragment extends Fragment
{
    NoDuesDBHelper noDuesDBHelperObj ;
    EditText etNam ,etAmout;
    SQLiteDatabase sqLiteDatabaseObj;
    String nameStr , amStr;

    Spinner addNameFromCtcSpinner;
    Uri conctUri = ContactsContract.Contacts.CONTENT_URI;

    private  String [] coloumnSA = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME};

    private static Collection<String> allDuesOnceColl;

    private static Collection<String> allPhnCtcsColl ;

    private static String [] saPhCtc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
      final View v = inflater.inflate(R.layout.fragment_add_dues, container, false);

        Bundle bdR = getArguments();
        if (bdR != null)
        {
            allPhnCtcsColl = bdR.getStringArrayList("al");
        }
        return v;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        etNam = (EditText) getView().findViewById(R.id.adDueFragEtName);
        etAmout = (EditText) getView().findViewById(R.id.addFragEtDueAmount) ;
        addNameFromCtcSpinner = (Spinner) getView().findViewById(R.id.addFragNameFrmCtctSpinner);
        noDuesDBHelperObj = new NoDuesDBHelper(getView().getContext());

        if (allPhnCtcsColl != null)
        {
            saPhCtc = new String[allPhnCtcsColl.size()];
            int i = 0 ;
            for (String str:allPhnCtcsColl)
            {
                saPhCtc[i++] = str ;
            }

        }
              ArrayAdapter<String> arrayAdapterNames = new ArrayAdapter<String>
                        (getView().getContext(),
                                R.layout.support_simple_spinner_dropdown_item,
                                saPhCtc);
                addNameFromCtcSpinner.setAdapter(arrayAdapterNames);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

}
