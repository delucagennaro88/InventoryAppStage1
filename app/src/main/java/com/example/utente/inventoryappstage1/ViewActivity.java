package com.example.utente.inventoryappstage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utente.inventoryappstage1.data.Deposito.DepositoEntry;

public class ViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private TextView mNameView;
    private TextView mPriceView;
    private TextView mQuantityView;
    private TextView mMarketView;
    private TextView mPhoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mNameView = findViewById(R.id.nome_prodotto);
        mPriceView = findViewById(R.id.prezzo_prodotto);
        mQuantityView = findViewById(R.id.quantita_prodotto);
        mMarketView = findViewById(R.id.market_prodotto);
        mPhoneView = findViewById(R.id.telefono_prodotto);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        if (mCurrentProductUri == null) {
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        Log.d("message", "onCreate ViewActivity");

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
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

            final int idIndex = cursor.getColumnIndex(DepositoEntry._ID);
            int nameIndex = cursor.getColumnIndex(DepositoEntry.NOME);
            int priceIndex = cursor.getColumnIndex(DepositoEntry.PREZZO);
            int quantityIndex = cursor.getColumnIndex(DepositoEntry.QUANTITA);
            int marketIndex = cursor.getColumnIndex(DepositoEntry.VENDITORE);
            int phoneIndex = cursor.getColumnIndex(DepositoEntry.TELEFONO);

            String currentName = cursor.getString(nameIndex);
            final int currentPrice = cursor.getInt(priceIndex);
            final int currentQuantity = cursor.getInt(quantityIndex);
            int currentSupplierName = cursor.getInt(marketIndex);
            final int currentSupplierPhone = cursor.getInt(phoneIndex);

            mNameView.setText(currentName);
            mPriceView.setText(Integer.toString(currentPrice));
            mQuantityView.setText(Integer.toString(currentQuantity));
            mPhoneView.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {
                case DepositoEntry.EBAY:
                    mMarketView.setText(getText(R.string.ebay_market));
                    break;
                case DepositoEntry.IBS:
                    mMarketView.setText(getText(R.string.ibs_market));
                    break;
                case DepositoEntry.MAREMAGNUM:
                    mMarketView.setText(getText(R.string.maremagnum_market));
                    break;
                default:
                    mMarketView.setText(getText(R.string.sconosciuto_market));
                    break;
            }

            Button productDecreaseButton = findViewById(R.id.less_button);
            productDecreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCount(idIndex, currentQuantity);
                }
            });

            Button productIncreaseButton = findViewById(R.id.plus_button);
            productIncreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCount(idIndex, currentQuantity);
                }
            });

            Button productDeleteButton = findViewById(R.id.erase_button);
            productDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });

            Button phoneButton = findViewById(R.id.tele_button);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = String.valueOf(currentSupplierPhone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void decreaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity - 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.change_quantity), Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , hai chiamato decreaseCount.");
        } else {
            Toast.makeText(this, getString(R.string.end_message), Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity + 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.change_quantity), Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , hai chiamato decreaseCount.");
        }
    }


    private void updateProduct(int productQuantity) {
        Log.d("message", "hai chiamato updateProduct");

        if (mCurrentProductUri == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(DepositoEntry.QUANTITA, productQuantity);

        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(DepositoEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.failure_insert),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.success_insert),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.failure_update),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.success_update),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.failure_delete),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.success_delete),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog);
        builder.setPositiveButton(R.string.erase, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
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
