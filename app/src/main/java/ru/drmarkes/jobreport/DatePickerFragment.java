package ru.drmarkes.jobreport;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import static android.content.DialogInterface.*;

/**
 * Created by Андрей on 22.01.2016.
 */
public class DatePickerFragment extends DialogFragment {
    private static final String DAY = "Day";
    private static final String MONTH = "Month";
    private static final String YEAR = "Year";

    private OnDateSetListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnDateSetListener) activity;
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int year = bundle.getInt(YEAR);
        int month = bundle.getInt(MONTH);
        int day = bundle.getInt(DAY);

        final DatePickerDialog datePickerDialog =
                new DatePickerDialog(getActivity(), getConstructorListener(), year, month, day);

        if(isAffectedVersion()) {
            datePickerDialog.setButton(BUTTON_POSITIVE,
                    getActivity().getString(android.R.string.ok), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatePicker datePicker = datePickerDialog.getDatePicker();
                            listener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(),
                                    datePicker.getDayOfMonth());
                        }
                    });
            datePickerDialog.setButton(BUTTON_NEGATIVE,
                    getActivity().getString(android.R.string.cancel), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
        }

        return datePickerDialog;
    }

    private boolean isAffectedVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    private OnDateSetListener getConstructorListener() {
        return isAffectedVersion() ? null: listener;
    }
}
