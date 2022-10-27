package com.example.budgetingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TodayAdapter extends FirebaseRecyclerAdapter<Data, TodayAdapter.myViewlet>{

private Context context;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TodayAdapter(@NonNull FirebaseRecyclerOptions<Data> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewlet holder, int position, @NonNull Data model) {
    holder.item.setText(model.getItem().toString());
    holder.date.setText(model.getDate());
    holder.amount.setText(String.valueOf(model.getAmount()));
    holder.month.setText(String.valueOf(model.getMonth()));

        switch (model.getItem()){
            case "Transport":
                holder.image.setImageResource(R.drawable.ic_transport);
                break;
            case "Food":
                holder.image.setImageResource(R.drawable.ic_food);
                break;
            case "House":
                holder.image.setImageResource(R.drawable.ic_house);
                break;
            case "Entertainment":
                holder.image.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Education":
                holder.image.setImageResource(R.drawable.ic_education);
                break;
            case "Charity":
                holder.image.setImageResource(R.drawable.ic_consultancy);
                break;
            case "Apparel":
                holder.image.setImageResource(R.drawable.ic_shirt);
                break;
            case "Health":
                holder.image.setImageResource(R.drawable.ic_health);
                break;
            case "Personal":
                holder.image.setImageResource(R.drawable.ic_personalcare);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @NonNull
    @Override
    public myViewlet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cardview,parent,false);
        return new TodayAdapter.myViewlet(v);
    }

    public  class myViewlet extends RecyclerView.ViewHolder{
        TextView item, date, amount, month;
        ImageView image;

        public myViewlet(@NonNull View itemView) {

            super(itemView);
            item = itemView.findViewById(R.id.item);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            month = itemView.findViewById(R.id.month);
            image = itemView.findViewById(R.id.imageCardview);



        }
    }
}
