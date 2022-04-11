package com.example.webviewtest.data;



import android.util.Log;


import com.google.firebase.auth.FirebaseUser;

import java.util.Hashtable;

/**
 * Class that stores userInfo that needs to be used throughout the app without Firebase
 */
public class localUsers {


    private static localUsers instance = new localUsers();
    private static Hashtable<String, String> userDict;



    private localUsers()
    {
        userDict = new Hashtable<String, String>();
        // probably make a dictionary of the {userName, user.getEmail()}
    }


    public static localUsers getInstance()
    {
        return instance;
    }

    public void saveName(FirebaseUser user, String userName)
    {
        userDict.put(user.getEmail(), userName);
    }

    public String retrieveName(FirebaseUser user)
    {
        Log.d("Is my dictionary empty?", ""+userDict.isEmpty());
        return userDict.get(user.getEmail());
    }


}

