package com.CPE001_2021.capstoneproject;


import android.os.Build;
import android.util.Log;
import java.util.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DatabaseSystemFunctions {

    private static DatabaseSystemFunctions instance = null;
    public ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
    public ArrayList<Ingredient> AllIngredients = new ArrayList<>();
    public ArrayList<Recipe> AllRecipes = new ArrayList<>();

    boolean inventoryItems_loaded = false;
    boolean allIngredients_loaded = false;
    boolean allRecipes_loaded = false;
    private boolean isAllIngredientFromALLRecipes_Loaded = false;

    public static DatabaseSystemFunctions getInstance()
    {
        if (instance == null)
            instance = new DatabaseSystemFunctions();

        return instance;
    }

    private DatabaseSystemFunctions(){
        SetupListener_AllInventoryItems();
        RetrieveAll_ValidIngredients();
        RetrieveAll_Recipes();
    }

    public static void dumpInstance(){
        instance = null;
    }

    public Ingredient getIngredient_Inventory(InventoryItem invItem){
        Ingredient ingredient = null;
        for(Ingredient item : AllIngredients){
            if(item.itemId.equals(invItem.getItemId())){
                ingredient = item;
                break;
            }
        }
        return ingredient;
    }

    public InventoryItem getInventory_Ingredient(Ingredient ingredient){
        InventoryItem invItem = null;
        for(InventoryItem invItem_ : inventoryItems){
            if(invItem_.getItemId().equals(ingredient.itemId)){
                invItem = invItem_;
                break;
            }
        }
        return invItem;
    }

    public boolean isIngredientExistInInventory(Ingredient item_){
        for(InventoryItem invItem : inventoryItems){
            if(invItem.getItemId().equals(item_.itemId)){
                return true;
            }
        }
        return false;
    }


    private void SetupListener_AllInventoryItems(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CapstoneDatabase/UserInventory_Items");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("Loading Data","Retrieving All Inventory Items WAIT");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                InventoryItem item_ = snapshot.getValue(InventoryItem.class);
                if(user.getUid().equals(item_.getUserId())){
                    inventoryItems.add(item_);
                    item_.inventoryId = snapshot.getKey();
                    Log.d("Inventory Item","Item Added: "+item_.getItemId());
                    //UpdateAllInventoryIngredientsToAllRecipe(); // pag may nadagdag, update yung ingredients sa lahat ng recipe.
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // In the case of inventory items, they are only able to change/update their item weight/amount
                // Assuming that in every case that the item has changed, the weight/amount is the one that changed.
                InventoryItem item_ = snapshot.getValue(InventoryItem.class);
                if(user.getUid().equals(item_.getUserId())){
                    for (InventoryItem item : inventoryItems){
                        if(item.getItemId().equals(item_.getItemId())){
                            Log.d("Inventory Item","Item Updated: From "+item.getItemWeightExp() + " to "+ item_.getItemWeightExp());
                            item.setItemWeightExp(item_.getItemWeightExp());
                            return;
                        }
                    }

                    //UpdateAllInventoryIngredientsToAllRecipe(); // pag may nabago, update yung ingredients sa lahat ng recipe.
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                InventoryItem item_ = snapshot.getValue(InventoryItem.class);
                if(user.getUid().equals(item_.getUserId())){
                    for(InventoryItem item : inventoryItems){
                        if(item.getItemId().equals(item_.getItemId())){
                            inventoryItems.remove(item);
                            break;
                        }
                    }

                    Log.d("Inventory Item","Item Removed: "+item_.getItemId());

                    //UpdateAllInventoryIngredientsToAllRecipe(); // pag may nawala, update yung ingredients sa lahat ng recipe.
                }

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
                //fragmentFridge.instance.Refresh_FridgeList();
                Log.d("Loading Data","DONE Retrieving All Inventory Items; Count: "+inventoryItems.size());

                inventoryItems_loaded = true;
                RefreshInventoryList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RetrieveAll_ValidIngredients() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CapstoneDatabase").child("SysIngredient");
        Log.d("Loading Data","Retrieving All Valid Ingredients WAIT");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ingredient newIngredient = snapshot.getValue(Ingredient.class);
                newIngredient.itemId = snapshot.getKey();
                AllIngredients.add(newIngredient);
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
                Log.d("Loading Data","DONE Retrieving All Valid Ingredients; COunt: "+AllIngredients.size());
                allIngredients_loaded = true;
                RefreshInventoryList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RetrieveAll_Recipes() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CapstoneDatabase").child("SysRecipe");
        Log.d("Loading Data","Retrieving All Recipes WAIT");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Recipe recipe = snapshot.getValue(Recipe.class);
                recipe.recipeId = snapshot.getKey();


                AllRecipes.add(recipe);
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
                Log.d("Loading Data","DONE Retrieving All Recipes; COunt: "+AllRecipes.size());
                allRecipes_loaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void RefreshInventoryList(){
        if(inventoryItems_loaded && allIngredients_loaded && allRecipes_loaded){
            Log.d("Loading Data","Refresh List");
            fragmentFridge.instance.RetrieveAll_ListAll_InventoryItems();

            UpdateInventory_ExpiryScores();
            for(InventoryItem item : inventoryItems){
                Log.d("Loading Data","Item Name: "+DatabaseSystemFunctions.getInstance().getIngredient_Inventory(item).getDefItemName() + " Score: "+item.itemExpiryScore);
            }
        }
    }

    public void AssignAllIngredientsToEachAllRecipe(){ // ACTS AS AN INITIALIZER, ASSIGNING RESPECTIVE INGREDIENTS FOR EACH RECIPE INGREDIENT
        Log.d("Loading Data","Assigning Respective Ingredients WAIT");
        for(Recipe recipe : AllRecipes){
            recipe.ingredients = DatabaseSystemFunctions.getInstance().getRecipeIngredients(recipe); // this will retrieve all ingredients para sa recipe nato na meron sa inventory
        }
        Log.d("Loading Data","Assigning Respective Ingredients DONE");
    }

    public ArrayList<Ingredient> getRecipeIngredients(Recipe recipe_){ // this will retun a list of ingredients that goes with the given recipe
        // set summary of scores for each recipe ingredients
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        String[] recipe_ingredients = recipe_.getScrIngredient().split("&");

        for (int i = 0; i < recipe_ingredients.length; i++) {
            String string_RI = recipe_ingredients[i].split("\\|")[2];
            Ingredient ingredient = getIngredientfromRecipeIngredient(string_RI,recipe_.recipeId);
            if(ingredient != null){
                ingredients.add(ingredient);
            }else{
                ingredients.add(null); // meaning, walang nahanap na ingredient para dun sa recipe ingredient nato
            }
        }
        return ingredients;
    }

    private Ingredient getIngredientfromRecipeIngredient(String recipeIngredient_string,String recipeID){
        // should be only looking in the inventory but for testing purposes, lahat ng ingredients ang need natin.
        // this is where the searching will prevail
        // will return null if below the threshold,
        // if the highest value return multiple ingredients, perform another set of comparison.
        // if the highest value return only one ingredient then return it

        // REMOVE UNNECESSARY WORDS
        String[] toRemove = {"pork cracklings with attached meat","kalamyas or bilimbi","bullet tuna","carabao or whole cow's milk",
                "you can substitute kesong puti or queso de bola","and","boneless","minced","thumb","size","peeled","large","diced","chopped",
                "seeded","skinless","cut","ounces","bunch","into","ends","trimmed","inch","separated",
                "sliced","taste","roma","pieces","lengths","to","or","about","thick","half","rounds","medium",
                "pound","halved","depending","small","quarted","crushed","lengthwise","beaten","on","quartered",
                "thinly","substitute","serving","parts","tendrils","scrubbed","cooked","mini","bearded","coarsely",
                "melted","matchsticks","deveined","you","pitted","frozen","from","strips","gherkins","shredded",
                "drained","wedges","cleaned","gutted","ripe","semi","rinsed","cracked","finely","package",
                "each","thirds","kabocha","thawed","individual","julienned","napa","stem","removed","halves",
                "poaching","leftover","husked","scaled","cubed","whole","grated","extracted","fresh","diagonally",
                "uncooked","room","temperature","chunks","firm","based","as","needed","cup","split","crosswise","stemmed",
                "cubes","grams","clear","cups","freshly","bias","a",
                "filipino","sweet","style","in","cored","very","ounce","young","chopped","ginisang","raw","pounded","with",
                "marrow","stems","can","thickness","new zealand","sweetened","attached","optional","kakang gata","deboned","flaked",
                "canned","the","pounds","up","homemade","store","bought","tablespoons","pared","optional","yard"
        };

        String modString = recipeIngredient_string.replaceAll("[^a-zA-Z]+"," ").toLowerCase();

        // NOT PERFECT BUT GOOD ENOUGH
        for (int i = 0; i < toRemove.length; i++) {
            modString = " " + modString + " ";
            modString = modString.replace(" " + toRemove[i].toLowerCase() + " "," ");
            modString = modString.replaceAll("\\s+", " "); // remove multiple spaces
        }
        modString = modString.trim();

        Ingredient bestIngredient = null;
        int smallestValue = Integer.MAX_VALUE;

        for(Ingredient item : AllIngredients){
            String[] itemNames = (item.getDefItemName() + "," + item.getAnalogousNames()).toLowerCase().split(",");
            int smallestValue_analogous = Integer.MAX_VALUE;
            int score = 0;

            // GET LOWEST SCORE FROM ANALOGOUS
            for (int n = 0; n < itemNames.length; n++) {
                int scoreHolder = compute_Levenshtein_distanceDP(modString,itemNames[n]);
                if(scoreHolder < smallestValue_analogous){
                    score = scoreHolder;
                    smallestValue_analogous = scoreHolder;
                }
            }

            // EVALUATE THE OBTAINED SCORE
            if(bestIngredient == null || score < smallestValue){
                bestIngredient = item;
                smallestValue = score;
            }
        }
//        Log.d("Recipe Ingredient"," > "+recipeID+" A(REAL): "+recipeIngredient_string+" B(MOD): " +modString +" C(OBTAIN): "+bestIngredient.getDefItemName()+ " D(SCORE): "+smallestValue);

        return bestIngredient;
    }

    private void UpdateInventory_ExpiryScores(){ // WILL BE CALLED WHENEVER INVENTORY SCORES IS USED
        for(InventoryItem item : inventoryItems){
            if(DatabaseSystemFunctions.getInstance().getIngredient_Inventory(item).getItemType().equals("p")){
                item.itemExpiryScore = CalculateItemRelevanceScore(item);
            }else{
                item.itemExpiryScore = 0f; // 0 means that the item has no relevance with time
            }
        }
    }


    public void UpdateRecipe_RecommendationScores(){ // WILL BE CALLED WHENEVER THE SYSTEM WILL USE THE RECIPE'S RECOMMENDATION SCORES
        if(!isAllIngredientFromALLRecipes_Loaded){
            AssignAllIngredientsToEachAllRecipe(); // WILL TAKE SOME TIME; WILL ONLY RUN ONCE
            isAllIngredientFromALLRecipes_Loaded = true;
        }
        UpdateInventory_ExpiryScores(); // MANDATORY CALL
        //Retrieve Scores
        for(Recipe recipe : AllRecipes){
            float scoreSum = 0f;
            int p_count = 0;
            int np_count = 0;
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                Ingredient item = recipe.ingredients.get(i);
                if(item != null){ // IN CASE
                    String[] itemInfo_ = recipe.getScrIngredient().split("&")[i].split("\\|"); // it is expected that the ingredient list aligns with the string ingredient list
                    if(!itemInfo_[1].equals("grams") && item.getItemType().equals("p")){
                        Log.d("Score","Recipe: "+recipe.recipeId + " Item: "+item.getDefItemName() + " is perishable but not using grams");

                    }

                    try{
                        float ratioAmount = (4*Float.parseFloat(itemInfo_[0]))/Float.parseFloat(recipe.getScrDefServing());
                    }catch(NumberFormatException e){
                        Log.d("Score","Recipe: "+recipe.recipeId + " Item: "+item.getDefItemName()+"; Reading Item Amount: "+itemInfo_[0]+" DEF SERVING: "+recipe.getScrDefServing());
                    }
                    continue;


//                    if(item.getItemType().equals("p")){
//                        p_count += 1;
//                    }else{
//                        np_count += 1;
//                    }

//                    InventoryItem invItem = DatabaseSystemFunctions.getInstance().getInventory_Ingredient(item);
//                    if(invItem != null){ // ITEM IS ON INVENTORY
//                        if(item.getItemType().equals("p")){
//                            float tally = 0f;
//                            tally += invItem.itemExpiryScore; // ADD EXPIRY SCORE 0 - 1 concept
//                            //Log.d("Score","Recipe: "+recipe.getScrName() + " has "+ item.getDefItemName()+" PERISHABLE "+ invItem.itemExpiryScore);
//
//                            float weightScore = DatabaseSystemFunctions.getInstance().GetInventoryItemTotalAmount(invItem.getItemWeightExp());
//                            String[] itemInfo = recipe.getScrIngredient().split("&")[i].split("\\|"); // it is expected that the ingredient list aligns with the string ingredient list
//
//
//
//                            float ratioAmount = (4*Float.parseFloat(itemInfo[0]))/Float.parseFloat(recipe.getScrDefServing());
//                            tally += weightScore/ratioAmount; // 0 -`   1 or more concept, either under or more than equal
//                            Log.d("Score","Recipe: "+recipe.getScrName() + "("+recipe.recipeId+") needs item: " + item.getDefItemName() + "("+ratioAmount+" grams) Inventory has "+ weightScore + " Expiry Score: "+ invItem.itemExpiryScore+" Amount Score: "+(weightScore/ratioAmount) + " Tally: "+tally);
//
//                            tally = tally / 2; // is divided by two
//                            scoreSum += Math.max(0, Math.min(1, tally)); // and clamped to 1 to make sure that the excess score does not affect the other tally.
//
//
//                        }else{
//                            scoreSum += 0.2f; // 0.2 is set by default as non perishables are not
//                            //Log.d("Score","Recipe: "+recipe.getScrName() + " has "+ item.getDefItemName() +" NON PERISHABLE 0.2f");
//                            Log.d("Score","Recipe: "+recipe.getScrName() + "("+recipe.recipeId+") needs item: " + item.getDefItemName() + " non perishable considered");
//                        }
//                        // FOR AMOUNT SCORE BELOW
//                    }else{
//                        //Log.d("Score","Recipe: "+recipe.getScrName() + " Item: "+item.getDefItemName() + "is not available");
//                        Log.d("Score","Recipe: "+recipe.getScrName() + "("+recipe.recipeId+") needs item: " + item.getDefItemName() + " but don't have in inventory");
//                    }
                }else{
                    Log.d("Score","Recipe: "+recipe.getScrName() +" Ingredient: ("+recipe.getScrIngredient().split("&")[i] + ") is NULL");
                }
            }

            // TALLYING IS DONE TO SCORE HOW RELEVANT ITEM ARE THROUGH THE VALUES OF 0 to 1
            // DIVIDING THE TALLY SCORES WITH THE TOTAL COUNT OF INGREDIENTS WILL OBTAIN HOW THE TALLY SCORES ACHIEVE THE EXPECTED TALLY SCORE
            Log.d("Score","Total of Ingredients: "+recipe.ingredients.size()+ " Obtained Count: "+ (p_count + np_count)+ " Total of Recipes: "+AllRecipes.size());
            scoreSum = scoreSum / (p_count * 1 + np_count * 0.2f);
            recipe.recipeScore = scoreSum;

            Log.d("Score",">>> Recipe: "+recipe.getScrName() + " Score: "+scoreSum);
        }
    }

    private float CalculateItemRelevanceScore(InventoryItem invItem){
        String earliestDateExp = invItem.getItemWeightExp().split(";")[0].split(",")[1];
        Ingredient ingredient = DatabaseSystemFunctions.getInstance().getIngredient_Inventory(invItem);
        long daysLeft = DatabaseSystemFunctions.getInstance().GetDaysBetweenDates(DatabaseSystemFunctions.getInstance().GetDateToday_String(),earliestDateExp);
        float itemExpiry_days = Float.parseFloat(ingredient.getExpiry());
        float expiryScore = (itemExpiry_days-daysLeft)/itemExpiry_days;
        return expiryScore;
    }




    // ##########################################################

    public Float GetInventoryItemTotalAmount(String text_){ // itemWeightExp is passed as string, total amount will be returned
        String[] info = text_.split(";");
        Float sum = 0f;
        for (String data : info){
            String[] weightExp = data.split(",");
            sum += Float.parseFloat(weightExp[0]);
        }
        return sum;
    }
    public String GetDateToday_String(){
        String DATE_FORMAT = "MM-dd-yyyy";
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateNow = sdf.format(c.getTime());
        return dateNow;
    }

    public long GetDaysBetweenDates(String start,String end){
        String DATE_FORMAT = "MM-dd-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate,endDate;
        long numberOfDays = 0;
        try{
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate,endDate,TimeUnit.DAYS);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff,TimeUnit.MILLISECONDS);
    }




    // ########################################################################################
    public int compute_Levenshtein_distanceDP(String str1,String str2){
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        for (int i = 0; i <= str1.length(); i++)
        {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }else if (j == 0) {
                    dp[i][j] = i;
                }else {
                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
                                    + NumOfReplacement(str1.charAt(i - 1),str2.charAt(j - 1)), // replace
                            dp[i - 1][j] + 1, // delete
                            dp[i][j - 1] + 1); // insert
                }
            }
        }
        return dp[str1.length()][str2.length()];
    }

    private int NumOfReplacement(char c1, char c2)
    {
        return c1 == c2 ? 0 : 1;
    }

    private int minm_edits(int... nums)
    {
        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }

}
