package com.example.ashu.okhttp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result {
    @SerializedName("items")
    public ArrayList<Items> arrayList;

    public ArrayList<Items> getArrayList() {
        return arrayList;
    }
}
