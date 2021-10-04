package com.timeepass.project.letsconnect.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.timeepass.project.letsconnect.Class.BottomSheetMenu;
import com.timeepass.project.letsconnect.ImageActivity;
import com.timeepass.project.letsconnect.IndividualPost;
import com.timeepass.project.letsconnect.R;
import com.timeepass.project.letsconnect.UserProfile.CreateProfile;
import com.timeepass.project.letsconnect.UserProfile.UpdateProfile;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment implements View.OnClickListener
{
    ImageView imageView;
    TextView nameEt, profEt, bioEt, emailEt, webEt, postTv;
    ImageButton ib_edit, imageButtonmenu;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentId = user.getUid();

    DocumentReference reference;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilefragment, container,false);

        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        reference = firestore.collection("user").document(currentId);

         imageView = getActivity().findViewById(R.id.iv_proffrag);
         nameEt = getActivity().findViewById(R.id.tv_name_proffrag);
         profEt = getActivity().findViewById(R.id.tv_prof_proffrag);
         bioEt = getActivity().findViewById(R.id.tv_bio_proffrag);
         emailEt = getActivity().findViewById(R.id.tv_email_proffrag);
         webEt = getActivity().findViewById(R.id.tv_web_proffrag);
         postTv = getActivity().findViewById(R.id.tv_post_proffrag);


         postTv.setOnClickListener(this);


        ib_edit = getActivity().findViewById(R.id.ib_edit_f1);
        imageButtonmenu = getActivity().findViewById(R.id.ib_menu_f1);

        imageButtonmenu.setOnClickListener(this);
        ib_edit.setOnClickListener(this);

        imageView.setOnClickListener(this);
        webEt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ib_edit_f1:
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_f1:
                BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                bottomSheetMenu.show(getFragmentManager(), "bottomsheet");
                break;
            case R.id.iv_proffrag:
                Intent intent1 = new Intent(getActivity(), ImageActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_post_proffrag:
                Intent intent5 = new Intent(getActivity(), IndividualPost.class);
                startActivity(intent5);
                break;
            case R.id.tv_web_proffrag:
                try {
                    String url = webEt.getText().toString();
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(Uri.parse(url));
                    startActivity(intent2);
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Invalid Url", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();




        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists())
                        {
                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String emailResult = task.getResult().getString("email");
                            String webResult = task.getResult().getString("web");
                            String urlResult = task.getResult().getString("url");
                            String profResult = task.getResult().getString("prof");

                            Picasso.get().load(urlResult).into(imageView);
                            nameEt.setText(nameResult);
                            bioEt.setText(bioResult);
                            emailEt.setText(emailResult);
                            webEt.setText(webResult);
                            profEt.setText(profResult);
                        }
                        else
                        {
                            Intent intent = new Intent(getActivity(), CreateProfile.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
