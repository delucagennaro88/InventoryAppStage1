package com.example.utente.inventoryappstage1;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.utente.inventoryappstage1.data.Deposito.DepositoEntry;

public class ArtCursorAdapter extends CursorAdapter {

    public ArtCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_art, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {


        Log.d("Posizione " + cursor.getPosition() + ":", " bindView() è stato chiamato.");

        TextView itemNameView = view.findViewById(R.id.name_product);
        TextView priceView = view.findViewById(R.id.product_price);
        TextView quantityView = view.findViewById(R.id.product_quantity);
        Button saleButton = view.findViewById(R.id.sale_button);

        final int idIndex = cursor.getColumnIndex(DepositoEntry._ID);
        int nameIndex = cursor.getColumnIndex(DepositoEntry.NOME);
        int priceIndex = cursor.getColumnIndex(DepositoEntry.PREZZO);
        int quantityIndex = cursor.getColumnIndex(DepositoEntry.QUANTITA);

        final String productID = cursor.getString(idIndex);
        String productName = cursor.getString(nameIndex);
        String productPrice = cursor.getString(priceIndex);
        final String productQuantity = cursor.getString(quantityIndex);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity Activity = (MainActivity) context;
                Activity.productSaleCount(Integer.valueOf(productID), Integer.valueOf(productQuantity));
            }
        });

        itemNameView.setText(productID + " ) " + productName);
        priceView.setText(context.getString(R.string.prezzo) + " : " + productPrice + "  " + context.getString(R.string.moneta));
        quantityView.setText(context.getString(R.string.ammontare) + " : " + productQuantity);

        Button productEditButton = view.findViewById(R.id.edit_button);
        productEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                Uri currentProdcuttUri = ContentUris.withAppendedId(DepositoEntry.CONTENT_URI, Long.parseLong(productID));
                intent.setData(currentProdcuttUri);
                context.startActivity(intent);
            }
        });

    }

}
