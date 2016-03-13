package com.vishnu.www.mystorage.Helpers;

import java.text.DecimalFormat;

/**
 * Created by Wishnu on 3/12/2016.
 */
public class Helper {


    private static DecimalFormat df = new DecimalFormat("0.00");
    public static String getSize(long size){
        float msize = (float)size/(1024*1024);
        if(msize>0){
            if(msize<100)
                return df.format(msize) + "MB";
            else
                return df.format(msize/1024) + "GB";
        }else {
            msize = (float) size /1024;
            return df.format(msize) + "KB";
        }
    }
}
