package cartelera.turnodetarde.example.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import cartelera.turnodetarde.example.com.model.Program;

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



    private SQLiteDatabase db;
    private GuiaDbHelper helper;

    public GuiaDb(Context context) {
        helper = new GuiaDbHelper(context);
        db = helper.getWritableDatabase();
    }

    public void cleanup() {
        db.close();
    }


    public ProgramAlarm getProgramAlarmById(int id) {
        Cursor c = db.query(TABLE_NAME, COLS, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
        ProgramAlarm program = null;
        if(c.moveToFirst()) {
            program = new ProgramAlarm();
            program.setId(c.getInt(0));
            program.setAlarm(new Date(c.getInt(1)));
            program.setStart(new Date(c.getInt(2)));
            program.setName(c.getString(3));
            program.setChannel(c.getString(4));
        }

        return program;
    }


    public void insertProgramAlarm(ProgramAlarm programAlarm) {
        ContentValues values = new ContentValues();
        values.put(COLS[0], programAlarm.getId());
        values.put(COLS[1], programAlarm.getAlarm().getTime());
        values.put(COLS[2], programAlarm.getStart().getTime());
        values.put(COLS[3], programAlarm.getName());
        values.put(COLS[4], programAlarm.getChannel());

        db.insert(TABLE_NAME, null, values);
    }

    public void deleteProgramAlarmById(int id) {
        db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }

}
