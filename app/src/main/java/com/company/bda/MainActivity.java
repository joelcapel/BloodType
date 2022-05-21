package com.company.bda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.company.bda.Adapter.UserAdapter;
import com.company.bda.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_imagen_perfil;
    private TextView nav_nombre, nav_email, nav_gruposangre, nav_tipo;

    private DatabaseReference userRef;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<User> userList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Type");

        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.progressbar);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this, userList);

        recyclerView.setAdapter(userAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tipo = snapshot.child("type").getValue().toString();
                if (tipo.equals("donante")){
                    leerReceptores();
                }else{
                    leerDonantes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nav_imagen_perfil = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_nombre = nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_gruposangre = nav_view.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_tipo = nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("nombre").getValue().toString();
                    nav_nombre.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    String gruposangre = snapshot.child("gruposangre").getValue().toString();
                    nav_gruposangre.setText(gruposangre);

                    String tipo = snapshot.child("type").getValue().toString();
                    nav_tipo.setText(tipo);

                    if (snapshot.hasChild("profilepictureurl")){
                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_imagen_perfil);
                    }else{
                        nav_imagen_perfil.setImageResource(R.drawable.profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void leerDonantes() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("donante");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No hay donantes", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void leerReceptores() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query = reference.orderByChild("type").equalTo("receptor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No hay receptores", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.amas:
                Intent intent3 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent3.putExtra("grupo", "A+");
                startActivity(intent3);
                break;

            case R.id.amenos:
                Intent intent4 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent4.putExtra("grupo", "A-");
                startActivity(intent4);
                break;

            case R.id.bmas:
                Intent intent5 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent5.putExtra("grupo", "B+");
                startActivity(intent5);
                break;

            case R.id.bmenos:
                Intent intent6 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent6.putExtra("grupo", "B-");
                startActivity(intent6);
                break;

            case R.id.abmas:
                Intent intent7 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent7.putExtra("grupo", "AB+");
                startActivity(intent7);
                break;

            case R.id.abmenos:
                Intent intent8 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent8.putExtra("grupo", "AB-");
                startActivity(intent8);
                break;

            case R.id.omas:
                Intent intent9 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent9.putExtra("grupo", "O+");
                startActivity(intent9);
                break;

            case R.id.omenos:
                Intent intent10 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent10.putExtra("grupo", "O-");
                startActivity(intent10);
                break;

            case R.id.perfil:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                break;

            case R.id.compatible:
                Intent intent11 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent11.putExtra("grupo", "Compatible conmigo");
                startActivity(intent11);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}