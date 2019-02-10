package com.example.rubze.attraction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout txtUserEmail, txtUserName, txtPass, txtPass2;
    private String myString;
    private Button btnValidate;
    private CheckBox cbPassword;
    private LottieAnimationView tickAnimation;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
    private DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.tb_register);
        setSupportActionBar(toolbar);
        tickAnimation = findViewById(R.id.lottie_register);
        editTextDeclare();
        myListeners();
    }

    private void registerWithEmail(String email, String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthWeakPasswordException e){
                                txtPass.setError(getString(R.string.weakPasswordError));
                                txtPass.requestFocus();
                            }
                            catch(FirebaseAuthInvalidCredentialsException e){
                                txtUserEmail.setError(getString(R.string.wrongEmail));
                                txtUserEmail.requestFocus();
                            }
                            catch(FirebaseAuthUserCollisionException e) {
                                txtUserEmail.setError(getString(R.string.duplicatedEmail));
                            }
                            catch(Exception e){};
                        }
                        else{
                            createUserDatabase();
                            startLoginActivity();
                        }
                    }
                });
    }

    private void startLoginActivity(){
        btnValidate.setVisibility(View.GONE);
        tickAnimation.playAnimation();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(
                        RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);

    }

    private void editTextDeclare()
    {
        txtUserName = findViewById(R.id.txt_register_userName);
        myString = getResources().getString(R.string.userName);
        txtUserName.setHint(myString);

        txtUserEmail = findViewById(R.id.txt_register_userEmail);
        myString = getResources().getString(R.string.email);
        txtUserEmail.setHint(myString);

        txtPass = findViewById(R.id.txt_register_password);
        myString = getResources().getString(R.string.password);
        txtPass.getEditText().setTransformationMethod
                (PasswordTransformationMethod.getInstance());
        txtPass.setHint(myString);

        txtPass2 = findViewById(R.id.txt_register_password2);
        myString = getResources().getString(R.string.password2);
        txtPass2.getEditText().setTransformationMethod
                (PasswordTransformationMethod.getInstance());
        txtPass2.setHint(myString);
    }

    private void myListeners()
    {
        checkBoxListener();
        btnValidateClickListener();
        disableErrorsOnTextChanged();
    }

    private void checkBoxListener()
    {
        cbPassword = findViewById(R.id.cb_register_showpassword);
        cbPassword.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                txtPass.getEditText().setTransformationMethod
                        (PasswordTransformationMethod.getInstance());
                txtPass2.getEditText().setTransformationMethod
                        (PasswordTransformationMethod.getInstance());
                if(isChecked) {
                    txtPass.getEditText().setTransformationMethod
                            (HideReturnsTransformationMethod.getInstance());
                    txtPass2.getEditText().setTransformationMethod
                            (HideReturnsTransformationMethod.getInstance());
                }
                txtPass.getEditText().setSelection(txtPass.getEditText().length());
                txtPass2.getEditText().setSelection(txtPass2.getEditText().length());
            }
        });
    }

    private void btnValidateClickListener()
    {
        btnValidate = findViewById(R.id.btn_register_validate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorController();
            }
        });
    }

    private void createUserDatabase(){
        firestore.setFirestoreSettings(settings);
        reference = firestore.collection("users").document();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String name = txtUserName.getEditText().getText().toString().trim();
            String email= txtUserEmail.getEditText().getText().toString().trim();

            User user = new User(id, name, email);
            reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("ADD", "USUARIO GUARDADO");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("USUARIO NO GUARDADO", e.getMessage());
                }
            });
        }
    }

    private void disableErrorsOnTextChanged()
    {
        txtUserEmail.getEditText().addTextChangedListener(new myTextWatcher());
        txtUserName.getEditText().addTextChangedListener(new myTextWatcher());
        txtPass.getEditText().addTextChangedListener(new myTextWatcher());
        txtPass2.getEditText().addTextChangedListener(new myTextWatcher());
    }

    class myTextWatcher implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            disableErrors();
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
        @Override
        public void afterTextChanged(Editable s) {}
    }

    private void errorController()
    {
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{8,}$";
        String userNamePattern = "^[a-z0-9_-]{3,15}$";

        String pass1 = txtPass.getEditText().getText().toString();
        String pass2 = txtPass2.getEditText().getText().toString();
        String user = txtUserName.getEditText().getText().toString().trim();
        String email = txtUserEmail.getEditText().getText().toString().trim();

        disableErrors();

        if(pass1.equals("") || pass2.equals("") || user.equals("") || email.equals(""))
            txtPass2.setError(getString(R.string.emptyField));
        else if(!user.matches(userNamePattern)) {
            txtUserName.setError(getString(R.string.userNamePattern));
        }
        else if(!pass1.equals(pass2)) {
            txtPass2.setError(getString(R.string.notCollisionPassword));
        }
        else if(!pass1.matches(passwordPattern)){
            txtPass2.setError(getString(R.string.weakPasswordError));
        }
        else registerWithEmail(email,pass1);
    }

    private void disableErrors()
    {
        txtPass.setError(null);
        txtPass.setErrorEnabled(false);
        txtPass2.setError(null);
        txtPass2.setErrorEnabled(false);
        txtUserName.setError(null);
        txtUserName.setErrorEnabled(false);
        txtUserEmail.setError(null);
        txtUserEmail.setErrorEnabled(false);
    }
}