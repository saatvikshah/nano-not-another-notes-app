package com.example.nano.supportclasses;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ProfilesDBHelper {

	public static final String ID = "id";
	public static final String NAME = "notename";
	public static final String CONTENT = "notecontent";
	public static final String BACKGROUND = "notebackground";
	public static final String DOB = "dateofbirth";
	public static final String BPATH = "bpath";
	public static final String PASSWORD_ENABLED = "passwordenabled";
	public static final String PASSWORD = "password";
	public static final String PROFILE = "profile";
	public static final String ATTACHMENT = "attachment";

	private static final String DATABASE_NAME = "notesDatabase";
	private static final String DATABASE_TABLE = "profiletable";
	private static final int DATABASE_VERSION = 1;

	String profileName;
	private DbHelper ourHelper;
	private Context ourContext;
	private SQLiteDatabase ourDatabase;

	public ProfilesDBHelper(Context c, String string) {
		ourContext = c;
		profileName = string;
	}

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" + ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME
					+ " TEXT DEFAULT 'Untitled'," + CONTENT + " TEXT," + BACKGROUND
					+ " BLOB," + DOB + " TEXT," + BPATH + " TEXT,"
					+ PASSWORD_ENABLED + " INTEGER," + PASSWORD + " TEXT,"
					+ PROFILE + " TEXT," + ATTACHMENT + " TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public ProfilesDBHelper open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createNote(String name, String content, String dob,
			String bpath, Bitmap bg, int pEnabled, String password,
			String attachment) {
		// TODO Auto-generated method stub
		byte[] background;
		if (bg != null) {
			background = convertBitmapToByte(bg);
		} else {
			background = null;
		}
		ContentValues cv = new ContentValues();
		cv.put(NAME, name);
		cv.put(CONTENT, content);
		// cv.put(PIC, pic);
		cv.put(BACKGROUND, background);
		cv.put(DOB, dob);
		cv.put(BPATH, bpath);
		// cv.put(PPATH, ppath);
		cv.put(PASSWORD_ENABLED, pEnabled);
		cv.put(PASSWORD, password);
		cv.put(PROFILE, profileName);
		cv.put(ATTACHMENT, attachment);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public long createNoteWithoutBack(String name, String content, String dob,
			int pEnabled, String password, String attachment) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(NAME, name);
		cv.put(CONTENT, content);
		cv.put(DOB, dob);
		cv.put(PASSWORD_ENABLED, pEnabled);
		cv.put(PASSWORD, password);
		cv.put(PROFILE, profileName);
		cv.put(ATTACHMENT, attachment);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public String[] getNoteNames() {
		// TODO Auto-generated method stub
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(NAME));
			i++;
		}
		return noteNames;
	}

	public String[] getNotePasswords() {
		// TODO Auto-generated method stub
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		String[] passwordz = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			passwordz[i] = c.getString(c.getColumnIndex(PASSWORD));
			i++;
		}
		return passwordz;
	}

	public int[] getNotePasswordsEnabled() {
		// TODO Auto-generated method stub
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		int[] passOn = new int[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			passOn[i] = c.getInt(c.getColumnIndex(PASSWORD_ENABLED));
			i++;
		}
		return passOn;
	}

	public String[] getNoteBackgroundPaths() {
		// TODO Auto-generated method stub
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		String[] noteBackPaths = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteBackPaths[i] = c.getString(c.getColumnIndex(BPATH));
			i++;
		}
		return noteBackPaths;
	}

	public String[] getNoteContents() {
		// TODO Auto-generated method stub
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(CONTENT));
			i++;
		}
		return noteNames;
	}

	public Bitmap[] getNoteBackgrounds() {
		// TODO Auto-generated method stub
		int i = 0;
		byte[] pic;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		Bitmap[] noteBackground = new Bitmap[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			pic = c.getBlob(c.getColumnIndex(BACKGROUND));
			noteBackground[i] = BitmapFactory.decodeByteArray(pic, 0,
					pic.length);
			i++;
		}
		return noteBackground;
	}

	public String[] getDOB() {
		// TODO Auto-generated method stub
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		String[] dates = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			dates[i] = c.getString(c.getColumnIndex(DOB));
			i++;
		}
		return dates;
	}

	public String[] getAttatchments() {
		// TODO Auto-generated method stub
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, PROFILE + "=?",
				new String[] { profileName }, null, null, null);
		String[] dates = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			dates[i] = c.getString(c.getColumnIndex(ATTACHMENT));
			i++;
		}
		return dates;
	}

	public String[] getAllNoteNames() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(NAME));
			i++;
		}
		return noteNames;
	}

	public String[] getAllNoteContents() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(CONTENT));
			i++;
		}
		return noteNames;
	}

	public String[] getAllNoteDOBs() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(DOB));
			i++;
		}
		return noteNames;
	}

	public String[] getAllNoteProfiles() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(PROFILE));
			i++;
		}
		return noteNames;
	}

	public String[] getAllNoteBackgroundPaths() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(BPATH));
			i++;
		}
		return noteNames;
	}

	public String[] getAllNotePasswords() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(PASSWORD));
			i++;
		}
		return noteNames;
	}

	public String[] getAllAttachments() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String[] noteNames = new String[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getString(c.getColumnIndex(ATTACHMENT));
			i++;
		}
		return noteNames;
	}

	public int[] getAllNotePassOn() {
		int i = 0;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		int[] noteNames = new int[c.getCount()];
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			noteNames[i] = c.getInt(c.getColumnIndex(PASSWORD_ENABLED));
			i++;
		}
		return noteNames;
	}

	public void changeNoteProfile(String noteName, String destinationProfile) {
		ContentValues cv = new ContentValues();
		cv.put(PROFILE, destinationProfile);
		ourDatabase.update(DATABASE_TABLE, cv, NAME + " = ?" + " AND "
				+ PROFILE + "=?", new String[] { noteName, profileName });
	}

	public void updateBackgroundPath(String noteName, String newPath) {
		ContentValues cv = new ContentValues();
		cv.put(BPATH, newPath);
		ourDatabase.update(DATABASE_TABLE, cv, NAME + " =?" + " AND " + PROFILE
				+ "=?", new String[] { noteName, profileName });
	}

	public void updateEntryByName(String oldName, String newName,
			String content, String dob, String bpath, Bitmap bg, int pEnabled,
			String password, String attachment) {
		// TODO Auto-generated method stub
		int i;
		byte[] background;
		if (bg != null) {
			background = convertBitmapToByte(bg);
		} else {
			background = null;
		}
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, NAME + "=?"
				+ " AND " + PROFILE + "=?",
				new String[] { oldName, profileName }, null, null, null);
		c.moveToFirst();
		i = c.getInt(c.getColumnIndex(ID));
		ContentValues cv = new ContentValues();
		cv.put(NAME, newName);
		cv.put(CONTENT, content);
		cv.put(BACKGROUND, background);
		cv.put(DOB, dob);
		cv.put(BPATH, bpath);
		cv.put(PASSWORD_ENABLED, pEnabled);
		cv.put(PASSWORD, password);
		cv.put(PROFILE, profileName);
		cv.put(ATTACHMENT, attachment);
		ourDatabase.update(DATABASE_TABLE, cv, ID + "=" + i + " AND " + PROFILE
				+ "=?", new String[] { profileName });
	}

	public void updateNotewithoutBack(String oldName, String newName,
			String content, String dob, int pEnabled, String password,
			String attachment) {
		// TODO Auto-generated method stub
		int i;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, NAME + "=?"
				+ " AND " + PROFILE + "=?",
				new String[] { oldName, profileName }, null, null, null);
		c.moveToFirst();
		i = c.getInt(c.getColumnIndex(ID));
		ContentValues cv = new ContentValues();
		cv.put(NAME, newName);
		cv.put(CONTENT, content);
		cv.put(DOB, dob);
		cv.put(PASSWORD_ENABLED, pEnabled);
		cv.put(PASSWORD, password);
		cv.put(PROFILE, profileName);
		cv.put(ATTACHMENT, attachment);
		ourDatabase.update(DATABASE_TABLE, cv, ID + "=" + i + " AND " + PROFILE
				+ "=?", new String[] { profileName });
	}

	public void updateEntryByContent(String oldContent, String name,
			String newContent, byte[] pic, byte[] background, String dob,
			String bpath, String ppath, int pEnabled, String password,
			String attachment) {
		// TODO Auto-generated method stub
		int i;
		String columns[] = { ID, NAME, CONTENT, BACKGROUND, DOB, BPATH,
				PASSWORD_ENABLED, PASSWORD, PROFILE, ATTACHMENT };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, CONTENT + "=?"
				+ " AND " + PROFILE + "=?", new String[] { oldContent,
				profileName }, null, null, null);
		c.moveToFirst();
		i = c.getInt(c.getColumnIndex(ID));
		ContentValues cv = new ContentValues();
		cv.put(NAME, name);
		cv.put(CONTENT, newContent);
		cv.put(BACKGROUND, background);
		cv.put(DOB, dob);
		cv.put(BPATH, bpath);
		cv.put(PASSWORD_ENABLED, pEnabled);
		cv.put(PASSWORD, password);
		cv.put(PROFILE, profileName);
		cv.put(ATTACHMENT, attachment);
		ourDatabase.update(DATABASE_TABLE, cv, ID + "=" + i + " AND " + PROFILE
				+ "=?", new String[] { profileName });
	}

	public void deleteEntry(String name) {
		ourDatabase.delete(DATABASE_TABLE, NAME + " = ?" + " AND " + PROFILE
				+ "=?", new String[] { name, profileName });
	}

	public void deleteProfile() {
		ourDatabase.delete(DATABASE_TABLE, PROFILE + "=?",
				new String[] { profileName });
	}

	private byte[] convertBitmapToByte(Bitmap b) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] img = bos.toByteArray();
		return img;
	}
}
