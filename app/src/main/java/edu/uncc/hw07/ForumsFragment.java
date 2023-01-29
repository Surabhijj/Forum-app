package edu.uncc.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ForumsFragment extends Fragment implements ForumRecyclerViewAdapter.ForumRecyclerViewAdapterInterface{

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String TAG = "deep";

    ArrayList<Forum> forumArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ForumRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle("Forums");
        super.onCreate(savedInstanceState);
    }

    public ForumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection("forum")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                forumArrayList.clear();

                                for (QueryDocumentSnapshot document : value){
                                    Forum forum = new Forum();

                                    forum.forum_description = document.get("description").toString();
                                    forum.forum_title = document.get("title").toString();
                                    forum.user_id = document.get("uID").toString();
                                    forum.user_name = document.get("userName").toString();
                                    forum.timestamp = (Timestamp) document.get("timestamp");
                                    forum.likes = (ArrayList<String>) document.get("likes");
                                    forum.post_id = document.getId();
                                    forumArrayList.add(forum);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.logout();
            }
        });

        view.findViewById(R.id.buttonCreateForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openCreateForum();
            }
        });

        recyclerView = view.findViewById(R.id.commentsRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ForumRecyclerViewAdapter(forumArrayList, this);

        recyclerView.setAdapter(adapter);


    }

    ForumFragmentInterface mListener;

    @Override
    public void openCommentsOfForum(Forum f) {
        mListener.viewComments(f);
    }

    interface ForumFragmentInterface{
        void logout();
        void openCreateForum();
        void viewComments(Forum f);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ForumFragmentInterface) context;
    }
}