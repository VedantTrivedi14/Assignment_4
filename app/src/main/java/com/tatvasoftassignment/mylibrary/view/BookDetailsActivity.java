package com.tatvasoftassignment.mylibrary.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tatvasoftassignment.mylibrary.database.DataHelper;
import com.tatvasoftassignment.mylibrary.R;
import com.tatvasoftassignment.mylibrary.model.Books;

public class BookDetailsActivity extends AppCompatActivity {
    int id;
    Books bookDetail;
    private final DataHelper db = new DataHelper(BookDetailsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        TextView vBookName = findViewById(R.id.vBookName);
        TextView vAuthorName = findViewById(R.id.vAuthorName);
        TextView vGenre = findViewById(R.id.vGenre);
        TextView vType = findViewById(R.id.vType);
        TextView vDate = findViewById(R.id.vDate);
        TextView vAge = findViewById(R.id.vAge);


        Intent i = getIntent();
        id = i.getExtras().getInt("id");
        Cursor cursor = db.getDataBookName(id);
        if (cursor.getCount() == 0) {
            Toast.makeText(BookDetailsActivity.this, "No Books Right Now", Toast.LENGTH_SHORT).show();

        }

        while (cursor.moveToNext()) {
            bookDetail = new Books(id, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        }

        vBookName.setText(bookDetail.getBookNames());
        vAuthorName.setText(bookDetail.getBookAuthorNames());
        vGenre.setText(bookDetail.getBookGenre());
        vType.setText(bookDetail.getBookType());
        vDate.setText(bookDetail.getBookLaunchDate());
        vAge.setText(bookDetail.getBookAgePrefer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.editDetail) {
            Intent i = new Intent(BookDetailsActivity.this, EditActivity.class);
            i.putExtra("id", id);
            startActivity(i);
        }
        return true;
    }
}