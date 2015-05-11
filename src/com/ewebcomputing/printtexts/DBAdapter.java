package com.ewebcomputing.printtexts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.HashMap;

public class DBAdapter {

    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "balance";
    private static final int DATABASE_VERSION = 1;
    private static final String BALANCE_CREATE =
            "CREATE TABLE balance ( id integer default NULL, openbal double default NULL, curbal double default NULL, last_updated timestamp NOT NULL default CURRENT_TIMESTAMP)";
    private static final String INV_CREATE =
            "CREATE TABLE inventory ( item varchar(128) NOT NULL, qty integer default NULL, last_updated timestamp NOT NULL default CURRENT_TIMESTAMP, PRIMARY KEY  (item))";
    private static final String TXN_CREATE =
            "CREATE TABLE txn ( id integer primary key autoincrement, amount double default NULL, created timestamp NOT NULL default CURRENT_TIMESTAMP, type integer default NULL, paymentType integer default NULL, item varchar(128) default NULL, qty integer default NULL, customer varchar(128) default NULL, FOREIGN KEY(item) references item (item))";
    private static final String BALANCE_INSERT1 =
            "insert into balance values(1,0,0,current_timestamp)";
    private static final String BALANCE_INSERT2 =
            "insert into balance values(2,0,0,current_timestamp)";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        this.open();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(BALANCE_CREATE);
            db.execSQL(INV_CREATE);
            db.execSQL(TXN_CREATE);
            db.execSQL(BALANCE_INSERT1);
            db.execSQL(BALANCE_INSERT2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                int newVersion) {
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    public void insertOrUpdate(Txn txn, boolean add) {
        int cnt = getItemCount(txn.item);

        if (cnt == 0) {
            insertItem(txn);
        } else {
            updateInventory(txn, cnt, add);
        }
    }
    //---insert a title into the database---

    public void insertTxn(Txn txn) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("amount", txn.amount);
        initialValues.put("type", txn.txnType);
        initialValues.put("paymenttype", txn.paymentType);
        initialValues.put("item", txn.item);
        initialValues.put("qty", txn.qty);

        initialValues.put("customer", txn.cust);

        try {
            db.insertOrThrow("Txn", null, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateInventory(Txn txn, int cnt, boolean add) {
        int qty = 0;
        if (add) {
            qty = cnt + txn.qty;
        } else {
            qty = cnt - txn.qty;
        }
        ContentValues args = new ContentValues();
        args.put("item", txn.item);
        args.put("qty", qty);
        return db.update("inventory", args,
                "item='" + txn.item + "'", null) > 0;
    }

    public boolean updateBalance(Txn txn, boolean credit) {
        ContentValues args = new ContentValues();
        if (credit) {
            args.put("curbal", txn.amount + getCurrentBalance(txn));
        } else {
            args.put("curbal", getCurrentBalance(txn) - txn.amount);
        }
        return db.update("balance", args,
                "id='" + txn.paymentType + "'", null) > 0;
    }

    public long insertItem(Txn txn) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("item", txn.item);
        initialValues.put("qty", txn.qty);
        return db.insert("inventory", null, initialValues);
    }

    public int getItemCount(String item) {

        Cursor c = db.query("inventory", new String[]{"item,qty"}, "item='" + item + "'", null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToNext();

            return c.getInt(1);
        }
        return 0;
    }

    public double getCurrentBalance(Txn txn) {

        Cursor c = db.query("balance", new String[]{"curbal"}, "id='" + txn.paymentType + "'", null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            // System.out.println("item "+c.getString(0));
            //System.out.println("qty "+c.getInt(1));
            return c.getDouble(0);
        }
        return 0;
    }

    public double[] getCurrentBalances() {
        Cursor c = db.query("balance", new String[]{"curbal"}, null, null, null, null, null);
        double[] balances = {0, 0};
        c.moveToNext();
        balances[0] = c.getDouble(0);
        c.moveToNext();
        balances[1] = c.getDouble(0);
        return balances;
    }

    public Cursor getAllTxn() {
        return db.query("txn", new String[]{
                        "item", "qty", "amount", "type", "created"},
                null,
                null,
                null,
                null,
                "created desc",
                "500");
    }

    public Cursor getInventory() {
        return db.query("inventory", new String[]{
                    "item", "qty"},
                null,
                null,
                null,
                null,
                "qty desc",
                "500");
    }

    public HashMap<String,Double> getCreditorsOrDebtors(boolean isCredit) {
        int t1 = isCredit ? 2 : 3;
        int t2 = isCredit ? 3 : 2;
        Cursor c = db.rawQuery("SELECT sum( amount ) amt, customer AS cust " +
                "FROM txn WHERE  TYPE  = " + t1 + " GROUP  BY customer ORDER  BY amt DESC  ", null);
        HashMap<String, Double> vals = new HashMap();

        while (c.moveToNext()) {
            double amt = c.getDouble(0);
            String cust = c.getString(1);
            vals.put(cust, amt);
        }
        c.close();
        c = db.rawQuery("SELECT sum( amount ) amt, customer AS cust " +
                "FROM txn WHERE  TYPE  = " + t2 + " GROUP  BY customer ORDER  BY amt DESC", null);
        while (c.moveToNext()) {
            double amt = c.getDouble(0);
            String cust = c.getString(1);
            if (!vals.containsKey(cust)) {
                vals.put(cust, (double) 0);
            }
            double amt1 = (double) vals.get(cust);
            vals.put(cust, amt);
            if (amt1 - amt > 0) {
                vals.put(cust, amt1 - amt);
            } else {
                vals.remove(cust);
            }
        }
        c.close();
        return vals;
    }
}