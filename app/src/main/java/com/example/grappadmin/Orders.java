package com.example.grappadmin;

import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grappadmin.Model.AdminOrders;
import com.example.grappadmin.Model.Users;
import com.example.grappadmin.ViewHolders.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Orders extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef,databaseReference;
    private FirebaseAuth firebaseAuth;
    private ImageView confirmorders, deleteorders;
    String currentUserID,currentUserEmail;

    public TextView userphone,username,userlocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new  LinearLayoutManager(this));
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        firebaseAuth = FirebaseAuth.getInstance();
        confirmorders = (ImageView) findViewById(R.id.confirmOrders);
        deleteorders = (ImageView) findViewById(R.id.deleteOrders);



        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();


        userphone = (TextView) findViewById(R.id.ownerPhone);
        userlocation = (TextView) findViewById(R.id.ownerLocation);
        username = (TextView) findViewById(R.id.owner);

}
    public  String userNameFromEmail(String email)
    {
        if(email.contains("@"))
        {
            return email.split("@")[0];
        }
        else
        {
            return email;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options
                = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef.child("UsersView"),
                        AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {

                        holder.foodItems.setText(" Food : " + model.getItemsBought());
                        holder.foodQuantity.setText(" Quantity : " + model.getQuantity());
                        holder.foodPrice.setText(" Price GHC  " + model.getPrice());
                        holder.foodDate.setText(" Date : " + model.getDate());
                        holder.foodTime.setText(" Time : " + model.getTime());
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_details,viewGroup,false);
                        AdminOrdersViewHolder holder = new AdminOrdersViewHolder(view);
                        return holder;
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }




    }
