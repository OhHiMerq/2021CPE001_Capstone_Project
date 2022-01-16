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

public class ListAdapter_Recipe extends ArrayAdapter<Recipe> {


    public ListAdapter_Recipe(Context context, ArrayList<Recipe> recipeArrayList){
        super(context,R.layout.list_recipe,recipeArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Recipe recipe = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_recipe,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.recipe_image);
        TextView recipeName = convertView.findViewById(R.id.recipe_name);
        TextView recipeCooktime = convertView.findViewById(R.id.recipe_cooktime);

        Glide.with(convertView).load(recipe.getImageURL()).into(imageView);
        recipeName.setText(recipe.getScrName());
        recipeCooktime.setText(recipe.getScrTimeReq());
        return convertView;
                //super.getView(position, convertView, parent);
    }
}
