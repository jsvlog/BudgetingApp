package com.example.budgetingapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BudgetAdapter extends FirebaseRecyclerAdapter<Data, BudgetAdapter.myViewlet> {
private Context context;
private DatabaseReference budgetRef;
private FirebaseAuth mAuth;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BudgetAdapter(@NonNull FirebaseRecyclerOptions<Data> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewlet holder, int position, @NonNull Data model) {
        holder.item.setText(model.getItem());
        holder.amount.setText(String.valueOf(model.getAmount()));
        holder.date.setText(model.getDate());
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

        //when one of item in recyclerview was clicked. this will trigger
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View myView = inflater.inflate(R.layout.update_layout, null);
                myDialog.setView(myView);

                AlertDialog alertDialog = myDialog.create();
                alertDialog.show();
                alertDialog.setCancelable(true);

                EditText date = myView.findViewById(R.id.updateDate);
                EditText item = myView.findViewById(R.id.updateItem);
                EditText amount = myView.findViewById(R.id.updateAmount);
                Button delete = myView.findViewById(R.id.updateDelete);
                Button update = myView.findViewById(R.id.updateUpdate);
                mAuth = FirebaseAuth.getInstance();
                budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(mAuth.getCurrentUser().getUid());

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(calendar.YEAR);
                final int month = calendar.get(calendar.MONTH);
                final int day = calendar.get(calendar.DAY_OF_MONTH);


                date.setText(model.getDate());
                item.setText(model.getItem());
                amount.setText(String.valueOf(model.getAmount()));

                //when click the update button. it will update the clicked item in recyclerview
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uitem = item.getText().toString();
                        int uamount = Integer.parseInt(String.valueOf(amount.getText()));


                        String uid = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
                        android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
                        String udate = dateFormat.format(cal.getTime());

                        MutableDateTime epoch = new MutableDateTime();
                        epoch.setDate(0);
                        DateTime now = new DateTime();
                        Months umonths = Months.monthsBetween(epoch,now);
                        Data data = new Data(uitem,udate,uid,uamount,umonths.getMonths());
                        budgetRef.child(uid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "updated sucessfuly", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(context, "updating failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                //delete button that deletes data from recyclerview
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uid = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                        budgetRef.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(context, "Deleted sucessfuly", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(context, "error deleting", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                //this is for date picker in updating one of the item in recyclerview
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            i1 = i1 + 1;
                            String udate = i2+"-"+i1+"-"+i;
                            date.setText(udate);
                            }
                        },year,month,day);
                        datePickerDialog.show();
                    }
                });
            }
        });



    }

    @NonNull
    @Override
    public myViewlet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cardview,parent,false);
        return new myViewlet(v);
    }

    public class myViewlet extends RecyclerView.ViewHolder{
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
