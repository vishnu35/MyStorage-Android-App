package com.vishnu.www.mystorage.Model;

import java.io.Serializable;

/**
 * Created by Wishnu on 3/12/2016.
 */
public class MyExt implements Comparable,Serializable{
    String extension;
    int count;

    public MyExt(String extension) {
        this.extension = extension;
        this.count = 1;
    }

    public String getExtension() {
        return extension;
    }

    public int getCount() {
        return count;
    }

    public int incrementCount(){
        count++;
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass()){
            return super.equals(o);
        }
        return this.extension == ((MyExt)o).extension;
    }

    @Override
    public int compareTo(Object another) {
        if (getClass() != another.getClass())
            return -1;
        MyExt other = (MyExt) another;
        if(this.count>other.count){
            return 1;
        }else if(this.count==other.count){
            return 0;
        }
        return -1;
    }

}
