package com.CPE001_2021.capstoneproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class fragmentFridge extends Fragment {
    String[] itemName,itemQuantity,itemExpiration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView_ = inflater.inflate(R.layout.fragment_fridge, container, false);
        Setup_Listview_Items(rootView_);

        FloatingActionButton addItemButton = (FloatingActionButton) rootView_.findViewById(R.id.addItem);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddItemActivity.class));
            }
        });
        return rootView_;
    }

    private void Setup_Listview_Items(View rootView){
        int[] imageId = {R.drawable.cabbage,R.drawable.bellpepper,R.drawable.carrot,R.drawable.chicken,R.drawable.cauliflower,R.drawable.kangkong,R.drawable.potato,R.drawable.pumpkin};
        itemName = new String[]{"Cabbage","Bell Pepper","Carrot","Chicken","Cauliflower","KangKong","Potato","Pumpkin"};
        itemQuantity = new String[]{"100g","47g","123g","313g","1112g","100g","47g","123g"};
        itemExpiration = new String[]{"7 days","12 days","10 days","18 days","13 days","7 days","12 days","10 days"};

        ArrayList<Item_ListElement> itemArrayList = new ArrayList<>();
        for(int i = 0; i < imageId.length;i++){
            Item_ListElement item = new Item_ListElement(itemName[i],itemQuantity[i],itemExpiration[i],imageId[i]);
            itemArrayList.add(item);
        }
        ListView listView = rootView.findViewById(R.id.listview_inventory);

        ListAdapter_Item listAdapter = new ListAdapter_Item(getActivity(),itemArrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),ItemActivity.class);
                i.putExtra("itemImage",imageId[position]);
                i.putExtra("itemName",itemName[position]);
                i.putExtra("itemQuantity",itemQuantity[position]);
                i.putExtra("itemExpiration",itemExpiration[position]);
                startActivity(i);
            }
        });
    }
}

