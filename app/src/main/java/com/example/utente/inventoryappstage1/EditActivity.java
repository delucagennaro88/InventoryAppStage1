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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.utente.inventoryappstage1.data.Deposito.DepositoEntry;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private EditText mNameEdit;
    private EditText mPriceEdit;
    private EditText mQuantityEdit;
    private Spinner mMarketEdit;
    private EditText mPhoneEdit;

    private int mMarketName = DepositoEntry.SCONOSCIUTO;

    private boolean mItemChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemChanged = true;
            Log.d("message", "onTouch");

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Log.d("message", "onCreate");

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.add_item));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        mNameEdit = findViewById(R.id.edit_name);
        mPriceEdit = findViewById(R.id.edit_price);
        mQuantityEdit = findViewById(R.id.edit_quantity);
        mMarketEdit = findViewById(R.id.select_market);
        mPhoneEdit = findViewById(R.id.edit_phone);

        mNameEdit.setOnTouchListener(mTouchListener);
        mPriceEdit.setOnTouchListener(mTouchListener);
        mQuantityEdit.setOnTouchListener(mTouchListener);
        mMarketEdit.setOnTouchListener(mTouchListener);
        mPhoneEdit.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter itemMarketAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_markets, android.R.layout.simple_spinner_item);

        itemMarketAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mMarketEdit.setAdapter(itemMarketAdapter);

        mMarketEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.ebay_market))) {
                        mMarketName = DepositoEntry.EBAY;
                    } else if (selection.equals(getString(R.string.ibs_market))) {
                        mMarketName = DepositoEntry.IBS;
                    } else if (selection.equals(getString(R.string.maremagnum_market))) {
                        mMarketName = DepositoEntry.MAREMAGNUM;
                    } else {
                        mMarketName = DepositoEntry.SCONOSCIUTO;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mMarketName = DepositoEntry.SCONOSCIUTO;
            }
        });
    }

    private void saveProduct() {
        String itemNameStr = mNameEdit.getText().toString().trim();
        String priceStr = mPriceEdit.getText().toString().trim();
        String quantityStr = mQuantityEdit.getText().toString().trim();
        String phoneStr = mPhoneEdit.getText().toString().trim();
        if (mCurrentProductUri == null) {
            if (TextUtils.isEmpty(itemNameStr)) {
                Toast.makeText(this, getString(R.string.required_name), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(priceStr)) {
                Toast.makeText(this, getString(R.string.required_price), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(quantityStr)) {
                Toast.makeText(this, getString(R.string.required_quantity), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mMarketName == DepositoEntry.SCONOSCIUTO) {
                Toast.makeText(this, getString(R.string.required_market), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phoneStr)) {
                Toast.makeText(this, getString(R.string.required_phone), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(DepositoEntry.NOME, itemNameStr);
            values.put(DepositoEntry.PREZZO, priceStr);
            values.put(DepositoEntry.QUANTITA, quantityStr);
            values.put(DepositoEntry.VENDITORE, mMarketName);
            values.put(DepositoEntry.TELEFONO, phoneStr);

            Uri newUri = getContentResolver().insert(DepositoEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.failure_insert),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.success_insert),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {

            if (TextUtils.isEmpty(itemNameStr)) {
                Toast.makeText(this, getString(R.string.required_name), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(priceStr)) {
                Toast.makeText(this, getString(R.string.required_price), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(quantityStr)) {
                Toast.makeText(this, getString(R.string.required_quantity), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mMarketName == DepositoEntry.SCONOSCIUTO) {
                Toast.makeText(this, getString(R.string.required_market), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phoneStr)) {
                Toast.makeText(this, getString(R.string.required_phone), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(DepositoEntry.NOME, itemNameStr);
            values.put(DepositoEntry.PREZZO, priceStr);
            values.put(DepositoEntry.QUANTITA, quantityStr);
            values.put(DepositoEntry.VENDITORE, mMarketName);
            values.put(DepositoEntry.TELEFONO, phoneStr);


            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.failure_update),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.success_update),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        Log.d("message", "apri EditActivity");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case android.R.id.home:
                if (!mItemChanged) {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mItemChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
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
            int nameIndex = cursor.getColumnIndex(DepositoEntry.NOME);
            int priceIndex = cursor.getColumnIndex(DepositoEntry.PREZZO);
            int quantityIndex = cursor.getColumnIndex(DepositoEntry.QUANTITA);
            int marketIndex = cursor.getColumnIndex(DepositoEntry.VENDITORE);
            int phoneIndex = cursor.getColumnIndex(DepositoEntry.TELEFONO);

            String currentName = cursor.getString(nameIndex);
            int currentPrice = cursor.getInt(priceIndex);
            int currentQuantity = cursor.getInt(quantityIndex);
            int currentSupplierName = cursor.getInt(marketIndex);
            int currentSupplierPhone = cursor.getInt(phoneIndex);

            mNameEdit.setText(currentName);
            mPriceEdit.setText(Integer.toString(currentPrice));
            mQuantityEdit.setText(Integer.toString(currentQuantity));
            mPhoneEdit.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {
                case DepositoEntry.EBAY:
                    mMarketEdit.setSelection(1);
                    break;
                case DepositoEntry.IBS:
                    mMarketEdit.setSelection(2);
                    break;
                case DepositoEntry.MAREMAGNUM:
                    mMarketEdit.setSelection(3);
                    break;
                default:
                    mMarketEdit.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEdit.setText("");
        mPriceEdit.setText("");
        mQuantityEdit.setText("");
        mPhoneEdit.setText("");
        mMarketEdit.setSelection(0);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.undo, discardButtonClickListener);
        builder.setNegativeButton(R.string.continue_modify, new DialogInterface.OnClickListener() {
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
