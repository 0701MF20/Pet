package com.example.pet;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pet.data.PetContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//these import statement are import as it takes me an our to find this error


/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements androidx.loader.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PET_LOADER_ID=0;
  PetCursorAdapter petCursorAdapter;
public static String LOG_TAG_Catalog=CatalogActivity.class.getSimpleName();
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
//ListView
        ListView listView=(ListView)findViewById(R.id.PetListViewId);
        //empty view
        View emptyView=findViewById(R.id.EmptyListView);

        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //that is why id for is important and we used in  on create loader because it has been later used by cursor adapter
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent a1=new Intent(CatalogActivity.this,EditorActivity.class);

                Uri CurrentUri= ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,id);

                a1.setData(CurrentUri);

                Log.e(LOG_TAG_Catalog,"URI IS "+CurrentUri);

                startActivity(a1);
            }
        });
        //pet cursor adapter should be null because initially we have to set to null

        petCursorAdapter=new PetCursorAdapter(this,null);
        listView.setAdapter(petCursorAdapter);
        //noinspection deprecation
        getSupportLoaderManager().initLoader(PET_LOADER_ID, null, this);
  //If there is no pet in our pet adpter cursors the it made no sense to even show option to delete all

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
    private void deleteAllEntries()
    {
        if(petCursorAdapter.getCursor().getCount()!=0) {
           int rowCount=getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, null, null);
           Log.e(LOG_TAG_Catalog,"Number of rows deleted are: "+rowCount);
           Toast.makeText(this,"All pet are deleted",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */

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
                insert();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllEntries();
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//creating cursor loader

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections={
                PetContract.PetEntry._Id,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED};
        return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI,projections,null,null,null);
        }

    @Override
    public void onLoadFinished(androidx.loader.content.Loader<Cursor> loader, Cursor data) {
        petCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(androidx.loader.content.Loader<Cursor> loader) {
        petCursorAdapter.swapCursor(null);

    }

}