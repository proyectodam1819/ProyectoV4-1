package org.izv.aad.proyecto.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.izv.aad.proyecto.Adapters.MyPrintDocumentAdapter;
import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;

public class ShowBook extends AppCompatActivity{

    private TextView show_author, show_dateStart, show_dateFinish, show_state, show_title;
    private TextView show_status, tvDateFinish;
    private ImageView show_logo, show_favStar;
    private Book book;
    private Author author;
    private RatingBar show_rating;
    private String status_book, dateStart, dateEnd;
    private ConstraintLayout constraintDates;

    private Manager manager;

    private static final int VALUE_EDIT_BOOK = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

        //para poner boton atras
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_layout_show_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editBook) {
            Intent intent = new Intent(this, ManageBooks.class);
            intent.putExtra("book", book);
            startActivityForResult(intent, VALUE_EDIT_BOOK);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init(){
        getIntentBook();
        show_title = findViewById(R.id.show_title);
        constraintDates = findViewById(R.id.constraintDates);
        tvDateFinish = findViewById(R.id.tvDateFinish);
        show_author = findViewById(R.id.show_author);
        show_dateStart = findViewById(R.id.show_dateStart);
        show_dateFinish = findViewById(R.id.show_dateFinish);
        show_state = findViewById(R.id.show_state);
        show_status = findViewById(R.id.show_status);
        show_rating = findViewById(R.id.show_rating);
        show_logo = findViewById(R.id.show_logo);
        show_favStar = findViewById(R.id.show_favStar);
        notEditableRatingBar();
        setValues();
        initFloatingButton();
    }

    private void getIntentBook(){
        Intent intent = getIntent();
        book = intent.getParcelableExtra("book");
    }

    private void getAuthor(){
        Manager manager = new Manager(this);
        author = manager.getAuthor(book.getIdAuthor());
    }

    private void setValues(){
        getAuthor();
        checkDates();
        show_title.setText(book.getTitle());
        show_author.setText(author.getName());
        show_dateStart.setText(dateStart);
        show_dateFinish.setText(dateEnd);
        show_state.setText(status_book);
        show_status.setText(book.getResume());
        show_rating.setRating(book.getAssessment());

        if(dateStart.equals("")){
            constraintDates.setVisibility(View.GONE);
        }else{
            if(dateEnd.equals("")){
                show_dateFinish.setVisibility(View.GONE);
                tvDateFinish.setVisibility(View.GONE);
            }
        }

        if(book.isFavorite()){
            show_favStar.setVisibility(View.VISIBLE);
        }
        Picasso.with(this).load(book.getUrlPhoto()).into(show_logo);
    }

    /*****
     * ******
     * CHECKERS
     */
    private void checkDates(){
        String vacio = "00-00-00";
        status_book = getString(R.string.No_started);
        dateStart = book.getStartDate().getDate();
        dateEnd = book.getEndDate().getDate();
        if(!dateStart.equals(vacio) && !dateEnd.equals(vacio)){
            //Empezado y acabado
            status_book = getString(R.string.Ended);
        }else{
            if(dateEnd.equals(vacio) && !dateStart.equals(vacio)){
                //Empezado
                dateEnd = "";
                status_book = getString(R.string.Started);
            }else{
                dateStart = "";
                dateEnd = "";
            }
        }
    }

    /************************
     * NO EDITABLE RATINGBAR
     * */

    private void notEditableRatingBar(){
        show_rating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == VALUE_EDIT_BOOK){
            Log.v("XYZ", "EDITO");
            manager = new Manager(this);
            book = data.getParcelableExtra("book");
            manager.updateBook(book);
            FirebaseCustom.updateBook(book);
            checkDates();
            setValues();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fabImprimir);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPrint();
            }
        });
    }

    private void doPrint() {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        printManager.print(jobName, new MyPrintDocumentAdapter(this,show_author,dateStart,dateEnd,show_state,show_status,book),
                null);
    }
}
