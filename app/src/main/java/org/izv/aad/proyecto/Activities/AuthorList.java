package org.izv.aad.proyecto.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.izv.aad.proyecto.Adapters.AdapterAuthorList;
import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.R;

import java.util.List;

public class AuthorList extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Author List");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        recyclerView = findViewById(R.id.recyclerAuthorList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Manager manager = new Manager(this);
        List <Author> authors = manager.getAllAuthor(null);

        AdapterAuthorList adapterAuthorList = new AdapterAuthorList(authors);
        recyclerView.setAdapter(adapterAuthorList);
    }

}
