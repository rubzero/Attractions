package com.example.rubze.attraction;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {
    private Toast toast;
    private String message;
    private int xOffset, yOffset;
    private LayoutInflater layoutInflater;
    private ViewGroup viewGroup;
    private Context context;

    public CustomToast(String message, int xOffset, int yOffset, LayoutInflater layoutInflater,
                       ViewGroup viewGroup, Context context){
        this.message=message;
        this.xOffset=xOffset;
        this.yOffset=yOffset;
        this.layoutInflater=layoutInflater;
        this.viewGroup=viewGroup;
        this.context=context;
    }

    public void displayToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layoutToast = inflater.inflate(R.layout.toast_custom_layout,
                getViewGroup());
        ((TextView)layoutToast.findViewById(R.id.lbl_toast)).setText(message);
        toast = new Toast(getContext());
        toast.setView(layoutToast);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, getxOffset(),getyOffset());
        toast.show();
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getxOffset() {
        return xOffset;
    }
    public int getyOffset() {
        return yOffset;
    }
    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }
    public ViewGroup getViewGroup() {
        return viewGroup;
    }
    public Context getContext() {
        return context;
    }
}
