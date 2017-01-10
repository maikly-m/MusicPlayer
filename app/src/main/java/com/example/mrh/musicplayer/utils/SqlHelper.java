package com.example.mrh.musicplayer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mrh.musicplayer.domain.DBHelper;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;

import java.util.ArrayList;
import java.util.HashMap;

public class SqlHelper {
	public static String DB_NAME = "musicPlayer";
	private Context context;
	private final SQLiteDatabase db;

	public SqlHelper (Context context) {
		this.context = context;
		db = new DBHelper(context).getWritableDatabase();
	}

	/**
	 * 关闭数据库，最后处理
	 */
	public void closeDb () {
		db.close();
	}
	public void Insert(String table, ContentValues values){
		try{
			db.insert(table, null, values);
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	public void CreatePlayTable(String table){
		String sql="CREATE TABLE " + table + " ( ID integer primary key autoincrement" +
				", listName vachar (50)";
		try{
			db.execSQL(sql);
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	public void Update(String table, ContentValues values, String
			whereClause, String[] whereArgs){
		try{
			db.update(table, values, whereClause, whereArgs);
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	public ArrayList<MusicList> getList (String table, String selection, String[] selectionArgs){
		ArrayList<MusicList> list = new ArrayList<>();
		try{
			Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, null);
			if (cursor != null){
				if (cursor.moveToFirst()){
					do{
						MusicList musicList = new MusicList();
						musicList.setListName(cursor.getString(1));
						list.add(musicList);
					}
					while (cursor.moveToNext());
				}
				cursor.close();
			}
		}
		catch(Exception e){
			e.getStackTrace();
		}
		return list;
	}
	public ArrayList<MusicInfo> getMusicInfo (String table, String
			selection, String[]selectionArgs){
		ArrayList<MusicInfo> list = new ArrayList<>();
		try{
			Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, null);
			if (cursor != null){
				if (cursor.moveToFirst()){
					do{
						MusicInfo musicInfo = new MusicInfo();
						musicInfo.set_ID(cursor.getString(1));
						musicInfo.setDISPLAY_NAME(cursor.getString(2));
						musicInfo.setTITLE(cursor.getString(3));
						musicInfo.setDURATION(cursor.getString(4));
						musicInfo.setARTIST(cursor.getString(5));
						musicInfo.setALBUM(cursor.getString(6));
						musicInfo.setYEAR(cursor.getString(7));
						musicInfo.setMIME_TYPE(cursor.getString(8));
						musicInfo.setSIZE(cursor.getString(9));
						musicInfo.setDATA(cursor.getString(10));
						musicInfo.setIMAGE(cursor.getString(11));
						musicInfo.setLYRIC(cursor.getString(12));
						list.add(musicInfo);
					}
					while (cursor.moveToNext());
				}
				cursor.close();
			}
		}
		catch(Exception e){
			e.getStackTrace();
		}
		return list;
	}

	public void Delete(String table, String whereClause, String[] whereArgs){
		try{
			db.delete(table, whereClause, whereArgs);
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	public void DeleteTable(String table){
		String sql="drop table " + table;
		try{
			db.execSQL(sql);
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}

	public void setList (String table, ContentValues cv){
		try{
			db.insert(table, null, cv);
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	//创建音乐媒体表
	public void CreateMusicTable(String table){
		DebugUtils.log_d("TAG", "CCCCCCC");
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
	}
	public void deleteMusicInfo(String table, String whereClause, String[]
			whereArgs){
		Delete(table, whereClause, whereArgs);
	}

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
