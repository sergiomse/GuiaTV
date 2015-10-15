package com.sergiomse.guiatv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sergiomse.guiatv.model.Link;
import com.sergiomse.guiatv.model.Program;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by sergiomse@gmail.com on 26/07/2015.
 */
public class GuiaDb {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "guia-db";
    private static final String TABLE_NAME = "alarms";
    private static final String[] COLS = {"_id", "start", "finish", "name", "details", "links", "channel"};

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY, " +
                    "start TEXT, " +
                    "finish TEXT, " +
                    "name TEXT, " +
                    "details TEXT," +
                    "links TEXT," +
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


    public boolean existProgramInDb(Program program) {
        Cursor c = db.query(TABLE_NAME, COLS, "start=? AND name=? AND channel=?", new String[]{program.getStart().toString(), program.getName(), program.getChannelName()}, null, null, null);
        boolean exists = false;

        if(c.moveToFirst()) {
            exists = true;
        }

        return exists;
    }

    public Program getFirstProgramAlarm() {
        List<Program> programs = new ArrayList<>();

        Cursor c = db.query(TABLE_NAME, COLS, null, null, null, null, null);
        Program program = null;
        while(c.moveToNext()) {
            program = new Program();
            program.setId(c.getInt(0));
            program.setStart(DateTime.parse(c.getString(1)));
            program.setFinish(DateTime.parse(c.getString(2)));
            program.setName(c.getString(3));
            program.setDetails(c.getString(4));
            program.setLinks(Link.arrayFromString(c.getString(5)));
            program.setChannelName(c.getString(6));
            programs.add(program);
        }

        Collections.sort(programs, new Comparator<Program>() {
            @Override
            public int compare(Program p1, Program p2) {
                return (int) (p1.getStart().getMillis() - p2.getStart().getMillis());
            }
        });

        return !programs.isEmpty() ? programs.get(0) : null;
    }

    public void insertProgramAlarm(Program program) {
        ContentValues values = new ContentValues();
        values.put(COLS[0], program.getId());
        values.put(COLS[1], program.getStart().toString());
        values.put(COLS[2], program.getFinish().toString());
        values.put(COLS[3], program.getName());
        values.put(COLS[4], program.getDetails());
        values.put(COLS[5], Link.toArrayString(program.getLinks()));
        values.put(COLS[6], program.getChannelName());

        db.insert(TABLE_NAME, null, values);
    }

    public void deleteProgramAlarm(Program program) {
        db.delete(TABLE_NAME, "start=? AND name=? AND channel=?", new String[]{program.getStart().toString(), program.getName(), program.getChannelName()});
    }

}
