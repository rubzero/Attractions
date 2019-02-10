package com.example.rubze.attraction;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class UserFormActivity extends AppCompatActivity {

    private TextInputLayout txtUser;
    private Button btnAccept;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();

    private CollectionReference getUserData;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        windowDimentions();
        variableDeclare();
        firestore.setFirestoreSettings(settings);
        getUserData = firestore.collection("users");
    }

    private void windowDimentions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.7),(int)(height*.3));
    }

    private void variableDeclare(){
        txtUser = findViewById(R.id.txt_userform_user);
        txtUser.setHint(getResources().getString(R.string.newUserName));
        btnAccept = findViewById(R.id.btn_userform_accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserDataFirestore();

            }
        });
    }

    private void setUserDataFirestore(){
        getUserData.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty())
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        user = snapshot.toObject(User.class);
                        String docId = snapshot.getId();
                        if(isCurrentUser(user.getId()))
                            setUserName(docId);
                    }
            }
        });
    }

    private void setUserName(String docId){
        final String userNamePattern = "^[a-z0-9_-]{3,15}$";
        if(txtUser.getEditText().getText().toString().trim().equals(""))
            txtUser.setError(getString(R.string.emptyField));
        else if(!txtUser.getEditText().getText()
                .toString().matches(userNamePattern))
            txtUser.setError(getString(R.string.userNameInvalid));
        else{
            getUserData.document(docId).update("name",
                    txtUser.getEditText().getText().toString().trim());
            finish();
        }
    }

    private boolean isCurrentUser(String uid){
        if(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()
                .equals(uid))
            return true;
        return false;
    }
}
