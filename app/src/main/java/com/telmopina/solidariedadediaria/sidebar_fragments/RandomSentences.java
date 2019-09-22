package com.telmopina.solidariedadediaria.sidebar_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.utils.AppGlobals;

import java.util.Random;

public class RandomSentences extends Fragment implements View.OnClickListener {


    private TextView txtSentence;
    private String[] array;
    private Random r;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.random_sentences);
        View mBaseView = inflater.inflate(R.layout.fragment_random_sentences, container, false);

        Button btnChangeSentence = mBaseView.findViewById(R.id.btnChangeSentence);
        txtSentence = mBaseView.findViewById(R.id.txtSentence);

        array = getResources().getStringArray(R.array.sentenceArray);

        r = new Random();

        txtSentence.setText(array[AppGlobals.getIntFromSharedPreferences(AppGlobals.KEY_SENTENCE_INDEX)]);

        btnChangeSentence.setOnClickListener(this);

        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        int index = r.nextInt(array.length);
        AppGlobals.saveIntToSharedPreferences(AppGlobals.KEY_SENTENCE_INDEX, index);
        txtSentence.setText(array[index]);
    }
}
