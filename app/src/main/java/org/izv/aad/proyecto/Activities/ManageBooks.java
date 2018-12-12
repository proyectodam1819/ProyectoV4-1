package org.izv.aad.proyecto.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.Dialogs.DialogCustom;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.CreateAuthor;
import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.Objects.DateCustom;
import org.izv.aad.proyecto.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ManageBooks extends AppCompatActivity {
    private Toolbar create_toolbar;
    private Button btAddAutor,btCreateBook;
    private CreateAuthor createAuthor;
    private Manager manager;
    private ProgressDialog progressDialog;
    private TextInputLayout ilFechIni,ilFechFin,ilTitle;
    private EditText etFechIni,etFechFin,etTitle, summary;
    private Spinner sp;
    private RatingBar rtBar;
    private List<Author> authors;
    private ImageView iVPhoto, ivFavorite, ivDeleteBook, ivCalendar;
    private Uri uri;
    private RadioGroup rdGroup;
    private ArrayList<String> authorsString;
    private ArrayAdapter<String> adaptadorSpiner;
    private static final int READ_REQUEST_CODE = 1;
    private final Calendar c = Calendar.getInstance();
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int mes = c.get(Calendar.MONTH);
    private final int anio = c.get(Calendar.YEAR);
    private InterfaceFireBase interfaceFireBase;

    private RadioButton rdFinish;
    private RadioButton rdStarted;

    //Datos del libro
    private DateCustom fechIni;
    private DateCustom fechFin;
    private String title;
    private Boolean favorite = false;
    private String resume;
    private Float rating;
    private Author authorSelected;

    //Recoger libro (editar)
    private Book bookEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();

        //para poner boton atras
        setSupportActionBar(create_toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void init(){
        manager = new Manager(this);

        getAuthors();
        addAdapterSpinner();

        progressDialog = DialogCustom.showDialog(this);
        btAddAutor = findViewById(R.id.btAddAutor);

        createAuthor = methodInterface();

        create_toolbar = findViewById(R.id.create_toolbar);
        ivDeleteBook = findViewById(R.id.book_deleteDate);
        ivFavorite = findViewById(R.id.ivFavorite);
        ilFechFin = findViewById(R.id.book_dateFinishLayout);
        ilFechIni = findViewById(R.id.book_dateStartLayout);
        ilTitle = findViewById(R.id.book_TitleLayout);
        etFechIni = ilFechIni.getEditText();
        etFechFin = ilFechFin.getEditText();
        etTitle = ilTitle.getEditText();
        rdFinish = findViewById(R.id.book_rdFinish);
        rdStarted = findViewById(R.id.book_rdStarted);
        uri = Uri.parse("");

        rtBar = findViewById(R.id.book_ratingBar);
        summary = findViewById(R.id.book_summary);
        btCreateBook=findViewById(R.id.book_btCreateBook);
        iVPhoto=findViewById(R.id.book_logo);
        rdGroup=findViewById(R.id.book_status);

        interfaceFireBase = getMethodInterface();


        ivCalendar = findViewById(R.id.book_icoDate);

        setListenerDialog();
        setListenerFoto();
        setListenerFechIni();
        setListenerFechFin();
        setListenerCreateBook();
        setListenerFavorite();
        setListenerDeleteDate();
        enableDisableDates();

        getBookToEdit();
        if(bookEdit != null) {
            setBookValues();
        }
    }

    /***********************************************
     ******************** DIALOG *******************
     *************************************************/

    private void showDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_author, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();

        Button negative = dialogView.findViewById(R.id.negative);
        Button positive = dialogView.findViewById(R.id.positive);
        final TextInputLayout tilCreateAuthor = dialogView.findViewById(R.id.tilCreateAuthor);
        alertDialog.show();

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edAuthor = tilCreateAuthor.getEditText();
                if(edAuthor.getText().toString().isEmpty()){
                    tilCreateAuthor.setError(getString(R.string.not_empty));
                }else {
                    progressDialog.show();
                    Author author = new Author(edAuthor.getText().toString());
                    createAuthor.create(author);
                    alertDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null) {
                uri = data.getData();
                Picasso.with(this).load(uri).into(iVPhoto);
            }
        }
    }

    /***********************************************
    ******************** CHEKERS *******************
    *************************************************/

    private  boolean checkNotEmptyText(TextInputLayout element){
        boolean correct=true;
        element.setError(null);
        if(element. getEditText().getText().toString().isEmpty()){
            element.setError(getString(R.string.not_empty));
            correct=false;
        }
        return correct;
    }

    private boolean checkRadios(){
        boolean correct=false;
        switch (rdGroup.getCheckedRadioButtonId()){
            case R.id.book_rdStarted:
                if(checkNotEmptyText(ilFechIni)) {
                    correct = true;
                }
                break;
            case R.id.book_rdFinish:
                if(checkNotEmptyText(ilFechIni) && checkNotEmptyText(ilFechFin) && compareDates(fechIni, fechFin)){
                    correct=true;
                }
                break;
            default:
                correct=true;
        }
        return correct;
    }

    private boolean compareDates(DateCustom fechIni, DateCustom fechFin) {
        boolean correct = true;
        ilFechFin.setError(null);
        if (Integer.parseInt(fechIni.getYear()) > Integer.parseInt(fechFin.getYear())) {
            correct = false;
            ilFechFin.setError(getString(R.string.fechFinishLess));
        }else if (Integer.parseInt(fechIni.getYear()) == Integer.parseInt(fechFin.getYear())) {
            if (Integer.parseInt(fechIni.getMonth()) > Integer.parseInt(fechFin.getMonth())) {
                correct = false;
                ilFechFin.setError(getString(R.string.fechFinishLess));
            }else if (Integer.parseInt(fechIni.getMonth()) == Integer.parseInt(fechFin.getMonth())) {
                if (Integer.parseInt(fechIni.getDay()) > Integer.parseInt(fechFin.getDay())) {
                    correct = false;
                    ilFechFin.setError(getString(R.string.fechFinishLess));
                }
            }

        }

        return correct;
    }

    /***********************************************
     ************** LISTENER AND ADAPTER *************
     *************************************************/

    private void setListenerFavorite(){
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorite==false){
                    ivFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                    favorite=true;
                }
                else{
                    ivFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                    favorite=false;
                }

            }
        });
    }

    private void setListenerFechIni() {
        etFechIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechClick(ManageBooks.this, etFechIni);
            }
        });
    }

    private void setListenerFechFin() {
        etFechFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechClick(ManageBooks.this, etFechFin);
            }
        });
    }

    private void addAdapterSpinner(){
        sp = findViewById(R.id.book_writter);
        authorsString = new ArrayList<>();
        for(Author author : authors){
            authorsString.add(author.getName());
        }
        adaptadorSpiner = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, authorsString);
        sp.setAdapter(adaptadorSpiner);
    }

    private void setListenerFoto() {
        iVPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
    }

    private void setListenerCreateBook() {
        btCreateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etTitle.getText().toString();

                fechIni = new DateCustom().setDate(etFechIni.getText().toString(), "d-m-y", getString(R.string.guion));
                fechFin = new DateCustom().setDate(etFechFin.getText().toString(), "d-m-y", getString(R.string.guion));

                resume=summary.getText().toString();
                rating=rtBar.getRating();

                for(Author author: authors){
                    if(author.getName() == sp.getSelectedItem().toString()){
                        authorSelected = author;
                        break;
                    }
                }

                if (checkNotEmptyText(ilTitle) && checkRadios()) {
                    if (uri != null) {
                        FirebaseCustom.sendPhoto(uri, interfaceFireBase);
                    } else {
                        FirebaseCustom.getPhoto(null, interfaceFireBase);
                    }
                }

            }
        });
    }

    private void setListenerDialog(){
        btAddAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    /***********************************************
     ****************** DATEPICKER *****************
     *************************************************/

    private void fechClick(Context context, final EditText editText) {
        DatePickerDialog recogerFecha = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int actualMonth = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String reformatDay = (dayOfMonth < 10) ? getString(R.string.cero) + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String reformatMonth = (actualMonth < 10) ? getString(R.string.cero) + String.valueOf(actualMonth) : String.valueOf(actualMonth);
                //Muestro la fecha con el formato deseado
                editText.setText(reformatDay + getString(R.string.guion) + reformatMonth + getString(R.string.guion) + year);
            }
        }, anio, mes, dia);
        recogerFecha.show();
    }

    /***********************************************
     ************** GET AND SET VALUES *************
     *************************************************/

    private void getAuthors(){
        authors = manager.getAllAuthor(null);
    }

    private void addItemToSpinner(Author author){
        authors.add(author);
        authorsString.add(author.getName());
        adaptadorSpiner.notifyDataSetChanged();
    }

    private void createBook(String photo){
        if(authorSelected == null){
            CharSequence mensaje = getString(R.string.not_author);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, mensaje, duration).show();
        } else {
            Book book = new Book(0, authorSelected.getId(), "", title, photo, resume, rating, favorite, fechIni, fechFin);
            Intent intent = new Intent();
            intent.putExtra("book", book);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void editBook(){
        bookEdit.setIdAuthor(authorSelected.getId());
        bookEdit.setTitle(title);
        bookEdit.setResume(resume);
        bookEdit.setAssessment(rating);
        bookEdit.setFavorite(favorite);
        bookEdit.setStartDate(fechIni);
        bookEdit.setEndDate(fechFin);
        Intent i = new Intent();
        i.putExtra("book", bookEdit);
        setResult(RESULT_OK, i);
        finish();
    }

    private void getBookToEdit(){
        Intent intent = getIntent();
        bookEdit = intent.getParcelableExtra("book");
    }

    private void setBookValues(){
        if(bookEdit.isFavorite()) {
            ivFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            favorite=true;
        }else{
            ivFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            favorite=false;
        }
        if(!bookEdit.getStartDate().getDate().equals("00-00-00")){
            ilFechIni.getEditText().setText(bookEdit.getStartDate().getDate());
        }
        if(!bookEdit.getEndDate().getDate().equals("00-00-00")){
            ilFechFin.getEditText().setText(bookEdit.getEndDate().getDate());
        }
        ilTitle.getEditText().setText(bookEdit.getTitle());
        rtBar.setRating(bookEdit.getAssessment());
        summary.setText(bookEdit.getResume());
        Picasso.with(this).load(bookEdit.getUrlPhoto()).into(iVPhoto);

        //Empezado y terminado
        if(!ilFechFin.getEditText().getText().toString().isEmpty()
                && !ilFechIni.getEditText().getText().toString().isEmpty()){
            rdFinish.setChecked(true);
        }else{
            //Empezado pero no acabado
            if(!bookEdit.getStartDate().getDate().isEmpty() && bookEdit.getEndDate().getDate().isEmpty()){
                rdStarted.setChecked(true);
            }
        }

        btCreateBook.setText(getString(R.string.editBook));
    }

    /***********************************************
     ************** INTERFACES METHODS *************
     *************************************************/

    private InterfaceFireBase getMethodInterface() {
        return new InterfaceFireBase() {
            @Override
            public void isCorrectlyLogUp(boolean isSuccessful, String error) {}

            @Override
            public Book getBook(Book book) {
                return null;
            }

            @Override
            public Author getAuthor(Author author) {
                return null;
            }

            @Override
            public void getUserLogin(FirebaseUser user, String error) {}

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

                if (string != null && !string.equals("null")) {
                    FirebaseCustom.getPhoto(string, interfaceFireBase);
                } else {
                    FirebaseCustom.getPhoto(null, interfaceFireBase);
                }

                return null;
            }

            @Override
            public String getRoutePhoto(String string) {
                if(bookEdit != null){
                    //Si la foto que tiene asignada el objeto y la del imageView son distintas,
                    //se actualiza la imágen del objeto, solo si la uri no está vacía ni es nula
                    if(!bookEdit.getUrlPhoto().equals(uri.toString()) && uri != null && !uri.toString().equals("")){
                        bookEdit.setUrlPhoto(string);
                    }
                    editBook();
                }else {
                    createBook(string);
                }
                return string;
            }
        };
    }

    private CreateAuthor methodInterface(){
        return new CreateAuthor() {
            @Override
            public void create(Author author) {
                long id = manager.insertAutor(author);
                author.setId(id);
                Author authorFirebase = FirebaseCustom.saveAuthor(author);
                manager.updateAuthor(authorFirebase);
                addItemToSpinner(authorFirebase);
                progressDialog.dismiss();
            }
        };
    }

    private void setListenerDeleteDate(){
        ivDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFechIni.setText("");
                etFechFin.setText("");
            }
        });
    }

    private void enableDisableDates(){
        etFechFin.setVisibility(View.GONE);
        etFechIni.setVisibility(View.GONE);
        ivDeleteBook.setVisibility(View.GONE);
        ivCalendar.setVisibility(View.GONE);
        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (rdGroup.getCheckedRadioButtonId()){
                    case R.id.book_rdStarted:
                        etFechFin.setVisibility(View.GONE);
                        etFechIni.setVisibility(View.VISIBLE);
                        ivDeleteBook.setVisibility(View.VISIBLE);
                        ivCalendar.setVisibility(View.VISIBLE);

                        break;
                    case R.id.book_rdFinish:
                        etFechIni.setVisibility(View.VISIBLE);
                        etFechFin.setVisibility(View.VISIBLE);
                        ivDeleteBook.setVisibility(View.VISIBLE);
                        ivCalendar.setVisibility(View.VISIBLE);

                        break;
                    case R.id.book_rdNoStarted:
                        etFechFin.setVisibility(View.GONE);
                        etFechIni.setVisibility(View.GONE);
                        ivDeleteBook.setVisibility(View.GONE);
                        ivCalendar.setVisibility(View.GONE);

                        break;

                }
            }
        });

    }

}
