package cartelera.turnodetarde.example.com;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sergiomse@gmail.com on 26/07/2015.
 */
public class GuiaDb {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "guia-db";
    private static final String TABLE_NAME = "alarms";
    private static final String[] COLS = {"_id", "dt_alarm", "dt_program", "name", "channel"};

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY, " +
                    "dt_alarm INTEGER, " +
                    "dt_program INTEGER, " +
                    "name TEXT, " +
                    "channel TEXT)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;




    public class GuiaDbHelper extends SQLiteOpenHelper {

        public GuiaDbHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }



}
