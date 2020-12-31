package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class IncomeFragment extends Fragment {

    //Firebase DB
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    LinearLayoutManager layoutManager;
    //RecyclerView
    private RecyclerView recyclerView;

    private TextView incomeTotal;

    //Update EditText
    private EditText editAmount,editType,editDescription;
    private Button btnUpdate,btnDelete;
    //Data item value
    String type,description,post_key;
    int amount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotal=myview.findViewById(R.id.income_txt_result);

        recyclerView = myview.findViewById(R.id.recycler_id_income);

         layoutManager = new LinearLayoutManager(getActivity());

//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           int totalValue=0;
           for(DataSnapshot mySnapshot:snapshot.getChildren()){
               Data data=mySnapshot.getValue(Data.class);
               totalValue+=data.getAmount();
               String strTotalValue=String.valueOf(totalValue);

               incomeTotal.setText("Rs. "+strTotalValue);

           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDatabase, Data.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {

            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false);
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
                        updateIncomeData();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
   public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

    public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }

        void setNote(String note) {

            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }

        void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        void setAmmount(int ammount) {
            TextView mAmmount = mView.findViewById(R.id.ammount_txt_income);
            String stammount = String.valueOf(ammount);
            mAmmount.setText(stammount);
        }

    }

    private void updateIncomeData(){
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
                 mIncomeDatabase.child(post_key).setValue(data);

                 dialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDatabase.child(post_key).removeValue();

           dialog.dismiss();
            }
        });
        dialog.show();
    }
}


