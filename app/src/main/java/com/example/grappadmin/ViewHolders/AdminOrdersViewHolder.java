package com.example.grappadmin.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.grappadmin.Interface.ItemClickListener;
import com.example.grappadmin.R;

import java.text.BreakIterator;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView foodItems;
    public TextView foodQuantity;
    public TextView foodPrice;
    public TextView foodDate;
    public TextView foodTime;
    public TextView buyer;
    public TextView buyersPhone;
    public TextView buyersLocation;
    public ItemClickListener listener;
    public AdminOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        foodItems = itemView.findViewById(R.id.foodname);
        foodQuantity = itemView.findViewById(R.id.foodquantity);
        foodPrice = itemView.findViewById(R.id.foodp);
        foodDate = itemView.findViewById(R.id.firstdate);
        foodTime = itemView.findViewById(R.id.firsttime);



    }

    public  void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
