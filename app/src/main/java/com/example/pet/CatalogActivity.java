package com.example.pet;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.example.pet.data.PetContract;
import com.example.pet.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PetCursorAdapter petCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0,null,this);
        displayDatabaseInfo();
    }
    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */

    private void insert()
    {

        //using content values to for row1
        ContentValues values=new ContentValues();

        //inserting values for row 1
        //Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        values.put(PetContract.PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER,PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,7);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri uri1=getContentResolver().insert(PetContract.PetEntry.CONTENT_URI,values);

    }
    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
     //   PetDbHelper mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
       // SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor=getContentResolver().query(PetContract.PetEntry.CONTENT_URI,
        null,
        null,
        null,
        null);
/**
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + PetContract.PetEntry.TABLE_NAME, null);*/

            //there is no need to moveToNext() FUNCTION TO MOVE THE CURSOR ACTUALLY Cursor actually customized cursor adapter automatically move it to next place
            ListView listView=(ListView)findViewById(R.id.PetListViewId);
            petCursorAdapter=new PetCursorAdapter(this,cursor);
            listView.setAdapter(petCursorAdapter);
            View emptyView=(View)findViewById(R.id.EmptyListView);
            listView.setEmptyView(emptyView);
      //here do not need to close the cursor because if the cursor is pointing to some useless uri then it actually breaks the flow and by the way usage of why cursor is not closed
      //actually i think cursor adaptor will automatically close the cursor when we do not need it so.
    }
//so after exiting the from the activity it will display the databse info
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                //Inserting data or row 1
                insert();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id,Bundle args) {

        String[] projections=new String[]{PetContract.PetEntry._Id, PetContract.PetEntry.COLUMN_PET_NAME, PetContract.PetEntry.COLUMN_PET_GENDER};
        CursorLoader cursorLoader=new CursorLoader(getApplicationContext(), PetContract.PetEntry.CONTENT_URI,projections,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        petCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     petCursorAdapter.swapCursor(null);
    }
}