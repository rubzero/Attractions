package com.example.rubze.attraction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

import java.util.Collections;
import java.util.Comparator;

public class QuizResultsActivity extends AppCompatActivity {

    private AttractionList attractionList, attractionListCopy;
    private ImageButton btnMaximize;
    private RecyclerView quizRecyclerView;
    private QuizRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        fragmentDimentions(0.75,0.42);
        inflateList();
        maximizeOnClickListener();
    }

    private void recyclerViewDeclare(){
        quizRecyclerView = findViewById(R.id.rv_quizresults);
        quizRecyclerView.setHasFixedSize(true);

        adapter = new QuizRecyclerViewAdapter(attractionList.getAttractionList());

        recyclerViewOnItemClick(adapter);
        quizRecyclerView.setAdapter(adapter);

        quizRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
    }

    private void fragmentDimentions(double a, double b) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*a),(int)(height*b));
    }

    private void inflateList(){
        attractionListCopy = new AttractionList();

        if(this.getIntent().getExtras()!=null) {
            Bundle bundle = this.getIntent().getExtras();
            attractionList = (AttractionList) bundle.getSerializable("results");
        }
        Collections.sort(attractionList.getAttractionList(),new ComparatorPunctuation());
        recyclerViewDeclare();
    }

    private void recyclerViewOnItemClick(final QuizRecyclerViewAdapter adapter){
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int attractionPosition = findPositionByImageId(
                        adapter.getItem(quizRecyclerView.getChildAdapterPosition(v)).getImage());

                Intent intent = new Intent(QuizResultsActivity.this, AttractionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",attractionPosition);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void maximizeOnClickListener(){
        btnMaximize = findViewById(R.id.btn_quizresults_maximize);
        btnMaximize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapter.getItemCount()!=3) {
                    adapter.setItemShown(3);
                    quizRecyclerView.setAdapter(adapter);
                    fragmentDimentions(.75, .42);
                }
                else {
                    adapter.setItemShown(attractionListCopy.getAttractionList().size());
                    quizRecyclerView.setAdapter(adapter);
                    fragmentDimentions(.75, .9);
                }
            }
        });
    }

    private int findPositionByImageId(int id){
        int position=-1;
        for(int i=0; i<attractionListCopy.getAttractionList().size(); i++){
            if(attractionListCopy.getAttractionList().get(i).getImage()==id)
                position=i;
        }
        return position;
    }

    private class ComparatorPunctuation implements Comparator<Attraction> {
        @Override
        public int compare(Attraction a1, Attraction a2) {
            return (a2.getPunctuation()-a1.getPunctuation());
        }
    }
}
