package org.izv.aad.proyecto.Activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Fragments.FragmentLogup1;
import org.izv.aad.proyecto.Fragments.FragmentLogup2;
import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;

import java.util.List;

public class Logup extends AppCompatActivity {

    private TextView create_mail,create_password, create_repitePassword,msg_error;
    private TextInputLayout create_mailLayout, create_passwordLayout, create_repitePasswordLayout;
    private FragmentLogup1 fragmentLogup1;
    private FragmentLogup2 fragmentLogup2;
    private Button create_next,create_logup;
    private InterfaceFireBase interfaceFireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logup);
        init();


        // Actions from buttons
        changeToFragment2();
        register();
    }



    private void init(){
        create_mailLayout = findViewById(R.id.create_mailLayout);
        create_passwordLayout = findViewById(R.id.create_passwordLayout);
        create_repitePasswordLayout = findViewById(R.id.create_repitePasswordLayout);
        msg_error = findViewById(R.id.msg_error);
        create_mail = create_mailLayout.getEditText();
        create_password = create_passwordLayout.getEditText();
        create_repitePassword = create_repitePasswordLayout.getEditText();

        create_next = findViewById(R.id.create_next);
        create_logup = findViewById(R.id.create_logup);

        interfaceFireBase = new InterfaceFireBase() {
            @Override
            public void isCorrectlyLogUp(boolean isSuccessful, String error) {
                if(isSuccessful){
                    endIntent();
                }else{
                    if(error.equals(getString(R.string.error_email_repeat))){
                        msg_error.setText(getString(R.string.msg_email_repeat));
                        msg_error.setVisibility(View.VISIBLE);
                        changeToFragment1();
                    }
                }
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

        fragmentLogup1 = (FragmentLogup1)getSupportFragmentManager().findFragmentById(R.id.fragmento1);
        fragmentLogup2 = (FragmentLogup2)getSupportFragmentManager().findFragmentById(R.id.fragment2);

        fragmentLogup2.getView().setVisibility(View.GONE);
    }

    private void changeToFragment2() {
        create_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_mailLayout.setError(null);
                if(!create_mail.getText().toString().equals("")){
                    fragmentLogup1.getView().setVisibility(View.GONE);
                    fragmentLogup2.getView().setVisibility(View.VISIBLE);
                }else{
                    create_mailLayout.setError(getString(R.string.not_empty));
                }
            }
        });
    }

    private void changeToFragment1(){
        fragmentLogup1.getView().setVisibility(View.VISIBLE);
        fragmentLogup2.getView().setVisibility(View.GONE);
    }

    private void register(){
        create_logup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_passwordLayout.setError(null);
                create_repitePasswordLayout.setError(null);
                if(create_password.getText().toString().equals("") || create_repitePassword.getText().toString().equals("")) {
                    if (create_password.getText().toString().equals("")) {
                        create_passwordLayout.setError(getString(R.string.not_empty));
                    }
                    if (create_repitePassword.getText().toString().equals("")) {
                       create_repitePasswordLayout.setError(getString(R.string.not_empty));
                    }
                }else if(!create_repitePassword.getText().toString().equals(create_password.getText().toString())){
                    create_repitePasswordLayout.setError(getString(R.string.not_equals));
                }else{
                    FirebaseCustom.logup(Logup.this, create_mail.getText().toString(), create_password.getText().toString(), interfaceFireBase);
                }
            }
        });
    }

    private void endIntent(){
        finish();
    }




}
