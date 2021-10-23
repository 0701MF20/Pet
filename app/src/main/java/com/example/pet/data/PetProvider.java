package com.example.pet.data;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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
//for declaring Urimatcher and declared as static AND WE DECLARED IT FINAL SO THAT NO ONE CAN CAN AC
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
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
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
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        //Sanity checkings:-

        //sanity checking for name (name cannot be null if null then throw illegal argument exception)
        String sanityName=contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        if(sanityName==null)
        {
            throw new IllegalArgumentException("Pet is must to proceed further storing the pet ");
        }
        //sanity checking for breed is not required as breed is not required for inserting the pet information.

        //sanity checking for gender(gender can not be null or any other gender integer other than unkown,female and male thus in that case illegalArgumentException
        Integer sanityGender=contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        if(sanityGender==null|| !PetContract.PetEntry.validGender(sanityGender))
        {
            throw new IllegalArgumentException("Pet must have valid gender");
        }

        //sanity checking for weight (weight can be null and if null then weight is default  0(already finished this part by creating table of database in pet dbHelper) ,weight should not null and negattive then we have to proceed for sanity checking
        Integer sanityWeight=contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if((sanityWeight!=null)&&(sanityWeight<0))
        {
            throw new IllegalArgumentException("Pet must have valid weight and it should be positive");
        }

        // Get writeable database
        SQLiteDatabase db=mDbHelpers.getWritableDatabase();
        //match the uri and extract the code associated with uri
        int match=sUriMatcher.match(uri);
        // Insert the new pet with the given values
        long ids;
        Uri uriss=null;
        switch(match)
        {
            case PET_ID:
                ids=db.insert(PetContract.PetEntry.TABLE_NAME,null,contentValues);
                // Once we know the ID of the new row in the table,
                // return the new URI with the ID appended to the end of it
            uriss=ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,ids);
           break;
            default:
              throw new IllegalArgumentException("Cannot insert the row"+uri);
        }
        return uriss;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        //sanity checking
        //now check for uri matcher
        int match=sUriMatcher.match(uri);

        switch (match)
        {//for whole table
            case PET_ID:
               return updatePet(uri,contentValues,selection,selectionArgs);
            // For the PETS code, extract out the ID from the URI,
            // so we know which row to update. Selection will be "_id=?" and selection
            // arguments will be a String array containing the actual ID.
               //for single rows
                case PETS:
                    selection= PetContract.PetEntry._Id+"=?";
                    selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                    return updatePet(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for"+uri);
        }
    }
    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs)
    {
        //first atleast check whether the contentvalues contain that key before doing data validation because there is possibility in update sql commmand
        //that column is not present
        //ContentValues.containkey(String key) return boolean
        //for checking whwether key value is present in content values
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_NAME))
        {
            //for name
            String sanityName=contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if(sanityName==null)
            {
                throw new IllegalArgumentException("Name of Pet is must");
            }

        }
        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER))
        {//for gender
            Integer sanityGender=contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
            if(sanityGender==null|| !PetContract.PetEntry.validGender(sanityGender))
            {
                throw new IllegalArgumentException("Gender name must b");
            }
        }
        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT))
        {
            //for weight
            Integer sanityWeight=contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            if(sanityWeight!=null&&sanityWeight>0)
            {
                throw new IllegalArgumentException("Weight must be positive");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        //if there is no value change in content value then does not change anything
        if(contentValues.size()==0)
        {
            return 0;
        }
        // Otherwise, get writeable database to update the data
        SQLiteDatabase db=mDbHelpers.getWritableDatabase();
        // Returns the number of database rows affected by the update statement
        return db.update(PetContract.PetEntry.TABLE_NAME,contentValues,selection,selectionArgs);
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        //get writable database
        SQLiteDatabase db=mDbHelpers.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        switch (match)
        {
            //FOR ALL TABLE ROW
            case PET_ID:
                // Delete all rows that match the selection and selection args
                return db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                //FOR SINGLE ROW OF TABLE
            case PETS:
                // Delete a single row given by the ID in the URI
                selection= PetContract.PetEntry._Id+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for"+uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match=sUriMatcher.match(uri);
        switch (match)
        {
            case PET_ID:
               return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PETS:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unkown URI "+uri+" with match "+match);
        }
    }
}