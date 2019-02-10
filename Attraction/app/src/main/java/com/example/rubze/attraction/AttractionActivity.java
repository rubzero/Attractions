package com.example.rubze.attraction;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AttractionActivity extends AppCompatActivity {

    private AttractionList attractionList;
    private Attraction attractionTracked;
    private TextView lblName, lblMunicipality, lblZone, lblArea;
    private ImageButton btnNext, btnBack, btnTrack;
    private int position;
    private String municipalityString, zoneString, areaString;
    private ImageView imageView;
    private GridLayout imageGridLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);
        Toolbar toolbar = findViewById(R.id.tb_attraction);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
            position = (bundle!=null) ?
                    bundle.getInt("position") : 0;

        attractionDeclare();
        setViewText(position);
        btnNextOnClickListener();
        setIconEspecsOnClick();
    }

    private void createWebView(String url){
        webView = findViewById(R.id.wv_webLink);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    private void setViewText(int position)
    {
        Attraction attraction = attractionList.getAttractionList().get(position);
        setImageView(attraction);
        String[] content = {attraction.getName(), attraction.getMunicipality(),
                attraction.getZone(),attraction.getArea(), attraction.getInfo()};
        lblName.setText(content[0]);
        lblMunicipality.setText(municipalityString+" "+content[1]);
        lblZone.setText(zoneString+" "+content[2]);
        lblArea.setText(areaString+" "+content[3]);
        createWebView(content[4]);
    }

    private void setImageView(Attraction attraction){
        removeImageView();
        ArrayList<Integer> arrayList = attraction.getDrawableIconArray();
        for(int i=0; i<arrayList.size();i++){
            ImageView imageView = new ImageView(imageGridLayout.getContext());
            imageView.setImageResource(arrayList.get(i));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imageGridLayout.addView(imageView);
        }
    }

    private void removeImageView(){
        imageGridLayout.removeAllViews();
    }

    private int getScreenWidth()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (size.x-60)/2;
    }

    private void attractionDeclare()
    {
        attractionList = new AttractionList();
        imageGridLayout = findViewById(R.id.gl_attraction);

        imageView = findViewById(R.id.iv_UserAvatar);
        lblName = findViewById(R.id.lbl_attraction_name);

        lblMunicipality = findViewById(R.id.lbl_attraction_municipality);
        lblMunicipality.setWidth(getScreenWidth());
        lblZone = findViewById(R.id.lbl_attraction_zone);
        lblArea = findViewById(R.id.lbl_attraction_area);

        municipalityString = getResources().getString(R.string.municipality);
        zoneString = getResources().getString(R.string.zone);
        areaString = getResources().getString(R.string.area);

        imageView.getLayoutParams().width = getScreenWidth();
        lblName.setTypeface(null, Typeface.BOLD_ITALIC);
    }

    private void btnNextOnClickListener() {
        imageView.setImageResource(attractionList.getAttractionList().
                get(position).getImage());

        btnNext = findViewById(R.id.btn_attraction_forward);
        btnBack = findViewById(R.id.btn_attraction_back);
        btnTrack = findViewById(R.id.btn_attraction_calendar);

        btnNext.setOnClickListener(new MyOnClickListener());
        btnBack.setOnClickListener(new MyOnClickListener());
        btnTrack.setOnClickListener(new MyOnClickListener());
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_attraction_forward:
                    position = (position == attractionList.getAttractionList().size() - 1) ?
                            0 : position + 1;
                    createNextActivity();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.btn_attraction_back:
                    position = (position == 0) ?
                            attractionList.getAttractionList().size() - 1 : position - 1;
                    createNextActivity();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case R.id.btn_attraction_calendar:
                    setAttractionTracked(attractionList.getAttractionList().get(position).getImage());
                    sendAttractionTracked();
                    break;
            }
        }
    }

    private void createNextActivity(){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        intent.putExtras(bundle);
        finish();
        startActivity(getIntent());
    }

    private void setAttractionTracked(int imageId){
        attractionTracked = attractionList.getAttractionByImage(imageId);
    }

    private void sendAttractionTracked(){
        Intent intent = new Intent(AttractionActivity.this, CalendarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("place",attractionTracked);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setIconEspecsOnClick(){
        imageGridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createToastContent();
            }
        });
    }

    private void createToastContent(){
        String legend = "";
        Attraction attraction =
            attractionList.getAttractionList().get(position);
        for(int i=0; i<attraction.getLegendStringId().size();i++) {
            legend = (legend.equals("")) ?
                    "- "+getResources().getString(attraction.getLegendStringId().get(i))
                    : legend+"\n- "+getResources().getString(attraction.getLegendStringId().get(i));
        }

        createCustomToast(legend);
    }

    private void createCustomToast(String legend){
        CustomToast toast = new CustomToast(legend, 0,0,
                getLayoutInflater(), (ViewGroup)findViewById(R.id.ll_toast), getBaseContext());
        toast.displayToast();
    }
}
