package com.example.utente.inventoryappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.utente.inventoryappstage1.data.Deposito.DepositoEntry;

public class DbDeposito extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public DbDeposito(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + DepositoEntry.TABELLA + " ("
                + DepositoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DepositoEntry.NOME + " TEXT NOT NULL, "
                + DepositoEntry.PREZZO + " INTEGER NOT NULL, "
                + DepositoEntry.QUANTITA + " INTEGER NOT NULL, "
                + DepositoEntry.VENDITORE + " INTEGER NOT NULL DEFAULT 0, "
                + DepositoEntry.TELEFONO + " INTEGER );";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DepositoEntry.TABELLA);
    }
}
