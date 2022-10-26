package com.example.budgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.time.Month;

public class BudgetActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef;
    private RecyclerView recyclerView;
    private TextView totalBudget;
    private BudgetAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(mAuth.getCurrentUser().getUid());
        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recyclerview);
        totalBudget = findViewById(R.id.totalbudget);
        totalBudget = findViewById(R.id.totalbudget);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });

        //for total budget. it will calculate the total budget from budgetref. it will calculate the total using model Data
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;

                for (DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    totalAmount = totalAmount + data.getAmount();
                    String sTotal = String.valueOf("Month Budget: $" + totalAmount);
                    totalBudget.setText(sTotal);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //this is for adding data from firebase to my recyclerview, onstart and on stop
    @Override
    protected void onStart() {
        super.onStart();
        String id = budgetRef.push().getKey();
        FirebaseRecyclerOptions<Data> options =new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budgetRef,Data.class).build();
        adapter = new BudgetAdapter(options,this);


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //add item to database
    private void additem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.show();
        dialog.setCancelable(false);


        final Spinner itemSpinner = myView.findViewById(R.id.itemSpinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String budgetItem = itemSpinner.getSelectedItem().toString();
                String budgetAmount = amount.getText().toString();

                if(budgetItem.equals("select item")){
                    Toast.makeText(BudgetActivity.this, "please select item", Toast.LENGTH_SHORT).show();

                }
                if(budgetAmount.isEmpty()){
                    amount.setError("please enter amount");

                }else{


                    String id = budgetRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch,now);

                    Data data = new Data(budgetItem,date,id,null,Integer.parseInt(budgetAmount),months.getMonths());

                    budgetRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BudgetActivity.this,"added successfuly",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(BudgetActivity.this,"failed to add",Toast.LENGTH_LONG).show();
                        }

                        }
                    });


                }
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
}



