package com.example.expensetracker;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetracker.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;


public class DashBoardFragment extends Fragment {
    private FloatingActionButton fab_income_button,fab_expense_button,fab_main_button;

    private TextView fab_income_text;
   private TextView fab_expense_text;
   private boolean isOpen=false;

   //Animation
    private Animation FadeOpen,FadeClose;

    //Firebase
     FirebaseAuth firebaseAuth;
    DatabaseReference incomeDatabaseReference;
    DatabaseReference expenseDatabaseReference;
    //TextView
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;

    //RecyclerView
    private RecyclerView incomeRecycler;
    private RecyclerView expenseRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View dashView=  inflater.inflate(R.layout.fragment_dash_board, container, false);
    //Firebase Database
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        String userId=firebaseUser.getUid();
        String url="https://expense-tracker-6647c-default-rtdb.firebaseio.com/";
         incomeDatabaseReference = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId);
         expenseDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId);





      fab_main_button = dashView.findViewById(R.id.fb_main_plus_button);
      fab_income_button=dashView.findViewById(R.id.income_ft_button);
      fab_expense_button = dashView.findViewById(R.id.expense_ft_button);

      fab_income_text=dashView.findViewById(R.id.icome_ft_text);
      fab_expense_text=dashView.findViewById(R.id.expense_ft_text);

     //Total Income and Expense Result Set..
     totalIncomeResult=dashView.findViewById(R.id.income_set_result);
     totalExpenseResult=dashView.findViewById(R.id.expense_set_result);
     //Recycler
        incomeRecycler=dashView.findViewById(R.id.recycler_income_dash);
        expenseRecycler=dashView.findViewById(R.id.recycler_expense_dash);


      //Animation connect..
        FadeOpen=AnimationUtils.loadAnimation(getActivity(),R.anim.flash_open_anim);
        FadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.flash_close_anim);

      fab_main_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              addData();
           if(isOpen){
               fab_income_button.startAnimation(FadeClose);
               fab_expense_button.startAnimation(FadeClose);
               fab_income_button.setClickable(false);
               fab_expense_button.setClickable(false);
               fab_income_text.startAnimation(FadeClose);
               fab_expense_text.startAnimation(FadeClose);
               fab_income_text.setClickable(false);
               fab_expense_text.setClickable(false);
               isOpen=false;

           }else{
               fab_income_button.startAnimation(FadeOpen);
               fab_expense_button.startAnimation(FadeOpen);
               fab_income_button.setClickable(true);
               fab_expense_button.setClickable(true);
               fab_income_text.startAnimation(FadeOpen);
               fab_expense_text.startAnimation(FadeOpen);
               fab_income_text.setClickable(true);
               fab_expense_text.setClickable(true);
               isOpen=true;
           }
          }
      });
          //Calculate total income..
        incomeDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               int totalSum=0;
                for(DataSnapshot mySnapshot:snapshot.getChildren()){
                    Data data=mySnapshot.getValue(Data.class);

                    assert data != null;
                    totalSum+=data.getAmount();

                    String setResult=String.valueOf(totalSum);
                    totalIncomeResult.setText("Rs."+setResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Calculate total expense
        expenseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalSum=0;
                for(DataSnapshot mySnapshot:snapshot.getChildren()){
                    Data data=mySnapshot.getValue(Data.class);

                    assert data != null;
                    totalSum+=data.getAmount();

                    String setResult=String.valueOf(totalSum);
                    totalExpenseResult.setText("Rs."+setResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LinearLayoutManager layoutManagerIncome=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
//        layoutManagerIncome.setStackFromEnd(true);
//        layoutManagerIncome.setReverseLayout(true);
//        incomeRecycler.setHasFixedSize(true);
        incomeRecycler.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
//        layoutManagerExpense.setStackFromEnd(true);
//        layoutManagerExpense.setReverseLayout(true);
//        expenseRecycler.setHasFixedSize(true);
        expenseRecycler.setLayoutManager(layoutManagerExpense);

           return dashView;
    }
    //Animation Class which we can use in any following methods...
    private void ftAnimation(){
        if(isOpen){
            fab_income_button.startAnimation(FadeClose);
            fab_expense_button.startAnimation(FadeClose);
            fab_income_button.setClickable(false);
            fab_expense_button.setClickable(false);
            fab_income_text.startAnimation(FadeClose);
            fab_expense_text.startAnimation(FadeClose);
            fab_income_text.setClickable(false);
            fab_expense_text.setClickable(false);
            isOpen=false;

        }else{
            fab_income_button.startAnimation(FadeOpen);
            fab_expense_button.startAnimation(FadeOpen);
            fab_income_button.setClickable(true);
            fab_expense_button.setClickable(true);
            fab_income_text.startAnimation(FadeOpen);
            fab_expense_text.startAnimation(FadeOpen);
            fab_income_text.setClickable(true);
            fab_expense_text.setClickable(true);
            isOpen=true;
        }
    }
    private void addData(){
        //Income FB Button
        fab_income_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              incomeDataInsert();
            }
        });
        fab_expense_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              expenseDataInsert();
            }
        });
    }
    public void incomeDataInsert(){
        final AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myViewModel=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myViewModel);
        final AlertDialog dialog=myDialog.create();
        dialog.setCancelable(false);

        final EditText enterAmount=myViewModel.findViewById(R.id.amount_enter);
        final EditText enterType=myViewModel.findViewById(R.id.type_enter);
        final EditText enterDesc=myViewModel.findViewById(R.id.description_enter);
        Button saveBtn=myViewModel.findViewById(R.id.btn_save);
        Button cancelBtn=myViewModel.findViewById(R.id.btn_cancel);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=enterType.getText().toString().trim();
                String amount=enterAmount.getText().toString().trim();
                String description=enterDesc.getText().toString().trim();
                if(TextUtils.isEmpty(type)){
                    enterType.setError("Cannot Be Empty..");
                    return;
                }
                if(TextUtils.isEmpty(amount)){
                    enterAmount.setError("Cannot Be Empty..");
                }
                int amountInt=Integer.parseInt(amount);
                if(TextUtils.isEmpty(description)){
                    enterDesc.setError("Cannot Be Empty..");
                }
                String id=incomeDatabaseReference.push().getKey();
                String mDate= DateFormat.getInstance().format(new Date());
                Data data=new Data(amountInt,type,description,id,mDate);
                assert id != null;
                incomeDatabaseReference.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data Added in Database", Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();


            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void expenseDataInsert(){
       final AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myView=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);
        final AlertDialog dialog=myDialog.create();
        dialog.setCancelable(false);
        final EditText amount=myView.findViewById(R.id.amount_enter);
        final EditText type=myView.findViewById(R.id.type_enter);
        final EditText description=myView.findViewById(R.id.description_enter);

        Button btnSave=myView.findViewById(R.id.btn_save);
        Button btnCancel=myView.findViewById(R.id.btn_cancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String strAmount=amount.getText().toString().trim();
                String strType=type.getText().toString().trim();
                String strDescription=description.getText().toString().trim();
                if(TextUtils.isEmpty(strAmount)){
                    amount.setError("Cannot Be Empty..");
                    return;
                }
                if(TextUtils.isEmpty(strType)){
                    type.setError("Cannot Be Empty..");
                }
                int amountInt=Integer.parseInt(strAmount);
                if(TextUtils.isEmpty(strDescription)){
                    description.setError("Cannot Be Empty..");
                }
                String id=expenseDatabaseReference.push().getKey();
                String mDate=DateFormat.getDateInstance().format(new Date());
                Data data=new Data(amountInt,strType,strDescription,id,mDate);
                assert id != null;
                expenseDatabaseReference.child(id).setValue(data);
                Toast.makeText(getActivity(), "Expense data inserted", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(incomeDatabaseReference, Data.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Data, DashBoardFragment.IncomeViewHolder>(options) {

            public DashBoardFragment.IncomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income, parent, false);
                return new IncomeViewHolder(view);
            }

            protected void onBindViewHolder(IncomeViewHolder holder, int position, @NonNull Data model) {
                holder.setIncomeAmount(model.getAmount());
                holder.setIncomeType(model.getType());

                holder.setIncomeDate(model.getDate());


            }
        };
        incomeRecycler.setAdapter(adapter);
        adapter.startListening();
        FirebaseRecyclerOptions<Data> options_exp = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(expenseDatabaseReference, Data.class)
                .build();

        FirebaseRecyclerAdapter exp_adapter = new FirebaseRecyclerAdapter<Data, DashBoardFragment.ExpenseViewHolder>(options_exp) {

            public DashBoardFragment.ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_expense, parent, false);
                return new ExpenseViewHolder(view);
            }

            protected void onBindViewHolder(ExpenseViewHolder holder, int position, @NonNull Data model) {
                holder.setIncomeAmount(model.getAmount());
                holder.setIncomeType(model.getType());

                holder.setIncomeDate(model.getDate());


            }
        };
        expenseRecycler.setAdapter(exp_adapter);
        exp_adapter.startListening();
    }
    //For Income Data
    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
        View mIncomeView;
        public IncomeViewHolder(View itemView){
            super(itemView);
            mIncomeView=itemView;
        }
        public void setIncomeType(String type){
            TextView mType=mIncomeView.findViewById(R.id.type_income_dashboard);
            mType.setText(type);

        }
        public void setIncomeAmount(int amount){
            TextView mAmount=mIncomeView.findViewById(R.id.amount_income_dashboard);
             String strAmount=String.valueOf(amount);
             mAmount.setText(strAmount);
        }
        public void setIncomeDate(String date){
            TextView mDate=mIncomeView.findViewById(R.id.date_income_dashboard);
            mDate.setText(date);
        }

    }

    //For Expense Data
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{
        View mExpenseView;
        public ExpenseViewHolder(View itemView){
            super(itemView);
            mExpenseView=itemView;
        }
        public void setIncomeType(String type){
            TextView mType=mExpenseView.findViewById(R.id.type_expense_dashboard);
            mType.setText(type);

        }
        public void setIncomeAmount(int amount){
            TextView mAmount=mExpenseView.findViewById(R.id.amount_expense_dashboard);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);
        }
        public void setIncomeDate(String date){
            TextView mDate=mExpenseView.findViewById(R.id.date_expense_dashboard);
            mDate.setText(date);
        }

    }

}