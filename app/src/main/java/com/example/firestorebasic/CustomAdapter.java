package com.example.firestorebasic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ModelViewHolder> {
    DisplayActivity displayActivity;
    List<Model> modelList;
    Context context;

    public CustomAdapter(DisplayActivity displayActivity, List<Model> modelList) {
        this.displayActivity= displayActivity;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item,parent,false);

        ModelViewHolder viewHolder= new ModelViewHolder(view);

        //setOnClick Listener.....
        viewHolder.setOnClickListener(new ClickInterface() {
            @Override
            public void onItemClick(View view, int position) {
                //show data in toast....

                String id= modelList.get(position).getId();
                String title= modelList.get(position).getTitle();
                String desc= modelList.get(position).getDescription();
                Toast.makeText(displayActivity, ""+id+"\n"+title+"\n"+desc, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongItemClick(View view, final int position) {

                //creating Alert Dialogue
                AlertDialog.Builder builder= new AlertDialog.Builder(displayActivity);
                String[] option= {"Update","Delete"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){

                            //update
                            String id= modelList.get(position).getId();
                            String title= modelList.get(position).getTitle();
                            String desc= modelList.get(position).getDescription();

                            Intent intent= new Intent(displayActivity,MainActivity.class);
                            intent.putExtra("uId",id);
                            intent.putExtra("uTitle",title);
                            intent.putExtra("uDescription",desc);
                            displayActivity.startActivity(intent);
                        }
                        if(which==1){

                            displayActivity.deleteData(position);
                        }
                    }
                }).create().show();

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        holder.title.setText(modelList.get(position).getTitle());
        holder.description.setText(modelList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
