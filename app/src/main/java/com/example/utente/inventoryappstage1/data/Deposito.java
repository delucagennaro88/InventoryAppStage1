package com.example.utente.inventoryappstage1.data;

import android.provider.BaseColumns;

public final class Deposito {
    private Deposito() {
    }

    public static abstract class Prodotto implements BaseColumns {

        public static final String TABELLA = "prodotti";
        public static final String _ID = BaseColumns._ID;
        public static final String NOME_COLONNA = "nome";
        public static final String PREZZO_COLONNA = "prezzo";
        public static final String QUANTITA = "quantità";
        public static final String VENDITORE = "rivenditore";
        public static final String TELEFONO = "telefono";
    }
}