package org.hitam.epics.biswajeet.anewbeginning;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText EmailEditText;
    private EditText PasswordEditText;
    private EditText PhoneEditText;
    private EditText ConfirmPasswordEditText;
    private EditText FullNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EmailEditText = findViewById(R.id.email);
        PasswordEditText = findViewById(R.id.password);
        PhoneEditText = findViewById(R.id.Phone);
        ConfirmPasswordEditText = findViewById(R.id.confirm_password);
        FullNameEditText = findViewById(R.id.fullname);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };


    }

    public void register(View view) {
        String FullName = FullNameEditText.getText().toString().trim();
        String Email = EmailEditText.getText().toString().trim();
        String Phone = PhoneEditText.getText().toString().trim();
        String Password = PasswordEditText.getText().toString().trim();
        String ConfirmPassword = ConfirmPasswordEditText.getText().toString().trim();

        /*
        * Condition check
        * 1. FullName should not be empty
        * 2. Email should be well formed i.e user@domain.com    //this is not actually necessary.. firebase will reject the request
        *       a. start with number or alphabet
        *       b. has @ and .
        *       c. @ and . are not together means user@.com is invalid
        *       d. . is not last char
        * 3. Phone number should be well formed i.e. minimum 10 digits (or 14 digits with country coude = max limit not required)
        * 4. Password should be minimum 6 characters long
        * 5. Password and ConfimPassword should match
        *
        * Implement all these using if else condition.
        * Wherever condition becomes false. set error in that field
        * and return from the function.
        * */

        boolean errors = false;
        if (FullName.length() == 0) {
            FullNameEditText.setError("Name Required");
            errors = true;
        }

        if (Phone.length() < 10) {
            PhoneEditText.setError("Invalid Phone number");
            errors = true;
        }
        if (Password.length() < 6) {
            PasswordEditText.setError("Minimum Password Size is 6");
            errors = true;
        }

        if (!ConfirmPassword.equals(Password)) {
            ConfirmPasswordEditText.setError("Password did not Match");
            errors = true;
        }
        if (errors) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(Email, Password)   //place user email id and password here
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            String[] s = task.getException().getMessage().split(":");
                            if (s.length == 1) {
                                builder.setMessage(s[0]);
                            } else {
                                builder.setMessage(s[1]);
                            }

                            builder.setPositiveButton("Close", null);
                            builder.create().show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            RegisterActivity.this.finish();
                        }
                    }
                });
    }
}
