package com.lamarrulla.mercadomexico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class BottomNavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private Boolean isLoggedInFB;
    private Boolean isLoggedInGoogle;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                logOut();
                break;
        }
    }

    private void logOut() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedInFB = accessToken != null && !accessToken.isExpired();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        isLoggedInGoogle = account != null && !account.isExpired();

        if(isLoggedInFB){
            LoginManager.getInstance().logOut();
            regresaMain();
        }else if(isLoggedInGoogle){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Toast.makeText(BottomNavigationActivity.this, "Usuario deslogeado", Toast.LENGTH_SHORT).show();
                            regresaMain();
                        }
                    });
        }else{
            Toast.makeText(this, "Exite un error al deslogear", Toast.LENGTH_SHORT).show();
        }
    }

    private void regresaMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}