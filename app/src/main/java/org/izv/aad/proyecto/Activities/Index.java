package org.izv.aad.proyecto.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.Adapters.AdapterIndex;
import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;
import org.izv.aad.proyecto.Interfaces.OnItemClickListener;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;
import org.izv.aad.proyecto.Utils.Gravatar;
import org.izv.aad.proyecto.Utils.SortAuthor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Index extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private RecyclerView recyclerBooks;
    private List<Book> books;
    private Manager manager;
    private TextView msg_error_index, userEmailTV;
    private InterfaceFireBase interfaceFireBase;
    private ImageView index_imageUser;
    private AdapterIndex adapterIndex;

    private Spinner spinnerAuthors;
    private ArrayAdapter<String> adapterSpinnerAuthor;
    private Button btSearchAuthor;

    private ConstraintLayout searcher, searcherAuthor;
    private EditText etSearch;

    public static int CODE_RESULT_MANAGEBOOKS_CREATE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        superInit();
    }

    private void superInit(){
        init();
        getEmailSharedPreferences();
        addSpinnerValues();
        searchByAuthor();
    }

    /**********************************************************
     * ******************** INIT METHODS ******************** *
     **********************************************************/

    private void init(){
        getInterfacesMethorFirebase();
        updateBooksFromFirebase();
        initFloatingButton();
        initNavigarionDrawer();
        msg_error_index = findViewById(R.id.msg_error_index);

        searcher = findViewById(R.id.searcher);
        searcherAuthor = findViewById(R.id.searcherAuthors);
        btSearchAuthor = findViewById(R.id.btSearchAuthor);
        etSearch = findViewById(R.id.etSearch);
        spinnerAuthors = findViewById(R.id.spìnnerAuthors);

        manager = new Manager(this);
        books = new ArrayList<>();
        getBooks();
        searcherBooks();


    }

    private void searchByAuthor() {
        btSearchAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = spinnerAuthors.getSelectedItem().toString();
                Author author = manager.getAuthor(name);

                books.clear();
                books.addAll(manager.getBooksByAuthor(author.getId()));
                adapterIndex.notifyDataSetChanged();
            }
        });
    }

    private void initFloatingButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageBooks = new Intent(Index.this, ManageBooks.class);
                startActivityForResult(manageBooks, CODE_RESULT_MANAGEBOOKS_CREATE);
            }
        });
    }

    private void initNavigarionDrawer(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        index_imageUser = headerView.findViewById(R.id.index_imageUser);
        userEmailTV=headerView.findViewById(R.id.userEmailTV);
    }

    private void initRecycler(){
        recyclerBooks = findViewById(R.id.recyclerBooks);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerBooks.setLayoutManager(mLayoutManager);
        adapterIndex = new AdapterIndex(this, manager, books, new OnItemClickListener() {
            @Override
            public void onBookClickListener(Book book) {
                //Cuando se haga click hará algo aquí
                Intent manageBooks = new Intent(Index.this, ShowBook.class);
                manageBooks.putExtra("book", book);
                startActivity(manageBooks);
            }
        });
        recyclerBooks.setAdapter(adapterIndex);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //Close navigation
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //Close app
        else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            if(searcher.getVisibility() == View.GONE) {


                if(searcherAuthor.getVisibility() == View.VISIBLE) {
                    searcherAuthor.setVisibility(View.GONE);
                    //toolbar.findItem(R.id.searchAuthors)..setIcon(getResources().getDrawable(R.drawable.ic_people));
                }

                searcher.setVisibility(View.VISIBLE);

                //item.setIcon(getResources().getDrawable(R.drawable.ic_close));
            }
            //cerrar
            else{
                searcher.setVisibility(View.GONE);
                //item.setIcon(getResources().getDrawable(R.drawable.ic_search));
                Log.v("XYZ", "cierro");

                showAllBooks();
            }

            return true;
        }

        if(id == R.id.searchAuthors){
            if(searcherAuthor.getVisibility() == View.GONE) {

                if(searcher.getVisibility() == View.VISIBLE) {
                    searcher.setVisibility(View.GONE);
                    //item.setIcon(getResources().getDrawable(R.drawable.ic_search));
                }

                searcherAuthor.setVisibility(View.VISIBLE);

                //item.setIcon(getResources().getDrawable(R.drawable.ic_close));
            }
            //cerrar
            else{
                searcherAuthor.setVisibility(View.GONE);
                //item.setIcon(getResources().getDrawable(R.drawable.ic_people));
                Log.v("XYZ", "cierro");

                showAllBooks();
            }

            return true;
        }

        if(id == R.id.sortTitle){
            //books.sort(null);
            Collections.sort(books, null);
            adapterIndex.notifyDataSetChanged();
            return true;
        }

        if(id == R.id.sortIdAuthor){
            //books.sort(new SortAuthor(manager));
            Collections.sort(books, new SortAuthor(manager));
            adapterIndex.notifyDataSetChanged();
            return true;
        }

        if(id == R.id.createBookMenu){
            Intent i = new Intent(Index.this,ManageBooks.class);
            startActivityForResult(i,CODE_RESULT_MANAGEBOOKS_CREATE);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**********************************************************
     * ***************** OPTIONS NAVIGATION ***************** *
     **********************************************************/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_createBook) {

            Intent manageBooks = new Intent(Index.this, ManageBooks.class);
            startActivityForResult(manageBooks, CODE_RESULT_MANAGEBOOKS_CREATE);

        } else if (id == R.id.nav_AuthorList) {

            Intent intentListAuthor = new Intent(Index.this, AuthorList.class);
            startActivity(intentListAuthor);

        } else if (id == R.id.nav_signout) {

            removeSharedPreferences();
            //Se borran las tablas y sus datos
            manager.dropTables();
            //Se crean las tablas vacías
            manager.createTables();
            finish();

        } else if (id == R.id.nav_help) {

            Intent intentHelp = new Intent(Index.this, Help.class);
            startActivity(intentHelp);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**********************************************************
     * ******************** UTILS METHODS ******************** *
     **********************************************************/

    private void getEmailSharedPreferences(){
        SharedPreferences pref = getSharedPreferences(Login.EMAIL, MODE_PRIVATE);
        String email = pref.getString("email", null);

        userEmailTV.setText(email);

        String imageUrl = null;
        if(email != null) {
            imageUrl = Gravatar.codeGravatarImage(email);
        }
        setGravatar(imageUrl);
    }

    private void setGravatar(String imageUrl){
        Glide.with(this)
            .load(imageUrl)
            .apply(
                new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
            )
            .into(index_imageUser);
    }

    private void removeSharedPreferences(){
        SharedPreferences pref = getSharedPreferences(Login.EMAIL, MODE_PRIVATE);
        pref.edit().clear().commit();
        pref = getSharedPreferences(Login.SAVE_SHARED_PREFERENCES, MODE_PRIVATE);
        pref.edit().clear().commit();
    }

    private void getBooks(){
        ThreadGetBooks hilo = new ThreadGetBooks();
        hilo.execute();
    }

    private void checkRecyclerBooks(){
        msg_error_index.setText(getString(R.string.no_books));
        if(books.size() <= 0){
            msg_error_index.setVisibility(View.VISIBLE);
        }else{
            msg_error_index.setVisibility(View.GONE);
            initRecycler();
        }
    }

    private void updateBooksFromFirebase(){
        FirebaseCustom.getAllBooks(interfaceFireBase);
        FirebaseCustom.getAllAuthors(interfaceFireBase);
    }

    private void getInterfacesMethorFirebase(){
        interfaceFireBase = new InterfaceFireBase() {
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

            }

            @Override
            public List<Book> getAllBooks(List<Book> booksInterface) {
                List<Book> booksManager = manager.getAllBooks();
                for(Book book : booksInterface){
                    boolean esta = false;
                    for(Book bookManager : booksManager){

                        if(bookManager.getKey() == null || bookManager.getKey().equals(book.getKey())){
                            esta = true;
                            break;
                        }
                    }
                    if(!esta) {
                        saveBooks(book, true);
                    }
                }
                books = booksInterface;

                if(adapterIndex == null) {
                    initRecycler();
                }
                adapterIndex.notifyDataSetChanged();
                checkRecyclerBooks();
                return booksInterface;
            }

            @Override
            public List<Author> getAllAuthors(List<Author> authors) {
                List<Author> authorsManager = manager.getAllAuthor(null);
                for(Author author : authors){
                    boolean esta = false;
                    for(Author authorManager : authorsManager){


                        if(authorManager.getKey() == null || authorManager.getKey().equals(author.getKey())){
                            esta = true;
                            break;
                        }
                    }
                    if(!esta) {
                        manager.insertAutor(author);
                    }
                }



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

    private void saveBooks(Book book, boolean getFromFirebase){
        Long id = manager.insertLibro(book);
        if(!getFromFirebase){
            book.setId(id);
            book = FirebaseCustom.saveBook(book);
            manager.updateBook(book);
        }
    }

    /**********************************************************
     * ******************** ANOTHER CLASS ******************** *
     **********************************************************/


    private class ThreadGetBooks extends AsyncTask <Integer, String, List<Book>> {

        public ThreadGetBooks(){}

        @Override
        protected List<Book> doInBackground(Integer... integers) {
            List<Book> books = manager.getAllBooks();

            if(books.size() <=0 ){
                books = new ArrayList<>();
            }
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> booksThread) {
            super.onPostExecute(books);
            books = booksThread;
            checkRecyclerBooks();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == CODE_RESULT_MANAGEBOOKS_CREATE){
            Log.v("XYZS", "2");
            Book book = data.getParcelableExtra("book");
            books.add(book);
            saveBooks(book, false);
            if(adapterIndex == null) {
                initRecycler();
            }
            adapterIndex.notifyDataSetChanged();
            checkRecyclerBooks();
        }
    }

    private void searchBooks(String titleSearch) {
        if(adapterIndex != null) {
            //Tiene libros
            if (manager.getFilterBooks(titleSearch).size() > 0) {
                books = manager.getFilterBooks(titleSearch);
                adapterIndex.notifyDataSetChanged();
                checkRecyclerBooks();
                //Si existe el mensaje
                if (msg_error_index.getVisibility() != View.GONE) {
                    msg_error_index.setVisibility(View.GONE);
                }
            }
            //No tiene libros
            else {
                final int size = books.size();
                books.clear();
                adapterIndex.notifyItemRangeRemoved(0, size);

                msg_error_index.setText(getString(R.string.no_books));
                msg_error_index.setVisibility(View.VISIBLE);
            }
        }
    }

    private void searcherBooks(){
        //Asociar el escuchador para el teclado de la máquina virtual
        etSearch.addTextChangedListener(initTextWatcher());
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String titleSearch = etSearch.getText().toString();
                searchBooks(titleSearch);

                return false;
            }
        });
    }

    private TextWatcher initTextWatcher(){
        return new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String titleSearch = etSearch.getText().toString();
                searchBooks(titleSearch);
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        ArrayList<Book> booksArray = new ArrayList<>();

        booksArray.addAll(books);

        savedInstanceState.putParcelableArrayList("books", booksArray);

        if(books.size()>0){
            initRecycler();
            adapterIndex.notifyItemRangeRemoved(0, books.size());
        }



        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        superInit();
        //books = savedInstanceState.getParcelableArrayList("books");
        getBooks();
    }

    private void addSpinnerValues(){
        List<Author> authors = manager.getAllAuthor(null);
        ArrayList<String> strAuthors = new ArrayList<>();

        for(Author author : authors){
            strAuthors.add(author.getName());
        }

        adapterSpinnerAuthor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strAuthors);

        spinnerAuthors.setAdapter(adapterSpinnerAuthor);
    }

    private void showAllBooks(){
        books.clear();
        books.addAll(manager.getAllBooks());
        adapterIndex.notifyDataSetChanged();
    }
}
