package com.ttcnpm.nuntius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Person_Profile extends AppCompatActivity {

    ImageView avatarIv, coverIv;
    TextView nameTv,statusTv,emailTv,phoneTv,genderTv,cityTv;
    Button btnsendrequest,btndeclinerequest;

    private DatabaseReference profileUserRef,UserRef;
    private FirebaseAuth mAuth;
    private String senderUserid, receiverUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person__profile);

        Intent intent = getIntent();
        receiverUserid = intent.getStringExtra("visit_user_id");

        mAuth = FirebaseAuth.getInstance();
      //  receiverUserid =  getIntent().getExtras().get("visit_user_id").toString();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //init views
        avatarIv = (ImageView) findViewById(R.id.personavatarIv);
        nameTv = (TextView) findViewById(R.id.personnameTV);
        statusTv =(TextView)findViewById(R.id.personstatusTv);
        emailTv = (TextView) findViewById(R.id.personemailTv);
        phoneTv = (TextView)findViewById(R.id.personphoneTv);
        genderTv = (TextView)findViewById(R.id.persongenderTv);
        cityTv = (TextView)findViewById(R.id.personcityTv);
        coverIv = (ImageView)findViewById(R.id.personcoverIv);
        btnsendrequest = (Button) findViewById(R.id.btn_send_friend_request);
        btndeclinerequest = (Button) findViewById(R.id.btn_decline_friend_request);


        Query userQuery = UserRef.orderByChild("uid").equalTo(receiverUserid);

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until data required get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String status = "" + ds.child("status").getValue();
                    String email = "Email: " + ds.child("email").getValue();
                    String phone = "Số điện thoại: " + ds.child("phone").getValue();
                    String gender = "Giới tính: " + ds.child("gender").getValue();
                    String image = "" + ds.child("image").getValue();
                    String city = "Thành phố: " + ds.child("city").getValue();
                    String cover = "" + ds.child("cover").getValue();
                    //set data
                    nameTv.setText(name);
                    statusTv.setText(status);
                    emailTv.setText(email);
                    phoneTv.setText(phone);
                    genderTv.setText(gender);
                    cityTv.setText(city);

                    try {
                        Picasso.get().load(image).into(avatarIv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_white).into(avatarIv);
                    }

                    try {
                        Picasso.get().load(cover).into(coverIv);
                    } catch (Exception e) {

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
