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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TodaySpending extends AppCompatActivity {
        private TextView todaySpent;
        private RecyclerView recyclerView;
        private FloatingActionButton fab;
        private FirebaseAuth mAuth;
        private DatabaseReference todayref;
        private TodayAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_spending);

        todaySpent = findViewById(R.id.todaySpending);
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);

        mAuth = FirebaseAuth.getInstance();
        todayref = FirebaseDatabase.getInstance().getReference().child("expense").child(mAuth.getCurrentUser().getUid());



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSpent();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = todayref.push().getKey();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(todayref,Data.class).build();
        adapter = new TodayAdapter(options,this);


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void addSpent() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.show();
        dialog.setCancelable(true);

        final Spinner itemSpinner = myView.findViewById(R.id.itemSpinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);
        final EditText note = myView.findViewById(R.id.note);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = todayref.push().getKey();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());
                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch,now);

                String spentItem = itemSpinner.getSelectedItem().toString();
                String stringAmount = amount.getText().toString();
                String spentNote = note.getText().toString();

                Data data = new Data(spentItem,date,id,spentNote,Integer.parseInt(stringAmount),months.getMonths());

                todayref.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TodaySpending.this, "Adding spent sucessful", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(TodaySpending.this, "fail to add spent item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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