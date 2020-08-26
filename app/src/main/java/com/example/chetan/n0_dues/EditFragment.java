package com.example.chetan.n0_dues;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Collection;
public class EditFragment extends Fragment
{
    private NoDuesDBHelper noDuesDBHelperObj;
    private SQLiteDatabase sqLiteDatabaseObj ;
    private Cursor cursorObj ;
    Spinner edit2Spinner ;
    EditText etNewName , etNewDue;
    static  Collection<String> spinnerColl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


        Bundle bd = getArguments() ;
        if (bd != null )
        {
           spinnerColl = bd.getStringArrayList("al2");
        }

        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        noDuesDBHelperObj = new NoDuesDBHelper(getView().getContext());

        etNewName = (EditText)getView().findViewById(R.id.etedFragNewName);
        etNewDue = (EditText)getView().findViewById(R.id.etedFragNewDue);
        edit2Spinner = (Spinner)getView().findViewById(R.id.edFragAspinner);

        loadSpinner();


    }

    private void loadSpinner()
    {
        String [] sa = new String[spinnerColl.size()];

        int i=0;
        for (String str:spinnerColl)
        {
            sa[i++]= str;
        }
        ArrayAdapter<String> arrayAdapterObj =  new ArrayAdapter<String>(
                getView().getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                sa);
        edit2Spinner.setAdapter(arrayAdapterObj);
    }

}
