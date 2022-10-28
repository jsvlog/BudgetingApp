package com.example.budgetingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;


public class TodayAdapter extends FirebaseRecyclerAdapter<Data, TodayAdapter.myViewlet>{

private Context context;
private DatabaseReference todayRef;
private FirebaseAuth mAuth;
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
                AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View myView = inflater.inflate(R.layout.update_layout, null);
                myDialog.setView(myView);

                final AlertDialog dialog = myDialog.create();
                dialog.show();
                dialog.setCancelable(true);

                final EditText updateDate = myView.findViewById(R.id.updateDate);
                final EditText updateItem = myView.findViewById(R.id.updateItem);
                final EditText updateAmount = myView.findViewById(R.id.updateAmount);
                final Button update = myView.findViewById(R.id.updateUpdate);
                final  Button delete = myView.findViewById(R.id.updateDelete);

                mAuth = FirebaseAuth.getInstance();

                todayRef = FirebaseDatabase.getInstance().getReference().child("expense").child(mAuth.getCurrentUser().getUid());

                updateDate.setText(model.getDate());
                updateItem.setText(model.getItem());
                updateAmount.setText(String.valueOf(model.getAmount()));

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String updatedDate = updateDate.getText().toString();
                        String updatedItem = updateItem.getText().toString();
                        String id = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                        int updatedAmount = Integer.parseInt(updateAmount.getText().toString());
                        MutableDateTime epoch = new MutableDateTime();
                        epoch.setDate(0);
                        DateTime now = new DateTime();
                        Months months = Months.monthsBetween(epoch,now);

                        Data data = new Data(updatedItem,updatedDate,id,updatedAmount,months.getMonths());

                        todayRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Updating Successful", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(context, "Update error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = getRef(holder.getAbsoluteAdapterPosition()).getKey();

                        todayRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(context, "deleting item error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });



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
