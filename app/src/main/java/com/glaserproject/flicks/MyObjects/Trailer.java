package com.glaserproject.flicks.MyObjects;

/**
 * Trailer Object
 */

public class Trailer {

    private String mKey;
    private String mName;
    private String mSite;

    public Trailer (String key, String name, String site){
        this.mKey = key;
        this.mName = name;
        this.mSite = site;
    }

    public String getKey () {
        return mKey;
    }
    public String getName () {
        return mName;
    }
    public String getSite (){
        return mSite;
    }
}
