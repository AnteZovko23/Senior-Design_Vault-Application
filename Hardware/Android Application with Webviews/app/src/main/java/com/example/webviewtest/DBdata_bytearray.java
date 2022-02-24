package com.example.webviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
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

        getData = findViewById(R.id.getData);
        getByteArray = findViewById(R.id.getByteArray);

        getData.setOnClickListener(v -> useFirebase(dbData));
        getByteArray.setOnClickListener(v -> BAconversion(byteArr, dbData));
    }

    private void useFirebase(TextView dbData)
    {
        user1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists())
                    {
                        dbData.setText(user1.getId() + " => " + doc.getData());
                    }
                }
                else
                {
                    dbData.setText("Error getting documents");
                }
            }
        }
        );

        dbMan.newUser(); //(adds new user as specified in firebasework
    }

    private void BAconversion(TextView byteArr, TextView dbData)
    {
        /* ideal */
        String baOutput = (String)dbData.getText();
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