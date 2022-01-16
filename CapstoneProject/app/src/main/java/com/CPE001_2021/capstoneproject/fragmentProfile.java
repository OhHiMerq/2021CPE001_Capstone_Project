package com.CPE001_2021.capstoneproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class fragmentProfile extends Fragment {
    private FirebaseUser user;
    private DatabaseReference ref;

    private String userID;

    private Button buttonSignOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("CapstoneDatabase/UserProfile");
        userID = user.getUid();

        final TextView profileName = (TextView) view.findViewById(R.id.profileName);
        final TextView profileEmail = (TextView) view.findViewById(R.id.profileEmail);
        buttonSignOut = (Button) view.findViewById(R.id.signOut);

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });

        ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userProfile = snapshot.getValue(Profile.class);

                if(userProfile != null){
                    String p_name = userProfile.userName;
                    String p_email = userProfile.userEmail;

                    profileName.setText(p_name);
                    profileEmail.setText(p_email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Something went Wrong!",Toast.LENGTH_LONG).show();
                SignOut();
            }
        });

        return view;
    }
    void SignOut(){
        FirebaseAuth.getInstance().signOut();
        DatabaseSystemFunctions.dumpInstance();
        startActivity(new Intent(new Intent(getActivity(),LoginActivity.class)));
    }


}