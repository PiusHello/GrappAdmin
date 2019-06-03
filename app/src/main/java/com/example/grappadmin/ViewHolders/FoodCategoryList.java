package com.example.grappadmin.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grappadmin.Interface.ItemClickListener;
import com.example.grappadmin.R;


public class FoodCategoryList extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView FoodCategoryName;
    public ImageView FoodCategoryImage;
    public TextView UploadFood;
    public ItemClickListener listener;

    public FoodCategoryList(@NonNull View itemView) {
        super(itemView);

        FoodCategoryImage = (ImageView) itemView.findViewById(R.id.food_category_image);
        FoodCategoryName = (TextView) itemView.findViewById(R.id.food_category_name);
        UploadFood =(TextView) itemView.findViewById(R.id.uploadFoodCategory);

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
