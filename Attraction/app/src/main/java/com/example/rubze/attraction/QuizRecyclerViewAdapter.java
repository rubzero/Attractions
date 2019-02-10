package com.example.rubze.attraction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizRecyclerViewAdapter
        extends RecyclerView.Adapter<QuizRecyclerViewAdapter.MyViewHolder>
        implements View.OnClickListener{

    private ArrayList<Attraction> attractions;
    private int itemShown;
    private View.OnClickListener onClickListener;

    public QuizRecyclerViewAdapter(ArrayList<Attraction> attractions){
        this.attractions = attractions;
        itemShown = 3;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.quiz_result_item, parent, false);
        itemView.setOnClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    public Attraction getItem(int position) {
        return attractions.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Attraction attraction = attractions.get(position);
        holder.bindItem(attraction);
    }
    @Override
    public int getItemCount() {
        return itemShown;
    }

    public void setItemShown(int n){
        itemShown=n;
    }

    @Override
    public void onClick(View v) {
        if(onClickListener != null)
            onClickListener.onClick(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView lblName, lblMunicipality, lblPercentage;
        private RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            lblName = itemView.findViewById(R.id.lbl_attraction_name);
            lblMunicipality = itemView.findViewById(R.id.lbl_attraction_municipality);
            lblPercentage = itemView.findViewById(R.id.lblPercentage);
            relativeLayout = itemView.findViewById(R.id.mRLayoutResults);
        }

        public void bindItem(Attraction attraction){
            lblName.setText(attraction.getName());
            lblMunicipality.setText(attraction.getMunicipality());
            lblPercentage.setText(attraction.getPercentage());
            relativeLayout.setBackgroundResource(attraction.getImageMini());
        }
    }
}
