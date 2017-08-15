package com.muni.examples.aibrilcall;

/**
 * Created by Administrator on 2017-08-03.
 */

public class ListViewItem {
    private String nameStr ;
    private String phoneStr ;

    public void setName(String name) {
        nameStr = name ;
    }
    public void setPhone(String phone) {
        phoneStr = phone ;
    }

    public String getName() {
        return this.nameStr ;
    }
    public String getPhone() {
        return this.phoneStr ;
    }
}
