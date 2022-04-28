package com.example.webviewtest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webviewtest.data.localUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.grpc.Context;

public class DBdata_bytearray extends AppCompatActivity {
// textView names/id's: DBdata & byteArr (former button reference is dbData)
// button id's: getData & getByteArray

    private FirebaseStorage userStorage;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currUser;
    private CollectionReference users;
    private DocumentReference userDoc;


    fireBaseWork dbMan = fireBaseWork.getInstance();
    String dataString1;

    Bitmap bitmap;

    String name, storagePath, uriPath, fileName, suffix, imgType;
    Uri picUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbdata_bytearray);

        Button getData = findViewById(R.id.getData);
        Button getByteArray = findViewById(R.id.getByteArray);
        Button sendPic = findViewById(R.id.sendPic);
        Button getPic = findViewById(R.id.getPic);
        ProgressBar uploadProgress = findViewById(R.id.uploadPB);

        TextView dbData = findViewById(R.id.DBdata);
        TextView byteArr = findViewById(R.id.byteArr);
        ImageView pic = findViewById(R.id.pic);

        userStorage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currUser = firebaseAuth.getCurrentUser();
        users = db.collection("user_Info");
        // might need to make/reference document of current user's name
        //  essentially; check if users.doc... is null or not
        // get's firestore document for current user
        //userDoc = users.document(currUser.getDisplayName());

        name = currUser.getDisplayName();
        suffix = "";
        userDoc = users.document(name);
        storagePath = name+"/";
        fileName = uriPath ="";
        imgType = ".jpg";

        uploadProgress.setVisibility(View.GONE);

        getData.setOnClickListener(v -> useFirebase(dbData, pic,"name"));
        getByteArray.setOnClickListener(v -> BAconversion(byteArr, dbData));
        sendPic.setOnClickListener(v -> sendPic(dbData, pic, sendPic, uploadProgress));
        getPic.setOnClickListener(v -> buildPic(pic, dbData));
    }


    private void incrementSuffix()
    {
        if (suffix == "")
            suffix = "1";
        else
            suffix = Integer.toString(Integer.parseInt(suffix)+1);

    }

    // change later to a check in folder for other files of saved name
    private void setFileName(StorageReference userRef)
    {
        // first use picture's name (if none, prompt user for name)

        //
        fileName = name+suffix+imgType;

        // check if file with same name is in user's folder
        /*
        userRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.d("item: ", item.getString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
        Log.d("current fileName: ", fileName);
        */
    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null)
                            return;
                        try {
                            Uri uri = data.getData();
                            //Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            //storagePath = getFileName(uri,getApplicationContext());
                            uriPath = uri.getPath();
                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                            //name = getFileName(uri, Context.current()); //<-- check function name
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                            Log.d("IOException","still issue with bitmap");
                        }
                    }
                }
            }
    );

    public void openFileChooser()
    {
        Intent intent = new Intent(); //(ACTION_OPEN_DOCUMENT);
        /**/
        intent.setType("image/*");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //String[] mimeTypes = {"image/*"};
        //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        sActivityResultLauncher.launch(intent);
    }

    /*
    String getFileName(Uri uri, Context context)
    {
        String res = null;
        if (uri.getScheme().equals("content"))
        {
            Cursor cursor = context.getContentResolver().query(uri,null,null,null,null);
            try
                {
                    if (cursor != null && cursor.moveToFirst())
                    {
                        res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
            finally
            {
                cursor.close();
            }
            if (res == null)
            {
                res =  uri.getPath();
                int cutt = res.lastIndexOf('/');
                if (cutt != -1)
                {
                    res = res.substring(cutt + 1);
                }
            }


        }
        return res;
    }
/**/
    private void useFirebase(TextView dbData, ImageView pic, String field)
    {
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
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
        dbData.setText(currUser.getDisplayName());


        /* */
        //dbMan.newUser(); //(adds new user as specified in firebasework
    }

    private void buildPic(ImageView pic, TextView dbData)
    {
        // accessing the correct user folder and picture name
        // MAKE GENERAL


        StorageReference storageRef = userStorage.getReference(storagePath);
        StorageReference smileyImgRef = storageRef.child(name+suffix+".jpg");
        incrementSuffix();
        final long maxSize = 1024*1024*200; // approximately 200 megabytes (use for video if we do)
        smileyImgRef.getBytes(maxSize).addOnSuccessListener(new OnSuccessListener<byte[]>()
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

    // rotates picture by 90 degrees clockwise
    // NEED TO EDIT PIVOTS X & Y
    private void rotatePic(ImageView pic)
    {
        Matrix matrix = new Matrix();
        pic.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate((float) 90, 100, 100); // we don't know pivotX & pivotY
        pic.setImageMatrix(matrix);
    }

    private void sendPic(TextView dbData, ImageView pic, Button button, ProgressBar progressBar)
    {
        // gets the specific smiley png
        // implement an image chooser here
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.smiley);

        //openFileChooser();

        //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),picUri);

        //Log.d("filePath: ", uriPath);
        //dbData.setText(uriPath);
        //bitmap = BitmapFactory.decodeFile(filePath);

        pic.setImageBitmap(bitmap);
        //rotatePic(pic);


        // converts picture to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        byte[] b = baos.toByteArray();


        // uploads picture (in form of byte array)
        //  might want to add in metadata (nice-to-have development feature to help with debugging
        //  from firebase website console viewpoint)
        StorageReference storageRef = userStorage.getReference(storagePath);
        setFileName(storageRef);
        StorageReference smileyImgRef = storageRef.child(fileName);
        incrementSuffix();

        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);

        UploadTask uploadTask = smileyImgRef.putBytes(b);
        uploadTask.addOnSuccessListener(DBdata_bytearray.this, new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
            }
        });


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