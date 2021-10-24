package com.sdkproject.nitcsocialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private CircleImageView mcircleImageView;
    private EditText mregistername,mregisteremail,mregisterpassword,mregisterpasswordc;
    private TextView mregisterdob;
    private Button mbtnregister;
    private FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private StorageReference mFireImageFolder;

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mcircleImageView=(CircleImageView)findViewById(R.id.profile_image);
        mregistername=(EditText)findViewById(R.id.registername);
        mregisteremail=(EditText)findViewById(R.id.registeremail);
        mregisterpassword=(EditText)findViewById(R.id.registerpassword);
        mregisterpasswordc=(EditText)findViewById(R.id.registerpasswordc);
        mregisterdob=(TextView)findViewById(R.id.registerdob);
        mbtnregister=(Button)findViewById(R.id.btnregister);
        mAuth=FirebaseAuth.getInstance();
        mcircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photointent=new Intent();
                photointent.setAction(Intent.ACTION_GET_CONTENT);
                photointent.setType("image/*");
                startActivityForResult(photointent,GalleryPick);
            }
        });

        mregisterdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog,mDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date=day+"/"+month+"/"+year;
                mregisterdob.setText(date);
            }
        };
        mbtnregister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ImageUri!=null) {
                if(!mregistername.getText().toString().isEmpty()) {
                    if(!mregisterdob.getText().toString().isEmpty())
                    {
                        String pattern="^[a-zA-Z0-9-.%_]+@(NITC|Nitc|nitc)\\.(AC|Ac|ac)\\.(IN|In|in)$";
                        Pattern p=Pattern.compile(pattern);
                        Matcher m=p.matcher(mregisteremail.getText().toString());
                        if(m.find())
                        {
                            if(mregisterpassword.getText().toString().length()>5)
                            {
                                if(mregisterpassword.getText().toString().equals(mregisterpasswordc.getText().toString()))
                                {
                                    String Name=mregistername.getText().toString();
                                    String DoB=mregisterdob.getText().toString();
                                    String Email=mregisteremail.getText().toString();
                                    String Password=mregisterpassword.getText().toString();
                                    mAuth.createUserWithEmailAndPassword(Email,Password)
                                            .addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        String UserID=mAuth.getCurrentUser().getUid();
                                                        mFireImageFolder=FirebaseStorage.getInstance().getReference().child("ProfileImage")
                                                                .child(UserID);
                                                        mFireImageFolder.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                mFireImageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        String imageUrl=String.valueOf(uri);
                                                                        String Followers="0";
                                                                        String Following="0";
                                                                        DatabaseReference df=FirebaseDatabase.getInstance()
                                                                                .getReference().child("NitcStudent").child(UserID);
                                                                        Map<String,Object> map=new HashMap<String, Object>();
                                                                        map.put("Name",Name);
                                                                        map.put("DoB",DoB);
                                                                        map.put("Email",Email);
                                                                        map.put("Password",Password);
                                                                        map.put("ProfilePhoto",imageUrl);
                                                                        map.put("Followers",Followers);
                                                                        map.put("Following",Following);
                                                                        df.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful())
                                                                                {
                                                                                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                                                }else{
                                                                                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }
                                                }
                                            });
                                }else{
                                    mregisterpasswordc.setError("Password not Matched");
                                }
                            }else{
                                mregisterpassword.setError("Password Must At Least 6 Character");
                            }

                        }else{
                            Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "DoB empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mregistername.setError("Enter Name");
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Load Profile Photo", Toast.LENGTH_SHORT).show();
            }
        }
});



    }

    public void GoToLoginPage(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            //CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
            mcircleImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}