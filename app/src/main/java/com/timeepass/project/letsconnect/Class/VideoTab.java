package com.timeepass.project.letsconnect.Class;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.timeepass.project.letsconnect.Fragments.ImagesFragment;
import com.timeepass.project.letsconnect.Fragments.VideosFragment;
import com.timeepass.project.letsconnect.R;

public class VideoTab extends Fragment
{
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.videos_tab, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        database = FirebaseDatabase.getInstance();


        recyclerView = getActivity().findViewById(R.id.rv_videosTab);

        reference = database.getReference("All videos").child(uid);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PostMember> options
                = new FirebaseRecyclerOptions.Builder<PostMember>()
                .setQuery(reference, PostMember.class)
                .build();

        FirebaseRecyclerAdapter<PostMember, VideosFragment> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PostMember, VideosFragment>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideosFragment holder, int position, @NonNull final PostMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentuserid = user.getUid();

                        final String postkey = getRef(position).getKey();

                        holder.SetVideo(getActivity(), model.getName(), model.getUrl(), model.getPostUri(),
                                model.getTime(), model.getUid(), model.getType(), model.getDesc());

                    }

                    @NonNull
                    @Override
                    public VideosFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.post_videos, parent, false);

                        return new VideosFragment(view);

                    }
                };

        firebaseRecyclerAdapter.startListening();

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}
