package com.lamarrulla.mercadomexico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.lamarrulla.mercadomexico.fragments.LoginFragment;

//import dagger.hilt.android.AndroidEntryPoint;

//@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName().toString();
    private final int FB_SIGN_IN = 64206;
    private final int RC_SIGN_IN = 9001;
    private Fragment fragment;
    private Boolean isLoggedInGoogle;
    private Boolean isLoggedInFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*  Valida si esta logueado en facebook */
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedInFB = accessToken != null && !accessToken.isExpired();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        isLoggedInGoogle = account != null && !account.isExpired();

        if(isLoggedInGoogle || isLoggedInFB){
            Intent intent = new Intent(this, BottomNavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            MainActivity.this.finish();
        }else{
            fragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.clyMain, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FB_SIGN_IN||requestCode==RC_SIGN_IN){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Boton personalizado facebook
    // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

}