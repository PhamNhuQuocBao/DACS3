package com.example.kintube.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kintube.Model.Comment;
import com.example.kintube.Model.User.User;
import com.example.kintube.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final List<Comment> listComment;
    private String imgUser, strUsername;
    Context context;

    public CommentAdapter(List<Comment> listComment, Context context) {
        this.listComment = listComment;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = listComment.get(position);
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(comment.getUser_id())) {
                    User user = snapshot.child(comment.getUser_id()).getValue(User.class);
                    imgUser = user.getImageUser();
                    strUsername = user.getName();
                    holder.imgUserComment.setImageResource(R.drawable.profile_default);
                    holder.tvUsername.setText(strUsername);
                    holder.tvContentComment.setText(comment.getContent());
                    holder.tvDateComment.setText(comment.getDate_comment());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System.out.println("============================" + strUsername + "========================");

    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgUserComment;
        public TextView tvUsername, tvDateComment, tvContentComment;
        public ViewHolder(View itemView) {
            super(itemView);
            imgUserComment = itemView.findViewById(R.id.tvImageUserComment);
            tvUsername = itemView.findViewById(R.id.tvUsernameComment);
            tvDateComment = itemView.findViewById(R.id.tvDateComment);
            tvContentComment = itemView.findViewById(R.id.tvContentComment);
        }
    }
}
