package com.example.webviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DBdata_bytearray extends AppCompatActivity {
// textView names/id's: DBdata & byteArr (former button reference is dbData)
// button id's: getData & getByteArray

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("user_Info");
    DocumentReference user1 = users.document("user1");
    allAbtBytes byteManager = allAbtBytes.getInstance();
    fireBaseWork dbMan = fireBaseWork.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbdata_bytearray);

        Button getData;
        Button getByteArray;
        TextView dbData = findViewById(R.id.DBdata);
        TextView byteArr = findViewById(R.id.byteArr);
        ImageView pic = findViewById(R.id.pic);

        getData = findViewById(R.id.getData);
        getByteArray = findViewById(R.id.getByteArray);

        getData.setOnClickListener(v -> useFirebase(dbData, pic));
        getByteArray.setOnClickListener(v -> BAconversion(byteArr, dbData));
    }

    private void useFirebase(TextView dbData, ImageView pic)
    {
        /*
        final DocumentSnapshot[] doc = new DocumentSnapshot[1];
        user1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    doc[0] = task.getResult();
                    if (doc[0].exists())
                    {
                        dbData.setText(user1.getId() + " => " + doc[0].getData());
                    }
                }
                else
                {
                    dbData.setText("Error getting documents");
                }
            }
        }
        );
        /**/

        String output = dbMan.retrieveData("name", "user_Info", "user1");
        Log.d("output string: ",output);
        dbData.setText(output);

        /* */
        //byte[] picBArr = dbMan.parseData("face_img", dbData.getText());
        //byte[] picBArr = doc[0].getData().toString().getBytes();
        //pic.setImageDrawable(byteManager.getPic(picBArr, getResources()));
        //dbMan.newUser(); //(adds new user as specified in firebasework
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