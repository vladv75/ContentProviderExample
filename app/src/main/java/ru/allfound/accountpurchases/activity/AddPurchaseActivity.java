package ru.allfound.accountpurchases.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.allfound.accountpurchases.sqlite.DatabaseHandler;
import ru.allfound.accountpurchases.model.Purchase;
import ru.allfound.accountpurchases.R;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

/*
 * AddPurchaseActivity.java    v.1.0 05.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class AddPurchaseActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    EditText editTextDescription;
    @NotEmpty
    EditText editTextCategory;
    @NotEmpty
    EditText editTextPrice;
    @NotEmpty
    EditText editTextDate;
    @NotEmpty
    EditText editTextTime;

    private Purchase purchase;
    private DatabaseHandler databaseHandler;
    private long id;
    private Validator validator;
    private Date date;

    private static String DATE_PATTERN = "dd.MM.yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpurchase);
        setupData();
        ;

        validator = new Validator(this);
        validator.setValidationListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.inflateMenu(R.menu.addpurchase_menuitem);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (id > 0) {
                    databaseHandler.deletePurchase(id);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        assert buttonSave != null;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void setupData() {
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextTime = (EditText) findViewById(R.id.editTextTime);

        purchase = new Purchase();
        databaseHandler = new DatabaseHandler(getApplicationContext());

        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        if (id > 0) {
            purchase = databaseHandler.findById(id);
            editTextDescription.setText(purchase.getDescription());
            editTextCategory.setText(purchase.getCategory());
            editTextPrice.setText(Integer.toString(purchase.getPrice()));
            editTextDate.setText(purchase.getDate());
            editTextTime.setText(purchase.getTime());
        }
    }

    @Override
    public void onValidationSucceeded() {
        purchase.setId(id);
        purchase.setDescription(editTextDescription.getText().toString());
        purchase.setCategory(editTextCategory.getText().toString());
        purchase.setPrice(Integer.parseInt((editTextPrice.getText().toString())));
        purchase.setDate(editTextDate.getText().toString());
        purchase.setTime(editTextTime.getText().toString());

        if (id == 0) {
            databaseHandler.addPurchase(purchase);
        } else {
            databaseHandler.update(purchase);
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getApplicationContext());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickTime(View view) {
        Calendar now = Calendar.getInstance();
        final TimePickerDialog d = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                        String minuteString = minute < 10 ? "0" + minute : "" + minute;
                        //String secondString = second < 10 ? "0" + second : "" + second;
                        String time = hourString + ":" + minuteString;
                        editTextTime.setText(time);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );
        d.show(getFragmentManager(), this.getClass().getName());
    }

    public void onClickDate(View view) {
        Calendar now = Calendar.getInstance();
        final DatePickerDialog d = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                        Calendar checkedCalendar = Calendar.getInstance();
                        checkedCalendar.set(year, monthOfYear, dayOfMonth);
                        date = checkedCalendar.getTime();
                        //etBirthday.setText(DateFormatter.convertDateToString(date));
                        editTextDate.setText(new SimpleDateFormat(DATE_PATTERN).format(date));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        d.setMaxDate(now);
        d.show(getFragmentManager(), this.getClass().getName());
    }

}
