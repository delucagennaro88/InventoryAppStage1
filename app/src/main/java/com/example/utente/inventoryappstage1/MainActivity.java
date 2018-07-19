package com.example.utente.inventoryappstage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utente.inventoryappstage1.data.Deposito.DepositoEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;

    ArtCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        ListView inventoryListView = findViewById(R.id.list);

        TextView emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        mCursorAdapter = new ArtCursorAdapter(this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                Uri currentProdcuttUri = ContentUris.withAppendedId(DepositoEntry.CONTENT_URI, id);
                intent.setData(currentProdcuttUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);

    }

    public void productSaleCount(int itemID, int itemQuantity) {
        itemQuantity = itemQuantity - 1;
        if (itemQuantity >= 0) {
            ContentValues values = new ContentValues();
            values.put(DepositoEntry.QUANTITA, itemQuantity);
            Uri updateUri = ContentUris.withAppendedId(DepositoEntry.CONTENT_URI, itemID);
            int rowsAffected = getContentResolver().update(updateUri, values, null, null);
            Toast.makeText(this, "Quantità cambiata", Toast.LENGTH_SHORT).show();

            Log.d("Log msg", "rowsAffected " + rowsAffected + " - itemID " + itemID + " - quantity " + itemQuantity + " , hai chiamato decreaseCount.");
        } else {
            Toast.makeText(this, "Mi dispiace, ritenta", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                DepositoEntry._ID,
                DepositoEntry.NOME,
                DepositoEntry.PREZZO,
                DepositoEntry.QUANTITA,
                DepositoEntry.VENDITORE,
                DepositoEntry.TELEFONO
        };

        return new CursorLoader(this,
                DepositoEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(DepositoEntry.CONTENT_URI, null, null);
        Toast.makeText(this, rowsDeleted + " " + getString(R.string.deleted_all), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.erase_all:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_messages);
        builder.setPositiveButton(R.string.erase, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllProducts();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
