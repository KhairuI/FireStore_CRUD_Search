package com.example.firestorebasic;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ModelViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView title;
    public AppCompatTextView description;
    View view;
    private ClickInterface clickInterface;


    public ModelViewHolder(@NonNull View itemView) {
        super(itemView);

        view= itemView;
        title= itemView.findViewById(R.id.singleTitleId);
        description= itemView.findViewById(R.id.singleDescriptionId);

        //itemClick
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemClick(v,getAdapterPosition());
            }
        });

        //onLong Click

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickInterface.onLongItemClick(v,getAdapterPosition());
                return true;
            }
        });
    }

    public void setOnClickListener(ClickInterface clickListener){

        clickInterface= clickListener;
    }
}
