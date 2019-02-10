package com.example.rubze.attraction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout txtUser, txtPassword;
    private String myString;
    private TextView passCount, lblRegister;
    private CheckBox chboxPassword;
    private Button btnLogin;
    private Spinner spinner;
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.tb_attraction);
        setSupportActionBar(toolbar);

        firabaseAuthStateListener();
        spinnerUsers();
        editTextDeclare();
        myListeners();
    }

    private void firabaseAuthStateListener(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private void loginWithEmail(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            rightParametersAnimation();
                        }
                        else
                            txtPassword.setError(getResources().getString(R.string.wrongCredentials));
                    }
                });
    }

    private void editTextDeclare()
    {
        txtUser = findViewById(R.id.txt_attraction_user);
        myString = getResources().getString(R.string.user);
        txtUser.setHint(myString);
        txtUser.getEditText().setOnClickListener(onTextClickListener);

        txtPassword = findViewById(R.id.txt_attraction_password);
        myString = getResources().getString(R.string.password);
        txtPassword.getEditText().setTransformationMethod
                (PasswordTransformationMethod.getInstance());
        txtPassword.setHint(myString);
        txtPassword.getEditText().setOnClickListener(onTextClickListener);
    }

    private void myListeners()
    {
        showPasswordOnChecked();
        validateButtonOnClick();
        countCharactersOnTextChanged();
        registerOnClick();
    }

    private void countCharactersOnTextChanged()
    {
        passCount = findViewById(R.id.lbl_login_count);
        txtPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String leng = (s.length()==0) ? "" : s.length()+"";
                passCount.setText(leng);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                disableErrors();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        txtUser.getEditText().addTextChangedListener(new myTextWatcher());
    }

    private void validateButtonOnClick()
    {
        btnLogin = findViewById(R.id.btn_login_validate);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long guest = spinner.getItemIdAtPosition(0);
                if(spinner.getSelectedItemId()==guest)
                    rightParametersAnimation();
                else errorController();
            }
        });
    }

    private void showPasswordOnChecked()
    {
        chboxPassword = findViewById(R.id.cb_login);
        chboxPassword.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txtPassword.getEditText().setTransformationMethod
                        (PasswordTransformationMethod.getInstance());
                if(isChecked)
                    txtPassword.getEditText().setTransformationMethod
                            (HideReturnsTransformationMethod.getInstance());
                txtPassword.getEditText().setSelection(txtPassword.getEditText().length());
            }
        });
    }

    private void registerOnClick()
    {
        lblRegister = findViewById(R.id.lbl_login_register);
        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void spinnerUsers(){
        spinner = findViewById(R.id.spinner_login);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_user,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean enabled = (position==0) ? false : true;
                setEnableEditText(txtUser.getEditText(), enabled);
                setEnableEditText(txtPassword.getEditText(), enabled);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setEnableEditText(EditText editText, boolean b) {
        if(!b) editText.setInputType(InputType.TYPE_NULL);
        else editText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void disableErrors()
    {
        txtUser.setError(null);
        txtUser.setErrorEnabled(false);
        txtPassword.setError(null);
        txtPassword.setErrorEnabled(false);
    }

    private void errorController()
    {
        disableErrors();
        String password = txtPassword.getEditText().getText().toString();
        String user = txtUser.getEditText().getText().toString().trim();
        if(user.equals("") || password.equals("")) {
            txtPassword.setError(getResources().getString(R.string.emptyField));
            return;
        }
        loginWithEmail(user, password);
    }

    private void rightParametersAnimation()
    {
        ProgressBar progressBar = findViewById(R.id.progress_login);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(
                LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class myTextWatcher implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            disableErrors();
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {}
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(authStateListener!=null)
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    private void createCustomToast(){
        CustomToast toast = new CustomToast(getString(R.string.guestToast), 0,300,
                getLayoutInflater(), (ViewGroup)findViewById(R.id.ll_toast), getBaseContext());
        toast.displayToast();
    }

    AdapterView.OnClickListener onTextClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(spinner.getSelectedItemPosition()==0) createCustomToast();
        }
    };
}
