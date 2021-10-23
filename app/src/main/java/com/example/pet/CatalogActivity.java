package com.example.pet;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.pet.data.PetContract;
import com.example.pet.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

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
Cursor cursor=getContentResolver().query(PetContract.PetEntry.CONTENT_URI,null,null,null,null);
/**
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + PetContract.PetEntry.TABLE_NAME, null);*/
//TO READ FROM DATABASE QUERY METHOD IS USED TO AVOID ERROR while riting sql query as a constant
   //     Cursor cursor=db.query(PetContract.PetEntry.TABLE_NAME,null,null,null,null,null,null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("The pet table have " + cursor.getCount()+".");
            //column names are just addded by appending
            displayView.append("\n"+PetContract.PetEntry._Id+"\t"+ PetContract.PetEntry.COLUMN_PET_NAME+"\t"+ PetContract.PetEntry.COLUMN_PET_BREED+"\t"+ PetContract.PetEntry.COLUMN_PET_GENDER+"\t"+ PetContract.PetEntry.COLUMN_PET_WEIGHT);
            //Extract the column index in cursor by using getColumnIndex
            int _idColumnIndex=cursor.getColumnIndex(PetContract.PetEntry._Id);
            int nameColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
            int weightColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            int breedColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
            int  genderColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
            //iterate the cursor to travel throughout the table of daytabase by using cursor by using moveToNext()
            while(cursor.moveToNext())
            {
                //Extracting the value of current cursor and store it in variable
             int pet_id=cursor.getInt(_idColumnIndex);
             String pet_name=cursor.getString(nameColumnIndex);
             String pet_breed=cursor.getString(breedColumnIndex);
             int pet_gender=cursor.getInt(genderColumnIndex);
             int pet_weight=cursor.getInt(weightColumnIndex);
             displayView.append("\n"+pet_id+"\t"+pet_name+"\t"+pet_breed+"\t"+pet_gender+"\t"+pet_weight+"\n");
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
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
}