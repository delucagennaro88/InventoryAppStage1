package com.example.utente.inventoryappstage1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utente.inventoryappstage1.data.Deposito.Prodotto;
import com.example.utente.inventoryappstage1.data.DbDeposito;

public class EditActivity extends AppCompatActivity {

    private EditText mEditName;
    private EditText mEditPrice;
    private EditText mEditQuantity;
    private EditText mEditMarket;
    private EditText mEditNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mEditName = (EditText) findViewById(R.id.change_name);
        mEditPrice = (EditText) findViewById(R.id.change_price);
        mEditQuantity = (EditText) findViewById(R.id.change_quantity);
        mEditMarket = (EditText) findViewById(R.id.change_sname);
        mEditNumber = (EditText) findViewById(R.id.change_phone);
    }

    private void insertItem() {

        String name_str = mEditName.getText().toString().trim();
        String price_str = mEditPrice.getText().toString().trim();
        int price_nr = Integer.parseInt(price_str);
        String quantity_str = mEditQuantity.getText().toString().trim();
        int quantity_nr = Integer.parseInt(quantity_str);
        String market_str = mEditMarket.getText().toString().trim();
        String phone_str = mEditNumber.getText().toString().trim();
        int phone_nr = Integer.parseInt(phone_str);

        DbDeposito mDbHelper = new DbDeposito(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Prodotto.NOME_COLONNA, name_str);
        values.put(Prodotto.PREZZO_COLONNA, price_nr);
        values.put(Prodotto.QUANTITA, quantity_nr);
        values.put(Prodotto.VENDITORE, market_str);
        values.put(Prodotto.TELEFONO, phone_nr);
        long newRowId = db.insert(Prodotto.TABELLA, null, values);

        if (newRowId == -1) {

            Toast.makeText(this, "Errore nel salvare", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Prodotto salvato con Id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save:

                insertItem();

                finish();
                return true;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
