package com.example.firestorebasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class DisplayActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private List<Model> modelList= new ArrayList<>();
    public CustomAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        getSupportActionBar().setTitle("All List");

        //find section

        recyclerView= findViewById(R.id.recycleViewId);
        firestore= FirebaseFirestore.getInstance();
        floatingActionButton= findViewById(R.id.floatingButtonId);
        recyclerView= findViewById(R.id.recycleViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        showData();

        
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DisplayActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showData() {
        //show spots dialogue here.....
        final AlertDialog dialogue= new SpotsDialog.Builder().setContext(DisplayActivity.this).setTheme(R.style.Custom).setCancelable(true).build();
        dialogue.show();

        firestore.collection("UserData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                modelList.clear();
                dialogue.dismiss();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    Model model= new Model(documentSnapshot.getString("id"),documentSnapshot.getString("title")
                            ,documentSnapshot.getString("description"));
                    modelList.add(model);
                }
                adapter= new CustomAdapter(DisplayActivity.this,modelList);
                recyclerView.setAdapter(adapter);


            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialogue.dismiss();
                        Toast.makeText(DisplayActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteData(final int position){

        //show spots dialogue here.....
        final AlertDialog dialogue= new SpotsDialog.Builder().setContext(DisplayActivity.this).setTheme(R.style.Custom).setCancelable(true).build();
        dialogue.show();
        firestore.collection("UserData").document(modelList.get(position).getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                dialogue.dismiss();
                Toast.makeText(DisplayActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                showData();
                //adapter.notifyItemRemoved(position);
               // modelList.clear();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                dialogue.dismiss();
                Toast.makeText(DisplayActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem= menu.findItem(R.id.searchId);
        SearchView searchView= (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    private void searchData(String s) {
        //show spots dialogue here.....
        final AlertDialog dialogue= new SpotsDialog.Builder().setContext(DisplayActivity.this).setTheme(R.style.Custom).setCancelable(true).build();
        dialogue.show();

        firestore.collection("UserData").whereEqualTo("search",s.toLowerCase()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        modelList.clear();
                        dialogue.dismiss();
                        for (DocumentSnapshot documentSnapshot: task.getResult()){
                            Model model= new Model(documentSnapshot.getString("id"),documentSnapshot.getString("title")
                                    ,documentSnapshot.getString("description"));
                            modelList.add(model);
                        }
                        adapter= new CustomAdapter(DisplayActivity.this,modelList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialogue.dismiss();
                        Toast.makeText(DisplayActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}