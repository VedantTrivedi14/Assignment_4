package com.tatvasoftassignment.mylibrary.view;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.tatvasoftassignment.mylibrary.database.DataHelper;

import java.util.Calendar;

public class AddBookActivity extends AppCompatActivity {

    private Button btnAddBook;
    private TextView txtDate;
    private Spinner mSpinner;
    private EditText etBookName, etAuthorName;
    public static int set_day;
    public static int set_month;
    public static int set_year;
    private RadioButton rFiction;
    private CheckBox cChild, cAdult, cSixtyPlus;
    String bookGenre, type, age = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        init_id();
        setLaunchingDate();
        setSpinner();
        DataHelper db = new DataHelper(AddBookActivity.this);
        btnAddBook.setOnClickListener(view -> {



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
                    age += R.string._18_to_60 + "\n";
                } else {
                    age += "";
                }
                if (cSixtyPlus.isChecked()) {
                    age += getString(R.string._60) + "\n";
                } else {
                    age += "";
                }

                db.insertData(etBookName.getText().toString(), etAuthorName.getText().toString(), bookGenre, type, txtDate.getText().toString(), age) ;
                    Toast.makeText(this, getString(R.string.Book_added_successfully), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
//                else{
//                    Toast.makeText(getApplicationContext(), getString(R.string.not_added_successfully), Toast.LENGTH_SHORT).show();
//                }



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

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener datePicker = (datePicker1, year, month, day) -> {
            set_year = year;
            Log.i("date", String.valueOf(set_year));
            set_month = month;
            Log.i("date", String.valueOf(set_month));
            set_day = day;
            Log.i("date", String.valueOf(set_day));
            calendar.set(Calendar.YEAR, set_year);
            calendar.set(Calendar.MONTH, set_month);
            calendar.set(Calendar.DAY_OF_MONTH, set_day);
            String launchDate = day + "/" + (month+1) + "/" + year;
            txtDate.setText(launchDate);
        };
        txtDate.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(this, datePicker, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );
            dpd.show();
        });
    }


}