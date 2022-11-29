package learnprogramming.academy.imbizo_wil3_v2.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class MainPassUpdate extends AppCompatActivity {

    String password;
    EditText edtUPassword;
    Button btnUpdatePassword;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pass_update);

        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        progressDialog = new ProgressDialog(this);

        btnUpdatePassword.setOnClickListener(update -> {
            if (areFieldReady()) {
                progressDialog.setTitle("Updating");
                progressDialog.show();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Password is updated", Toast.LENGTH_SHORT).show();

                            finish();

                            Intent intent = new Intent(MainPassUpdate.this, MainActivity.class);
                            startActivity(intent);


                        } else {
                            progressDialog.dismiss();
                            Log.d("TAG", "onComplete: " + task.getException());
                            Toast.makeText(getApplicationContext(), "Please Re-Login to change Password!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.checkView = 5;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainPassUpdate.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }

    private boolean areFieldReady() {

        edtUPassword = findViewById(R.id.edtUPass);
        password = edtUPassword.getText().toString().trim();

        boolean flag = false;
        View requestView = null;

        if (password.isEmpty()) {
            edtUPassword.setError("Field is required");
            flag = true;
            requestView = edtUPassword;
        } else if (password.length() < 8) {
            edtUPassword.setError("Minimum 8 characters");
            flag = true;
            requestView = edtUPassword;
        }

        if (flag) {
            requestView.requestFocus();
            return false;
        } else {
            return true;
        }

    }

}