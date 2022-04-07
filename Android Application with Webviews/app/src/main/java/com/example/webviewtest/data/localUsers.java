package com.example.webviewtest.data;


import com.google.firebase.auth.FirebaseUser;

import java.util.Hashtable;

/**
 * Class that stores userInfo that needs to be used throughout the app without Firebase
 */
public class localUsers {

    private Hashtable<String, String> userDict;

    private localUsers()
    {
        userDict = new Hashtable<String, String>();
        // probably make a dictionary of the {userName, user.getEmail()}
    }

    public void saveName(FirebaseUser user, String userName)
    {
        userDict.put(userName, user.getEmail());
    }


}
