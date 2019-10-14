
package com.example.ksbnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ksbnews.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText et_mob;
    private Button btn_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_mob =findViewById(R.id.et_mob);

        btn_otp =findViewById(R.id.btn_otp);

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = et_mob.getText().toString().trim();
                if(mobile.isEmpty() || mobile.length() < 10)
                {
                    et_mob.setError("Enter a valid mobile...");
                    et_mob.requestFocus();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this,verifyActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

    }

}
