package com.example.cymas.databuruh;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cymas.databuruh.domain.Buruh;

/**
 * Created by cymas on 28/10/15.
 */
public class DBAdapter extends SQLiteOpenHelper {
    private static final String DB_NAME = 'databuruh';
    private static final String TABLE_NAME = 'buruh';
    private static final String COL_ID = 'id';
    private static final String COL_NAMA = 'nama';
    private static final String COL_ALAMAT = 'jabatan';
    private static final String COL_JENISKELAMIN = 'jenisKelamin';
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME + ";";

    private SQLiteDatabase sqliteDatabase = null;

    public DBAdapter(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL(DROP_TABLE);
    }

    public void openDB() {
        if (sqliteDatabase == null) {
            sqliteDatabase = getWritableDatabase();
        }
    }

    public void closeDB() {
        if (sqliteDatabase != null) {
            if (sqliteDatabase.isOpen()) {
                sqliteDatabase.close();
            }
        }
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COL_NAMA + " TEXT," + COL_ALAMAT + " TEXT,"
                + COL_JENISKELAMIN + " TEXT);");
    }

    public void updateSiswa(Buruh buruh) {
        sqliteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAMA, buruh.getNama());
        cv.put(COL_ALAMAT, buruh.getAlamat());
        cv.put(COL_JENISKELAMIN, buruh.getJenisKelamin());
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[] { buruh.getId() };
        sqliteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void save(Buruh buruh) {
        sqliteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAMA, buruh.getNama());
        contentValues.put(COL_ALAMAT, buruh.getAlamat());
        contentValues.put(COL_JENISKELAMIN, buruh.getJenisKelamin());

        sqliteDatabase.insertWithOnConflict(TABLE_NAME, null,
                contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        sqliteDatabase.close();
    }

    public void delete(Siswa siswa) {
        sqliteDatabase = getWritableDatabase();
        String whereClause = COL_ID + "==?";
        String[] whereArgs = new String[] { String.valueOf(siswa.getId()) };
        sqliteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void deleteAll() {
        sqliteDatabase = getWritableDatabase();
        sqliteDatabase.delete(TABLE_NAME, null, null);
        sqliteDatabase.close();
    }

    public List<Siswa> getAllSiswa() {
        sqliteDatabase = getWritableDatabase();

        Cursor cursor = this.sqliteDatabase.query(TABLE_NAME, new String[] {
                COL_ID, COL_NAME, COL_KELAS }, null, null, null, null, null);
        List<Siswa> siswas = new ArrayList<Siswa>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Siswa siswa = new Siswa();
                siswa.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                siswa.setNama(cursor.getString(cursor
                        .getColumnIndex(COL_NAME)));
                siswa.setKelas(cursor.getString(cursor
                        .getColumnIndex(COL_KELAS)));
                siswas.add(siswa);
            }
            sqliteDatabase.close();
            return siswas;
        } else {
            sqliteDatabase.close();
            return new ArrayList<Siswa>();
        }
    }
}
