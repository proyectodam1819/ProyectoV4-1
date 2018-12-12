package org.izv.aad.proyecto.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.izv.aad.proyecto.Adapters.AdapterHelp;
import org.izv.aad.proyecto.Objects.HelpObject;
import org.izv.aad.proyecto.R;

import java.util.ArrayList;

public class Help extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ayuda");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        recyclerView = findViewById(R.id.recyclerHelp);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        AdapterHelp helpAdapter = new AdapterHelp(crearAyudas());
        recyclerView.setAdapter(helpAdapter);
    }

    private ArrayList<HelpObject> crearAyudas(){
        ArrayList <HelpObject> helps = new ArrayList<>();
        helps.add(new HelpObject(getString(R.string.HelpTitle1), getString(R.string.HelpSubTitle1)));
        helps.add(new HelpObject(getString(R.string.HelpTitle2), getString(R.string.HelpSubTitle2)));
        helps.add(new HelpObject(getString(R.string.HelpTitle3), getString(R.string.HelpSubTitle3)));
        return helps;
    }
}
