package learnprogramming.academy.imbizo_wil3_v2.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import learnprogramming.academy.imbizo_wil3_v2.Create.CreateCategory;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class MainEmailUpdate extends AppCompatActivity {

    Button btnUpdateEmail;
    EditText edtUEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.checkView = 5;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainEmailUpdate.this, MainActivity.class);
        startActivity(intent);
        finish();
        MainActivity.checkView = 5;
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_email_update);

        btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
        edtUEmail = findViewById(R.id.edtUEmail);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String email1 = currentUser.getEmail();
        String[] tokens = email1.split("@");

        btnUpdateEmail.setOnClickListener(update -> {

            String email = edtUEmail.getText().toString().trim();
            String[] tokens1 = email.split("@");
            if (email.isEmpty()) {
                edtUEmail.setError("Email is required");
                edtUEmail.requestFocus();
            } else {
                progressDialog.setTitle("Updating");
                progressDialog.show();



                currentUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(tokens[0]);
                            databaseReference.removeValue();

                            FirebaseDatabase databaseCategories = FirebaseDatabase.getInstance();
                            DatabaseReference refCategories = databaseCategories.getReference("Users");
                            DatabaseReference newPost = refCategories.child(tokens1[0]);
                            newPost.child("name").setValue(email);
                            newPost.child("type").setValue("user");


                            progressDialog.dismiss();

                            finish();

                            Intent intent = new Intent(MainEmailUpdate.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            progressDialog.dismiss();
                            Log.d("TAG", "onComplete: " + task.getException());
                            Toast.makeText(getApplicationContext(), "Please Re-Login to change Email!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

    }


}