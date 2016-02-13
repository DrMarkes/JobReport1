package ru.drmarkes.jobreport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import ru.drmarkes.jobreport.provider.ContractClass;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemLongClickListener, DissmisDialogFragment.NoticeDialogListener {
    private static final String YEAR = "Year";
    private static final String MONTH = "Month";
    private static final String DAY = "Day";

    private DataAdapter dataAdapter;
    private String daySelection;
    private TextView dateTextView;
    private Calendar calendar;

    private int day;
    private int month;
    private int year;

    long rowId;

    Toolbar toolbar;
    ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        listViewItems = (ListView) findViewById(R.id.listViewItems);
        dataAdapter = new DataAdapter(this, null, 0);
        listViewItems.setAdapter(dataAdapter);
        listViewItems.setOnItemLongClickListener(this);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        if (savedInstanceState != null) {
            day = savedInstanceState.getInt(DAY);
            month = savedInstanceState.getInt(MONTH);
            year = savedInstanceState.getInt(YEAR);
            calendar.set(year, month, day);
        } else {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }

        getDaySelection();
        showDate();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent saveIntent = new Intent(this, SaveActivity.class);
                startActivity(saveIntent);
                break;
            case R.id.dateTextView:
                Bundle bundle = new Bundle();
                bundle.putInt(YEAR, year);
                bundle.putInt(MONTH, month);
                bundle.putInt(DAY, day);

                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selection = new String[]{daySelection};
        String day = "day = ?";
        return new CursorLoader(
                this,
                ContractClass.Job.CONTENT_URI,
                ContractClass.Job.DATA_PROJECTION,
                day,
                selection,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapter.swapCursor(null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth;
        month = monthOfYear;
        this.year = year;
        calendar.set(year, monthOfYear, dayOfMonth);
        showDate();
        getDaySelection();
        getSupportLoaderManager().restartLoader(0, null, this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(DAY, day);
        outState.putInt(MONTH, month);
        outState.putInt(YEAR, year);
        super.onSaveInstanceState(outState);
    }

    private void showDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void getDaySelection() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        daySelection = simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DataAdapter.Holder holder = (DataAdapter.Holder) view.getTag();
        rowId = holder.RecordID;

        DialogFragment newFragment = new DissmisDialogFragment();
        newFragment.show(getSupportFragmentManager(), "dissmis");
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        getContentResolver().delete(Uri.withAppendedPath(ContractClass.Job.CONTENT_URI,
                Long.toString(rowId)), null, null);
        dataAdapter.notifyDataSetChanged();
    }
}
