package learnprogramming.academy.imbizo_wil3_v2.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import learnprogramming.academy.imbizo_wil3_v2.Adapters.CategoryAdapter;
import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.OOP.Category;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class CommunityPageFragment extends Fragment {

    //Initialise variable
    private OnFragmentInteractionListener mListener;
    private View homeView;
    RecyclerView commentRecyclerView;
    CategoryAdapter commentAdapter;
    List<Category> commentList;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Categories");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeView = inflater.inflate(R.layout.fragment_community_page, container, false);

        commentRecyclerView  = homeView.findViewById(R.id.commentRV);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentRecyclerView.setHasFixedSize(true);

        return homeView;
    }

    @Override
    public void onStart() {
        super.onStart();

        MainActivity.checkView = 4;

        // Get List Categories from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {

                    Category comment = postsnap.getValue(Category.class);
                    commentList.add(comment) ;
                }

                commentAdapter = new CategoryAdapter(getActivity(),commentList);
                commentRecyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}