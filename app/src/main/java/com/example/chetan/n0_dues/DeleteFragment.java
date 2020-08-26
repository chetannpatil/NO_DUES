package com.example.chetan.n0_dues;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import java.util.Collection;

public class DeleteFragment extends Fragment
{
    private NoDuesDBHelper noDuesDBHelperObj;
    private SQLiteDatabase sqLiteDatabaseObj ;
    private Cursor cursorObj ;
    private static ListView delFragLv;
    private static Collection<String> allDuesOnceCol ;
    private static boolean empytLVBool = false ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Bundle b= getArguments() ;
        if (b != null)
        {
            allDuesOnceCol = b.getStringArrayList("al2");
        }
        return inflater.inflate(R.layout.fragment_delete, container, false);
    }


    @Override
    public void onStart()
    {
        super.onStart();
        noDuesDBHelperObj = new NoDuesDBHelper(getView().getContext());
        delFragLv = (ListView)getView().findViewById(R.id.delFragLV);
        showAllDues();
       delFragLv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {


                if (empytLVBool == false)
                {
                    TextView tv = (TextView) view;
                    final String dueStr = tv.getText().toString();
                    AlertDialog.Builder bull = new AlertDialog.Builder(view.getContext());
                    bull.setCancelable(true);

                    bull.setIcon(R.mipmap.chetan_patil);
                    bull.setTitle("DO YOU REALY WANT TO DELETE  " + dueStr + "  ?");
                    bull.setPositiveButton("YES DELETE ", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String[] sa = dueStr.split("-");
                            String nameStr ="";
                            if (sa.length > 0)
                                nameStr = sa[0];
                            try {
                                int resInt = noDuesDBHelperObj.deleteFromDb(dueStr);

                                if (resInt == 1)
                                {
                                    Toast.makeText(getView().getContext(), nameStr + " IS BEEN SUCCESSFULLY DELETED :)", Toast.LENGTH_LONG).show();
                                    //dele from statc coll
                                    allDuesOnceCol.remove(dueStr);
                                    if (allDuesOnceCol.size() == 0)
                                    {
                                        empytLVBool = true ;
                                    }
                                    if (allDuesOnceCol.size() == 0)
                                    {
                                        String [] sa2 = getResources().getStringArray(R.array.del2SA);
                                        ArrayAdapter<String> arrayAdapterObj2 = new ArrayAdapter<String>(
                                                getView().getContext(),
                                                R.layout.support_simple_spinner_dropdown_item,
                                                sa2);

                                        delFragLv.setAdapter(arrayAdapterObj2);

                                    }
                                    else
                                        showAllDues();

                                } else
                                    Toast.makeText(getView().getContext(), nameStr + " DOES NOT EXISTS IN THE DUES LIST TO DELETE", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getView().getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    bull.setNegativeButton("DO NOT DELETE :(", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getView().getContext(), "DONT WORRY " + dueStr + " IS SAFE FOR NOW :) ", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog alrt = bull.create();

                    alrt.show();
                }
                else
                {
                    Toast.makeText(getView().getContext(),"NO WORTH OF STAYING HERE ,PLEASE GO BACK TO MAIN MENU",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void showAllDues()
    {
        String [] saDues = new String[allDuesOnceCol.size()];
        int indexInt = 0;
        for (String str:allDuesOnceCol)
        {
            saDues[indexInt++] = str ;

        }
        ArrayAdapter<String> duesArrayAda = new
                ArrayAdapter<String>(getView().getContext(),
                android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,
                saDues);
       delFragLv.setAdapter(duesArrayAda);

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
}
