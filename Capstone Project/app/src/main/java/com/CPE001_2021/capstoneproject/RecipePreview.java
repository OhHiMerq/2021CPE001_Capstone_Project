package com.CPE001_2021.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RecipePreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_preview);

        TextView recipeName_TextView = (TextView) findViewById(R.id.recipeName);
        TextView recipeCookTime_TextView = (TextView) findViewById(R.id.recipeTimeReq);
        TextView recipeDescription_TextView = (TextView) findViewById(R.id.recipeDescription);
        ImageView recipeImage_ImageView = (ImageView) findViewById(R.id.recipeImageView);
        Intent intent = getIntent();
        String recipeName = intent.getStringExtra("recipeName");
        String recipeCookTime = intent.getStringExtra("recipeCookTime");
        String recipeDescription = intent.getStringExtra("recipeDescription");
        String recipeImageLink = intent.getStringExtra("recipeImageLink");

        recipeName_TextView.setText(recipeName);
        recipeCookTime_TextView.setText("Cook Time: "+recipeCookTime);
        recipeDescription_TextView.setText(recipeDescription);

        Glide.with(this)
                .load(recipeImageLink)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(recipeImage_ImageView);
    }
}