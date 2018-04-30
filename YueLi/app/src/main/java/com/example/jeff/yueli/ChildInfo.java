package com.example.jeff.yueli;

/**
 * Created by jeff on 18-3-5.
 */

public class ChildInfo {
    private String word;
    private String location;
    private int recordid;

    public String getWord(){return word;}
    public String getLocation(){return location;}
    public int getid(){return recordid;}

    public void setWord(String Word){
        this.word = Word;
    }
    public void setLocation(String Location){
        this.location = Location;
    }

    public void setrecordid(int i) {recordid=i;}
}
