package com.asset.assetapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.asset.assetapp.models.Asset;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public final class AssetDbAdapter {

    AssetDbHelper assetDbHelper;
    private Context context;

    public AssetDbAdapter(Context context) {
        assetDbHelper = new AssetDbHelper(context);
        this.context = context;
    }

    public boolean databaseExists() {
        File dbFile = context.getDatabasePath(AssetDbHelper.DATABASE_NAME);
        return dbFile.exists();
    }

    public long insertAsset(Asset asset){
        SQLiteDatabase db = assetDbHelper.getWritableDatabase();
        if (doesAssetExist(db, asset.getId())){
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(AssetDbHelper.COLUMN_NAME_ID, asset.getId());
        values.put(AssetDbHelper.COLUMN_NAME_IMAGE, getBitmapAsByteArray(asset.getImage()));
        values.put(AssetDbHelper.COLUMN_NAME_TITLE, asset.getTitle());
        values.put(AssetDbHelper.COLUMN_NAME_DESCRIPTION, asset.getDescription());
        values.put(AssetDbHelper.COLUMN_NAME_SERIALNUMBER, asset.getSerialNumber());
        values.put(AssetDbHelper.COLUMN_NAME_MANUFACTURER, asset.getManufacturer());
        values.put(AssetDbHelper.COLUMN_NAME_MODELNUMBER, asset.getModelNumber());
        values.put(AssetDbHelper.COLUMN_NAME_LATITUDE, asset.getLocation().getLatitude());
        values.put(AssetDbHelper.COLUMN_NAME_LONGITUDE, asset.getLocation().getLongitude());

        return db.insert(AssetDbHelper.TABLE_NAME, null, values);
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private boolean doesAssetExist(SQLiteDatabase db, String assetId) {
        String[] columns = { AssetDbHelper.COLUMN_NAME_ID };
        String selection = AssetDbHelper.COLUMN_NAME_ID + " =?";
        String[] selectionArgs = { assetId };
        String limit = "1";

        Cursor cursor = db.query(AssetDbHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public ArrayList<Asset> getData() {
        SQLiteDatabase db = assetDbHelper.getWritableDatabase();
        String[] columns = {
                AssetDbHelper.COLUMN_NAME_ID,
                AssetDbHelper.COLUMN_NAME_IMAGE,
                AssetDbHelper.COLUMN_NAME_TITLE,
                AssetDbHelper.COLUMN_NAME_DESCRIPTION,
                AssetDbHelper.COLUMN_NAME_SERIALNUMBER,
                AssetDbHelper.COLUMN_NAME_MANUFACTURER,
                AssetDbHelper.COLUMN_NAME_MODELNUMBER,
                AssetDbHelper.COLUMN_NAME_LATITUDE,
                AssetDbHelper.COLUMN_NAME_LONGITUDE
        };
        Cursor cursor = db.query(AssetDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<Asset> assets = new ArrayList<>();
        while (cursor.moveToNext()){
            String Id = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_ID));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_IMAGE));
            String Title = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_TITLE));
            String Description = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_DESCRIPTION));
            String SerialNumber = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_SERIALNUMBER));
            String Manufacturer = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_MANUFACTURER));
            String ModelNumber = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_MODELNUMBER));
            String Latitude = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_LATITUDE));
            String Longitude = cursor.getString(cursor.getColumnIndexOrThrow(AssetDbHelper.COLUMN_NAME_LONGITUDE));

            Bitmap AssetImage = null;
            if (imageBytes != null){
                AssetImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }

            Asset asset = new Asset(Id, AssetImage, Title, Description, SerialNumber, Manufacturer, ModelNumber, new Asset.Location(Double.parseDouble(Latitude), Double.parseDouble(Longitude)));
            assets.add(asset);
        }
        cursor.close();

        return assets;
    }

    public int delete (String Id) {
        SQLiteDatabase db = assetDbHelper.getWritableDatabase();

        if (!doesAssetExist(db, Id))
            return -1;

        String[] whereArgs = { Id };

        return db.delete(AssetDbHelper.TABLE_NAME, AssetDbHelper.COLUMN_NAME_ID + " = ?", whereArgs);
    }

    public class AssetDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "Assets.db";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "Asset";
        private static final String COLUMN_NAME_ID = "_Id";
        private static final String COLUMN_NAME_IMAGE = "Image";
        private static final String COLUMN_NAME_TITLE = "Title";
        private static final String COLUMN_NAME_DESCRIPTION = "Description";
        private static final String COLUMN_NAME_SERIALNUMBER = "SerialNumber";
        private static final String COLUMN_NAME_MANUFACTURER = "Manufacturer";
        private static final String COLUMN_NAME_MODELNUMBER = "ModelNumber";
        private static final String COLUMN_NAME_LATITUDE = "Latitude";
        private static final String COLUMN_NAME_LONGITUDE = "Longitude";

        private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_ID + " TEXT PRIMARY KEY," + COLUMN_NAME_IMAGE + " BLOB," + COLUMN_NAME_TITLE + " TEXT," + COLUMN_NAME_DESCRIPTION + " TEXT," + COLUMN_NAME_SERIALNUMBER + " TEXT," + COLUMN_NAME_MANUFACTURER + " TEXT," + COLUMN_NAME_MODELNUMBER + " TEXT," + COLUMN_NAME_LATITUDE + " REAL," + COLUMN_NAME_LONGITUDE + " REAL)";

        private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public AssetDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(SQL_CREATE_ENTRIES);
            } catch (Exception e) {
                Log.e("Create Database", e.getMessage());
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(SQL_DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
                Log.e("Update Database", e.getMessage());
            }
        }
    }
}
