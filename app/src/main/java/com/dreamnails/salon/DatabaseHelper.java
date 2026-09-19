package com.dreamnails.salon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB = "dream_nails.db";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE customers(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,phone TEXT,notes TEXT,created_at INTEGER)");
        db.execSQL("CREATE TABLE sales(id INTEGER PRIMARY KEY AUTOINCREMENT,customer_name TEXT,phone TEXT,service TEXT,price REAL,sale_date INTEGER,notes TEXT)");
        db.execSQL("CREATE TABLE bookings(id INTEGER PRIMARY KEY AUTOINCREMENT,customer_name TEXT,phone TEXT,service TEXT,price REAL,booking_time INTEGER,status TEXT,notes TEXT)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public long addCustomer(String name, String phone, String notes) {
        ContentValues v = new ContentValues();
        v.put("name", name); v.put("phone", phone); v.put("notes", notes); v.put("created_at", System.currentTimeMillis());
        return getWritableDatabase().insert("customers", null, v);
    }

    public long addSale(String name, String phone, String service, double price, String notes) {
        ContentValues v = new ContentValues();
        v.put("customer_name", name); v.put("phone", phone); v.put("service", service); v.put("price", price);
        v.put("sale_date", System.currentTimeMillis()); v.put("notes", notes);
        return getWritableDatabase().insert("sales", null, v);
    }

    public long addBooking(String name, String phone, String service, double price, long time, String notes) {
        ContentValues v = new ContentValues();
        v.put("customer_name", name); v.put("phone", phone); v.put("service", service); v.put("price", price);
        v.put("booking_time", time); v.put("status", "Confirmed"); v.put("notes", notes);
        return getWritableDatabase().insert("bookings", null, v);
    }

    public Cursor customers() {
        return getReadableDatabase().rawQuery("SELECT id,name,phone,notes,created_at FROM customers ORDER BY created_at DESC", null);
    }

    public Cursor sales() {
        return getReadableDatabase().rawQuery("SELECT id,customer_name,phone,service,price,sale_date,notes FROM sales ORDER BY sale_date DESC", null);
    }

    public Cursor bookings() {
        return getReadableDatabase().rawQuery("SELECT id,customer_name,phone,service,price,booking_time,status,notes FROM bookings ORDER BY booking_time ASC", null);
    }

    public int customerCount() {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM customers", null)) { c.moveToFirst(); return c.getInt(0); }
    }

    public int bookingCountToday() {
        long now = System.currentTimeMillis();
        java.util.Calendar s = java.util.Calendar.getInstance();
        s.setTimeInMillis(now); s.set(java.util.Calendar.HOUR_OF_DAY,0); s.set(java.util.Calendar.MINUTE,0); s.set(java.util.Calendar.SECOND,0); s.set(java.util.Calendar.MILLISECOND,0);
        long start = s.getTimeInMillis(); long end = start + 86400000L;
        try (Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM bookings WHERE booking_time>=? AND booking_time<?", new String[]{String.valueOf(start),String.valueOf(end)})) {
            c.moveToFirst(); return c.getInt(0);
        }
    }

    public double salesToday() {
        java.util.Calendar s = java.util.Calendar.getInstance();
        s.set(java.util.Calendar.HOUR_OF_DAY,0); s.set(java.util.Calendar.MINUTE,0); s.set(java.util.Calendar.SECOND,0); s.set(java.util.Calendar.MILLISECOND,0);
        long start=s.getTimeInMillis(); long end=start+86400000L;
        try (Cursor c=getReadableDatabase().rawQuery("SELECT COALESCE(SUM(price),0) FROM sales WHERE sale_date>=? AND sale_date<?", new String[]{String.valueOf(start),String.valueOf(end)})) {
            c.moveToFirst(); return c.getDouble(0);
        }
    }

    public double salesMonth() {
        java.util.Calendar s=java.util.Calendar.getInstance();
        s.set(java.util.Calendar.DAY_OF_MONTH,1); s.set(java.util.Calendar.HOUR_OF_DAY,0); s.set(java.util.Calendar.MINUTE,0); s.set(java.util.Calendar.SECOND,0); s.set(java.util.Calendar.MILLISECOND,0);
        long start=s.getTimeInMillis();
        try (Cursor c=getReadableDatabase().rawQuery("SELECT COALESCE(SUM(price),0) FROM sales WHERE sale_date>=?", new String[]{String.valueOf(start)})) {
            c.moveToFirst(); return c.getDouble(0);
        }
    }
}
