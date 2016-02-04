package ru.drmarkes.jobreport;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ru.drmarkes.jobreport.provider.ContractClass;

public class SaveActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private static final String YEAR = "Year";
    private static final String MONTH = "Month";
    private static final String DAY = "Day";

    int year;
    int month;
    int day;

    TextView nameEditText;
    TextView numberEditText;

    private TextView dateTextView;
    Calendar calendar;
    Date date;

    Spinner spinnerOrder;
    Spinner spinnerDepartment;
    Spinner spinnerManipulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        FloatingActionButton save = (FloatingActionButton) findViewById(R.id.save);
        save.setOnClickListener(this);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        calendar = Calendar.getInstance(TimeZone.getDefault());

        if (savedInstanceState != null) {
            year = savedInstanceState.getInt(YEAR);
            month = savedInstanceState.getInt(MONTH);
            day = savedInstanceState.getInt(DAY);
            calendar.set(year, month, day);
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        numberEditText = (EditText) findViewById(R.id.numberEditText);

        showDate();
        initSpinner();
    }

    private void initSpinner() {
        spinnerOrder = (Spinner) findViewById(R.id.spinnerOrder);
        ArrayAdapter<CharSequence> adapterOrder = ArrayAdapter.createFromResource(
                this, R.array.order, android.R.layout.simple_spinner_item);
        adapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapterOrder);

        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);
        ArrayAdapter<CharSequence> adapterDepartment = ArrayAdapter.createFromResource(
                this, R.array.department, android.R.layout.simple_spinner_item);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(adapterDepartment);

        spinnerManipulation = (Spinner) findViewById(R.id.spinnerManipulation);
        ArrayAdapter<CharSequence> adapterManipulation = ArrayAdapter.createFromResource(
                this, R.array.manipulation, android.R.layout.simple_spinner_item);
        adapterManipulation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerManipulation.setAdapter(adapterManipulation);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateTextView:
                Bundle bundle = new Bundle();
                bundle.putInt(DAY, day);
                bundle.putInt(MONTH, month);
                bundle.putInt(YEAR, year);

                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.save:
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();

                ContentValues saveContentValues = new ContentValues();
                saveContentValues.put(ContractClass.Job.COLUMN_NAME_DATE, date.getTime()/1000);
                saveContentValues.put(ContractClass.Job.COLUMN_NAME_ORDER, spinnerOrder.getSelectedItem().toString());
                saveContentValues.put(ContractClass.Job.COLUMN_NAME_DEPARTMENT, spinnerDepartment.getSelectedItem().toString());
                saveContentValues.put(ContractClass.Job.COLUMN_NAME_MANIPULATION, spinnerManipulation.getSelectedItem().toString());
                saveContentValues.put(ContractClass.Job.COLUMN_NAME_PATIENT, name);
                saveContentValues.put(ContractClass.Job.COLUMN_NAME_ROOM_HISTORY, number);
                getContentResolver().insert(ContractClass.Job.CONTENT_URI, saveContentValues);
                finish();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        calendar.set(year, monthOfYear, dayOfMonth);
        showDate();
    }

    private void showDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        date = calendar.getTime();
        dateTextView.setText(simpleDateFormat.format(date));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanseState) {
        savedInstanseState.putInt(YEAR, year);
        savedInstanseState.putInt(MONTH, month);
        savedInstanseState.putInt(DAY, day);
        super.onSaveInstanceState(savedInstanseState);
    }
}
