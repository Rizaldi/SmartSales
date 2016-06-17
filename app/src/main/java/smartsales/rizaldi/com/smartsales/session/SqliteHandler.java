package smartsales.rizaldi.com.smartsales.session;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by Toshiba-PC on 4/14/2016.
 */
public class SqliteHandler extends SQLiteOpenHelper {
    private static final String TAG = SqliteHandler.class.getSimpleName();
    private static final int Database_Version = 1;

    private static final String Database_Name = "smartsales";
    private static final String TABLE_USER = "user";

    public SqliteHandler(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + Database_Name + ".db", null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_login_tbl="CREATE TABLE " + TABLE_USER+"(id TEXT PRIMARY KEY,user_id TEXT,organization_id TEXT,name TEXT," +
                "username TEXT,group_id TEXT,group_name TEXT,employee_status TEXT,employee_id TEXT,position_name TEXT," +
                "position_status TEXT,warehouse_id TEXT)";
        db.execSQL(create_login_tbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+TABLE_USER);
        onCreate(db);
    }
    public void addUser(String user_id,String organitaion_id,String name,String username,String group_id,String group_name,
                        String employee_status,String employee_id,String position_name,String position_status,String warehouse_id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("user_id",user_id);
        cv.put("organization_id",organitaion_id);
        cv.put("name",name);
        cv.put("username",username);
        cv.put("group_id", group_id);
        cv.put("group_name", group_name);
        cv.put("employee_status", employee_status);
        cv.put("employee_id", employee_id);
        cv.put("position_name", position_name);
        cv.put("position_status", position_status);
        cv.put("warehouse_id", warehouse_id);
        db.insert(TABLE_USER, null, cv);
        db.close();
    }

    public HashMap<String,String> getUserDetails(){
        HashMap<String,String> user=new HashMap<String,String>();
        String selectQuery="SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        cursor.moveToFirst();
        if(cursor.getCount()>0){
            user.put("user_id",cursor.getString(1));
            user.put("organization_id",cursor.getString(2));
            user.put("name",cursor.getString(3));
            user.put("username",cursor.getString(4));
            user.put("group_id", cursor.getString(5));
            user.put("group_name", cursor.getString(6));
            user.put("employee_status", cursor.getString(7));
            user.put("employee_id", cursor.getString(8));
            user.put("position_name", cursor.getString(9));
            user.put("position_status", cursor.getString(10));
            user.put("warehouse_id", cursor.getString(11));
        }
        cursor.close();
        db.close();
        return user;
    }
    public void deleteUser(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
    }
}
