package com.vishnu.www.mystorage.Helpers;

import com.vishnu.www.mystorage.Model.MyExt;
import com.vishnu.www.mystorage.Model.MyFile;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by Wishnu on 3/12/2016.
 */
public class ExtMaxHeap extends PriorityQueue<MyExt> {
    private int size;
    private static final long serialVersionUID = 1L;

    public ExtMaxHeap(int size) {
        this.size = size;
    }

    @Override
    public boolean add(MyExt ext) {
        if(this.remove(ext)){
            size++;
        }
        if(size==0){
            if(ext.getCount()<this.peek().getCount())
                return false;
            else
                this.poll();
        }else
            size--;
        return super.add(ext);
    }

    public ArrayList<MyExt> getList(){
        MyExt ext = null;
        Stack<MyExt> st = new Stack<MyExt>();
        while((ext= this.poll())!=null){
            size++;
            st.push(ext);
        }

        ArrayList<MyExt> list = new ArrayList<MyExt>();
        while(!st.isEmpty()){
            ext = st.pop();
            list.add(ext);
        }
        return list;
    }
}
