package com.muni.examples.aibrilcall;

/**
 * Created by Administrator on 2017-07-31.
 */

public class CallData {

    private String number;
    private String name;
    private String data;
    private String opponentNumber;
    //getters & setters....
    public void setNumber(String str){
        number=str;
    }
    public void setName(String str){
        name=str;
    }
    public void setData(String str){
        data=str;
    }
    public void setOpponentNumber(String str) {opponentNumber=str;}

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getOpponentNumber() { return opponentNumber; }

}