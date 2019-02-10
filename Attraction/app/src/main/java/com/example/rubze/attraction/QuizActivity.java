package com.example.rubze.attraction;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private AttractionList attractionList, attractionListCopy;
    private TextView lblQuestion, lblTitle, lblInstructions;
    private RadioGroup radioGroup;
    private RadioButton radio1, radio2, radio3;
    private LinearLayout quizLLayout, preQuizLLayout;
    private Button btnStart;
    private ImageButton btnPrevious, btnNext;
    private int quizPosition;
    private String[] questions, answers;
    private PunctuationList punctuationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        fragmentDimentions();
        fragmentDeclare();
    }

    private void fragmentDeclare() {
        questions = getResources().getStringArray(R.array.question_array);
        answers = getResources().getStringArray(R.array.answers_array);

        attractionList = new AttractionList();
        punctuationList = new PunctuationList();

        lblQuestion = findViewById(R.id.lbl_quiz_question);
        radioGroup = findViewById(R.id.rg_quiz);
        lblTitle = findViewById(R.id.lbl_quiz_title);
        lblInstructions = findViewById(R.id.lbl_quiz_instructions);
        preQuizLLayout = findViewById(R.id.ll_quiz_pre);
        quizLLayout = findViewById(R.id.ll_quiz);
        radio1 = findViewById(R.id.r_quiz_1);
        radio2 = findViewById(R.id.r_quiz_2);
        radio3 = findViewById(R.id.r_quiz_3);

        previousConfig();
        btnValidateOnClickListener();
    }

    private void previousConfig(){
        quizLLayout.setVisibility(View.GONE);
        lblTitle.setTypeface(null, Typeface.BOLD_ITALIC);
        lblInstructions.setTypeface(null, Typeface.ITALIC);
    }

    private void fragmentDimentions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.7),(int)(height*.4));
    }

    private void btnValidateOnClickListener() {
        quizPosition = 0;
        btnStart = findViewById(R.id.btn_quiz_start);
        btnNext = findViewById(R.id.btn_quiz_next);
        btnPrevious = findViewById(R.id.btn_quiz_previous);
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        btnStart.setOnClickListener(myOnClickListener);
        btnNext.setOnClickListener(myOnClickListener);
        btnPrevious.setOnClickListener(myOnClickListener);
    }

    class MyOnClickListener implements View.OnClickListener {
        int visibility = 0;
        int answersCount = 3;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_quiz_start:
                    attractionListCopy = attractionList;
                    preQuizLLayout.setVisibility(View.GONE);
                    quizLLayout.setVisibility(View.VISIBLE);
                    btnPrevious.setVisibility(View.GONE);
                    break;

                case R.id.btn_quiz_next:
                    if(radioGroup.getCheckedRadioButtonId()!=-1){
                        addQuizPunctuation(radioGroup.getCheckedRadioButtonId(), quizPosition);
                        quizPosition++;
                        if(quizPosition == questions.length) {
                            quizCheck();
                            setPercentages();
                            break;
                        }
                        lblQuestion.setText(questions[quizPosition]);
                        radio1.setText(answers[answersCount]);
                        radio2.setText(answers[answersCount+1]);
                        radio3.setText(answers[answersCount+2]);
                        answersCount+=3;
                        radioGroup.clearCheck();
                        if(getPreviousRadioButton(quizPosition)!=0)
                            radioGroup.check(getPreviousRadioButton(quizPosition));
                    }
                break;

                case R.id.btn_quiz_previous:
                    quizPosition = (quizPosition == 0) ?
                            0 : quizPosition - 1;
                    lblQuestion.setText(questions[quizPosition]);
                    radioGroup.check(getPreviousRadioButton(quizPosition));
                break;
            }
            checkVisibility(visibility);
        }
    }

    private void addQuizPunctuation(int id, int position){

        int punctuation = 0;

        switch (id) {
            case R.id.r_quiz_1: punctuation=1; break;
            case R.id.r_quiz_2: punctuation=2; break;
            case R.id.r_quiz_3: punctuation=3; break;
        }
        try {
            if (punctuationList.getPunctuationList().get(position) != null)
                punctuationList.removePunctuation(position);
        }
        catch(Exception e){}
        punctuationList.addPunctuation(position,punctuation);
        radioGroup.clearCheck();
    }

    private void quizCheck(){

        int punctuation = 0;
        int valor = 0;

        int sizePunctuation = punctuationList.getPunctuationList().size();
        int sizeAttraction = attractionListCopy.getAttractionList().size();

        for(int i=0; i<sizePunctuation;i++) {
            punctuation = punctuationList.getPunctuationList().get(i);
            for (int j = 0; j < sizeAttraction; j++) {
                valor = attractionListCopy.getAttractionList().get(j).getLevels().get(i);
                switch (valor-punctuation){
                    case 0: attractionListCopy.getAttractionList().get(j).setPunctuation(
                            attractionListCopy.getAttractionList().get(j).getPunctuation()+3); break;
                    case 1: attractionListCopy.getAttractionList().get(j).setPunctuation(
                            attractionListCopy.getAttractionList().get(j).getPunctuation()+1); break;
                    case -1: attractionListCopy.getAttractionList().get(j).setPunctuation(
                            attractionListCopy.getAttractionList().get(j).getPunctuation()+1); break;
                }
            }
        }
    }

    private void checkVisibility(int visibility) {
        visibility = (quizPosition == 0) ? View.INVISIBLE : View.VISIBLE;
        btnPrevious.setVisibility(visibility);
    }

    private void setPercentages(){

        for(int i=0; i<attractionListCopy.getAttractionList().size();i++)
            attractionListCopy.getAttractionList().get(i).createPercentage();

        Intent intent = new Intent(QuizActivity.this, QuizResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("results",attractionListCopy);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private int getPreviousRadioButton(int pos){
        int radioId=0;
        try{
            switch (punctuationList.getPunctuationList().get(pos)) {
                case 1: radioId = R.id.r_quiz_2; break;
                case 2: radioId = R.id.r_quiz_2; break;
                case 3: radioId = R.id.r_quiz_3; break;
                default : radioId=0; break;
                }
        }
        catch(Exception e){};
        return radioId;
    }

    private class PunctuationList{
        private ArrayList<Integer> punctuation;
        private PunctuationList(){
            punctuation = new ArrayList<>();
        }
        public ArrayList<Integer> getPunctuationList(){
            return punctuation;
        }
        public void addPunctuation(int pos, int p){
            punctuation.add(pos, p);
        }
        public void removePunctuation(int pos){
            punctuation.remove(pos);
        }
        public void cleanPunctuation(){
            for(int i=0; i<punctuation.size(); i++)
                punctuation.remove(punctuation.get(i));
        }
    }
}