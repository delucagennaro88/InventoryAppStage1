package com.example.utente.inventoryappstage1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.utente.inventoryappstage1.data.DbDeposito;
import com.example.utente.inventoryappstage1.data.Deposito.Prodotto;

public class MainActivity extends AppCompatActivity {

    private DbDeposito mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new DbDeposito(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Prodotto._ID,
                Prodotto.NOME_COLONNA,
                Prodotto.PREZZO_COLONNA,
                Prodotto.QUANTITA,
                Prodotto.VENDITORE,
                Prodotto.TELEFONO
        };
        Cursor cursor = db.query(
                Prodotto.TABELLA,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = findViewById(R.id.item_text);

        try {
            displayView.setText("Inventory contains : " + cursor.getCount() + " products.\n\n");

            displayView.append(
                    Prodotto._ID + " | " +
                            Prodotto.NOME_COLONNA + " | " +
                            Prodotto.PREZZO_COLONNA + " | " +
                            Prodotto.QUANTITA + " | " +
                            Prodotto.VENDITORE + " | " +
                            Prodotto.TELEFONO + "\n");

            int idColumnIndex = cursor.getColumnIndex(Prodotto._ID);
            int nameColumnIndex = cursor.getColumnIndex(Prodotto.NOME_COLONNA);
            int priceColumnIndex = cursor.getColumnIndex(Prodotto.PREZZO_COLONNA);
            int quantityColumnIndex = cursor.getColumnIndex(Prodotto.QUANTITA);
            int supplierNameColumnIndex = cursor.getColumnIndex(Prodotto.VENDITORE);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(Prodotto.TELEFONO);
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }

        } finally {
            cursor.close();
        }
    }
}
