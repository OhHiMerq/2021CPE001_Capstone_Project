package com.CPE001_2021.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {
    private FirebaseUser user;
    FirebaseDatabase database;
    Dialog dialog;

    ArrayList<Ingredient> AllIngredients = new ArrayList<>();
    Ingredient selectedIngredient;


    private boolean manualInput;
    private TextView statusText;
    private TextView searchTextView;
    private EditText weightInput;
    private Switch manualInputSwitch;
    private Button addItemButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        addItemButton = (Button) findViewById(R.id.addItem);
        statusText = (TextView) findViewById(R.id.statusBar);
        searchTextView =(TextView) findViewById(R.id.searchTextView);
        manualInputSwitch = (Switch) findViewById(R.id.manualDataInput);
        weightInput = (EditText) findViewById(R.id.weightItem);

        AllIngredients = RetrieveAll_ValidIngredients();

        SetUpInputFields_Manual(manualInputSwitch.isChecked());


        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AddItemActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayList<String> ingredientNames = new ArrayList<>();
                for(Ingredient ingredient : AllIngredients){
                    ingredientNames.add(ingredient.getDefItemName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_list_item_1,ingredientNames);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ArrayList<String> filteredIngredients_String = new ArrayList<>();
                        for(Ingredient ingredient: AllIngredients){
                            boolean defName = ingredient.getDefItemName().toLowerCase().replaceAll("[^a-zA-Z0-9]"," ").contains(s.toString().toLowerCase().replaceAll("[^a-zA-Z0-9]"," "));
                            boolean analName = ingredient.getAnalogousNames().toLowerCase().replaceAll("[^a-zA-Z0-9]"," ").contains(s.toString().toLowerCase().replaceAll("[^a-zA-Z0-9]"," "));
                            if(defName || analName){
                                filteredIngredients_String.add(ingredient.getDefItemName());
                            }
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_list_item_1,filteredIngredients_String);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        searchTextView.setText(parent.getItemAtPosition(position).toString());
                        SetUp_SelectedItem(AllIngredients.get(ingredientNames.indexOf(parent.getItemAtPosition(position).toString())));
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

        manualInputSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetUpInputFields_Manual(isChecked);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }
        });

    }

    private void AddItem(){
        if (searchTextView.getText().toString().isEmpty() || selectedIngredient.equals(null)){
            searchTextView.setError("Select an Item!");
            searchTextView.requestFocus();
            return;
        }

        if (weightInput.getText().toString().isEmpty()){
            weightInput.setError("Provide an Amount!");
            weightInput.requestFocus();
            return;
        }

        String exp = ((TextView) findViewById(R.id.itemExpirationText)).getText().toString();
        String keyDateText = "Date: ";
        String dateSlice = exp.substring(exp.indexOf(keyDateText) + keyDateText.length());

        String weightExp = weightInput.getText().toString() + ','+dateSlice+';';
        DatabaseReference ref = database.getReference("CapstoneDatabase/UserInventory_Items");
        
//check here if the item already exists, if it exists, just append a string for weightExp data.
        Log.d("Inventory Item","Selected Item ID "+ selectedIngredient.itemId);
        for(InventoryItem item : DatabaseSystemFunctions.getInstance().inventoryItems){
            Log.d("Inventory Item","Read item id "+ item.getItemId() + " User: "+ user.getUid());
            if(selectedIngredient.itemId.equals(item.getItemId()) && user.getUid().equals(item.getItemId())){
                weightExp = item.getItemWeightExp() + weightExp;
                Log.d("Inventory Item","Item already exist "+ weightExp);
                break;
            }
        }
        InventoryItem invItem = new InventoryItem(user.getUid(),
                selectedIngredient.itemId,
                weightExp);
        ref.push().setValue(invItem);


    }
    private void SetUpInputFields_Manual(boolean state){
        manualInput = state;
        searchTextView.setEnabled(state);
        weightInput.setEnabled(state);
        if(state){
            statusText.setText("Details will be defined manually.\nPlease fill up the following input fields.");
        }else{
            statusText.setText("Details will be defined by the device automatically.\nPlease wait for the device to return values.");
        }
    }


    private ArrayList<Ingredient> RetrieveAll_ValidIngredients(){
        LoadingDialog loadingDialog = new LoadingDialog(AddItemActivity.this,"Retrieving Valid Ingredients");
        loadingDialog.startLoadingDialog();
        ArrayList<Ingredient> validIngredients = new ArrayList<>();
        DatabaseReference ref = database.getReference("CapstoneDatabase").child("SysIngredient");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ingredient newIngredient = snapshot.getValue(Ingredient.class);
                newIngredient.itemId = snapshot.getKey();
                validIngredients.add(newIngredient);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return validIngredients;
    }

    private void SetUp_SelectedItem(Ingredient ingredient_){
        selectedIngredient = ingredient_;
        ImageView itemImageView = findViewById(R.id.itemImageView);
        Glide.with(this)
                .load(ingredient_.getImageURL())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(itemImageView);
        
        TextView itemNameText = findViewById(R.id.itemNameText);
        itemNameText.setText(ingredient_.getDefItemName());

        TextView itemTypeText = findViewById(R.id.itemTypeText);
        if(ingredient_.getItemType().equals("p")){
            itemTypeText.setText("Perishable");
        }else if(ingredient_.getItemType().equals("np")){
            itemTypeText.setText("Non-Perishable");
        }else{
            itemTypeText.setText("Undefinable Type");
        }

//        String dt = "2012-01-04";  // Start date
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(sdf.parse(dt));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        c.add(Calendar.DATE, 40);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
//        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
//        String output = sdf1.format(c.getTime());

        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.add(Calendar.DATE,5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String dateExp = sdf.format(c.getTime());

        TextView exp = findViewById(R.id.itemExpirationText);
        exp.setText("Will expire in "+ingredient_.getExpiry()+" days\nDate: "+dateExp);

    }

}