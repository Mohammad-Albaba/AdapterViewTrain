package com.example.adapterviewtrain.interfaces;

import android.view.View;

public interface OnItemListClickListener{
    void onImageClick(View view, int position);
    void onTextClick(View view, int position,String text);
}