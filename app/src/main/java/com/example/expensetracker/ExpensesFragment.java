package com.example.expensetracker;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensetracker.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class ExpensesFragment extends Fragment {

//Firebase Database
    private FirebaseAuth firebaseAuth;
    private DatabaseReference expenseDatabase;
    private RecyclerView recyclerView;

    //Update EditText
    private EditText editAmount,editType,editDescription;
    private Button btnUpdate,btnDelete;
    //Data item value
    String type,description,post_key;
    int amount;
    private TextView expenseTotalSum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_expenses, container, false);
       firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String userId=firebaseUser.getUid();
        expenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId);

        expenseTotalSum=view.findViewById(R.id.expense_txt_result);

        recyclerView=view.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        expenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalValue=0;
                for(DataSnapshot mySnapshot:snapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    totalValue += data.getAmount();
                    String strExpenseTotal = String.valueOf(totalValue);

                    expenseTotalSum.setText("Rs. " + strExpenseTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(expenseDatabase, Data.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {

            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false);
                return new MyViewHolder(view);
            }

            protected void onBindViewHolder(MyViewHolder holder, int position, @NonNull Data model) {
                holder.setAmmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key=getRef(position).getKey();

                        type=model.getType();
                        description=model.getNote();
                        amount=model.getAmount();
                        updateExpense();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_text_expense);
            mType.setText(type);
        }

        void setNote(String note) {

            TextView mNote = mView.findViewById(R.id.details_text_expense);
            mNote.setText(note);
        }

        void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.date_text_expense);
            mDate.setText(date);
        }

        void setAmmount(int amount) {
            TextView mAmount = mView.findViewById(R.id.amount_text_expense);
            String stamount = String.valueOf(amount);
            mAmount.setText(stamount);
        }


    }

    private void updateExpense(){
        AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myView=inflater.inflate(R.layout.update_data_item,null);
        myDialog.setView(myView);
        editAmount=myView.findViewById(R.id.amount_enter);
        editType=myView.findViewById(R.id.type_enter);
        editDescription=myView.findViewById(R.id.description_enter);
        //Set data to edit Text
        editType.setText(type);
        editType.setSelection(type.length());

        editDescription.setText(description);
        editDescription.setSelection(description.length());

        editAmount.setText(String.valueOf(amount));
        editAmount.setSelection(String.valueOf(amount).length());

        btnUpdate=myView.findViewById(R.id.btn_upd_update);
        btnDelete=myView.findViewById(R.id.btn_upd_del);
        AlertDialog dialog=myDialog.create();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=editType.getText().toString().trim();
                description=editDescription.getText().toString().trim();
                String strAmount=String.valueOf(amount);
                strAmount=editAmount.getText().toString().trim();

                int myAmount=Integer.parseInt(strAmount);
                String date= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(myAmount,type,description,post_key,date);
                expenseDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
