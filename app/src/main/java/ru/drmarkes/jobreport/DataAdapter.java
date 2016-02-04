package ru.drmarkes.jobreport;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.drmarkes.jobreport.provider.ContractClass;

/**
 * Created by Андрей on 24.01.2016.
 */
public class DataAdapter extends CursorAdapter {
    private LayoutInflater layoutInflater;

    public DataAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View root = layoutInflater.inflate(R.layout.my_list_item, parent, false);
        Holder holder = new Holder();
        TextView textViewOrders = (TextView)root.findViewById(R.id.orders);
        TextView textViewDepartment = (TextView)root.findViewById(R.id.department);
        TextView textViewManipulation = (TextView)root.findViewById(R.id.manipulation);
        TextView textViewPatient = (TextView)root.findViewById(R.id.patient);
        TextView textViewNumber = (TextView)root.findViewById(R.id.number);
        holder.textViewOrders = textViewOrders;
        holder.textViewDepartment = textViewDepartment;
        holder.textViewManipulation = textViewManipulation;
        holder.textViewPatient = textViewPatient;
        holder.textViewNumber = textViewNumber;
        root.setTag(holder);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(ContractClass.Job._ID));
//      String date = cursor.getString(cursor.getColumnIndex(ContractClass.Job.COLUMN_NAME_DAY));
        String order = cursor.getString(cursor.getColumnIndex(ContractClass.Job.COLUMN_NAME_ORDER));
        String department = cursor.getString(cursor.getColumnIndex(ContractClass.Job.COLUMN_NAME_DEPARTMENT));
        String manipulation = cursor.getString(cursor.getColumnIndex(ContractClass.Job.COLUMN_NAME_MANIPULATION));
        String patient = cursor.getString(cursor.getColumnIndex(ContractClass.Job.COLUMN_NAME_PATIENT));
        String roomHisrory = cursor.getString(cursor.getColumnIndex(ContractClass.Job.COLUMN_NAME_ROOM_HISTORY));
        if(!roomHisrory.equals("")) {
            roomHisrory ="№" + cursor.getString(cursor.getColumnIndex(ContractClass.Job.COLUMN_NAME_ROOM_HISTORY));
        } else {
            roomHisrory = "";
        }
        Holder holder = (Holder)view.getTag();
        if(holder != null) {
            holder.textViewOrders.setText(order);
            holder.textViewDepartment.setText(department);
            holder.textViewManipulation.setText(manipulation);
            holder.textViewPatient.setText(patient);
            holder.textViewNumber.setText(roomHisrory);
            holder.RecordID = id;
        }
    }

    private static class Holder {
        public TextView textViewOrders;
        public TextView textViewDepartment;
        public TextView textViewManipulation;
        public TextView textViewPatient;
        public TextView textViewNumber;
        public long RecordID;
    }
}
