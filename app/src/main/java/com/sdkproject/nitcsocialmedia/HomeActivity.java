package com.sdkproject.nitcsocialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private CircleImageView mhomeprofilephoto;
    private TextView mhomeFollowers,mhomeFollowing;
    String UserName,UserDob,UserEmail,UserProfilePhoto,UserFollowers,UserFollowing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mhomeprofilephoto=(CircleImageView)findViewById(R.id.homeprofilephoto);
        mhomeFollowers=(TextView)findViewById(R.id.homeFollowers);
        mhomeFollowing=(TextView)findViewById(R.id.homeFollowing);



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference df=FirebaseDatabase.getInstance().getReference().child("NitcStudent")
                .child(mAuth.getCurrentUser().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    MyData myData=dataSnapshot.getValue(MyData.class);
                    UserName=myData.getName();
                    UserDob=myData.getDoB();
                    UserEmail=myData.getEmail();
                    UserProfilePhoto=myData.getProfilePhoto();
                    UserFollowers=myData.getFollowers();
                    UserFollowing=myData.getFollowing();

                    Picasso.get().load(UserProfilePhoto).into(mhomeprofilephoto);
                    mhomeFollowers.setText(UserFollowers);
                    mhomeFollowing.setText(UserFollowing);
                }
            }
        });
    }
}