package com.timeepass.project.letsconnect.Class;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.timeepass.project.letsconnect.R;
import com.timeepass.project.letsconnect.RelatedQuestions;
import com.timeepass.project.letsconnect.YourQuestions;

public class BottomSheetMenu_AskFrag extends BottomSheetDialogFragment
{

    CardView cardView, cardView2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_askfrag, null);

        cardView = view.findViewById(R.id.related_askfrag);
        cardView2 = view.findViewById(R.id.your_question_askfrag);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), RelatedQuestions.class));
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), YourQuestions.class));
            }
        });
        return view;
    }
}
