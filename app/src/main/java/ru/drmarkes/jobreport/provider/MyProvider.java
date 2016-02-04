package ru.drmarkes.jobreport.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import java.util.HashMap;

/**
 * Created by Андрей on 24.01.2016.
 */
public class MyProvider extends ContentProvider {
    private static final int DATABASE_VERSION = 4;
    private static final int JOB = 1;
    private static final int JOB_ID = 2;
    private DataBaseHelper dbHelper;
    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ContractClass.AUTHORITY, "job", JOB);
        sUriMatcher.addURI(ContractClass.AUTHORITY, "job/#", JOB_ID);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "JobDB";

        public static final String DATABASE_TABLE_JOB = ContractClass.Job.TABLE_NAME;

        public static final String KEY_ROWID = "_id";
        public static final String KEY_DATE = "date";
        public static final String KEY_ORDERS = "orders";
        public static final String KEY_DEPARTMENT = "department";
        public static final String KEY_MANIPULATION = "manipulation";
        public static final String KEY_PATIENT = "patient";
        public static final String KEY_ROOM_HISTORY = "room_history";

        private static final String DATABASE_CREATE_TABLE_JOB =
                "create table " + DATABASE_TABLE_JOB + " ("
                + KEY_ROWID + " integer primary key autoincrement, "
                + KEY_DATE + " integer, "
                + KEY_ORDERS + " string, "
                + KEY_DEPARTMENT + " string, "
                + KEY_MANIPULATION + " string, "
                + KEY_PATIENT + " string, "
                + KEY_ROOM_HISTORY + " integer);";

        DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TABLE_JOB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_JOB);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String OrderBy;

        switch (sUriMatcher.match(uri)) {
            case JOB:
                queryBuilder.setTables(ContractClass.Job.TABLE_NAME);
                OrderBy = ContractClass.Job.DEFAULT_SORT_ORDER;
                break;
            case JOB_ID:
                queryBuilder.setTables(ContractClass.Job.TABLE_NAME);
                queryBuilder.appendWhere(ContractClass.Job._ID + " = " + uri.getPathSegments().get(ContractClass.Job.JOB_ID_PATH_POSITION));
                OrderBy = ContractClass.Job.DEFAULT_SORT_ORDER;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, OrderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case JOB:
                return ContractClass.Job.CONTENT_TYPE;
            case JOB_ID:
                return ContractClass.Job.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if(sUriMatcher.match(uri) != JOB) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;

        if(initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        long rowId = -1;
        Uri rowUri = Uri.EMPTY;

        if(!values.containsKey(ContractClass.Job.COLUMN_NAME_DATE)) {
            values.put(ContractClass.Job.COLUMN_NAME_DATE, 0);
        }

        rowId = db.insert(ContractClass.Job.TABLE_NAME, ContractClass.Job.COLUMN_NAME_DATE, values);
        if(rowId > 0) {
            rowUri = ContentUris.withAppendedId(ContractClass.Job.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(rowUri, null);
        }

        return rowUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int count;
        String finalWhere;
        String id;
        switch (sUriMatcher.match(uri)) {
            case JOB:
                count = database.delete(ContractClass.Job.TABLE_NAME, selection, selectionArgs);
                break;
            case JOB_ID:
                id = uri.getPathSegments().get(ContractClass.Job.JOB_ID_PATH_POSITION);
                finalWhere = ContractClass.Job._ID + " = " + id;
                if(selection != null) {
                    finalWhere = finalWhere + " AND " + selection;
                }
                count = database.delete(ContractClass.Job.TABLE_NAME, finalWhere, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int count;
        String finalWhere;
        String id;
        switch (sUriMatcher.match(uri)) {
            case JOB:
                count = database.update(ContractClass.Job.TABLE_NAME, values, selection, selectionArgs);
                break;
            case JOB_ID:
                id = uri.getPathSegments().get(ContractClass.Job.JOB_ID_PATH_POSITION);
                finalWhere = ContractClass.Job._ID + " = " + id;
                if(selection != null) {
                    finalWhere = finalWhere + " AND " + selection;
                }
                count = database.update(ContractClass.Job.TABLE_NAME, values, finalWhere, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
