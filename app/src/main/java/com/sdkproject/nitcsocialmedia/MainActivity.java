package com.sdkproject.nitcsocialmedia;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText mloginemail,mloginpassword;
    private Button mloginbtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mloginemail=(EditText)findViewById(R.id.loginemail);
        mloginpassword=(EditText)findViewById(R.id.loginpassword);
        mloginbtn=(Button)findViewById(R.id.btnlogin);
        mAuth=FirebaseAuth.getInstance();


        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mloginemail.getText().toString().isEmpty())
                {
                    String pattern="^[A-Za-z0-9._%]+@(NITC|Nitc|nitc)\\.(AC|ac|Ac)\\.(IN|In|in)$";
                    Pattern p=Pattern.compile(pattern);
                    Matcher m=p.matcher(mloginemail.getText().toString());
                    if(m.find())
                    {
                        if(!mloginpassword.getText().toString().isEmpty())
                        {
                            String email=mloginemail.getText().toString();
                            String password=mloginpassword.getText().toString();
                            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                    }else{
                                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            mloginpassword.setError("Enter Password");
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Not", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mloginemail.setError("Enter Email");
                }
            }
        });
    }

    public void GoToRegisterActivity(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }
}