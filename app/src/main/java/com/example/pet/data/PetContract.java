package com.example.pet.data;
import android.content.ContentResolver;
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

        /**Content uri for referring to table*/
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_SET);

        ///MIME TYPE WILL PROVIDE THE DATA TYPE THAT CONTENT PROVIDER CAN HANDLES

        //MIME TYPE OF THE {@link #CONTENT_URI} for a list of pet
        public static final String CONTENT_LIST_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_SET;
        //MIME TYPE OF THE {@link #CONTENT_URI} for a single pet
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_SET;

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

        //validGender Method
        public static Boolean validGender(int gendersIntegralValues)
        {
    if(gendersIntegralValues==GENDER_UNKNOWN||gendersIntegralValues==GENDER_MALE||gendersIntegralValues==GENDER_FEMALE)
    {
        return true;
    }
    return false;
    }
    }
}
