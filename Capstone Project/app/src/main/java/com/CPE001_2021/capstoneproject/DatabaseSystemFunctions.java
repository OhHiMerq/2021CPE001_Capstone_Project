package com.CPE001_2021.capstoneproject;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseSystemFunctions {

    private static DatabaseSystemFunctions instance = null;
    public ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    public static DatabaseSystemFunctions getInstance()
    {
        if (instance == null)
            instance = new DatabaseSystemFunctions();

        return instance;
    }

    public static void dumpInstance(){
        instance = null;
    }

    private DatabaseSystemFunctions(){
        SetupListener_AllInventoryItems();
    }

    private void SetupListener_AllInventoryItems(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CapstoneDatabase/UserInventory_Items");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                InventoryItem item_ = snapshot.getValue(InventoryItem.class);
                if(user.getUid() == item_.getUserId()){
                    inventoryItems.add(item_);
                    Log.d("Inventory Item","Item Added: "+item_.getItemId());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // In the case of inventory items, they are only able to change/update their item weight/amount
                // Assuming that in every case that the item has changed, the weight/amount is the one that changed.
                InventoryItem item_ = snapshot.getValue(InventoryItem.class);
                if(user.getUid() == item_.getUserId()){
                    for (InventoryItem item : inventoryItems){
                        if(item.getItemId() == item_.getItemId()){
                            Log.d("Inventory Item","Item Updated: From "+item.getItemWeightExp() + " to "+ item_.getItemWeightExp());
                            item.setItemWeightExp(item_.getItemWeightExp());
                            return;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                InventoryItem item_ = snapshot.getValue(InventoryItem.class);
                if(user.getUid() == item_.getUserId()){
                    inventoryItems.remove(item_);
                    Log.d("Inventory Item","Item Removed: "+item_.getItemId());
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
