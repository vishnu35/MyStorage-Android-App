package com.vishnu.www.mystorage.Model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Wishnu on 3/12/2016.
 */
public class MyFile implements Comparable,Serializable {
    private File file;
    private String name;
    private String ext;
    private long size;

    public MyFile(File file){
        this.name = file.getName();
        this.ext = getFileExtension();
        this.size = file.length();
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public long getSize() {
        return size;
    }

    private String getFileExtension() {
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int compareTo(Object another) {
        if (getClass() != another.getClass())
            return -1;
        MyFile other = (MyFile) another;
        if(this.size>other.size){
            return 1;
        }else if(this.size==other.size){
            return 0;
        }
        return -1;
    }
}
