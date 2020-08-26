package com.example.chetan.n0_dues;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
public class NoDuesDBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME ="nodues" ;
    private static final int DB_VERSION = 1;
    SQLiteDatabase dbObj ;
    public NoDuesDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String cretaeTablStr = "CREATE TABLE ENTRY (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+"NAME TEXT, "+"AMOUNT INTEGER);";
        db.execSQL(cretaeTablStr);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {

    }
  public ArrayList<String> retriveAllDues() throws NoDuesAvailableYetException
    {
        ArrayList<String> alREt = new ArrayList<String>();
        dbObj = getReadableDatabase() ;
        String selectCoomandStr = "SELECT * FROM ENTRY";
        Cursor c = dbObj.rawQuery(selectCoomandStr,null);

            if (c.getCount() > 0)
            {
                while (c.moveToNext())
                {
                    alREt.add(c.getString(1)+"-"+c.getInt(2));
                }
            }
            else
                throw new NoDuesAvailableYetException("THERE ARE NO DUES DETAILS ADDED YET");
       return alREt ;
    }
    public int deleteFromDb(String str)
    {
        int i = 0;
        dbObj = getWritableDatabase() ;
        String[] sa = str.split("-");
        String nameStr = sa[0];
        int amt = Integer.parseInt(sa[sa.length-1]);
      //  String delComnStr = "DELETE FROM ENTRY WHERE NAME = "+"'"+str+"'";


       // i = dbObj.delete("ENTRY","NAME"+" = '"+str+"'",null);
        String whereStr = "NAME"+" = '"+nameStr+"' AND AMOUNT = "+amt;

        i = dbObj.delete("ENTRY",whereStr,null);
       // dbObj.execSQL(delComnStr);
        return i;
    }

    public int upDateDB(String oldName ,String oldDue, int newdue)
    {

        dbObj = getWritableDatabase() ;
        ContentValues cv = new ContentValues();
        cv.put("AMOUNT",newdue);
        int oldDueInt = Integer.parseInt(oldDue);
        String whereStr = "NAME"+" = '"+oldName+"' AND AMOUNT = "+oldDueInt;

      // int resInt =  dbObj.update("ENTRY",cv,"NAME = '"+oldName+"'",null);
        int resInt = dbObj.update("ENTRY",cv,whereStr,null);

      /*  String upCmdStr = "UPDATE ENTRY SET AMOUNT ="+newdue+" WHERE NAME ='"+oldName+"'";
        dbObj.execSQL(upCmdStr);
        return 1;*/
       return resInt ;
    }
    public int updateDB(String oldName ,String oldDue, String newName)
    {
        dbObj = getWritableDatabase() ;
        ContentValues cv = new ContentValues();
        cv.put("NAME",newName);

        int oldDueInt = Integer.parseInt(oldDue);
        String whereStr = "NAME"+" = '"+oldName+"' AND AMOUNT = "+oldDueInt;

        int resInt = dbObj.update("ENTRY",cv,whereStr,null);
        //int resInt =  dbObj.update("ENTRY",cv,"NAME = '"+oldname+"'",null);
       // String upCmdStr = "UPDATE ENTRY SET NAME = '"+newName+"' WHERE NAME ='"+oldname+"'";
        return resInt ;
      /*  dbObj.execSQL(upCmdStr);
        return 0;*/
    }
    public int updateBoth(String oldName,String oldDue,String newName,int newdue)
    {
        dbObj = getWritableDatabase() ;
        ContentValues cv = new ContentValues();
        cv.put("NAME",newName);
        cv.put("AMOUNT",newdue);

        int oldDueInt = Integer.parseInt(oldDue);
        String whereStr = "NAME"+" = '"+oldName+"' AND AMOUNT = "+oldDueInt;

        int resInt = dbObj.update("ENTRY",cv,whereStr,null);

     // int resInt =  dbObj.update("ENTRY",cv,"NAME = '"+oldName+"'",null);
       // String upCmdStr = "UPDATE ENTRY SET NAME = '"+newName+"' , AMOUNT ="+newdue+" WHERE NAME ='"+oldName+"'";
       // dbObj.execSQL(upCmdStr);

        return resInt;
    }
}

 /*   public Map<String,String> retriveAllDues()
    {

        Map<String,String> mp = new LinkedHashMap<String, String>();
        dbObj = this.getWritableDatabase() ;

        String selectCoomandStr = "SELECT * FROM ENTRY";
       // String isTableExistStr ="SELECT name FROM sqlite_master WHERE type='table' AND name='ENTRY'";
        Cursor c = dbObj.rawQuery(selectCoomandStr,null);

      //  Cursor c = dbObj.rawQuery(isTableExistStr,null);

        if (c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                mp.put(c.getString(1),c.getString(2));
            }
        }
        else
        {
            throw new IllegalArgumentException("THERE ARE NO DUES DETAILS ADDED YET");
        }
  if (mp.size() == 0)
      throw  new IllegalArgumentException("MAP size = 0,,,nothing retrved frm DB :(");
       return mp ;
    }*/


  /*  public long addToNoDuesDb(String name ,int amt)
    {
        dbObj = getWritableDatabase();
        ContentValues dueCV = new ContentValues();
        dueCV.put("NAME",name);
        dueCV.put("AMOUNT",amt);

       long succLong= dbObj.insert("ENTRY",null,dueCV);
        if (succLong == -1)
            throw new IllegalArgumentException("addidtion failed ");

        return succLong ;
    }*/