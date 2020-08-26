package com.example.chetan.n0_dues;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collection;
public class ShowAllDuesFragment extends Fragment
{


    private static ListView showAllDueLV;
    private NoDuesDBHelper noDuesDBHelperObj;
    private SQLiteDatabase sqLiteDatabaseObj ;
    private Cursor cursorObj ;
    private static Collection<String> allDuesOnceColl ;
    private static boolean fromMA2Bool ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {        // Inflate the layout for this fragment

        if (savedInstanceState != null)
        {
            allDuesOnceColl = getArguments().getStringArrayList("al");
        }
        Bundle b = getArguments() ;
        if (b != null)
        {
            allDuesOnceColl = b.getStringArrayList("al");
        }

        if (allDuesOnceColl == null)
        {
            fromMA2Bool = false ;
        }
        else
        {
            fromMA2Bool = true ;
        }

        return inflater.inflate(R.layout.fragment_show_all_dues, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showAllDueLV = (ListView)(getView().findViewById(R.id.showDuesFargLV));


        if (fromMA2Bool == false)
        {
            showAllDues();
        }
        else
        {
            loadMyLV(allDuesOnceColl);
        }

       showAllDueLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
       {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View v, int i, long l)
           {
               TextView tv = (TextView) v;
               String dueSA [] = tv.getText().toString().split("-");
               String n = dueSA[0];
               int d = Integer.parseInt(dueSA[dueSA.length-1]);
               Toast.makeText(v.getContext(),n.toUpperCase()+" HAS TO GIVE "+d+" INR TO U",Toast.LENGTH_LONG).show();
           }
       });

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (cursorObj != null)
            cursorObj.close();
        if (sqLiteDatabaseObj != null)
            sqLiteDatabaseObj.close();
    }
    private void showAllDues()
    {

        noDuesDBHelperObj = new NoDuesDBHelper(getView().getContext());
        ArrayList<String> alRecv = null;
        try
        {
            alRecv = noDuesDBHelperObj.retriveAllDues();

            String [] saDues = new String[alRecv.size()];
            int indexInt = 0;
            for (String str:alRecv)
            {
                saDues[indexInt++] = str ;
            }
            ArrayAdapter<String> duesArrayAda = new
                    ArrayAdapter<String>(getView().getContext(),
                    android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    saDues);
            showAllDueLV.setAdapter(duesArrayAda);

        }
        catch (Exception e)
        {
            Toast.makeText(getView().getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void loadMyLV(Collection<String> col)
    {
        String [] sa = new String[col.size()];
        int i =0 ;
        for (String str:col)
        {
            sa[i++] = str ;
        }
        ArrayAdapter<String> arrayAdapterObj = new ArrayAdapter<String>
                (
                getView().getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                sa);
        showAllDueLV.setAdapter(arrayAdapterObj);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}
