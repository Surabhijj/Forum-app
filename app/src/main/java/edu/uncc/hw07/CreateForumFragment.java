package edu.uncc.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class CreateForumFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public CreateForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle("New Forum");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_forum, container, false);
    }

    EditText titleTextView;
    EditText descTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTextView = view.findViewById(R.id.editTextForumTitle);
        descTextView = view.findViewById(R.id.editTextForumDescription);

        view.findViewById(R.id.cancelButtonAddForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.goBackToForumFrag();
            }
        });

        view.findViewById(R.id.createForumButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleTextView.getText().toString();
                String description = descTextView.getText().toString();
                String uID = mAuth.getCurrentUser().getUid();
                String userName = mAuth.getCurrentUser().getDisplayName();
                Timestamp ts = new Timestamp(new Date());

                if (title.isEmpty() || description.isEmpty()){
                    Toast.makeText(getActivity(), "Please Fill All the fields", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String,Object> val = new HashMap<>();
                    val.put("title",title);
                    val.put("description",description);
                    val.put("uID",uID);
                    val.put("userName",userName);

                    HashMap<String, Object> comments = new HashMap<>();
                    comments.put("name","deep");
                    comments.put("uid","1234");
                    comments.put("time","time");

                    ArrayList<String> likes = new ArrayList<>();

//                    val.put("comments",comments);
                    val.put("likes",likes);
                    val.put("timestamp",ts);

                    DocumentReference docRef = db.collection("forum")
                            .document();


                    docRef.set(val)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Forum Saved Successfully", Toast.LENGTH_SHORT).show();
                                    mlistener.goBackToForumFrag();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error : Forum Not Saved !!", Toast.LENGTH_SHORT).show();
                                }
                            });

//                    HashMap<String,Object> v = new HashMap<>();
//                    docRef.collection("new_comments").document().set(v)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(getActivity(), "SUCCESS ", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                }
            }
        });

    }

    CreateForumInterface mlistener;

    interface CreateForumInterface{
        void goBackToForumFrag();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mlistener = (CreateForumInterface) context;
    }
}