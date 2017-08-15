package com.muni.examples.aibrilcall;

/**
 * Created by Administrator on 2017-07-31.
 */

public class CallData {

    private String number;
    private String data;
    private String opponentNumber;
    private String time;
    //getters & setters....
    public void setNumber(String str){
        number=str;
    }
    public void setData(String str){
        data=str;
    }
    public void setOpponentNumber(String str) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!진입");
        str=str.replaceAll("-","");
        opponentNumber=str;
    }
    public void setTime(String str){
        time=str;
    }

    public String getNumber() {
        return number;
    }

    public String getData() {
        return data;
    }

    public String getOpponentNumber() {
        return opponentNumber; }

    public String getTime() {
        return time;
    }

}