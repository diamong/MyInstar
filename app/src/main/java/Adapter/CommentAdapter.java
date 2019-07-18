package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diamong.myinstar.MainActivity;
import com.diamong.myinstar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Model.Comment;
import Model.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mComment;

    private FirebaseUser firebaseUser;

    public CommentAdapter(Context mContext, List<Comment> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,viewGroup,false);



        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComment.get(i);

        viewHolder.comment.setText(comment.getComment());
        getUserInfo(viewHolder.imageProfile,viewHolder.nickname,comment.getPublisher());

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid",comment.getPublisher());
                mContext.startActivity(intent);
            }
        });

        viewHolder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid",comment.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageProfile;
        public TextView nickname,comment;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile=itemView.findViewById(R.id.commentitem_imageview_profile);
            nickname=itemView.findViewById(R.id.commentitem_textview_nickname);
            comment=itemView.findViewById(R.id.commentitem_textview_comment);
        }
    }

    private void getUserInfo(final ImageView imageView, final TextView nickname, String publisherId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(imageView);
                nickname.setText(user.getNickname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
