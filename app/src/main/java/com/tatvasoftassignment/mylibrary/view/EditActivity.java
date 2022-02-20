package com.tatvasoftassignment.mylibrary.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

import com.tatvasoftassignment.mylibrary.R;
import com.tatvasoftassignment.mylibrary.Utils.Constants;
import com.tatvasoftassignment.mylibrary.database.DataHelper;
import com.tatvasoftassignment.mylibrary.model.Books;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        init_id();
        setLaunchingDate();
        setSpinner();

        etBookName.setKeyListener(null);
        etAuthorName.setKeyListener(null);

        DataHelper db = new DataHelper(EditActivity.this);
        Intent i = getIntent();
        int id = i.getExtras().getInt(Constants.id);
        Cursor cursor = db.getDataBookName(id);
        if (cursor.getCount() == 0) {
            Toast.makeText(EditActivity.this, getString(R.string.No_Details_of_this_Books), Toast.LENGTH_SHORT).show();
            finish();
        }
        while (cursor.moveToNext()) {

            bookData = new Books(id, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        }

        displayExistingData();


        btnAddBook.setText(getString(R.string.Edit));
        btnAddBook.setOnClickListener(v -> {

            if (isValid()) {
                if (rFiction.isChecked()) {
                    type = getString(R.string.fiction);
                } else {
                    type = getString(R.string.non_fiction);
                }
                if (cChild.isChecked()) {
                    age = getString(R.string.below_18) + "\n";
                } else {
                    age += "";
                }
                if (cAdult.isChecked()) {
                    age += getString(R.string._18_to_60) + "\n";
                } else {
                    age += "";
                }
                if (cSixtyPlus.isChecked()) {
                    age += getString(R.string._60) + "\n";
                } else {
                    age += "";
                }
                Boolean updateData = db.updateData(id, Objects.requireNonNull(etBookName.getText()).toString(), Objects.requireNonNull(etAuthorName.getText()).toString(), bookGenre, type, Objects.requireNonNull(txtDate.getText()).toString(), age);
                if (updateData) {
                    Toast.makeText(EditActivity.this, getString(R.string.Data_Updated), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private Boolean isValid() {
        boolean valid = true;

        if (etBookName.getText().toString().length() == 0) {
            Toast.makeText(this, getString(R.string.enter_book_name), Toast.LENGTH_LONG).show();
            valid = false;
        } else if (etAuthorName.getText().toString().length() == 0) {
            Toast.makeText(this, getString(R.string.enter_author_name), Toast.LENGTH_LONG).show();
            valid = false;
        } else if (txtDate.getText().toString().length() == 0) {

            Toast.makeText(this, getString(R.string.set_launching_date_of_book), Toast.LENGTH_LONG).show();
            valid = false;
        } else if (!cChild.isChecked() && !cAdult.isChecked() && !cSixtyPlus.isChecked()) {
            Toast.makeText(this, getString(R.string.select_age_group_suitable_for_book_read), Toast.LENGTH_LONG).show();
            valid = false;
        }


        return valid;
    }

    private void init_id() {
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
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date;
        try {
            date = simpleDateFormat.parse(txtDate.getText().toString());
            assert date != null;
            Log.d("date",simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int day1 = AddBookActivity.set_day;
        int month1 = AddBookActivity.set_month;
        int year1 = AddBookActivity.set_year;

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            String dateString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(calendar.getTime());
            txtDate.setText(dateString);
        };

        txtDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.updateDate(year1,month1,day1);
            datePickerDialog.show();
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

        if (bookData.getBookAgePrefer().contains(getString(R.string.below_18))) {
            cChild.setChecked(true);
        }
        if (bookData.getBookAgePrefer().contains(getString(R.string._18_to_60))) {
            cAdult.setChecked(true);
        }
        if (bookData.getBookAgePrefer().contains(getString(R.string._60))) {
            cSixtyPlus.setChecked(true);
        }
        if (bookData.getBookType().equals(getString(R.string.fiction))) {
            rFiction.setChecked(true);
        } else {
            rNonFiction.setChecked(true);
        }

    }

}