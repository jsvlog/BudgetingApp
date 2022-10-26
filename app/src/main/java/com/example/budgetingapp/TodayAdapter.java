package com.example.budgetingapp;

import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TodayAdapter extends FirebaseRecyclerAdapter<Data, TodayAdapter.myViewlet>{


    @Override
    protected void onBindViewHolder(@NonNull myViewlet holder, int position, @NonNull Data model) {

    }

    @NonNull
    @Override
    public myViewlet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public  class myViewlet extends RecyclerView.ViewHolder{

        public myViewlet(@NonNull View itemView) {
            super(itemView);
        }
    }
}
