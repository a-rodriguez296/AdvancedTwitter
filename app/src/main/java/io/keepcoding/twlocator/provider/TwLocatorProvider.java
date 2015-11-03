package io.keepcoding.twlocator.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import io.keepcoding.twlocator.model.db.DBConstants;
import io.keepcoding.twlocator.model.db.DBHelper;

public class TwLocatorProvider extends ContentProvider {


    public static final String TWLOCATOR_PROVIDER = "io.keepcoding.twlocator.provider";


    public static final Uri TWEETS_URI = Uri.parse("content://" + TWLOCATOR_PROVIDER + "/tweets");


    private static final int ALL_TWEETS = 1;

    private static final int SINGLE_TWEET = 2;


    private static final UriMatcher uriMatcher;


    //Contructor estático
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TWLOCATOR_PROVIDER, "tweets", ALL_TWEETS);
        uriMatcher.addURI(TWLOCATOR_PROVIDER, "tweets/#", SINGLE_TWEET);
    }

    private DBHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());

        return false;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_TWEETS:
                return "vnd.android.cursor.dir/vnd.arf.tweet";
            case SINGLE_TWEET:
                return "vnd.android.cursor.item/vnd.arf.tweet";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Open the database.

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Replace these with valid SQL statements if necessary.
        String groupBy = null;
        String having = null;
        // Use an SQLite Query Builder to simplify constructing the database query.

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(getTableName(uri));

        // If this is a row query, limit the result set to the passed in row.
        String rowID = null;
        switch (uriMatcher.match(uri)) {
            case SINGLE_TWEET :
                rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DBConstants.KEY_TWEET_ID + "=" + rowID);
                break;

            default: break;
        }

        // Specify the table on which to perform the query. This can // be a specific table or a join as required. queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);
        // Execute the query.

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        // Return the result Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // Open a read / write database to support the transaction.

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = getTableName(uri);

        // To add empty rows to your database by passing in an empty Content Values object
        // you must use the null column hack parameter to specify the name of the column that can be set to null.
        String nullColumnHack = null;
        // Insert the values into the table
        long id = db.insert(tableName, nullColumnHack, contentValues);
        // Construct and return the URI of the newly inserted row.
        if (id > -1) {
            // Construct and return the URI of the newly inserted row.
            Uri insertedUri = null;
            switch (uriMatcher.match(uri)) {
                case ALL_TWEETS:
                    insertedUri = ContentUris.withAppendedId(TWEETS_URI, id);
                    break;
                case SINGLE_TWEET :
                    insertedUri = ContentUris.withAppendedId(TWEETS_URI, id);
                    break;
                default: break;
            }

            // Notify any observers of the change in the data set.
            //Con esto se le notifica a los observadores interesados de los cambios
            getContext().getContentResolver().notifyChange(uri, null);
            getContext().getContentResolver().notifyChange(insertedUri, null);

            return insertedUri;
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = getTableName(uri);
        String rowID = null;

        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_TWEET:
                rowID = uri.getPathSegments().get(1);
                selection = DBConstants.KEY_TWEET_ID + "=" + rowID;
                break;
            default:
                break;
        }

        // Perform the deletion.
        int deleteCount = db.delete(tableName, selection, selectionArgs);
        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the number of deleted items.
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String rowID = null;
        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_TWEET :
                rowID = uri.getPathSegments().get(1);
                selection = DBConstants.KEY_TWEET_ID + "=" + rowID;
                break;
            default:
                break;
        }

        if (rowID == null) {
            return -1;
        }

        int updateCount = db.update(getTableName(uri), contentValues, selection, selectionArgs);

        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;

    }


    private String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case ALL_TWEETS:
                tableName = DBConstants.TABLE_TWEET;
                break;
            case SINGLE_TWEET :
                tableName = DBConstants.TABLE_TWEET;
                break;
            default: break;
        }
        return tableName;
    }


}
