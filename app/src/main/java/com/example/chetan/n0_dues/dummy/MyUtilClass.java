package com.example.chetan.n0_dues.dummy;

import com.example.chetan.n0_dues.DuplicateDueException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class MyUtilClass
{
    public static Collection<String> sortByAlphabatical(Collection<String> col)
    {

        ArrayList<String> al = new ArrayList<String>(col);
        Collections.sort(al);
        return  al ;
    }

    public static Collection<String> sortByAmount(Collection<String> col)
    {
        MyAMountComparator myAMC = new MyAMountComparator();
        Map<Integer ,String> mp = new TreeMap<Integer, String>(myAMC);
       for (String str:col)
       {
           String []sa = str.split("-");
           mp.put(Integer.parseInt(sa[sa.length-1]),sa[0]);
       }
        //rite back to col
        ArrayList<String> al = new ArrayList<String >();
        for (Object obj:mp.entrySet())
        {
            Map.Entry<Integer,String> ent = (Map.Entry<Integer, String>) obj ;
            al.add(ent.getKey()+"-"+ent.getValue());
        }

        return al;
    }

    public static void validateForDuplication(String nameStr,String dueStr,Collection<String> allDuesOnceColl) throws DuplicateDueException
    {
        int cmpDueInt = Integer.parseInt(dueStr);
        if (allDuesOnceColl != null && allDuesOnceColl.size() > 0 )
        {
            for (String strFull :allDuesOnceColl)
            {
                String [] sa = strFull.split("-");
                String n = sa[0];
                String d = sa[sa.length-1];
                int dInt = Integer.parseInt(d);
                if ( (n.trim().equalsIgnoreCase(nameStr.trim()) )&&  (dInt == cmpDueInt) )
                {
                    throw new DuplicateDueException("THE DUE "+nameStr+" "+dueStr+" ALL READY EXISTS IN YOUR DUE LIST SO CAN NOT ADD DUPLICATE");
                }
            }

        }

    }

    public static void validateBoth(String n,String due)
    {
        if (n != null && due != null)
        {
            if ( n.trim().length() == 0 && due.trim().length() == 0)
                throw new IllegalArgumentException("IF U DONT WANNA GIVE NEITHER OF NEW DETAILS THEN WHAT U WANNA EDIT");
        }

    }

    public static void validateDue(String due)
    {
        int dueInt = 0;
        if (due != null && due.trim().length() > 0)
        {
            dueInt = Integer.parseInt(due);
            if (dueInt <= 0)
                throw new IllegalArgumentException("DUE AMOUNT CAN NOT BE ZERO/NEGATIVE");
        }
    }


    //valida
    public static void  validate(String n,String amt)
    {
        if ( n == null || n.trim().length() == 0)
            throw new IllegalArgumentException("NAME CAN NOT BE BLANK");
        if ( amt == null || amt.trim().length() == 0)
            throw new IllegalArgumentException("DUE AMOUNT CAN NOT BE BLANK");
        int dueInt = Integer.parseInt(amt);
        if (dueInt <= 0)
            throw new IllegalArgumentException("DUE AMOUNT CAN NOT BE ZERO/NEGATIVE");
    }
}
