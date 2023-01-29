package edu.uncc.hw07;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ForumFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAUth = FirebaseAuth.getInstance();
    String TAG = "deep";

    ArrayList<Comment> commentArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    ForumCommentRecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    private static final String FORUM_OBJ = "forum_object";
    private static final String POST_ID = "post_id";

    private Forum forum;
    String post_id;

    public ForumFragment() {
        // Required empty public constructor
    }


    public static ForumFragment newInstance(Forum f) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(FORUM_OBJ, f);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forum = (Forum) getArguments().getSerializable(FORUM_OBJ);
        }
        getActivity().setTitle("Forum");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textViewForumTitle = view.findViewById(R.id.textViewForumTitle);
        TextView textViewForumCreatedBy = view.findViewById(R.id.textViewForumCreatedBy);
        TextView textViewForumText = view.findViewById(R.id.textViewForumText);
        TextView editTextComment = view.findViewById(R.id.editTextComment);
        TextView textViewCommentsCount = view.findViewById(R.id.textViewCommentsCount);

        textViewForumTitle.setText(forum.getForum_title());
        textViewForumText.setText(forum.getForum_description());
        textViewForumCreatedBy.setText(forum.getUser_name());

        view.findViewById(R.id.buttonSubmitComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editTextComment.getText().toString().trim();
                if (comment.isEmpty()){
                    Toast.makeText(getActivity(), "Enter comment to post", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String,Object> val = new HashMap<>();
                    val.put("comment_text",comment);
                    val.put("timestamp",new Timestamp(new Date()));
                    val.put("user_name",mAUth.getCurrentUser().getDisplayName());
                    val.put("user_id",mAUth.getCurrentUser().getUid());

                    db.collection("forum")
                            .document(forum.getPost_id())
                            .collection("comments")
                            .add(val)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
//                                    Toast.makeText(getActivity(), "Comment Posted", Toast.LENGTH_SHORT).show();
                                    editTextComment.setText(null);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        db.collection("forum")
                .document(forum.getPost_id())
                .collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentArrayList.clear();

                        if (value.size() > 0){
                            for (QueryDocumentSnapshot document : value){
                                Comment comment = new Comment();
                                comment.comment_text = document.get("comment_text").toString();
                                comment.timestamp = (Timestamp) document.get("timestamp");
                                comment.user_id = document.get("user_id").toString();
                                comment.user_name = document.get("user_name").toString();
                                comment.comment_id = document.getId();

                                commentArrayList.add(comment);
                            }
                        }

                        textViewCommentsCount.setText(commentArrayList.size()+ " Comments");
                        adapter.notifyDataSetChanged();
                    }
                });

        recyclerView = view.findViewById(R.id.commentsRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ForumCommentRecyclerViewAdapter(commentArrayList,forum.getPost_id());

        recyclerView.setAdapter(adapter);

        super.onViewCreated(view, savedInstanceState);
    }

}