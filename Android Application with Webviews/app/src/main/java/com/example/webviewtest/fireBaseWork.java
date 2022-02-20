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

import org.w3c.dom.Document;

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
        //users.document("user3").set(user);

         // things that firebase tool in android studio had me put in; alternate to last line
         // while also giving logcat line in android studio (search "from fireBaseWork" <-- tag)

        instance.db.collection("user_Info").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("from fireBaseWork", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("from fireBaseWork", "Error adding document", e);
                    }
                });
                /**/
    }

    static void insertData(String data, String fieldName, String collection, String document)
    {
        // making references to pre-existing collection and document
        CollectionReference thisColl = instance.db.collection(collection);
        DocumentReference thisDoc = instance.db.document(thisColl+""+document);

        // make hashmap and put data of specific type (string for now) in it before updating FireBase collection
        Map<String, Object> insertion = new HashMap<>();
        insertion.put(fieldName, data);
        thisColl.document(document).set(insertion);
    }

    static String retrieveData(String fieldName, String collection, String document)
    {
        return "test";
    }
}
