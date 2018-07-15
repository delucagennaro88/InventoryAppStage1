package com.example.utente.inventoryappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.utente.inventoryappstage1.data.Deposito.Prodotto;

public class DbDeposito extends SQLiteOpenHelper {
    public static final String LOG_TAG = DbDeposito.class.getSimpleName();

    private static final String DATABASE_NAME = "shelter.db";

    private static final int DATABASE_VERSION = 1;

    public DbDeposito(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + Prodotto.TABELLA + " ("
                + Prodotto._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Prodotto.NOME_COLONNA + " TEXT NOT NULL, "
                + Prodotto.PREZZO_COLONNA + " INTEGER NOT NULL, "
                + Prodotto.QUANTITA + " INTEGER NOT NULL DEFAULT 0, "
                + Prodotto.VENDITORE + " TEXT NOT NULL, "
                + Prodotto.TELEFONO + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
