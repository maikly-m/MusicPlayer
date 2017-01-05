package com.example.mrh.musicplayer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mrh.musicplayer.domain.DBHelper;

import java.util.HashMap;

public class SqlHelper {
	public static String DB_NAME = "musicPlayer";

	public static SQLiteDatabase getSQLiteDatabase(Context context){
		DBHelper dbHelper = new DBHelper(context);
		return dbHelper.getWritableDatabase();
	}
	public static void Insert(Context context, String table, ContentValues values){
		SQLiteDatabase db= getSQLiteDatabase(context);
		try{
			db.insert(table, null, values);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
		}
	//创建音乐媒体表
	public static void CreateMusicTable(Context context,String table){
		DebugUtils.log_d("TAG", "CCCCCCC");
		SQLiteDatabase db= getSQLiteDatabase(context);
		String sql="CREATE TABLE " + table + " ( ID integer primary key autoincrement" +
				", _ID int (20), DISPLAY_NAME varchar (50)," +
				"TITLE varchar (50), DURATION varchar (50), ARTIST varchar (50), ALBUM varchar " +
				"(50)," +
				" YEAR varchar (50), MIME_TYPE varchar (50), SIZE varchar (50), DATA varchar (50)" +
				", IMAGE vachar (50), LYRIC vachar (50))";
		try{
			db.execSQL(sql);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}

	//创建播放列表
	public static void CreatePlayTable(Context context,String table){
		SQLiteDatabase db= getSQLiteDatabase(context);
		String sql="CREATE TABLE " + table + " ( ID integer primary key autoincrement" +
				", listName vachar (50)";
		try{
			db.execSQL(sql);
		}
		catch(Exception e){
			e.getStackTrace();
		}
		db.close();
	}
	public static void Update(Context context,String table, ContentValues values, String
			whereClause, String[] whereArgs){
		SQLiteDatabase db= getSQLiteDatabase(context);
		try{
			db.update(table, values, whereClause, whereArgs); 
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public static HashMap<String, Object> Query(Context context,String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy, String having, String
			orderBy){
		SQLiteDatabase db=getSQLiteDatabase(context);
		Cursor cursor = null ;
		try{
			cursor=db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("cursor", cursor);
		hashMap.put("db", db);
		return hashMap;
		
	}
	
	public static void Delete(Context context,String table, String whereClause, String[] whereArgs){
		SQLiteDatabase db= getSQLiteDatabase(context);
		try{
			db.delete(table, whereClause, whereArgs);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
	
	public static void DeleteTable(Context context,String table){
		SQLiteDatabase db= getSQLiteDatabase(context);
		String sql="drop table " + table;
			try{
				db.execSQL(sql);
			}
			catch(Exception e){
				e.getStackTrace();
			}
		db.close();
	}
}
