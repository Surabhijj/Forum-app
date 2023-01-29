package edu.uncc.hw07;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ForumCommentRecyclerViewAdapter extends RecyclerView.Adapter<ForumCommentRecyclerViewAdapter.ForumCommentRecyclerViewHolder> {

    ArrayList<Comment> commentArrayList = new ArrayList<>();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String currentUserId = mAuth.getCurrentUser().getUid();
    String post_id;

    public ForumCommentRecyclerViewAdapter(ArrayList<Comment> commentArrayList, String post_id) {
        this.commentArrayList = commentArrayList;
        this.post_id = post_id;
    }

    @NonNull
    @Override
    public ForumCommentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row_item,parent,false);
        ForumCommentRecyclerViewHolder forumCommentRecyclerViewHolder = new ForumCommentRecyclerViewHolder(view);
        return forumCommentRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumCommentRecyclerViewHolder holder, int position) {
        Comment comment = commentArrayList.get(position);
        holder.textViewCommentText.setText(comment.getComment_text());
        holder.textViewCommentCreatedAt.setText(comment.getTimestamp());
        holder.textViewCommentCreatedBy.setText(comment.getUser_name());

        if (comment.getUser_id().equalsIgnoreCase(currentUserId)){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }else{
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("forum")
                        .document(post_id)
                        .collection("comments")
                        .document(comment.comment_id)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                }else{
                                    Toast.makeText(view.getContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.commentArrayList.size();
    }


    public static class ForumCommentRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCommentCreatedBy;
        TextView textViewCommentText;
        TextView textViewCommentCreatedAt;
        ImageView imageViewDelete;
        View rootView;

        public ForumCommentRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewCommentCreatedBy = itemView.findViewById(R.id.textViewCommentCreatedBy);
            textViewCommentText = itemView.findViewById(R.id.textViewCommentText);
            textViewCommentCreatedAt = itemView.findViewById(R.id.textViewCommentCreatedAt);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
