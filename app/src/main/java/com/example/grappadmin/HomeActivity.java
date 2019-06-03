package com.example.grappadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.grappadmin.Model.AdminOrders;
import com.example.grappadmin.Model.FoodCategory;
import com.example.grappadmin.Prevalent.Prevalent;
import com.example.grappadmin.ViewHolders.FoodCategoryList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String currentUserID,currentUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);

        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Paper.init(this);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Panel");
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    String username = userNameFromEmail(currentUserEmail);

    View headerView = navigationView.getHeaderView(0);
    final TextView profileName = headerView.findViewById(R.id.managersName);
    final CircleImageView profileImage = headerView.findViewById(R.id.profile_image);

        profileName.setText(username);

        databaseReference.child(currentUserID).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists())
            {
                String fullname = dataSnapshot.child("Username").getValue().toString();
                Log.v("username",""+fullname+"");
                // profileName.setText(fullname);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
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

        FirebaseRecyclerOptions<FoodCategory> options=
                new FirebaseRecyclerOptions.Builder<FoodCategory>()
                        .setQuery(databaseReference,FoodCategory.class)
                        .build();

        FirebaseRecyclerAdapter<FoodCategory, FoodCategoryList> adapter=
                new FirebaseRecyclerAdapter<FoodCategory, FoodCategoryList>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FoodCategoryList holder, int position, @NonNull FoodCategory model)
                    {
                        holder.FoodCategoryName.setText(model.getCategoryName());
                        holder.UploadFood.setText(model.getUploadFoodCategory());
                        Picasso.get().load(model.getCategoryImage()).into(holder.FoodCategoryImage);

                        //final String food_key =getRef(position).getKey().toString();
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this,UploadFood.class);
                                //intent.putExtra("CategoryID",food_key);
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FoodCategoryList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_category_list,viewGroup,false);
                        FoodCategoryList holder = new FoodCategoryList(view);
                        return holder;


                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification_badge) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders)
        {
            Intent gotoOrderActivity = new Intent(HomeActivity.this, Orders.class);
            startActivity(gotoOrderActivity);
            // Handle the camera action
        } else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent loginIntent = new Intent(HomeActivity.this,LoginAdminActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        }

        else if (id == R.id.nav_upload)
        {
            Intent gotoUploadActivity = new Intent(HomeActivity.this,UploadFood.class);
            startActivity(gotoUploadActivity);
        }

//        else if (id == R.id.nav_orders)
//        {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
