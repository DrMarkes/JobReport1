package ru.drmarkes.jobreport.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Андрей on 24.01.2016.
 */
public final class ContractClass {
    public final static String AUTHORITY = "ru.drmarkes.jobreport.provider.ContractClass";

    private ContractClass(){}

    public final static class Job implements BaseColumns {
        private Job(){}
        public static final String TABLE_NAME = "job";

        private static final String SCHEME = "content://";
        private static final String PATH_JOB = "/job";
        private static final String PATH_JOB_ID = "/job/";

        public static final int JOB_ID_PATH_POSITION = 1;
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_JOB);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_JOB_ID);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ru.drmarkes.job";
        public static final String CONTENT_ITEM_TYPE = "vng.android.cursor.item/vnd.ru.drmarkes.job";
        public static final String DEFAULT_SORT_ORDER = "date ASC";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DATE_DAY = "strftime('%d %m %Y', date, 'unixepoch') as day";
        public static final String COLUMN_NAME_ORDER = "orders";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_DEPARTMENT = "department";
        public static final String COLUMN_NAME_MANIPULATION = "manipulation";
        public static final String COLUMN_NAME_PATIENT = "patient";
        public static final String COLUMN_NAME_ROOM_HISTORY = "room_history";

        public static final String[] DATA_PROJECTION = new String[] {
                Job._ID,
                Job.COLUMN_NAME_DATE_DAY,
                Job.COLUMN_NAME_DATE,
                Job.COLUMN_NAME_ORDER,
                Job.COLUMN_NAME_DEPARTMENT,
                Job.COLUMN_NAME_MANIPULATION,
                Job.COLUMN_NAME_PATIENT,
                Job.COLUMN_NAME_ROOM_HISTORY
        };
    }

}
