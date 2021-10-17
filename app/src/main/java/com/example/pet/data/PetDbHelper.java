package com.example.pet.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class PetDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "shelter.db";
    public static final int DATABASE_VERSION = 1;
    private final String SQL_CREATE_PETS_TABLE = " CREATE TABLE if not exists " + PetContract.PetEntry.TABLE_NAME + " (" +
            PetContract.PetEntry._Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " +
            PetContract.PetEntry.COLUMN_PET_BREED + " TEXT , " +
            PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL , " +
            PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0 );";

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
// The database is still at version 1, so there's nothing to do be done here.
    }
}
