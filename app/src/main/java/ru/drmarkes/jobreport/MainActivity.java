package ru.drmarkes.jobreport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import ru.drmarkes.jobreport.provider.ContractClass;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        ListView listViewItems = (ListView) findViewById(R.id.listViewItems);
        dataAdapter = new DataAdapter(this, null, 0);
        listViewItems.setAdapter(dataAdapter);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        if(savedInstanceState != null) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        calendar.set(year, monthOfYear, dayOfMonth);
        showDate();
        getDaySelection();
        changeLoader();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(DAY, day);
        outState.putInt(MONTH, month);
        outState.putInt(YEAR, year);
        super.onSaveInstanceState(outState);
    }

    private void changeLoader() {
        getDaySelection();
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    private void showDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void getDaySelection() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        daySelection = simpleDateFormat.format(calendar.getTime());
    }
}
