package com.example.pet.data;
import android.net.Uri;
import android.provider.BaseColumns;
public final class PetContract {
    PetContract()
    {
    }
    public static final String CONTENT_AUTHORITY="com.example.pet";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static  final String PATH_SET="pet";
    public static final class PetEntry implements BaseColumns {
        /**Content uri for refering to table*/
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_SET);
        /**Table name and column name */
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
