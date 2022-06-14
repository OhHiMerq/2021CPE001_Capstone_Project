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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter_Item extends ArrayAdapter {
    public ListAdapter_Item(Context context, ArrayList<InventoryItem> itemArrayList){
        super(context, R.layout.list_item,itemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        Item_ListElement item = (Item_ListElement) getItem(position);
//
//        if(convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
//        }
//
//        ImageView imageView = convertView.findViewById(R.id.itemImg);
//        TextView itemName = convertView.findViewById(R.id.itemName);
//        TextView itemExp = convertView.findViewById(R.id.itemExpiration);
//        TextView itemQty = convertView.findViewById(R.id.itemQuantity);
//
//        imageView.setImageResource(item.imageId);
//        itemName.setText(item.name);
//        itemQty.setText(item.quantity);
//        itemExp.setText(item.expiration);
        InventoryItem item = (InventoryItem) getItem(position);
//        Log.d("Loading Data","Inventory Item: "+item.getItemId());
        Ingredient ingredientItem = DatabaseSystemFunctions.getInstance().getIngredient_Inventory(item);
//        Log.d("Loading Data","Item: "+ingredientItem.itemId+" Name: "+ingredientItem.getDefItemName());
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.itemImg);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemExp = convertView.findViewById(R.id.itemAmountExpiration);
        TextView itemQty = convertView.findViewById(R.id.textViewTitle);

        itemName.setText(ingredientItem.getDefItemName());
        Glide.with(convertView)
                .load(ingredientItem.getImageURL())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(imageView);



        if(ingredientItem.getItemType().equals("p")){
            itemQty.setText(DatabaseSystemFunctions.getInstance().GetInventoryItemTotalAmount(item.getItemWeightExp()).toString() + " grams");
            String[] expWeight = item.getItemWeightExp().split(";");
            String[] info = expWeight[0].split(",");
            itemExp.setText(DatabaseSystemFunctions.getInstance().GetDaysBetweenDates(DatabaseSystemFunctions.getInstance().GetDateToday_String(),info[1])+" day(s) left");
        }else{
            itemQty.setText("- - -");
            itemExp.setText("* NP *");
        }



        return convertView;
//        return super.getView(position, convertView, parent);
    }
}
