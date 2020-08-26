package com.example.chetan.n0_dues;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chetan.n0_dues.dummy.MyUtilClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>
{

    Spinner edit2Spinner ;
    EditText etNewName , etNewDue;
    static  Collection<String> spinnerColl;

    private static boolean empytLVBool = false ;

    private static ListView delFragLv;

    InnerAsykTask innerAsykTaskObj ;

    static NoDuesDBHelper noDuesDBHelperObj;

    ListView ma2DrawerLV;
    DrawerLayout ma2DL;
    MyABDT myABDTObj;
    ActionBar myActionBar;

    static Menu myMenu;
    //adddition

    EditText etNam, etAmout;
    SQLiteDatabase sqLiteDatabaseObj;
    String nameStr, amStr;

    Spinner addNameFromCtcSpinner;
    Uri conctUri = ContactsContract.Contacts.CONTENT_URI;

    private String[] coloumnSA = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME};

    private static Collection<String> allDuesOnceColl;

    private static Collection<String> allPhnCtcsColl;

    private static ListView showDuesFragLV;

    //innerc calss synt
    private class InnerAsykTask extends AsyncTask<String,Void,Boolean>
    {
        ContentValues cv;
        long result ;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            cv = new ContentValues();
        }

        @Override
        protected Boolean doInBackground(String... valsSA)
        {
            String n = valsSA[0];
            String amt = valsSA[1];
            cv.put("NAME",n);
            cv.put("AMOUNT",amt);

            sqLiteDatabaseObj = noDuesDBHelperObj.getWritableDatabase() ;
            result = sqLiteDatabaseObj.insert("ENTRY",null,cv);
            try
            {
                allDuesOnceColl = noDuesDBHelperObj.retriveAllDues();
            }
            catch (Exception e)
            {
                allDuesOnceColl = new ArrayList<String>();
            }


            if (result == -1)
                return false ;
            else
                return true ;
        }


        @Override
        protected void onPostExecute(Boolean success)
        {
            super.onPostExecute(success);
            if (success == false)
                Toast.makeText(getApplicationContext(),nameStr+" COULD NOT BE ADDED :(",Toast.LENGTH_LONG).show();
            else if (success == true )
                Toast.makeText(getApplicationContext(),nameStr+" IS BEEN ADDED SUCCESSFULLY",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        getLoaderManager().initLoader(1,savedInstanceState,this);

        noDuesDBHelperObj = new NoDuesDBHelper(this);

        try
        {
            allDuesOnceColl = noDuesDBHelperObj.retriveAllDues();
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getLocalizedMessage().toUpperCase(),Toast.LENGTH_LONG).show();
        }


        ma2DrawerLV = (ListView) findViewById(R.id.ma2DrawerLV);

        ma2DL = (DrawerLayout) findViewById(R.id.ma2DrawerLayout);

        ma2DrawerLV.setOnItemClickListener(new InnerDrawer());

        myABDTObj = new MyABDT(this, ma2DL, R.string.openDrawer, R.string.closeDrawer);


        getFragmentManager().addOnBackStackChangedListener
                (new FragmentManager.OnBackStackChangedListener()
                {

            @Override
            public void onBackStackChanged()
            {
                int pos = getPositionOfCurrentFrag();
                setTitleOfMyBar(pos);
                if (myMenu != null)
                    setReleventMenuItems(myMenu);
                else
                    Toast.makeText(getApplicationContext(), "myMENU is null :(", Toast.LENGTH_LONG).show();

            }
        });

        ma2DL.addDrawerListener(myABDTObj);


        myActionBar = getSupportActionBar();
        if (myActionBar != null) {
            myActionBar.setDisplayHomeAsUpEnabled(true);
            myActionBar.setHomeButtonEnabled(true);

        } else {
            Toast.makeText(this, "NULL My ab", Toast.LENGTH_LONG).show();
        }

        //loading al  dues in ma's main screen
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ShowAllDuesFragment shFrag = new ShowAllDuesFragment();
        ft.replace(R.id.ma2FrameLayout, shFrag, "displayedFrag");

        ft.commit();
        showDuesFragLV = (ListView) findViewById(R.id.showDuesFargLV);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle b)
    {
        if( i == 1 )
        {
            return new CursorLoader(getApplicationContext(),conctUri,coloumnSA,null,null,null);
        }
        else
            return null ;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cur)
    {
        ArrayList<String> al = new ArrayList<String>();

        if (cur != null && cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                while (cur.moveToNext())
                {
                    al.add(cur.getString(0));
                }
            }
            Collections.sort(al);
        }
         allPhnCtcsColl = new ArrayList<>(al);

        String[] namesFromctcSA = new String[al.size()];
        int i = 0;
        for (String nameStr : al)
        {
            namesFromctcSA[i++] = nameStr;
        }
       }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }


    class InnerDrawer implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View vw, int posInt, long l) {

            try {

                Intent switchIntent = null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Bundle bd = new Bundle();
                Fragment frag = null ;
                int indexOfArrayInt = 0 ;
                switch (posInt)
                {
                    case 0://show
                        noDuesDBHelperObj.retriveAllDues();
                        frag = new ShowAllDuesFragment();
                        indexOfArrayInt = 0 ;
                        break;
                    case 1://add

                        frag = new AddDuesFragment();
                        indexOfArrayInt = 1;
                        //creae bundle
                        bd.putStringArrayList("al",new ArrayList<String>(allPhnCtcsColl));
                        break;
                    case 2://delete
                        allDuesOnceColl = noDuesDBHelperObj.retriveAllDues();
                        frag =  new DeleteFragment() ;
                        bd.putStringArrayList("al2",new ArrayList<String>(allDuesOnceColl));
                        indexOfArrayInt = 2;
                        break;
                    case 3://edit

                        allDuesOnceColl = noDuesDBHelperObj.retriveAllDues();
                        frag = new EditFragment();
                        bd.putStringArrayList("al2",new ArrayList<String>(allDuesOnceColl));
                        spinnerColl = allDuesOnceColl ;
                        indexOfArrayInt = 3;
                        break;
                    default://credits
                        frag = new CreditsFragment();
                        indexOfArrayInt = 4;
                }

                frag.setArguments(bd);
                ft.replace(R.id.ma2FrameLayout,frag, "displayedFrag");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                ma2DL.closeDrawer(ma2DrawerLV);
                setTitleOfMyBar(posInt);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    class MyABDT extends ActionBarDrawerToggle
    {

        public MyABDT(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView)
        {
            super.onDrawerClosed(drawerView);
            invalidateOptionsMenu();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (myABDTObj.onOptionsItemSelected(item) == true)
            return true;
        else
        {
            int idOfClickedItemInt = item.getItemId();

            ShowAllDuesFragment shFrag = new ShowAllDuesFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Bundle bd = new Bundle();
            Collection<String> cl = null ;

            switch (idOfClickedItemInt)
            {
                case R.id.creditCallMe:
                    String pn = "tel:9742179809";
                    Uri pnUri = Uri.parse(pn);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, pnUri);
                    Intent ctIntent = Intent.createChooser(callIntent, "calling");
                    startActivity(ctIntent);
                    break;

                case R.id.creditsMailMe:
                  /*  Intent mailIntent = new Intent(Intent.ACTION_SEND);
                    mailIntent.setType("message/rfc822");
                    mailIntent.putExtra(Intent.EXTRA_EMAIL, R.string.chetu_is_gmail_com);
                    Intent createIntent = Intent.createChooser(mailIntent, "e mailing");
                    startActivity(createIntent);*/
                    Intent gmailIntent = new Intent(Intent.ACTION_VIEW);
                    gmailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
                    gmailIntent.putExtra(Intent.EXTRA_EMAIL,R.string.chetu_is_gmail_com);
                    gmailIntent.setData(Uri.parse("chetu.is@gmail.com"));
                    gmailIntent.putExtra(Intent.EXTRA_SUBJECT,"FEED BACK");
                    gmailIntent.setType("text/plain");
                    gmailIntent.putExtra(Intent.EXTRA_TEXT,"Thank you so much Chetan");
                    startActivity(gmailIntent);

                    break;
                case R.id.showDuesSortByAmount:
                    cl = MyUtilClass.sortByAmount(allDuesOnceColl);
                    bd.putStringArrayList("al",new ArrayList<String>(cl));
                    shFrag.setArguments(bd);
                    ft.replace(R.id.ma2FrameLayout,shFrag);
                    ft.commit() ;
                    break;
                case R.id.shDuesSortByDate:
                     cl = MyUtilClass.sortByAlphabatical(allDuesOnceColl);
                    bd.putStringArrayList("al",new ArrayList<String>(cl));
                    shFrag.setArguments(bd);
                    ft.replace(R.id.ma2FrameLayout,shFrag);
                    ft.commit() ;
                    break;
                default:
                    Toast.makeText(this, item.toString().toUpperCase(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater mI = getMenuInflater();
        mI.inflate(R.menu.menu_options, m);
        myMenu = m;
              setReleventMenuItems(m);
            return super.onCreateOptionsMenu(m);
    }


    private void setTitleOfMyBar(int pos)
    {
        ActionBar ab = getSupportActionBar();
        String headStr = getTitleOfMyBar();
        if ((ab != null))
            ab.setTitle(headStr);
        else
        {
            Toast.makeText(this, "NULL My action bar :(", Toast.LENGTH_LONG).show();
        }
    }

    private String getTitleOfMyBar()
    {
        int pos = getPositionOfCurrentFrag();
        String headStr = "";
        int indexInt = 0 ;
        switch (pos)
        {
            case 0: indexInt = 0 ;
                break;

            case 1:
                indexInt = 1;
                break;
            case 2:
                indexInt = 2;
                break;
            case 3:
                indexInt = 3 ;
                break;
            default:
                indexInt = 4 ;
             break;
        }
        if (indexInt == 4)
        {
           headStr="By" ;
        }
        else
        {
            headStr = getResources().getStringArray(R.array.ma2NDOptions)[indexInt];
        }
        return headStr;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu m)
    {
        setReleventMenuItems(m);
        myMenu = m;
        if (ma2DL.isDrawerOpen(ma2DrawerLV) == true)
        {
            m.clear();
        }
        return super.onPrepareOptionsMenu(m);
    }

    private void setReleventMenuItems(Menu m)
    {
        String titleStr = getTitleOfMyBar();

        MenuItem mICallMe = m.findItem(R.id.creditCallMe);

        MenuItem mIMailMe = m.findItem(R.id.creditsMailMe);

        MenuItem mISortByDate = m.findItem(R.id.shDuesSortByDate);

        MenuItem mISortByAmount = m.findItem(R.id.showDuesSortByAmount);

        switch (titleStr)
        {
            case "ALL DUES":

                if (mICallMe != null)
                    mICallMe.setVisible(false);
                if (mIMailMe != null)
                    mIMailMe.setVisible(false);
                break;
            case "By":
                if (mISortByDate != null)
                    mISortByDate.setVisible(false);
                if (mISortByAmount != null)
                    mISortByAmount.setVisible(false);
                break;

            default:
                if (mICallMe != null)
                    mICallMe.setVisible(false);
                if (mIMailMe != null)
                    mIMailMe.setVisible(false);
                if (mISortByDate != null)
                    mISortByDate.setVisible(false);
                if (mISortByAmount != null)
                    mISortByAmount.setVisible(false);
                 break;
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        myABDTObj.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        myABDTObj.onConfigurationChanged(newConfig);
    }

    private int getPositionOfCurrentFrag()
    {
        Fragment fr = getFragmentManager().findFragmentByTag("displayedFrag");
        int posInt = 0;
        if (fr instanceof ShowAllDuesFragment)
            posInt = 0;
        else if (fr instanceof AddDuesFragment)
            posInt = 1;
        else if (fr instanceof DeleteFragment)
            posInt = 2;
        else if (fr instanceof EditFragment)
            posInt = 3 ;
        else if (fr instanceof CreditsFragment)
            posInt = 4;
        return posInt;
    }


    @Override
    public void onAttachFragment(Fragment fragment)
    {
        super.onAttachFragment(fragment);
     }

    public void selectCtOfMA(View v)
    {
        addNameFromCtcSpinner = (Spinner)findViewById(R.id.addFragNameFrmCtctSpinner);
        String nStr = addNameFromCtcSpinner.getSelectedItem().toString();
        etNam = (EditText) findViewById(R.id.adDueFragEtName);
        if (nStr != null)
            etNam.setText(nStr);
        Toast.makeText(getApplicationContext()," ADD DUES OF "+nStr.toUpperCase()+" NOW",Toast.LENGTH_SHORT).show();
    }

    public void addToSqLite(View v)
    {
        try
        {
            etNam = (EditText) findViewById(R.id.adDueFragEtName);
            etAmout = (EditText) findViewById(R.id.addFragEtDueAmount) ;


            nameStr = etNam.getText().toString();
            amStr = etAmout.getText().toString();
            //validate for null & empty
            MyUtilClass.validate(nameStr,amStr);
            //bvalid duplicate date
            MyUtilClass.validateForDuplication(nameStr,amStr,allDuesOnceColl);

            innerAsykTaskObj = new InnerAsykTask();
            innerAsykTaskObj.execute(nameStr,amStr);
            etNam.setText("");
            etAmout.setText("");
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void makeChangesOfMA(View v)
    {
        try
        {
            etNewName = (EditText)findViewById(R.id.etedFragNewName);
            etNewDue = (EditText)findViewById(R.id.etedFragNewDue);
            edit2Spinner = (Spinner)findViewById(R.id.edFragAspinner);

            String newName = etNewName.getText().toString();
            String newDue = etNewDue.getText().toString() ;
            String editSpinnerStr="";
            String oldName="",oldDue="";

            String replStr ="";

            if (edit2Spinner != null)
            {
                editSpinnerStr = edit2Spinner.getSelectedItem().toString();
                String [] sa = editSpinnerStr.split("-");
                oldName = sa[0];
                oldDue = sa[sa.length-1];

            }
            if (newName != null && newDue != null)
            {
                MyUtilClass.validateBoth(newName,newDue);
            }
           if (newDue != null)
           {
               if (newDue.trim().length() > 0)
               MyUtilClass.validateDue(newDue);
           }
            if (newName != null && newDue != null)
            {
                if (newName.trim().length() > 0 && newDue.trim().length() > 0)
                MyUtilClass.validateForDuplication(newName,newDue,spinnerColl);
                else if (newName.trim().length() > 0 && newDue.trim().length() <= 0)
                {

                    MyUtilClass.validateForDuplication(newName,oldDue,allDuesOnceColl);
                }
                else if (newName.trim().length() <= 0 && newDue.trim().length() > 0)
                {
                    MyUtilClass.validateForDuplication(oldName,newDue,allDuesOnceColl);
                }
            }
             if (edit2Spinner != null)
            {
                editSpinnerStr = edit2Spinner.getSelectedItem().toString();
                String [] sa = editSpinnerStr.split("-");
                oldName = sa[0];
                oldDue = sa[sa.length-1];

            }

            int lenNewNameInt = newName.trim().length() , lenNewDueInt = newDue.trim().length();

            int x = 0;
            if ( ( lenNewNameInt > 0 )  && (lenNewDueInt > 0) )
            {
                int newDueInt = Integer.parseInt(newDue);
                x =  noDuesDBHelperObj.updateBoth(oldName,oldDue,newName,newDueInt);
                replStr = newName+"-"+newDue;

            }
            else if ( (lenNewNameInt > 0) && (lenNewDueInt == 0))
            {
                x =  noDuesDBHelperObj.updateDB(oldName,oldDue,newName);
                replStr = newName+"-"+oldDue;
            }
            else if (( lenNewNameInt == 0 ) && ( lenNewDueInt > 0))
            {
                int newDueInt = Integer.parseInt(newDue);
                x =  noDuesDBHelperObj.upDateDB(oldName,oldDue,newDueInt);
                replStr = oldName+"-"+newDue;
            }
            if (x <= 0)
                Toast.makeText(getApplicationContext(),oldName+" DOES NOT EXISTS IN DUES LIST TO EDIT",Toast.LENGTH_LONG).show();
            else
            {
                Toast.makeText(getApplicationContext(),"SUCCESSFULLY UPDATED "+x+" DUES DETAILS",Toast.LENGTH_LONG).show();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Bundle bd = new Bundle();

                    bd.putStringArrayList("al2",new ArrayList<String>(noDuesDBHelperObj.retriveAllDues()));



                EditFragment eFrag = new EditFragment();
                eFrag.setArguments(bd);
                ft.replace(R.id.ma2FrameLayout,eFrag,"displayedFrag");
                ft.addToBackStack(null);
                ft.commit();
                etNewName.setText("");
                etNewDue.setText("");
            }


        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }

    }


}

