package com.example.webviewtest;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class fireBaseWork
{
    private static fireBaseWork instance = new fireBaseWork();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private fireBaseWork(){}

    public static fireBaseWork getInstance()
    {
        return instance;
    }

    //function to make new documents in a Firebase collection:
    static void newUser()
    {
        CollectionReference users = instance.db.collection("user_Info");

        Map<String, Object> user = new HashMap<>();
        user.put("name", "Chris John");
        user.put("last", "John");
        user.put("arm_pin", "bytearray2");
        user.put("disarm_pin", "bytearray3");
        user.put("face_img", "base64_2");
        users.document("user2").set(user);

        /* // things that firebase tool in android studio had me put in
        instance.db.collection("user_Info").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

         */

    }
}
