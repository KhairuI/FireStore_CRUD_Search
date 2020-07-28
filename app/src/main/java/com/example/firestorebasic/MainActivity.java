package com.example.firestorebasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

   private AppCompatEditText titleEditText,descriptionEdiText;
   private AppCompatButton saveButton,displayButton;
    private FirebaseFirestore firestore;
    private String updateId,updateTitle,updateDescription;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findSection();
        
        bundle= getIntent().getExtras();
        if(bundle!=null){
            //update data
            updateData();
        }
        else {
            //Insert Data
            getSupportActionBar().setTitle("Insert data");
            saveButton.setText("SAVE");
            
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1= getIntent().getExtras();
                if(bundle1 != null){
                    //update
                    String id= updateId;
                    String title= titleEditText.getEditableText().toString();
                    String desc= descriptionEdiText.getEditableText().toString();
                    updateDatabase(id,title,desc);
                }
                else {
                    //Insert data
                    String title= titleEditText.getEditableText().toString();
                    String desc= descriptionEdiText.getEditableText().toString();
                    if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)){
                        insertData(title,desc);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Empty field", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MainActivity.this,DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void updateDatabase(String id, String title, String desc) {
        //show spots dialogue here.....
        final AlertDialog dialogue= new SpotsDialog.Builder().setContext(MainActivity.this).setTheme(R.style.Custom).setCancelable(true).build();
        dialogue.show();
        Map<String,String> updateMap= new HashMap<>();
        updateMap.put("title",title);
        updateMap.put("description",desc);

        firestore.collection("UserData").document(id).update("title",title,
                "search",title.toLowerCase(),"description",desc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialogue.dismiss();
                        Toast.makeText(MainActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogue.dismiss();
                Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateData() {
        getSupportActionBar().setTitle("Update data");
        saveButton.setText("UPDATE");
        updateId= bundle.getString("uId");
        updateTitle= bundle.getString("uTitle");
        updateDescription= bundle.getString("uDescription");

        //set data again in Textview
        titleEditText.setText(updateTitle);
        descriptionEdiText.setText(updateDescription);
    }

    private void insertData(String title, String desc) {
        //show spots dialogue here.....
        final AlertDialog dialogue= new SpotsDialog.Builder().setContext(MainActivity.this).setTheme(R.style.Custom).setCancelable(true).build();
        dialogue.show();

        String id= UUID.randomUUID().toString();
        Map<String,String> dataMap= new HashMap<>();
        dataMap.put("id",id);
        dataMap.put("title",title);
        dataMap.put("search",title.toLowerCase());
        dataMap.put("description",desc);

        //add on firestore,.....
        firestore.collection("UserData").document(id).set(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                dialogue.dismiss();
                Toast.makeText(MainActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogue.dismiss();
                Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findSection() {

        titleEditText= findViewById(R.id.titleId);
        descriptionEdiText= findViewById(R.id.descriptionId);
        saveButton= findViewById(R.id.saveButtonId);
        displayButton= findViewById(R.id.displayButtonId);
        firestore= FirebaseFirestore.getInstance();
    }
}