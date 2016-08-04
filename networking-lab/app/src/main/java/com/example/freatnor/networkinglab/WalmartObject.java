package com.example.freatnor.networkinglab;

/**
 * Created by Jonathan Taylor on 8/4/16.
 */
public class WalmartObject {

    private String mName;
    private String mPrice;

    public WalmartObject(String name, String price) {
        mName = name;
        mPrice = price;
    }

    public String getName() {
        return mName;
    }

    public String getPrice() {
        return mPrice;
    }
}
