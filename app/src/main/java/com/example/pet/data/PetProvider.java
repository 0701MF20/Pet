package com.example.pet.data;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.renderscript.Sampler;

import com.example.pet.CatalogActivity;

public class PetProvider extends ContentProvider {
    /**Tag for the log messgaes*/
    public static final String LOG_TAG=PetProvider.class.getSimpleName();
//FOR INTEGER CODE CONSTANTS
    /** URI matcher code for the content URI for the pets table */
    public static final int PET_ID=100;
    /** URI matcher code for the content URI for a single pet in the pets table */
    public static final int PETS=101;
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
//for declaring Urimatcher and declared as static
public static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
static {
    //1st parrameter is content authority , 2nd parameter is path and last parameter is alloted code constant
    sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_SET,PET_ID);
    sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_SET+"/#",PETS);
}
//REFERENCE VARIABLE OF PetDbHelper
private PetDbHelper mDbHelpers;
/**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // Create and initialize a PetDbHelper object to gain access to the pets database.
      mDbHelpers=new PetDbHelper(getContext());
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //sqlite database is object is created and get readable method is called
        SQLiteDatabase db=mDbHelpers.getReadableDatabase();
        //cursor
      Cursor cursor;
      //match through UriMatcher
      int match=sUriMatcher.match(uri);
      switch(match)
      {
          case PET_ID:
           //for whole table pet
          cursor=db.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
          break;
          case PETS:
              //for single id of pet
            selection= PetContract.PetEntry._Id+"=?";
            selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
            cursor=db.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
              break;
          default:
         throw new IllegalArgumentException("Cannot Query unknown URI"+uri);
      }
      return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}