package com.m2m.yafun.view.pages.translate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.m2m.yafun.R;
import com.m2m.yafun.model.api.service.OnLanguageDetermineListener;
import com.m2m.yafun.model.api.service.OnLanguagesReceivedListener;
import com.m2m.yafun.model.api.service.OnTranslateListener;
import com.m2m.yafun.model.api.service.result.Languages;
import com.m2m.yafun.model.api.service.result.TranslateResult;
import com.m2m.yafun.view.OnFragmentInteractionListener;
import com.m2m.yafun.view.pages.Page;

import java.util.ArrayList;
import java.util.List;

public class TranslateFragment extends Page implements OnLanguagesReceivedListener, OnTranslateListener {

    private OnFragmentInteractionListener mListener;

    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView detectedLanguageView;

    private EditText textToTranslate;
    private ImageView clear;

    private TranslationsAdapter translationsAdapter;

    private String detectedLanguage = "en";

    public TranslateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_translate, container, false);
        spinnerFrom = (Spinner) result.findViewById(R.id.spinnerLanguageFrom);
        spinnerTo = (Spinner) result.findViewById(R.id.spinnerLanguageTo);
        detectedLanguageView = (TextView) result.findViewById(R.id.detectedLanguage);
        detectedLanguageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromLanguage(detectedLanguage);
            }
        });
        textToTranslate = (EditText) result.findViewById(R.id.textToTranslate);
        clear = (ImageView) result.findViewById(R.id.clearInput);
        initInput();
        initTranslationsView((RecyclerView) result.findViewById(R.id.translationsRecyclerView));

        return result;
    }

    private void setFromLanguage(String detectedLanguage) {
        LanguagesAdapterWithAutoDetect adapter = (LanguagesAdapterWithAutoDetect) spinnerFrom.getAdapter();
        if (adapter == null)
            return;
        int position = adapter.getLanguagePosition(detectedLanguage);
        spinnerFrom.setSelection(position, true);
        detectedLanguageView.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLanguagesSelection();
    }

    private void initInput() {
        textToTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clear.setVisibility(View.GONE);
                    updateTranslation(new ArrayList<String>());
                    return;
                }
                if (s.length() == 1)
                    return;
                clear.setVisibility(View.VISIBLE);
                detectLangIfNeededAndTranslate(s.toString());
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToTranslate.setText("");
            }
        });
    }

    private void initLanguagesSelection() {
        Languages languages = getTranslateApi().getLanguagesCache();
        if (languages == null)
            getTranslateApi().getLanguages(this);
        else
            initLanguagesSpinners(languages);
    }

    @Override
    public void onLanguagesReceived(Languages languages) {
        initLanguagesSpinners(languages);
    }

    @Override
    public void onLanguagesReceiveError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    AdapterView.OnItemSelectedListener onSelectionChangedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            detectLangIfNeededAndTranslate(getCurrentTextToTranslate());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void initLanguagesSpinners(Languages languages) {
        if (languages == null)
            return;

        LanguagesAdapterWithAutoDetect from = new LanguagesAdapterWithAutoDetect(getContext(), languages);
        spinnerFrom.setAdapter(from);
        spinnerFrom.setOnItemSelectedListener(onSelectionChangedListener);

        LanguagesAdapter to = new LanguagesAdapter(getContext(), languages);
        spinnerTo.setAdapter(to);
        spinnerTo.setOnItemSelectedListener(onSelectionChangedListener);
    }

    private TranslateTask translateTask;

    private void detectLangIfNeededAndTranslate(String text) {
        if (needToDetectLanguage())
            detectAndTranslate(text);
        else
            translate(text, getSelectedDirection());
    }

    private boolean needToDetectLanguage() {
        return getSelectedLanguage(spinnerFrom) == null;
    }

    private void detectAndTranslate(String text) {
        getTranslateApi().detectLanguage(text, new OnLanguageDetermineListener() {
            @Override
            public void onLanguageDetermined(String toDetermine, String language) {
                detectedLanguage = language;
                String displayName = getTranslateApi().getLanguagesCache().getLanguageDisplayName(language);
                detectedLanguageView.setVisibility(View.VISIBLE);
                detectedLanguageView.setText(displayName);
                String to = getSelectedLanguage(spinnerTo);
                String direction = getDirection(language, to);
                translate(toDetermine, direction);
            }

            @Override
            public void onLanguagesDetermineError(String error) {

            }
        });
    }

    private void translate(String text, String direction) {
        if (translateTask != null && !translateTask.isCancelled())
            translateTask.cancel(true);
        translateTask = new TranslateTask(this);
        translateTask.execute(text, direction);
    }

    @Override
    public void onTranslated(String toTranslate, TranslateResult translateResult) {
        String currentText = getCurrentTextToTranslate();
        if (currentText.equals(toTranslate))
            updateTranslation(translateResult.getTranslatedText());
        else
            updateTranslation(new ArrayList<String>());
    }

    @Override
    public void onTranslateError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (translateTask != null)
            translateTask.cancel(true);
    }

    private void initTranslationsView(RecyclerView translationsView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        translationsAdapter = new TranslationsAdapter(new ArrayList<String>());
        translationsView.setAdapter(translationsAdapter);
        translationsView.setLayoutManager(layoutManager);
        translationsView.setItemAnimator(itemAnimator);
    }

    private void updateTranslation(List<String> translation) {
        translationsAdapter.update(translation);
    }

    private String getCurrentTextToTranslate() {
        return textToTranslate.getText().toString();
    }

    private String getSelectedDirection() {
        String from = getSelectedLanguage(spinnerFrom);
        String to = getSelectedLanguage(spinnerTo);
        return getDirection(from, to);
    }

    private String getDirection(String from, String to) {
        if (from != null && to != null)
            return from + "-" + to;
        return "en-ru";
    }

    private String getSelectedLanguage(Spinner languagesSpinner) {
        return ((LanguagesAdapter) languagesSpinner.getAdapter()).getLanguageId(languagesSpinner.getSelectedItemPosition());
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

}
