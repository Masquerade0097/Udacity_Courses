/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;

import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_PET_BREED;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_PET_GENDER;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_PET_NAME;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_PET_WEIGHT;
import static com.example.android.pets.data.PetContract.PetEntry.CONTENT_URI;
import static com.example.android.pets.data.PetContract.PetEntry.GENDER_MALE;
import static com.example.android.pets.data.PetContract.PetEntry._ID;
import static com.example.android.pets.data.PetProvider.LOG_TAG;

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

//        PetDbHelper mDbHelper = new PetDbHelper(this);
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        String mPetName;
        String mPetBreed;
        int mPetWeight;
        int mPetGender;
        int mPetId;
        String printIt;

        TextView displayTable = (TextView) findViewById(R.id.text_view_info);
        displayTable.setText("Table - ");

//        PetDbHelper mDbHelper = new PetDbHelper(this);
//
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projections = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_WEIGHT,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER
        };
//PREVIOUS APPROACH
        /*Cursor cursor = db.query(
                PetEntry.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );*/
//NEW APPROACH
        Cursor cursor = getContentResolver().query(CONTENT_URI,projections,null,null,null);
        if(cursor == null){
            Log.v(LOG_TAG, "Cursor here is nulll ");

        }


//        // Perform this raw SQL query "SELECT * FROM pets"
//        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            if (cursor != null) {
                displayView.setText("Number of rows in pets database table: " + cursor.getCount());
            }
            else{
                Log.v(LOG_TAG, "Cursor here is nulll ");

            }


            while(cursor.moveToNext()){

                mPetName = cursor.getString(cursor.getColumnIndex(COLUMN_PET_NAME));
                mPetBreed = cursor.getString(cursor.getColumnIndex(COLUMN_PET_BREED));
                mPetGender = cursor.getInt(cursor.getColumnIndex(COLUMN_PET_GENDER));
                mPetWeight = cursor.getInt(cursor.getColumnIndex(COLUMN_PET_WEIGHT));
                mPetId = cursor.getInt(cursor.getColumnIndex(_ID));

//                printIt = mPetId + mPetName + mPetBreed + mPetGender + mPetWeight + "\n";
                displayTable.append(("\n" + mPetId + " - " +
                        mPetName + " - " +
                        mPetBreed + " - " +
                        mPetGender + " - " +
                        mPetWeight));
            }
//            displayTable.append(("\n" + mPetId + " - " +
//                    mPetName + " - " +
//                    mPetBreed + " - " +
//                    mPetGender + " - " +
//                    mPetWeight));


        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.

            if (cursor != null) {
                cursor.close();
            }
            else{
                Log.v(LOG_TAG, "Cursor here is nulll ");
            }

        }
    }

    private void insertPet(){

        ContentValues rowValues = new ContentValues();
        rowValues.put(PetEntry.COLUMN_PET_NAME,"Toto");
        rowValues.put(PetEntry.COLUMN_PET_BREED,"Terrier");
        rowValues.put(PetEntry.COLUMN_PET_GENDER,GENDER_MALE);
        rowValues.put(PetEntry.COLUMN_PET_WEIGHT,75);

        Uri uriInsert = getContentResolver().insert(CONTENT_URI,rowValues);

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
                insertPet();
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
