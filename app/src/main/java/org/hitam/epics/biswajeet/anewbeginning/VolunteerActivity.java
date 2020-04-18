package org.hitam.epics.biswajeet.anewbeginning;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.hitam.epics.biswajeet.anewbeginning.support.Mailing;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

public class VolunteerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        ActionBar actionBar=getActionBar();
        actionBar.hide();
    }
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(VolunteerActivity.this,"There is no back action", Toast.LENGTH_LONG).show();
        return;
    }


    public void donate(View view) {
        startActivity(new Intent(this, DonateActivity.class));
    }

    public void Logout(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(VolunteerActivity.this,HomeActivity.class));
                        finish();
                    }
                });
    }

    public void events(View view) {
        startActivity(new Intent(this, EventsActivity.class));
    }

}
