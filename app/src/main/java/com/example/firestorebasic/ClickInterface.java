package com.example.firestorebasic;

import android.view.View;

//interface for click listener........
public interface ClickInterface {

    void onItemClick(View view, int position);
    void onLongItemClick(View view, int position);
}
