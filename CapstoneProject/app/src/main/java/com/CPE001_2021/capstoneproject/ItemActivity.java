package com.CPE001_2021.capstoneproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.CPE001_2021.capstoneproject.R;

// THIS CLASS WAS USED FOR GETTING THE INTENT EXTRA DATA AND PREVIEW DETAILS ONCE AN ITEM WAS CLICKED FROM THE LIST
public class ItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        TextView _name = findViewById(R.id.itemName);
        TextView _quantity = findViewById(R.id.itemQuantity);
        TextView _expiration = findViewById(R.id.itemExpiration);
        ImageView _image = findViewById(R.id.itemImage);

        String itemName = getIntent().getStringExtra("itemName");
        String itemQuantity = getIntent().getStringExtra("itemQuantity");
        String itemExpiration = getIntent().getStringExtra("itemExpiration");
        int itemImage = getIntent().getIntExtra("itemImage",R.drawable.cabbage);

        _name.setText(itemName);
        _quantity.setText(itemQuantity);
        _expiration.setText(itemExpiration);
        _image.setImageResource(itemImage);
    }
}