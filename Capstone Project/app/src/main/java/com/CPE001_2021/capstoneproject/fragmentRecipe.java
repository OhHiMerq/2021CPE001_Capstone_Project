package com.CPE001_2021.capstoneproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class fragmentRecipe extends Fragment {
    // Show All and searchText is only for the scrolling feature with limited queries
    boolean ShowAll;
    String searchText = "";

    View rootView;
    String[] recipeName,recipeCooktime,recipeImageURL;
    FirebaseDatabase database;

    ListView listView;
    Button filter_button,top_button;

    private int preLast;
    int recipe_per_page = 10;
    int current_page_count = 0;

    private ArrayList<Recipe> FocusedRecipes = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        setHasOptionsMenu(true);

        ShowAll = true; // Set Default Filter


        listView = rootView.findViewById(R.id.listview_recipe);
        filter_button = rootView.findViewById(R.id.filterButton);
        top_button = rootView.findViewById(R.id.goTopButton);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                switch(view.getId())
                {
                    case R.id.listview_recipe:
                        if(ShowAll && searchText.isEmpty()){
                            final int lastItem = firstVisibleItem + visibleItemCount;
                            if(lastItem == totalItemCount)
                            {
                                if(preLast!=lastItem)
                                {
//                                //to avoid multiple calls for last item
//                                Log.d("Last", "Last");

                                    // save index and top position
                                    int index = listView.getFirstVisiblePosition();
                                    View v = listView.getChildAt(0);
                                    int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

                                    RetrieveRecipe_List(index,top);

                                    preLast = lastItem;
                                }
                            }
                        }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent recipeIntent = new Intent(getActivity(),RecipePreview.class);
                recipeIntent.putExtra("recipeName",FocusedRecipes.get(position).getScrName());
                recipeIntent.putExtra("recipeCookTime",FocusedRecipes.get(position).getScrTimeReq());
                recipeIntent.putExtra("recipeDescription",FocusedRecipes.get(position).getScrDescription());
                recipeIntent.putExtra("recipeImageLink",FocusedRecipes.get(position).getImageURL());
                startActivity(recipeIntent);
            }
        });

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAll = !ShowAll;
                SetupFilterButton();
            }
        });

        top_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelectionFromTop(0, 0);
            }
        });


        database = FirebaseDatabase.getInstance();
        SetupFilterButton();

        return rootView;
    }

    public void SetupFilterButton(){
        if (ShowAll){
            filter_button.setText("Show Recommendation");
            FocusedRecipes =  Retrieve_AllRecipe(); // Retrieve all recipes for the search bar
            RetrieveRecipe_List(0,0); // Retrieve all recipes with limiting queries for the listview
            // Both functions above are separated for the sake of code organization.
        }else{
            filter_button.setText("Show All");
            FocusedRecipes = RetrieveRecommended_Recipe(); // Retrieve all recommended recipes and list it with the listview
        }
    }

    private ArrayList<Recipe> Retrieve_AllRecipe(){
        LoadingDialog ld = new LoadingDialog(getActivity(),"Retrieving All Recipes");
        ld.startLoadingDialog();
        ArrayList<Recipe> allRecipes = new ArrayList<>();
        DatabaseReference ref = database.getReference("CapstoneDatabase").child("SysRecipe");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Log__",snapshot.getKey());
                Recipe newRecipe = snapshot.getValue(Recipe.class);
                allRecipes.add(newRecipe);
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
                ld.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return allRecipes;
    }

    // Will only be called ulit pag nag scroll down sa last item, para hindi iload lahat ng recipe.
    public void RetrieveRecipe_List(int scroll_index, int scroll_top){ // Default
        LoadingDialog loadingDialog = new LoadingDialog(getActivity(),"Refreshing List");
        loadingDialog.startLoadingDialog();
        ArrayList<Recipe> _recipe = new ArrayList<>();
        DatabaseReference ref = database.getReference("CapstoneDatabase").child("SysRecipe");
        //Query query = ref.orderByKey().limitToFirst(50);
        current_page_count += recipe_per_page;
        Query query = ref.orderByKey().limitToFirst(current_page_count);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Log__",snapshot.getKey());
                Recipe newRecipe = snapshot.getValue(Recipe.class);
                _recipe.add(newRecipe);
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

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListAdapter_Recipe listAdapter = new ListAdapter_Recipe(getActivity(),_recipe);
                listView.setAdapter(listAdapter);
                // restore index and position
                listView.setSelectionFromTop(scroll_index, scroll_top);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<Recipe> RetrieveRecommended_Recipe(){ // will be called
        ArrayList<Recipe>  recommendedRecipe = new ArrayList<>();
        DatabaseSystemFunctions.getInstance().UpdateRecipe_RecommendationScores();
        recommendedRecipe = DatabaseSystemFunctions.getInstance().AllRecipes;
        
        ListAdapter_Recipe listAdapter = new ListAdapter_Recipe(getActivity(),recommendedRecipe);
        listView.setAdapter(listAdapter);

//        for(Recipe recipe : recommendedRecipe){
//            Log.d("Recipe Summary","Recipe: "+recipe.getScrName());
//            for(Ingredient ingredient : recipe.ingredients){
//                if(ingredient == null){
//                    Log.d("Recipe Summary","Recipe"+recipe.getScrName()+": NULL");
//                }else{
//                    Log.d("Recipe Summary","Recipe"+recipe.getScrName()+": "+ingredient.getDefItemName());
//                }
//
//            }
//            Log.d("Recipe Summary","###########################");
//        }
        return recommendedRecipe;
//        LoadingDialog loadingDialog = new LoadingDialog(getActivity(),"Building Recommendations");
//        loadingDialog.startLoadingDialog();
//        ArrayList<Recipe> recommendedRecipe = new ArrayList<>();
//
//
//        DatabaseReference ref = database.getReference("CapstoneDatabase").child("SysRecipe");
//        Query query = ref.orderByKey().limitToFirst(current_page_count);
//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                // PUT HERE THE RECIPE SCORING ALGORITHM
//                Recipe newRecipe = snapshot.getValue(Recipe.class);
//                newRecipe.recipeId = snapshot.getKey();
//
//
//                recommendedRecipe.add(newRecipe);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // SORT recommendedRecipe based on Recipe Scores [Ranked]
//                // after sorting, get the top given ranks. TOP 10
//                ListAdapter_Recipe listAdapter = new ListAdapter_Recipe(getActivity(),recommendedRecipe);
//                listView.setAdapter(listAdapter);
//                loadingDialog.dismissDialog();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                DatabaseReference ref = database.getReference("CapstoneDatabase").child("SysRecipe");
//                Query q = ref.orderByChild("scrName").startAt(query).endAt(query + "\uf8ff");
//                processSearch(q);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;

                // SHOW ALL LIST
                if(searchText.isEmpty()){ // This makes the list reset
                    RetrieveRecipe_List(0,0);
                    SetupFilterButton();
                }else{
                    ArrayList<Recipe> filteredRecipes = new ArrayList<>();
                    for(Recipe recipe: FocusedRecipes){
                        if(recipe.getScrName().toLowerCase().replaceAll("[^a-zA-Z0-9]"," ").contains(newText.toLowerCase().replaceAll("[^a-zA-Z0-9]"," "))){
                            filteredRecipes.add(recipe);
                        }
                    }
                    FocusedRecipes = filteredRecipes;
                    ListAdapter_Recipe listAdapter = new ListAdapter_Recipe(getActivity(),filteredRecipes);
                    listView.setAdapter(listAdapter);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }





}