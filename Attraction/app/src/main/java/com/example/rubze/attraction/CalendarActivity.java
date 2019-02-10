package com.example.rubze.attraction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private Button btnValidate;
    private CalendarView calendarView;
    private Attraction attraction;
    private Bundle bundle;
    private int day, nMonth;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        fragmentDimentions(1,0.7);
        variableDeclare();
    }

    private void fragmentDimentions(double a, double b) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*a),(int)(height*b));
    }

    private void variableDeclare(){
        btnValidate = findViewById(R.id.btn_calendar);
        calendarView = findViewById(R.id.cv_calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                day = dayOfMonth;
                nMonth = month;
            }
        });
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDate();
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        attraction = (Attraction)bundle.getSerializable("place");
    }

    private void sendDate(){
        bundle = new Bundle();
        notChangedDate();
        if(rightDate(day, nMonth)) {
            bundle.putInt("day", day);
            bundle.putInt("month", nMonth-1);
            bundle.putSerializable("place", attraction);
            Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        else createCustomToast();

    }

    private void createCustomToast(){
        CustomToast toast = new CustomToast(getString(R.string.wrongDate), 0,300,
                getLayoutInflater(), (ViewGroup)findViewById(R.id.ll_toast), getBaseContext());
        toast.displayToast();
    }

    private void notChangedDate(){
        String Str;
        if(day==0 || nMonth==0) {
            dateFormat = new SimpleDateFormat("dd");
            Str = dateFormat.format(calendarView.getDate());
            day=Integer.parseInt(Str);

            dateFormat = new SimpleDateFormat("MM");
            Str = dateFormat.format(calendarView.getDate());
            nMonth=Integer.parseInt(Str);
        }
    }

    private boolean rightDate(int d, int m){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        int dayInt = Integer.parseInt(day.format(todayDate));
        int monthInt = Integer.parseInt(month.format(todayDate));
        if(m<monthInt-1)
            return false;
        if(m==monthInt-1 && d<dayInt)
            return false;
        return true;
    }
}
