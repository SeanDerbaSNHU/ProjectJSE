package com.example.projectjse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    private Button btnSearch;
    private EditText editSearch;
    private RecyclerView recyclerView;

    private FirebaseFirestore fStore;

    private ArrayList<String> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fStore = FirebaseFirestore.getInstance();

        btnSearch = findViewById(R.id.btnSearch);
        editSearch = findViewById(R.id.editTextSearch);
        recyclerView = findViewById(R.id.recyclerView);

        userList = new ArrayList<String>();
        userList.add("test");

        fetchAllUsers();
        //setAdaptor();
    }

    public void searchOnClick(View view){
        userList.clear();
        String search = editSearch.getText().toString().trim();
        fStore.collection("users")
                .whereEqualTo("username", search)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userList.add(document.get("username").toString());
                            }
                        }
                    }
                });
       // recyclerAdapter adapter = new recyclerAdapter(userList);
       // recyclerView.swapAdapter(adapter, false);
    }

   /* private void setAdaptor(){
        recyclerAdapter adapter = new recyclerAdapter(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    } */

    private void fetchAllUsers(){
        fStore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userList.add(document.get("username").toString());
                                userList.add("test");
                            }
                        }
                        else{
                            userList.add("Fail");
                        }

                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userList.add("Failed to search");
            }
        });
    }
}