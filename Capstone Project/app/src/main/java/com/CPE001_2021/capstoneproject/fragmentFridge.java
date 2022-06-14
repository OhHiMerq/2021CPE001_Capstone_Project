package com.CPE001_2021.capstoneproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class fragmentFridge extends Fragment {
    public static fragmentFridge instance;
    private ArrayList<InventoryItem> FocusedInventoryItem = new ArrayList<>();
    ListView listView;
    String[] itemName,itemQuantity,itemExpiration;
    View rootView_;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView_ = inflater.inflate(R.layout.fragment_fridge, container, false);
        setHasOptionsMenu(true);
        instance = this;
        Setup_Listview_Items();

        FloatingActionButton addItemButton = (FloatingActionButton) rootView_.findViewById(R.id.addItem);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddItemActivity.class));
            }
        });
        return rootView_;
    }

    private void Setup_Listview_Items(){
//        int[] imageId = {R.drawable.cabbage,R.drawable.bellpepper,R.drawable.carrot,R.drawable.chicken,R.drawable.cauliflower,R.drawable.kangkong,R.drawable.potato,R.drawable.pumpkin};
//        itemName = new String[]{"Cabbage","Bell Pepper","Carrot","Chicken","Cauliflower","KangKong","Potato","Pumpkin"};
//        itemQuantity = new String[]{"100g","47g","123g","313g","1112g","100g","47g","123g"};
//        itemExpiration = new String[]{"7 days","12 days","10 days","18 days","13 days","7 days","12 days","10 days"};
//
//        ArrayList<InventoryItem> itemArrayList = new ArrayList<>();
//        for(int i = 0; i < imageId.length;i++){
//            Item_ListElement item = new Item_ListElement(itemName[i],itemQuantity[i],itemExpiration[i],imageId[i]);
//            itemArrayList.add(item);
//        }
        listView = rootView_.findViewById(R.id.listview_inventory);
//        Refresh_FridgeList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),ItemActivity.class);
                i.putExtra("itemName",DatabaseSystemFunctions.getInstance().getIngredient_Inventory(FocusedInventoryItem.get(position)).getDefItemName());
                i.putExtra("itemWeightExpiration",FocusedInventoryItem.get(position).getItemWeightExp());
                i.putExtra("itemImageURL",DatabaseSystemFunctions.getInstance().getIngredient_Inventory(FocusedInventoryItem.get(position)).getImageURL());
                i.putExtra("itemInventoryID",FocusedInventoryItem.get(position).inventoryId);
                i.putExtra("itemType",DatabaseSystemFunctions.getInstance().getIngredient_Inventory(FocusedInventoryItem.get(position)).getItemType());
                startActivity(i);
            }
        });
    }

    public void RetrieveAll_ListAll_InventoryItems(){
//  Focused Ingredients are
        FocusedInventoryItem = DatabaseSystemFunctions.getInstance().inventoryItems;
//        FocusedIngredients = new ArrayList<>();
//        for(InventoryItem invItem : inventoryItems){
//            Ingredient ingredient = DatabaseSystemFunctions.getInstance().getIngredient_Inventory(invItem);
//            FocusedIngredients.add(ingredient);
//        }
        ListAdapter_Item listAdapter = new ListAdapter_Item(getActivity(),FocusedInventoryItem);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // SHOW ALL LIST
                if(newText.isEmpty()){ // This makes the list reset
                    RetrieveAll_ListAll_InventoryItems();
                }else{
                    ArrayList<InventoryItem> filteredInventory = new ArrayList<>();
                    for(InventoryItem invItem: FocusedInventoryItem){
                        Ingredient ingredient = DatabaseSystemFunctions.getInstance().getIngredient_Inventory(invItem);
                        if(ingredient.getDefItemName().toLowerCase().replaceAll("[^a-zA-Z0-9]"," ").contains(newText.toLowerCase().replaceAll("[^a-zA-Z0-9]"," "))){
                            filteredInventory.add(invItem);
                        }
                    }
                    FocusedInventoryItem = filteredInventory;
                    ListAdapter_Item listAdapter = new ListAdapter_Item(getActivity(),filteredInventory);
                    listView.setAdapter(listAdapter);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }
}

