package com.tatvasoftassignment.mylibrary;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private Button btnAddBook;
    private TextView txtDate;
    private Spinner mSpinner;
    private EditText etBookName, etAuthorName;
    private RadioButton rFiction, rNonFiction;
    private CheckBox cChild, cAdult, cSixtyPlus;
    String bookGenre, type, age = "";
    Books bookData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        memoryAllocation();
        setLaunchingDate();
        setSpinner();

        etBookName.setKeyListener(null);
        etAuthorName.setKeyListener(null);

        DataHelper db = new DataHelper(EditActivity.this);
        Intent i = getIntent();
        int id = i.getExtras().getInt("id");
        Cursor cursor = db.getDataBookName(id);
        if (cursor.getCount() == 0) {
            Toast.makeText(EditActivity.this, "No Details of this Books ", Toast.LENGTH_SHORT).show();
            finish();
        }
        while (cursor.moveToNext()) {

            bookData = new Books(id, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        }

        displayExistingData();


        btnAddBook.setText("Edit");
        btnAddBook.setOnClickListener(v -> {

            if (isValid()) {
                if (rFiction.isChecked()) {
                    type = "Fiction";
                } else {
                    type = "Non-fiction";
                }
                if (cChild.isChecked()) {
                    age = "child" + "\n";
                } else {
                    age += "";
                }
                if (cAdult.isChecked()) {
                    age += "Adult" + "\n";
                } else {
                    age += "";
                }
                if (cSixtyPlus.isChecked()) {
                    age += "senior citizen" + "\n";
                } else {
                    age += "";
                }
                Boolean updateData = db.updateData(id, Objects.requireNonNull(etBookName.getText()).toString(), Objects.requireNonNull(etAuthorName.getText()).toString(), bookGenre, type, Objects.requireNonNull(txtDate.getText()).toString(), age);
                if (updateData) {
                    Toast.makeText(EditActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private Boolean isValid() {
        boolean valid = true;

        if (etBookName.getText().toString().length() == 0) {
            Toast.makeText(this, "Enter Book Name", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (etAuthorName.getText().toString().length() == 0) {
            Toast.makeText(this, "Enter Author Name", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (txtDate.getText().toString().length() == 0) {

            Toast.makeText(this, "select Date", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (!cChild.isChecked() && !cAdult.isChecked() && !cSixtyPlus.isChecked()) {
            Toast.makeText(this, "select age group", Toast.LENGTH_LONG).show();
            valid = false;
        }


        return valid;
    }

    private void memoryAllocation() {
        txtDate = findViewById(R.id.txtDate);
        mSpinner = findViewById(R.id.mSpinner);
        btnAddBook = findViewById(R.id.btnAddBook);
        etBookName = findViewById(R.id.etBookName);
        etAuthorName = findViewById(R.id.etAuthorName);
        rFiction = findViewById(R.id.rFiction);
        rNonFiction = findViewById(R.id.rNonFiction);
        cChild = findViewById(R.id.cChild);
        cAdult = findViewById(R.id.cAdult);
        cSixtyPlus = findViewById(R.id.cSixtyPulse);


    }

    private void setSpinner() {

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.genreArray, android.R.layout.simple_spinner_item);

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapterSpinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookGenre = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setLaunchingDate() {
        txtDate.setOnClickListener(v -> {
            int date, set_month, set_year;
            GregorianCalendar gc = new GregorianCalendar();
            date = gc.get(Calendar.DAY_OF_MONTH);
            set_month = gc.get(Calendar.MONTH);
            set_year = gc.get(Calendar.YEAR);
            DatePickerDialog dpd = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                String launchDate = day + "/" + (month + 1) + "/" + year;
                txtDate.setText(launchDate);
            }, set_year, set_month, date);
            dpd.show();
        });

    }

    private void displayExistingData() {

        etBookName.setText(bookData.getBookNames());
        etAuthorName.setText(bookData.getBookAuthorNames());
        txtDate.setText(bookData.getBookLaunchDate());

        switch (bookData.getBookGenre()) {
            case "Science Fiction":
                mSpinner.setSelection(0);
                break;
            case "Historical Fiction":
                mSpinner.setSelection(1);
                break;
            case "Children’s":
                mSpinner.setSelection(2);
                break;
            case "Mystery":
                mSpinner.setSelection(3);
                break;
            case "Adventure":
                mSpinner.setSelection(4);
                break;
            case "Cooking":
                mSpinner.setSelection(5);
                break;
            case "Health":
                mSpinner.setSelection(6);
                break;
            case "Art":
                mSpinner.setSelection(7);
                break;
            case "Motivational":
                mSpinner.setSelection(8);
                break;
            case "Humor":
                mSpinner.setSelection(9);
                break;
        }

        if (bookData.getBookAgePrefer().contains("child")) {
            cChild.setChecked(true);
        }
        if (bookData.getBookAgePrefer().contains("Adult")) {
            cAdult.setChecked(true);
        }
        if (bookData.getBookAgePrefer().contains("senior citizen")) {
            cSixtyPlus.setChecked(true);
        }
        if (bookData.getBookType().equals("Fiction")) {
            rFiction.setChecked(true);
        } else {
            rNonFiction.setChecked(true);
        }

    }

}