package com.CPE001_2021.capstoneproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter_Item extends ArrayAdapter {
    public ListAdapter_Item(Context context, ArrayList<Item_ListElement> itemArrayList){
        super(context, R.layout.list_item,itemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Item_ListElement item = (Item_ListElement) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.itemImg);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemExp = convertView.findViewById(R.id.itemExpiration);
        TextView itemQty = convertView.findViewById(R.id.itemQuantity);

        imageView.setImageResource(item.imageId);
        itemName.setText(item.name);
        itemQty.setText(item.quantity);
        itemExp.setText(item.expiration);

        return convertView;
        //return super.getView(position, convertView, parent);
    }
}
