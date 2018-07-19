package com.example.utente.inventoryappstage1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Deposito {

    public static final String CONTENT_AUTHORITY = "com.example.utente.inventoryappstage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY = "product";

    public final static class DepositoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public final static String TABELLA = "prodotti";

        public final static String _ID = BaseColumns._ID;
        public final static String NOME = "prodotto";
        public final static String PREZZO = "prezzo";
        public final static String QUANTITA = "quantità";
        public final static String VENDITORE = "venditore";
        public final static String TELEFONO = "telefono";

        public final static int SCONOSCIUTO = 0;
        public final static int EBAY = 1;
        public final static int IBS = 2;
        public final static int MAREMAGNUM = 3;

        public static boolean SellerValidity(int rivenditore) {
            return rivenditore == SCONOSCIUTO || rivenditore == EBAY || rivenditore == IBS || rivenditore == MAREMAGNUM;
        }
    }
}
