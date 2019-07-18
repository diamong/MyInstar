package com.diamong.myinstar.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diamong.myinstar.EditProfileActivity;
import com.diamong.myinstar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Adapter.MyFotoAdapter;
import Model.Post;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView imageProfile;
    ImageView imageOptions;
    TextView posts, followers, following, username, bio, nickname;
    Button buttonProfile;

    private List<String> mySaves;
    MyFotoAdapter myFotoAdapterSaves;
    List<Post> postListSaves;

    FirebaseUser firebaseUser;
    String profileId;


    RecyclerView recyclerViewMyfotos, recyclerViewSavedfotos;

    ImageButton buttonMyFotos, buttonSavedFotos;

    MyFotoAdapter myFotoAdapter;
    List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileid", "none");

        recyclerViewMyfotos = view.findViewById(R.id.profile_recyclerview_fotos);
        recyclerViewSavedfotos = view.findViewById(R.id.profile_recyclerview_saved);

        imageProfile = view.findViewById(R.id.profile_imageview_profile);
        imageOptions = view.findViewById(R.id.profile_imageview_options);
        posts = view.findViewById(R.id.profile_textview_posts);
        followers = view.findViewById(R.id.profile_textview_followers);
        following = view.findViewById(R.id.profile_textview_following);
        username = view.findViewById(R.id.profile_textview_username);
        bio = view.findViewById(R.id.profile_textview_bio);
        nickname = view.findViewById(R.id.profile_textview_nickname);
        buttonMyFotos = view.findViewById(R.id.profile_imagebutton_myfotos);
        buttonSavedFotos = view.findViewById(R.id.profile_imagebutton_saved);
        buttonProfile = view.findViewById(R.id.profile_button_editprofile);

        recyclerViewMyfotos.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerViewMyfotos.setLayoutManager(linearLayoutManager);
        postList= new ArrayList<>();
        myFotoAdapter= new MyFotoAdapter(getContext(),postList);
        recyclerViewMyfotos.setAdapter(myFotoAdapter);

        recyclerViewSavedfotos.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new GridLayoutManager(getContext(),3);
        recyclerViewSavedfotos.setLayoutManager(linearLayoutManager1);
        postListSaves= new ArrayList<>();
        myFotoAdapterSaves= new MyFotoAdapter(getContext(),postListSaves);
        recyclerViewSavedfotos.setAdapter(myFotoAdapterSaves);

        recyclerViewMyfotos.setVisibility(View.VISIBLE);
        recyclerViewSavedfotos.setVisibility(View.GONE);

        userInfo();
        getFollowers();
        getNrPosts();
        myFotos();
        beSave();

        if (profileId.equals(firebaseUser.getUid())) {
            buttonProfile.setText(getString(R.string.edit_profile));
        } else {
            checkFollow();
            buttonSavedFotos.setVisibility(View.GONE);
        }

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = buttonProfile.getText().toString();

                if (btn.equals(getString(R.string.edit_profile))) {

                    startActivity(new Intent(getContext(), EditProfileActivity.class));


                } else if (btn.equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                } else if (btn.equals("following")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        buttonMyFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMyfotos.setVisibility(View.VISIBLE);
                recyclerViewSavedfotos.setVisibility(View.GONE);
            }
        });

        buttonSavedFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMyfotos.setVisibility(View.GONE);
                recyclerViewSavedfotos.setVisibility(View.VISIBLE);
            }
        });




        return view;
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageurl()).into(imageProfile);
                nickname.setText(user.getNickname());
                username.setText(user.getUsername());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileId).exists()) {
                    buttonProfile.setText("following");

                } else {
                    buttonProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getNrPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)) {
                        i++;
                    }
                }

                posts.setText("" + i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)){
                        postList.add(post);
                    }
                }

                Collections.reverse(postList);
                myFotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void beSave(){
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        mySaves.add(snapshot.getKey());
                    }

                    readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readSaves() {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postListSaves.clear();
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);

                        for (String id :mySaves){
                            if (post.getPostid().equals(id)){
                                postListSaves.add(post);
                            }
                        }
                    }

                    myFotoAdapterSaves.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

}
