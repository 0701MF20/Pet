package com.example.pet;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.pet.data.PetContract;

public class PetCursorAdapter extends CursorAdapter {
    public PetCursorAdapter(Context context, Cursor c) {
        super(context,c,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       return LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    String pet_name=cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME));
    String pet_breed=cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED));
    TextView petName=(TextView)view.findViewById(R.id.name_pet_Id);
    TextView petBreed=(TextView)view.findViewById(R.id.breed_pet_Id);
    petName.setText(pet_name);
    petBreed.setText(pet_breed);
    }


}
