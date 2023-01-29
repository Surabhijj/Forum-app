package edu.uncc.hw07;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener,SignUpFragment.SignUpListener,ForumsFragment.ForumFragmentInterface,CreateForumFragment.CreateForumInterface {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mAuth.getCurrentUser() != null) {
            onLoginSuccess();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();


        }
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void login() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onLoginSuccess() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new ForumsFragment())
                .commit();
    }

    @Override
    public void logout() {
        mAuth.signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new LoginFragment())
                .commit();
    }

    @Override
    public void openCreateForum() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new CreateForumFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewComments(Forum f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new ForumFragment().newInstance(f))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToForumFrag() {
        getSupportFragmentManager().popBackStack();
    }
}