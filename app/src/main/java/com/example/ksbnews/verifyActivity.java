package com.example.ksbnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyActivity extends AppCompatActivity {


    private String mVerificationId;

    private EditText et_otp;

    private FirebaseAuth mAuth;

    private Button btn_Verify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        mAuth = FirebaseAuth.getInstance();
        et_otp = findViewById(R.id.et_otp);
        btn_Verify = findViewById(R.id.btn_otp);


        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = et_otp.getText().toString().trim();
                if(code.isEmpty() || code.length() < 6)
                {
                        et_otp.setError("Enter correct OTP");
                        et_otp.requestFocus();
                        return;
                }


                verifyVerificationCode(code);
            }
        });

    }

    private void sendVerificationCode(String mobile)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if(code!= null)
            {
                et_otp.setText(code);
                //verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(verifyActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s, forceResendingToken);


            mVerificationId = s;
        }

    };

    private void verifyVerificationCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);
    }
   /*private void verifyVerificationCode(String code)
    {
        PhoneAuthCredential credential;
        credential = PhoneAuthCredential.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);
    }*/

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(verifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(verifyActivity.this,ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            String message = "Something went wrong.. we will fix it soon...";

                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                message = "Invalid code entered";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {



                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
}
