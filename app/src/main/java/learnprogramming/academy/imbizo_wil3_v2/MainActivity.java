package learnprogramming.academy.imbizo_wil3_v2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import learnprogramming.academy.imbizo_wil3_v2.Fragments.AccountFragment;
import learnprogramming.academy.imbizo_wil3_v2.Fragments.CommunityPageFragment;
import learnprogramming.academy.imbizo_wil3_v2.Fragments.ContactFragment;
import learnprogramming.academy.imbizo_wil3_v2.Fragments.HomeFragment;
import learnprogramming.academy.imbizo_wil3_v2.Main.MainLectures;
import learnprogramming.academy.imbizo_wil3_v2.Main.MainLogin;
import learnprogramming.academy.imbizo_wil3_v2.Main.MainSavedLectures;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public static int checkView;
    public String usernameEmail;
    NavigationView navigationView;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser currentUser;
    FirebaseDatabase databaseCategories;

    DatabaseReference refCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();
        usernameEmail = currentUser.getEmail();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    Intent intent = new Intent(MainActivity.this, MainLogin.class);
                    startActivity(intent);
                }
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.homeNB);
        }
        if(checkView == 2) {
            replaceFragment(new MainLectures());
        }
        if(checkView == 5) {
            replaceFragment(new AccountFragment());
            navigationView.setCheckedItem(R.id.accountNB);
        }
        if(checkView == 4) {
            replaceFragment(new CommunityPageFragment());
            navigationView.setCheckedItem(R.id.communityNB);
        }
        if(checkView == 6) {
            replaceFragment(new MainSavedLectures());
            navigationView.setCheckedItem(R.id.savedLecturesNB);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        if(checkView == 0) {
            replaceFragment(new HomeFragment());
        }
        if(checkView == 1) {
            replaceFragment(new HomeFragment());
        }
        if(checkView == 2) {
            replaceFragment(new MainLectures());
        }
        if(checkView == 3) {
            replaceFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.homeNB);
        }
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.savedLecturesNB:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainSavedLectures()).commit();
                break;
            case R.id.homeNB:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
                break;
            case R.id.accountNB:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountFragment()).commit();
                break;
            case R.id.communityNB:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CommunityPageFragment()).commit();
                break;
            case R.id.comWhatsApp:
                WhatsAppFeature();
                break;
            case R.id.comCommunicate:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ContactFragment()).commit();
                break;
            case R.id.personalityNB:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.16personalities.com/free-personality-test"));
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void WhatsAppFeature()
    {
        String num = "+27606722100";
        String text = "Hi, This is <Name>! I Would like you to assist me!";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone="+num+"&text="+text));
        startActivity(intent);


    }
}