package com.timeepass.project.letsconnect.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.timeepass.project.letsconnect.Class.PostMember;
import com.timeepass.project.letsconnect.Class.PostViewHolder;
import com.timeepass.project.letsconnect.Class.QuestionMember;
import com.timeepass.project.letsconnect.Class.Viewholder_Question;
import com.timeepass.project.letsconnect.PostActivity;
import com.timeepass.project.letsconnect.R;
import com.timeepass.project.letsconnect.ReplyActivity;
import com.timeepass.project.letsconnect.UserProfile.UpdateProfile;

import java.security.Permission;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener
{
    Button button;
    RecyclerView recyclerView;
    FirebaseDatabase database  = FirebaseDatabase.getInstance();
    DatabaseReference reference, likesref;
    Boolean likecheccker = false;
    DatabaseReference db1, db2, db3;

    //extra
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment, container,false);

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button = getActivity().findViewById(R.id.createpost_homefrag);

        reference = database.getReference("All posts");
        likesref = database.getReference("post likes");

        recyclerView = getActivity().findViewById(R.id.rv_posts);
        recyclerView.setHasFixedSize(true);
        button.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        //extra
        documentReference = db.collection("user").document(currentuid);
        //

        db1 = database.getReference("All images").child(currentuid);
        db2 = database.getReference("All videos").child(currentuid);
        db3 = database.getReference("All posts");

    }




    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.createpost_homefrag:
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
                break;
        }
    }




    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<PostMember> options
                = new FirebaseRecyclerOptions.Builder<PostMember>()
                .setQuery(reference, PostMember.class)
                .build();

        FirebaseRecyclerAdapter<PostMember, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PostMember, PostViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final PostMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentuserid = user.getUid();

                        final String postkey = getRef(position).getKey();

                        holder.SetPost(getActivity(), model.getName(), model.getUrl(), model.getPostUri(),
                                model.getTime(), model.getUid(), model.getType(), model.getDesc());


//                        holder.setitem(getActivity(), model.getName(), model.getUrl(), model.getUserid(), model.getKey(),
//                                model.getQuestion(), model.getPrivacy(), model.getTime());


                        final String url = getItem(position).getPostUri();
                        final String name = getItem(position).getName();
                     //   String url = getItem(position).getUrl();
                        final String time = getItem(position).getTime();
                        final String type = getItem(position).getType();
                        final String userid = getItem(position).getUid();




                        holder.likeschecker(postkey);


                        holder.menuoptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                showDialog(name, url, time, userid, type);
                            }
                        });


                        holder.likebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                likecheccker = true;

                                likesref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(likecheccker.equals(true))
                                        {
                                            if(snapshot.child(postkey).hasChild(currentuserid))
                                            {
                                                likesref.child(postkey).child(currentuserid).removeValue();
                                              //  delete(time);
                                                likecheccker = false;
                                            }
                                            else
                                            {
                                                likesref.child(postkey).child(currentuserid).setValue(true);

                                                likecheccker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.post_layout, parent, false);

                        return new PostViewHolder(view);

                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }



    public void showDialog(String name, String url, String time, String userid, String type)
    {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.post_options, null);
        TextView download = view.findViewById(R.id.download_tv_post);
        TextView share = view.findViewById(R.id.share_tv_post);
        TextView delete = view.findViewById(R.id.delete_tv_post);
        TextView copyurl = view.findViewById(R.id.copyurl_tv_post);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        alertDialog.show();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        if(userid.equals(currentUserid))
        {
            delete.setVisibility(View.VISIBLE);
        }
        else
        {
            delete.setVisibility(View.INVISIBLE);
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Query query = db1.orderByChild("time").equalTo(time);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                        {
                            dataSnapshot1.getRef().removeValue();

                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Query query2 = db2.orderByChild("time").equalTo(time);
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                        {
                            dataSnapshot1.getRef().removeValue();

                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Query query3 = db3.orderByChild("time").equalTo(time);
                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                        {
                            dataSnapshot1.getRef().removeValue();

                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                reference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });


                alertDialog.dismiss();
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                        if(type.equals("iv"))
                        {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading image...");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name +System.currentTimeMillis() + ".jpg");


                            DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();
                        }
                        else
                        {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading video...");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name +System.currentTimeMillis() + ".mp4");


                            DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                        Toast.makeText(getActivity(), "No permissions", Toast.LENGTH_SHORT).show();
                    }
                };

                TedPermission.with(getActivity())
                        .setPermissionListener(permissionListener)
                        .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sharetext = name + "\n" + "\n" + url;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, sharetext);
                intent.setType("text/plain");
                startActivity(intent.createChooser(intent, "share via"));

                alertDialog.dismiss();
            }
        });


        copyurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager cp = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("String", url);
                cp.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();
            }
        });
    }
}
