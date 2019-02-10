package com.example.rubze.attraction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TextView lblAll, lblQuiz, lblNextVisitName, lblDate, lblComment1,
            lblComment2, lblComment3;
    private EditText txtComment;
    private ImageView ivNextVisit;
    private ImageButton btnMaximize, btnAddComment, btnRemoveComment;
    private RelativeLayout relativeLayout;
    private RelativeLayout rlSlideDown;
    private Intent intent;
    private Toolbar toolbar;
    private Attraction attraction;
    private Bundle bundle;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private CollectionReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        drawerLayout();

        variableDeclare();
        slideDownLayoutDeclare();
        addCommentListener();
        removeCommentListener();
        setNextVisit();
        menuOnClickListener();
    }

    private void drawerLayout(){
        DrawerLayout drawer = findViewById(R.id.dl_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        createDrawerUserHolders();
    }

    private void createDrawerUserHolders(){
        NavigationView navigationView = findViewById(R.id.nv_main);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView userName = headerView.findViewById(R.id.lblUserName);
        TextView userEmail = headerView.findViewById(R.id.lblUserEmail);
        ImageView userImage = headerView.findViewById(R.id.iv_UserAvatar);

        setUserData(userName, userEmail, userImage);
    }

    private void setUserData(TextView name, TextView email, ImageView image){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            getUserDataFirestore(name,image);
        }
        else {
            email.setText(getString(R.string.guestEmail));
            name.setText(getString(R.string.guest));
            image.setImageResource(R.mipmap.ic_avatar_round);
        }
    }

    private void getUserDataFirestore(final TextView name, final ImageView image){
        firestore.setFirestoreSettings(settings);
        reference = firestore.collection("users");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty())
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);
                        if(isCurrentUser(user.getId())){
                            name.setText(user.getName());
                            if(user.getPhoto()!=0)
                                image.setImageResource(user.getPhoto());
                            else image.setImageResource(R.mipmap.ic_avatar_round);
                        }
                    }
            }
        });
    }

    private boolean isCurrentUser(String uid){
        if(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()
                .equals(uid))
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.dl_main);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.nav_exit : {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_profile: {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    Intent intent = new Intent(MainActivity.this, UserFormActivity.class);
                    startActivity(intent);
                } else guestUserToast();
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.dl_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void guestUserToast(){
        CustomToast toast = new CustomToast(getString(R.string.guestToastProfile), 0,0,
                getLayoutInflater(), (ViewGroup)findViewById(R.id.ll_toast), getBaseContext());
        toast.displayToast();
    }

    private void setNextVisit(){
        bundle = this.getIntent().getExtras();
        try {
            setNextPlace();
            setNextDate();
            setNextImage();
            transition();
        }
        catch (Exception e){};
    }

    private void setNextPlace(){
        if (bundle.getSerializable("place") != null) {
            attraction = (Attraction) (bundle.getSerializable("place"));
            lblNextVisitName.setText(attraction.getName());
        }
    }

    private void setNextDate(){
        int day = bundle.getInt("day");
        int month = bundle.getInt("month");
        month++;
        lblDate.setText(formatDate(day)
                +"/"+formatDate(month));
    }

    private void setNextImage(){
        ivNextVisit.setImageResource(attraction.getImageMini());
    }

    private String formatDate(int n){
        String date = ""+n;
        if(n<10)
            date="0"+n;
        return date;
    }

    private void variableDeclare(){
        lblNextVisitName = findViewById(R.id.lblNextVisitName);
        lblDate = findViewById(R.id.lblDate);
        lblComment1 = findViewById(R.id.lbl_contentmain_comment1);
        lblComment2 = findViewById(R.id.lbl_contentmain_comment2);
        lblComment3 = findViewById(R.id.lbl_contentmain_comment3);
        txtComment = findViewById(R.id.txt_contentmain_comment);
        btnAddComment = findViewById(R.id.btn_contentmain_addcomment);
        btnRemoveComment = findViewById(R.id.btn_contentmain_removecomment);
        ivNextVisit = findViewById(R.id.iv_contentmain_nextvisit);
    }

    private void addCommentListener(){
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtComment.getText().toString().equals("")) {
                    if (lblComment1.getText().toString().equals(""))
                        lblComment1.setText("- "+txtComment.getText().toString());
                    else if (lblComment2.getText().toString().equals(""))
                        lblComment2.setText("- "+txtComment.getText().toString());
                    else if (lblComment3.getText().toString().equals(""))
                        lblComment3.setText("- "+txtComment.getText().toString());
                }
                txtComment.getText().clear();
            }
        });
    }

    private void removeCommentListener(){
        btnRemoveComment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!lblComment3.getText().toString().equals(""))
                    lblComment3.setText("");
                else if (!lblComment2.getText().toString().equals(""))
                    lblComment2.setText("");
                else if (!lblComment1.getText().toString().equals(""))
                    lblComment1.setText("");
                txtComment.getText().clear();
            }
        });
    }

    private void slideDownLayoutDeclare() {
        rlSlideDown = findViewById(R.id.rl_contentmain_slideview);
        rlSlideDown.setVisibility(View.VISIBLE);
        rlSlideDown.setAlpha(0.0f);
        slideDownButtonListener();
    }

    private void slideDownButtonListener(){
        btnMaximize = findViewById(R.id.btn_contentmain_maximize);
        btnMaximize.setOnClickListener(new View.OnClickListener(){
            boolean displayed = false;
            @Override
            public void onClick(View v) {
                if (attraction != null) {
                    displayed = (displayed) ? false : true;
                    if (displayed)
                        layoutSlideDown();
                    else
                        layoutSlideUp();
                }else slideDownButtonToast();
            }
        });
    }

    private void slideDownButtonToast(){
        CustomToast toast = new CustomToast(getString(R.string.addVisitToast), 0,0,
                getLayoutInflater(), (ViewGroup)findViewById(R.id.ll_toast), getBaseContext());
        toast.displayToast();
    }

    private void layoutSlideDown(){
        rlSlideDown.animate().translationY
                (0).alpha(1.0f).setListener(null);
        rlSlideDown.setVisibility(View.VISIBLE);
    }

    private void layoutSlideUp(){
        rlSlideDown.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        rlSlideDown.setVisibility(View.GONE);
                    }
                });

    }

    private void transition(){
        relativeLayout = findViewById(R.id.mLLayoutNextVisit);

        TransitionDrawable transition = (TransitionDrawable)
                relativeLayout.getBackground();
        transition.startTransition(1000);
        transition.reverseTransition(1500);
    }

    private void menuOnClickListener() {
        lblAll = findViewById(R.id.btn_toolbarmain_1);
        lblQuiz = findViewById(R.id.btn_toolbarmain_2);

        lblAll.setWidth(getScreenWidth());
        lblAll.setText(R.string.all);
        lblQuiz.setWidth(getScreenWidth());
        lblQuiz.setText(R.string.quiz);

        lblAll.setOnClickListener(new mTextOnClickListener());
        lblQuiz.setOnClickListener(new mTextOnClickListener());
    }

    private int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x/3;
    }

    class mTextOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            if(textView == lblQuiz)
                intent = new Intent(MainActivity.this,QuizActivity.class);
            if(textView == lblAll) {
                intent = new Intent(MainActivity.this, AttractionActivity.class);
            }
            if(intent != null)
                startActivity(intent);
        }
    }
}



