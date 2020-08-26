package com.example.chetan.n0_dues.dummy;

import java.util.Comparator;

public class MyAMountComparator implements Comparator<Integer>
{

    @Override
    public int compare(Integer i1, Integer i2)
    {
        if (i1 == i2)
            return 1;
        else if (i1 < i2)
            return -1;
        else
            return 1;

    }
}
