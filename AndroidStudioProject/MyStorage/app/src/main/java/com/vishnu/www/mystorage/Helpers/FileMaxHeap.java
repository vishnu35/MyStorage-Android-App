package com.vishnu.www.mystorage.Helpers;

import com.vishnu.www.mystorage.Model.MyExt;
import com.vishnu.www.mystorage.Model.MyFile;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by Wishnu on 3/12/2016.
 */
public class FileMaxHeap extends PriorityQueue<MyFile> {
    int size;
    private static final long serialVersionUID = 1L;

    public FileMaxHeap(int size) {
        this.size = size;
    }

    @Override
    public boolean add(MyFile file) {
        if(size==0){
            if(file.getSize()<this.peek().getSize())
                return false;
            else
                this.poll();
        }else
            size--;
        return super.add(file);
    }

    public ArrayList<MyFile> getList(){
        MyFile file = null;
        Stack<MyFile> st = new Stack<MyFile>();
        while((file = this.poll())!=null){
            size++;
            st.push(file);
        }

        ArrayList<MyFile> list = new ArrayList<MyFile>();
        while(!st.isEmpty()){
            file = st.pop();
            list.add(file);
        }
        return list;
    }
}
