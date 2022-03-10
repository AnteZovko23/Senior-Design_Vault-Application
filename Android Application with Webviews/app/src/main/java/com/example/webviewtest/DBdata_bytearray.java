package com.example.webviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import io.grpc.Context;

public class DBdata_bytearray extends AppCompatActivity {
// textView names/id's: DBdata & byteArr (former button reference is dbData)
// button id's: getData & getByteArray


    FirebaseStorage userStorage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("user_Info");
    DocumentReference user1 = users.document("user1");
    DocumentReference user3 = users.document("user3");
    DocumentReference user4 = users.document("user4");
    //allAbtBytes byteManager = allAbtBytes.getInstance().makeInstance(this);
    fireBaseWork dbMan = fireBaseWork.getInstance();
    String dataString1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbdata_bytearray);

        Button getData;
        Button getByteArray;
        Button sendPic;
        Button getPic;

        TextView dbData = findViewById(R.id.DBdata);
        TextView byteArr = findViewById(R.id.byteArr);
        ImageView pic = findViewById(R.id.pic);

        getData = findViewById(R.id.getData);
        getByteArray = findViewById(R.id.getByteArray);
        sendPic = findViewById(R.id.sendPic);
        getPic = findViewById(R.id.getPic);

        getData.setOnClickListener(v -> useFirebase(dbData, pic,"field"));
        getByteArray.setOnClickListener(v -> BAconversion(byteArr, dbData));
        sendPic.setOnClickListener(v -> sendPic(dbData));
        getPic.setOnClickListener(v -> buildPic(pic, dbData));
    }
/*
    private String retrieveData(String fieldName, String collection, String document)
    {


        return dataString1;
    }
    /**/

    private void useFirebase(TextView dbData, ImageView pic, String field)
    {
        user4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                          {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                              {
                                                  if (task.isSuccessful()) {
                                                      DocumentSnapshot doc = task.getResult();
                                                      if (doc != null && doc.exists())
                                                      {
                                                          dataString1 = dbMan.decodeData("name", doc.getString("name"));
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

        //String output = dbMan.retrieveData("name", "user_Info", "user1");
        //Log.d("output string: ",output);
        //dbMan.decodeData("name", dataString1);
        dbData.setText(dataString1);

        /* */
        //byte[] picBArr = dbMan.parseData("face_img", dbData.getText());
        //byte[] picBArr = doc[0].getData().toString().getBytes();
        //pic.setImageDrawable(byteManager.getPic(picBArr, getResources()));
        //dbMan.newUser(); //(adds new user as specified in firebasework
    }

    private void buildPic(ImageView pic, TextView dbData)
    {
        // accessing the correct user folder and picture name
        // MAKE GENERAL
        String name = "test";
        String filePath = ""+user3.getId();

        StorageReference storageRef = userStorage.getReference(filePath);
        StorageReference smileyImgRef = storageRef.child(name+".png");
        smileyImgRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>()
        {
           @Override
           public void onSuccess(byte[] ba)
           {
               Bitmap finalPic = BitmapFactory.decodeByteArray(ba, 0, ba.length);
               pic.setImageBitmap(finalPic);
           }
        });

        /*
        // OLD CODE: transfers base 64 string of picture into a bitmap
        byte[] ba = Base64.decode(dataString1, Base64.DEFAULT);
        /**/
    }
    private void sendPic(TextView dbData)
    {
        // gets the specific smiley png
        // implement an image chooser here
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.smiley);

        // converts picture to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] b = baos.toByteArray();

        // copy 'getPic' directory navigation here --might use instance variable that is only changed
        //  in this function
        String name = "test";
        String filePath = ""+user3.getId();

        /*  // old idea to encode pic as base 64 string and sending it to FireStore (not using now)
        String smileyPic = Base64.encodeToString(b, Base64.DEFAULT);
        /**/

        // uploads picture (in form of byte array)
        //  might want to add in metadata (nice-to-have development feature to help with debugging
        //  from firebase website console viewpoint)
        StorageReference storageRef = userStorage.getReference(filePath);
        StorageReference smileyImgRef = storageRef.child(name+".png");
        UploadTask uploadTask = smileyImgRef.putBytes(b);



    }

    private void BAconversion(TextView byteArr, TextView dbData)
    {
        /* ideal */
        String baOutput = dbData.getText().toString();
        byte[] byteArray = baOutput.getBytes();
         /**/
        /*
        String baOutput = "test";
        byte[] byteArray = byteManager.createByteArray(baOutput);
        /**/

        String output = " in a bytearray:\n"  + byteArray;
        byteArr.setText(output);
    }
}