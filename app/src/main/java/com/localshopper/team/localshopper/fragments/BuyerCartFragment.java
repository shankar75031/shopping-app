package com.localshopper.team.localshopper.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.localshopper.team.localshopper.R;
import com.localshopper.team.localshopper.adapters.BuyerCartAdapter;
import com.localshopper.team.localshopper.constants.Constants;
import com.localshopper.team.localshopper.models.OrderItemModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerCartFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<OrderItemModel> itemsModelArrayList;
    FirebaseFirestore db;
    BuyerCartAdapter buyerCartAdapter;

    public BuyerCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer_cart, container, false);
        recyclerView = view.findViewById(R.id.buyer_cart_recyc_frag_buycar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        itemsModelArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        buyerCartAdapter = new BuyerCartAdapter();
        buyerCartAdapter.setOrderItemModelArrayList(itemsModelArrayList);
        recyclerView.setAdapter(buyerCartAdapter);
//        fetchData();
        return view;
    }

    public void fetchData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USER_NAME, "");

        db.collection("users")
                .document(username)
                .collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("hello", document.getId());
                                itemsModelArrayList.add(document.toObject(OrderItemModel.class));
                                buyerCartAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Hello", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}
