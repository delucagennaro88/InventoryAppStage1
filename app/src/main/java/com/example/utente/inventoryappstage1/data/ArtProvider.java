package com.example.utente.inventoryappstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.utente.inventoryappstage1.data.Deposito.DepositoEntry;

public class ArtProvider extends ContentProvider {

    private static final int PRODUCTS = 100;

    private static final int PRODUCT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Deposito.CONTENT_AUTHORITY, Deposito.PATH_INVENTORY, PRODUCTS);

        sUriMatcher.addURI(Deposito.CONTENT_AUTHORITY, Deposito.PATH_INVENTORY + "/#", PRODUCT_ID);
    }

    private DbDeposito mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DbDeposito((getContext()));
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(DepositoEntry.TABELLA, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = DepositoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DepositoEntry.TABELLA, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Non riesco a trovare " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Inserimento non supportato " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return DepositoEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return DepositoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("URI sconosciuto" + uri + " con match " + match);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String nameProduct = values.getAsString(DepositoEntry.NOME);
        if (nameProduct == null) {
            throw new IllegalArgumentException("Nome obbligatorio");
        }

        Integer priceProduct = values.getAsInteger(DepositoEntry.PREZZO);
        if (priceProduct != null && priceProduct < 0) {
            throw new IllegalArgumentException("Prezzo obbligatorio");
        }

        Integer quantityProduct = values.getAsInteger(DepositoEntry.QUANTITA);
        if (quantityProduct != null && quantityProduct < 0) {
            throw new IllegalArgumentException("Quantità obbligatorio");
        }

        Integer nomeRivenditore = values.getAsInteger(DepositoEntry.VENDITORE);
        if (nomeRivenditore == null || !DepositoEntry.SellerValidity(nomeRivenditore)) {
            throw new IllegalArgumentException("Venditore obbligatorio");
        }

        Integer supplierPhone = values.getAsInteger(DepositoEntry.TELEFONO);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Telefono obbligatorio");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(DepositoEntry.TABELLA, null, values);
        if (id == -1) {
            Log.v("message:", "Errore inserimento " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                rowsDeleted = database.delete(DepositoEntry.TABELLA, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = DepositoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(DepositoEntry.TABELLA, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cancellazione non supportata " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = DepositoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Aggiornamento non supportato " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(DepositoEntry.NOME)) {
            String nameProduct = values.getAsString(DepositoEntry.NOME);
            if (nameProduct == null) {
                throw new IllegalArgumentException("Nome obbligatorio");
            }
        }
        if (values.containsKey(DepositoEntry.PREZZO)) {
            Integer priceProduct = values.getAsInteger(DepositoEntry.PREZZO);
            if (priceProduct != null && priceProduct < 0) {
                throw new
                        IllegalArgumentException("Prezzo obbligatorio");
            }
        }

        if (values.containsKey(DepositoEntry.QUANTITA)) {
            Integer quantityProduct = values.getAsInteger(DepositoEntry.QUANTITA);
            if (quantityProduct != null && quantityProduct < 0) {
                throw new
                        IllegalArgumentException("Quantità obbligatoria");
            }
        }
        if (values.containsKey(DepositoEntry.VENDITORE)) {
            Integer nomeRivenditore = values.getAsInteger(DepositoEntry.VENDITORE);
            if (nomeRivenditore == null || !DepositoEntry.SellerValidity(nomeRivenditore)) {
                throw new IllegalArgumentException("Venditore obbligatorio");
            }
        }

        if (values.containsKey(DepositoEntry.TELEFONO)) {
            Integer supplierPhone = values.getAsInteger(DepositoEntry.TELEFONO);
            if (supplierPhone != null && supplierPhone < 0) {
                throw new
                        IllegalArgumentException("Telefono obbligatorio");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(DepositoEntry.TABELLA, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}