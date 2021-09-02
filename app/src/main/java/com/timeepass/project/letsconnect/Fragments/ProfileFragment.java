package com.timeepass.project.letsconnect.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.timeepass.project.letsconnect.R;
import com.timeepass.project.letsconnect.UserProfile.CreateProfile;

public class ProfileFragment extends Fragment implements View.OnClickListener
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilefragment, container,false);

        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        TextView textView = getActivity().findViewById(R.id.tv_prof);

        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_prof:
            Intent intent = new Intent(getActivity(), CreateProfile.class);
            startActivity(intent);
        }
    }
}
