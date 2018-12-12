package org.izv.aad.proyecto.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.Dialogs.DialogCustom;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;

import java.util.List;

public class Login extends AppCompatActivity {
    private TextView tvLogup;
    private ImageView loginlogo;
    private TextInputEditText loginmailET;
    private TextInputLayout createmailLayout;
    private ImageView createicoMail;
    private TextInputEditText loginpasswordET;
    private TextInputLayout createpasswordLayout;
    private ImageView createicoPasswordIV;
    private Button loginbtLogin;
    private TextView textView5;
    private TextView textViewError;
    private InterfaceFireBase interfaceFireBase;
    private ProgressDialog progressDialog;
    public static final String SAVE_SHARED_PREFERENCES = "user.xml";
    public static final String EMAIL = "email.xml";
    private CheckBox checkRemeber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initLogUp();
        setClickLogin();
        checkSharedPreferences();
    }

    private void init(){
        tvLogup = findViewById(R.id.tvLogup);
        textView5 = findViewById(R.id.textView5);
        loginbtLogin = findViewById(R.id.book_btCreateBook);
        createicoPasswordIV = findViewById(R.id.create_icoPassword);
        createpasswordLayout = findViewById(R.id.create_passwordLayout);
        loginpasswordET = findViewById(R.id.login_password);
        createicoMail = findViewById(R.id.create_icoMail);
        createmailLayout = findViewById(R.id.create_mailLayout);
        loginmailET = findViewById(R.id.login_mail);
        loginlogo = findViewById(R.id.login_logo);
        textViewError=findViewById(R.id.textView);
        checkRemeber = findViewById(R.id.create_checkRemember);
        progressDialog = DialogCustom.showDialog(this);
        interfaceFireBase = managerCallBack();
    }

    public void botonLoginClick() {
        if(loginmailET.getText().toString().equals("")){
            createmailLayout.setError(getString(R.string.msg_no_text_in_email));
            createmailLayout.setErrorEnabled(true);
            textViewError.setVisibility(View.GONE);
        }
        if(loginpasswordET.getText().toString().equals("")){
            createpasswordLayout.setError(getString(R.string.msg_no_text_in_contraseña));
            createpasswordLayout.setErrorEnabled(true);
            textViewError.setVisibility(View.GONE);
        }

        if(!loginpasswordET.getText().toString().equals("") && !loginmailET.getText().toString().equals("")) {
            createmailLayout.setErrorEnabled(false);
            createpasswordLayout.setErrorEnabled(false);
            progressDialog.show();
            saveSharedPreferencesEmail(loginmailET.getText().toString());
            FirebaseCustom.login(this, loginmailET.getText().toString(), loginpasswordET.getText().toString(), interfaceFireBase);

        }
    }

    private void initLogUp(){
        tvLogup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Logup.class);
                startActivity(intent);
            }
        });
    }

    private void setClickLogin(){
        loginbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonLoginClick();
            }
        });
    }

    private InterfaceFireBase managerCallBack(){
        return new InterfaceFireBase() {
            @Override
            public void isCorrectlyLogUp(boolean isSuccessful, String error) {

            }

            @Override
            public Book getBook(Book book) {
                return null;
            }

            @Override
            public Author getAuthor(Author author) {
                return null;
            }

            @Override
            public void getUserLogin(FirebaseUser user, String error) {
                progressDialog.dismiss();
                if(error.equals(getString(R.string.error_login_email))){
                    createmailLayout.setError(getString(R.string.msg_login_email));
                    createmailLayout.setErrorEnabled(true);
                    textViewError.setVisibility(View.GONE);
                }else if(error.equals(getString(R.string.error_login_clave))){
                    createpasswordLayout.setError(getString(R.string.msg_login_clave));
                    createpasswordLayout.setErrorEnabled(true);
                    textViewError.setVisibility(View.GONE);
                }else if(error.equals(getString(R.string.error_login_no_email))) {
                    createmailLayout.setError(getString(R.string.msg_login_no_email));
                    createmailLayout.setErrorEnabled(true);
                    textViewError.setVisibility(View.GONE);
                }else{
                    if (checkRemeber.isChecked()) {
                        String email = loginmailET.getText().toString();
                        String pass = loginpasswordET.getText().toString();
                        saveSharedPreferences(email, pass);
                    }
                    Intent i = new Intent(Login.this, Index.class);
                    startActivity(i);
                }
            }

            @Override
            public List<Book> getAllBooks(List<Book> books) {
                return null;
            }

            @Override
            public List<Author> getAllAuthors(List<Author> authors) {
                return null;
            }

            @Override
            public String sendRoutePhoto(String string) {
                return null;
            }

            @Override
            public String getRoutePhoto(String string) {
                return null;
            }
        };
    }

    private void saveSharedPreferences(String email, String pass){
        SharedPreferences prefs =  getSharedPreferences(SAVE_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.commit();
    }

    private void saveSharedPreferencesEmail(String email){
        SharedPreferences prefs =  getSharedPreferences(EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.commit();
    }

    private void checkSharedPreferences(){
        SharedPreferences pref = getSharedPreferences(SAVE_SHARED_PREFERENCES, MODE_PRIVATE);
        String email = pref.getString("email", null);
        String pass = pref.getString("pass", null);
        if(email != null && pass != null){
            progressDialog.show();
            saveSharedPreferencesEmail(email);
            FirebaseCustom.login(this, email, pass, interfaceFireBase);

        }
    }


}
