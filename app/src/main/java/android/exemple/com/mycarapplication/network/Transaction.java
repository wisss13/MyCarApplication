package android.exemple.com.mycarapplication.network;

import org.json.JSONArray;

import android.exemple.com.mycarapplication.domain.WrapObjToNetwork;

public interface Transaction {
    WrapObjToNetwork doBefore();

    void doAfter(JSONArray jsonArray);
}
