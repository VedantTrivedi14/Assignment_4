package com.tatvasoftassignment.mylibrary.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.tatvasoftassignment.mylibrary.R;
import com.tatvasoftassignment.mylibrary.adapter.RecyclerViewAdapter;
import com.tatvasoftassignment.mylibrary.database.DataHelper;
import com.tatvasoftassignment.mylibrary.model.Books;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private String bookGenre;
    private RecyclerView recBookList;
    private DataHelper db;
    private TextView txtMassage;
    private RecyclerViewAdapter adapter;
    private final ArrayList<Books> book = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recBookList = findViewById(R.id.recBookList);
        txtMassage = findViewById(R.id.txtMassage);
        db = new DataHelper(MainActivity.this);
        createBookDataList();
        setAdapterRecyclerView();
        setNavigationDrawer();
    }

    public void createBookDataList() {
        Cursor getData = db.getData();
        if (getData.getCount() == 0) {
            txtMassage.setVisibility(View.VISIBLE);

        } else {
            txtMassage.setVisibility(View.INVISIBLE);
        }
        while (getData.moveToNext()) {
            book.add(new Books(getData.getInt(0), getData.getString(1), getData.getString(2), getData.getString(3), getData.getString(4), getData.getString(5), getData.getString(6)));
        }
    }

    public void setAdapterRecyclerView() {

        adapter = new RecyclerViewAdapter(book, this);
        recBookList.setAdapter(adapter);
        recBookList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recBookList.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.booklist_menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.actionSearch));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);


        return true;
    }

    private void setNavigationDrawer() {
        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(this.getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6200EE")));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.bookNameSort:
                adapter.sortDataByBookName();
                adapter.notifyDataSetChanged();
                break;
            case R.id.launchDate:
                adapter.sortDataByBookLaunchDate();
                adapter.notifyDataSetChanged();
                break;
            case R.id.noFilter:
                adapter.getFilter().filter("");
                adapter.notifyDataSetChanged();
                break;

            case R.id.rFic:
                adapter.getFilter().filter(getString(R.string.fiction));
                adapter.notifyDataSetChanged();
                break;
            case R.id.rNonFic:
                adapter.getFilter().filter(getString(R.string.non_fiction));
                adapter.notifyDataSetChanged();
                break;
            case R.id.genreFilter:
                View view = inflater.inflate(R.layout.menu_spinner, null);
                builder.setView(view);

                Spinner mSpinner = view.findViewById(R.id.miSpinner);
                String[] book = getResources().getStringArray(R.array.genreArray);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, book);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mSpinner.setAdapter(dataAdapter);
                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        bookGenre = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                builder.setNeutralButton(getString(R.string.Filter), (dialog, which) -> {
                    adapter.getFilter().filter(bookGenre);
                    adapter.notifyDataSetChanged();
                });
                builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {

                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miBookList:

                Toast.makeText(getApplicationContext(), getString(R.string.You_are_already_here), Toast.LENGTH_LONG).show();
                navigationView.setCheckedItem(R.id.miAddBook);
                drawerLayout.closeDrawers();
                break;

            case R.id.miAddBook:

                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(intent);
                navigationView.setCheckedItem(R.id.miAddBook);
                drawerLayout.closeDrawers();
                break;
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return false;
    }
}