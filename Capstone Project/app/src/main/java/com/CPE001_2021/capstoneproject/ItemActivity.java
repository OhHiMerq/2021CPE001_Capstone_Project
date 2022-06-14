package com.CPE001_2021.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// THIS CLASS WAS USED FOR GETTING THE INTENT EXTRA DATA AND PREVIEW DETAILS ONCE AN ITEM WAS CLICKED FROM THE LIST
public class ItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Button deleteItemButton = findViewById(R.id.itemDeleteButton);
        Button updateItemButton = findViewById(R.id.itemUpdateButton);

        TextView _name = findViewById(R.id.itemName);
        TextView _weight_expiration = findViewById(R.id.itemAmountExpiration);
        TextView _total_amount = findViewById(R.id.itemTotalAmount);
        ImageView _imageURL = findViewById(R.id.itemImage);

        String itemName = getIntent().getStringExtra("itemName");
        String itemWeightExpiration = getIntent().getStringExtra("itemWeightExpiration");
        String itemImageURL = getIntent().getStringExtra("itemImageURL");
        String itemInventoryID = getIntent().getStringExtra("itemInventoryID");
        String itemType = getIntent().getStringExtra("itemType");

        _name.setText(itemName);

        Glide.with(ItemActivity.this)
                .load(itemImageURL)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(_imageURL);


        if(itemType.equals("p")){
            String[] expWeight = itemWeightExpiration.split(";");
            String weightExp_textview_display = "";

            for (int i = 0; i < expWeight.length; i++) {
                String[] data = expWeight[i].split(",");
                weightExp_textview_display += data[0] + " grams";

                String dt = data[1];
                String dayDifference = String.valueOf(DatabaseSystemFunctions.getInstance().GetDaysBetweenDates(DatabaseSystemFunctions.getInstance().GetDateToday_String(), data[1]));
                weightExp_textview_display += " : "+dayDifference+" days left ("+data[1]+")";
                if(i < expWeight.length - 1){ // not last
                    weightExp_textview_display += "\n";
                }
            }
            _weight_expiration.setText(weightExp_textview_display);
            _total_amount.setText("Total Amount: "+DatabaseSystemFunctions.getInstance().GetInventoryItemTotalAmount(itemWeightExpiration) + " grams");
        }else{
            _weight_expiration.setText("* Data not Considered *");
            _total_amount.setText("Total Amount: NP");
        }




        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CapstoneDatabase/UserInventory_Items").child(itemInventoryID);
                ref.removeValue();
            }
        });

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddItemActivity.class);
                i.putExtra("itemName",itemName);
                i.putExtra("itemUpdate",true);
                i.putExtra("itemWeightExpiration",itemWeightExpiration);
                i.putExtra("itemInventoryID",itemInventoryID);

                startActivity(i);
            }
        });
    }
}