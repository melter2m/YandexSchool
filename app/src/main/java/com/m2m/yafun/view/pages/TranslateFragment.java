package com.m2m.yafun.view.pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.m2m.yafun.R;
import com.m2m.yafun.model.api.TranslateApi;
import com.m2m.yafun.model.api.service.result.Languages;
import com.m2m.yafun.model.api.service.TranslateApiListener;
import com.m2m.yafun.model.api.service.result.TranslateResult;
import com.m2m.yafun.view.OnFragmentInteractionListener;

public class TranslateFragment extends Fragment implements TranslateApiListener {

    private OnFragmentInteractionListener mListener;

    public TranslateFragment() {
        TranslateApi api = new TranslateApi();
        api.getLanguages(this);
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLanguagesReceived(Languages languages) {
        if (languages == null)
            return;
        Toast.makeText(getContext(), languages.getDirections().size() + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLanguagesReceiveError(String error) {

    }

    @Override
    public void onLanguageDetermined(String toDetermine, String language) {

    }

    @Override
    public void onLanguagesDetermineError(String error) {

    }

    @Override
    public void onTranslated(String toTranslate, TranslateResult translateResult) {

    }

    @Override
    public void onTranslateError(String error) {

    }
}
