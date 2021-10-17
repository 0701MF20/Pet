package com.example.pet.data;
import android.provider.BaseColumns;
public final class PetContract {
    PetContract()
    {
    }
    public static final class PetEntry implements BaseColumns {
        public static final String TABLE_NAME="pet";
        public static final String _Id=BaseColumns._ID;
        public static final String COLUMN_PET_NAME="name";
        public static final String COLUMN_PET_BREED="breed";
        public static final String COLUMN_PET_GENDER="gender";
        public static final String COLUMN_PET_WEIGHT="weight";
        /*For gender*/
        public static final int GENDER_UNKNOWN=0;
        public static final int GENDER_MALE=1;
        public static final int GENDER_FEMALE=2;

    }
}
