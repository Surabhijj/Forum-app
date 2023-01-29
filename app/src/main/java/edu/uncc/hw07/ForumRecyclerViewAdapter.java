package edu.uncc.hw07;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ForumRecyclerViewHolder>{

    ArrayList<Forum> arrayList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUser = mAuth.getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ForumRecyclerViewAdapterInterface mListener;

    public ForumRecyclerViewAdapter(ArrayList<Forum> val, ForumRecyclerViewAdapterInterface mListener){
        this.arrayList = val;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ForumRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_row_item,parent,false);
        ForumRecyclerViewHolder forumRecyclerViewHolder = new ForumRecyclerViewHolder(view,mListener);
        return forumRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumRecyclerViewHolder holder, int position) {
        Forum forum = arrayList.get(position);
        holder.textViewForumTitle.setText(forum.getForum_title());
        holder.textViewForumCreatedBy.setText(forum.getUser_name());
        String s = forum.getForum_description();
        String finalDesc = s.substring(0, Math.min(s.length(), 95));
        if (s.length() > 94){
            finalDesc = finalDesc + "...";
        }
        holder.textViewForumText.setText(finalDesc);
        holder.textViewForumLikesDate.setText(forum.getLikes().size()+" Likes | "+forum.getTimestamp());

        if (currentUser.equalsIgnoreCase(forum.getUser_id())){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }else{
            holder.imageViewDelete.setVisibility(View.GONE);
        }

//        String[] likesVal = forum.getLikes();
        ArrayList<String> likesVal = forum.getLikes();

        if (likesVal.contains(currentUser)){
            holder.imageViewLike.setBackgroundResource(R.drawable.like_favorite);
            holder.imageViewLike.setTag(R.drawable.like_favorite);
        }else{
            holder.imageViewLike.setBackgroundResource(R.drawable.like_not_favorite);
            holder.imageViewLike.setTag(R.drawable.like_not_favorite);
        }


        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("forum")
                        .document(forum.getPost_id())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(view.getContext(), "Forum Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = (ImageView) view;
                assert(R.id.imageViewLike == imageView.getId());

                // See here
                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;

                ArrayList<String> currentLikeList = forum.getLikes();

                switch (integer) {
                    case R.drawable.like_favorite:
                        currentLikeList.remove(currentUser);
                        break;
                    case R.drawable.like_not_favorite:
                        currentLikeList.add(currentUser);
                        break;

                }
                db.collection("forum").document(forum.getPost_id())
                        .update("likes",currentLikeList)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openCommentsOfForum(forum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public static class ForumRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView textViewForumTitle;
        TextView textViewForumCreatedBy;
        TextView textViewForumText;
        TextView textViewForumLikesDate;
        ImageView imageViewDelete;
        ImageView imageViewLike;
        View rootView;
        ForumRecyclerViewAdapterInterface mListener;

        public ForumRecyclerViewHolder(@NonNull View itemView, ForumRecyclerViewAdapterInterface mListener) {
            super(itemView);
            rootView = itemView;
            this.mListener = mListener;
            textViewForumTitle = itemView.findViewById(R.id.textViewForumTitle);
            textViewForumCreatedBy = itemView.findViewById(R.id.textViewForumCreatedBy);
            textViewForumText = itemView.findViewById(R.id.textViewForumText);
            textViewForumLikesDate = itemView.findViewById(R.id.textViewForumLikesDate);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            imageViewLike = (ImageView) itemView.findViewById(R.id.imageViewLike);

        }
    }

    interface ForumRecyclerViewAdapterInterface{
        void openCommentsOfForum(Forum f);
    }


}
