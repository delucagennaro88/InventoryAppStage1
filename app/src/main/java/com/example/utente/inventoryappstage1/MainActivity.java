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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new DbDeposito(this);
        showInfo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        showInfo();
    }

    private void showInfo() {

        DbDeposito mDbHelper = new DbDeposito(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Prodotto.TABELLA, null);
        try {

            TextView displayView = (TextView) findViewById(R.id.item_text);
            displayView.setText("Rows in Database: " + cursor.getCount());
        } finally {
            cursor.close();
        }
    }
}
