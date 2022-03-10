package com.example.webviewtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class fireBaseWork
{

    private static String dataString1;

    private static fireBaseWork instance = new fireBaseWork();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference thisColl = db.collection("userInfo");
    DocumentReference thisDoc = thisColl.document("user1");

    private fireBaseWork(){}

    public static fireBaseWork getInstance()
    {
        return instance;
    }

    // testing function to get byte array of sample picture
    static String PicB()
    {
        /*
        // make file ('smiley.png') into a drawable
        File file = new File("/app/src/main/res/drawable/smiley.png");
        ImageDecoder.Source source = ImageDecoder.createSource(file);
        Bitmap bmp = ImageDecoder.decodeBitmap(source);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();

        String newS = new String(byteArray, StandardCharsets.UTF_8);
        return newS
         */
        /**/
        /* essentially, get a file input stream of the picture (1);
                        declare a 1024 byte array and write the pictures bytes into the output stream (2);
                        convert output stream to a byte array --just bos.toByteArray()--(3);
                        encode output stream to a string using utf_8 characters (4);
                        return the string for use in other functions/database (5)
         */
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/app/src/main/res/drawable/smiley.png");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int leng = 0;
            while ((leng = fis.read(b)) != -1)
            {
                bos.write(b, 0, leng);
            }
            Log.d("mylog", "here");
            String newS = new String(bos.toByteArray(), StandardCharsets.UTF_8);
            return newS;
        } catch (Exception e) {
            Log.d("mylog", e.toString());
        }
        return "failed";
        /**/
    }

    //function to make new documents in a Firebase collection:
    static void newUser()
    {
        CollectionReference users = instance.db.collection("user_Info");

        // byte array to String call: PicB().toString()
        //Log.d("Picture bytearray: ", PicB());
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Chris John");
        user.put("last", "John");
        user.put("arm_pin", "bytearray2");
        user.put("disarm_pin", "bytearray3");
        user.put("face_img", "base64_2");
        users.document("user3").set(user);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", encodeData("name","Chris John"));
        user2.put("last", encodeData("last","John"));
        user2.put("arm_pin", encodeData("arm_pin","bytearray2"));
        user2.put("disarm_pin", encodeData("disarm_pin","bytearray3"));
        user2.put("face_img", encodeData("face_img","base64_2"));
        users.document("user4").set(user2);

         // things that firebase tool in android studio had me put in; alternate to last line
         // while also giving logcat line in android studio (search "from fireBaseWork" <-- tag)
/*
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
        DocumentReference thisDoc = thisColl.document(document);

        // make hashmap and put data of specific type (string for now) in it before updating FireBase collection
        Map<String, Object> insertion = new HashMap<>();
        insertion.put(fieldName, encodeData(fieldName, data));
        thisColl.document(document).set(insertion);
    }

    static String retrieveData(String fieldName, String collection, String document)
    {

        instance.thisDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                                {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot doc = task.getResult();
                                                        if (doc != null && doc.exists())
                                                        {
                                                            dataString1 = doc.getString("name");
                                                        }


                                                    }
                                                    else
                                                        {
                                                            Log.d("FireBase: ", "failed");
                                                        }
                                                    }

                                            }
        );
        /**/



        return dataString1;
    }

    static boolean isUpper(char c)
    {
        if (((int)c > 64) && ((int)c < 91))
            return true;
        return false;
    }

    static boolean isLower(char c)
    {
        if (((int)c > 96) && ((int)c < 123))
            return true;
        return false;
    }

    static String encodeData(String fieldName, String value)
    {
        String toUpload = "";
        //fieldName will be key for Vigenere Cipher
        // might need to check the fieldName's length in comparison to data's

        int j;
        // var for index of key string (fieldName)
        for (j = 0; j < fieldName.length(); j++)
        {
            if ((isLower(fieldName.charAt(j)) || isUpper(fieldName.charAt(j))))
                toUpload += fieldName.charAt(j);

        }
        fieldName = toUpload;
        toUpload = "";

        if (fieldName.length() < value.length()) {
            j = 0;
            while (fieldName.length() < value.length()) {
                fieldName += fieldName.charAt(j);
                j++;
            }
        }
        j = 0;

        for (int i = 0; i < value.length(); i++)
        {
            if (isUpper(value.charAt(i)))
            {
                if (isLower(fieldName.charAt(j)))
                {
                    toUpload += (char)((((((int)(value.charAt(i))-65) + ((int)(fieldName.charAt(j))-32-65))) % 26) + 65);
                    j++;
                }
                else
                {
                    toUpload += (char)((((((int)(value.charAt(i))-65) + ((int)(fieldName.charAt(j))-65))) % 26) + 65);
                    j++;
                }
            }
            else if (isLower(value.charAt(i)))
            {
                if (isUpper(fieldName.charAt(j)))
                {
                    toUpload += (char)((((((int)(value.charAt(i))-97) + ((int)(fieldName.charAt(j))-65))) % 26) + 97);
                    j++;
                }
                else
                {
                    toUpload += (char)((((((int)(value.charAt(i))-97) + ((int)(fieldName.charAt(j))-97))) % 26) + 97);
                    j++;
                }
            }
            else
                toUpload += value.charAt(i);
        }

        return toUpload;
    }

    static String decodeData(String fieldName, String value)
    {
        String toUse = "";
        //fieldName will be key for Vigenere Cipher
        // might need to check the fieldName's length in comparison to data's

        int j;
        // var for index of key string (fieldName)
        for (j = 0; j < fieldName.length(); j++)
        {
            if ((isLower(fieldName.charAt(j)) || isUpper(fieldName.charAt(j))))
                toUse += fieldName.charAt(j);

        }
        fieldName = toUse;
        toUse = "";

        if (fieldName.length() < value.length()) {
            j = 0;
            while (fieldName.length() < value.length()) {
                fieldName += fieldName.charAt(j);
                j++;
            }
        }
        j = 0;

        for (int i = 0; i < value.length(); i++)
            {
                if (isUpper(value.charAt(i)))
                {
                    if (isLower(fieldName.charAt(j)))
                    {
                        int check = ((((int)(value.charAt(i))-65) - ((int)(fieldName.charAt(j))-32-65)));
                        if (check < 0)
                            check = 26 + check;
                        toUse += (char)((check % 26) + 65);

                    }
                    else
                    {
                        int check = ((((int)(value.charAt(i))-65) - ((int)(fieldName.charAt(j))-65)));
                        if (check < 0)
                            check = 26 + check;
                        toUse += (char)((check % 26) + 65);

                    }
                    j++;
                }
                else if (isLower(value.charAt(i)))
                {
                    if (isUpper(fieldName.charAt(j)))
                    {
                        int check = ((((int)(value.charAt(i))-97) - ((int)(fieldName.charAt(j))-65)));
                        if (check < 0)
                            check = 26 + check;
                        toUse += (char)((check % 26) + 97);

                    }
                    else
                    {
                        int check = ((((int)(value.charAt(i))-97) - ((int)(fieldName.charAt(j))-97)));
                        if (check < 0)
                            check = 26 + check;
                        toUse += (char)((check % 26) + 97);

                    }
                    j++;
                }
                else
                    toUse += value.charAt(i);
            }

        return toUse;
    }

    static byte[] parseArray(String fieldName, String data)
    {
        byte[] toReturn = {};
        char[] delimiters = {'=',','};
        String[] arrays = data.split("=");
        String[] partTwo = new String[20];
        String[] temp;
        for (int i = 0; i < arrays.length; i++)
        {
            temp = arrays[i].split(",");
            for (int j = 0; j < temp.length; j++)
            {
                partTwo[i+j] = temp[j];
            }
        }

        // get the data in array index following that matching with fieldName
        for (int i = 0; i < partTwo.length; i++)
        {
            if (partTwo[i].equals(fieldName))
            {
                toReturn = partTwo[i+1].getBytes();
            }
        }

        return toReturn;


    }
}
