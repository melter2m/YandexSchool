package com.m2m.yafun.view.pages.translate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.view.YaLicenseInitializer;
import com.m2m.yafun.view.pages.Page;

import java.util.ArrayList;
import java.util.Calendar;

public class TranslatePage extends Page implements OnLanguagesReceivedListener, OnTranslateListener {

    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView detectedLanguageView;

    private EditText textToTranslate;
    private ImageView clear;

    private String detectedLanguage = "en";

    private TextView translationContent;
    private ImageView favoriteIndicator;
    private CardView translationView;
    private TranslateResult currentTranslateResult;
    private String translatedText;

    private AddToHistoryTask addToHistoryTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_translate, container, false);
        spinnerFrom = (Spinner) result.findViewById(R.id.spinnerLanguageFrom);
        spinnerTo = (Spinner) result.findViewById(R.id.spinnerLanguageTo);
        result.findViewById(R.id.exchangeLang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeLanguages();
            }
        });
        YaLicenseInitializer.initClickableTextView((TextView) result.findViewById(R.id.textViewYaLic));
        detectedLanguageView = (TextView) result.findViewById(R.id.detectedLanguage);
        detectedLanguageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromLanguage(detectedLanguage);
            }
        });
        textToTranslate = (EditText) result.findViewById(R.id.textToTranslate);
        clear = (ImageView) result.findViewById(R.id.clearInput);

        translationView = (CardView) result.findViewById(R.id.cvTranslationView);
        translationContent = (TextView) result.findViewById(R.id.translationContent);
        favoriteIndicator = (ImageView) result.findViewById(R.id.imageViewFavorite);
        favoriteIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavoriteState();
            }
        });
        initInput();
        setEmptyTranslation();

        return result;
    }

    private void exchangeLanguages() {
        String from = getSelectedLanguage(spinnerFrom);
        if (from == null)
            from = detectedLanguage;
        if (from == null)
            return;

        String to = getSelectedLanguage(spinnerTo);
        setFromLanguage(to);
        setLanguageForSpinner(spinnerTo, from);
        translate(getCurrentTextToTranslate(), getSelectedDirection());
    }

    private void setFromLanguage(String detectedLanguage) {
        if (setLanguageForSpinner(spinnerFrom, detectedLanguage))
            detectedLanguageView.setVisibility(View.GONE);
    }

    private boolean setLanguageForSpinner(Spinner spinner, String language) {
        LanguagesAdapter adapter = (LanguagesAdapter) spinner.getAdapter();
        if (adapter == null)
            return false;
        int position = adapter.getLanguagePosition(language);
        spinner.setSelection(position, true);
        return true;
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
                cancelAddToHistoryTask();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    clear.setVisibility(View.GONE);
                    setEmptyTranslation();
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
        this.translatedText = toTranslate;
        this.currentTranslateResult = translateResult;
        if (currentText.equals(toTranslate))
            updateTranslation(toTranslate, translateResult);
        else
            setEmptyTranslation();
        startAddToHistoryTask();
    }

    private void startAddToHistoryTask() {
        cancelAddToHistoryTask();
        addToHistoryTask = new AddToHistoryTask(this, translatedText, currentTranslateResult);
        addToHistoryTask.execute();
    }

    private void cancelAddToHistoryTask() {
        if (addToHistoryTask != null && addToHistoryTask.getStatus() == AsyncTask.Status.RUNNING)
            addToHistoryTask.cancel(true);
    }

    private void changeFavoriteState() {
        if (translatedText == null || currentTranslateResult == null)
            return;
        IHistoryGateway gateway = getDatabaseContext().createHistoryGateway();
        HistoryItem fromHistory = gateway.getItem(translatedText, currentTranslateResult.getTranslateDirection());
        if (fromHistory == null) {
            addToHistory(gateway, translatedText, currentTranslateResult, true);
            setFavoriteIndicator(true);
        } else {
            gateway.setFavorite(fromHistory, !fromHistory.isFavorite());
            setFavoriteIndicator(!fromHistory.isFavorite());
        }
        notifyOthersToUpdate();
    }

    public void addToHistory(String text, TranslateResult result, boolean isFavorite) {
        IHistoryGateway gateway = getDatabaseContext().createHistoryGateway();
        addToHistory(gateway, text, result, isFavorite);
    }

    private void addToHistory(IHistoryGateway gateway, String text, TranslateResult result, boolean isFavorite) {
        HistoryItem fromHistory = gateway.getItem(text, result.getTranslateDirection());
        if (fromHistory != null)
            return;
        gateway.insertItem(new HistoryItem(Calendar.getInstance(), text, result.getTranslateDirection(), result.getTranslatedText(), isFavorite));
    }

    private void setEmptyTranslation() {
        updateTranslation("", new TranslateResult(-1, "", new ArrayList<String>()));
    }

    private void updateTranslation(String toTranslate, TranslateResult translateResult) {
        if (translateResult.getTranslatedText().size() == 0 || toTranslate.isEmpty())
            translationView.setVisibility(View.GONE);
        else
            translationView.setVisibility(View.VISIBLE);

        translationContent.setText(translateResult.getTranslationTotalString());
        IHistoryGateway gateway = getDatabaseContext().createHistoryGateway();
        HistoryItem fromHistory = gateway.getItem(toTranslate, translateResult.getTranslateDirection());
        if (fromHistory == null || !fromHistory.translationEqual(translateResult.getTranslatedText()) || !fromHistory.isFavorite())
            setFavoriteIndicator(false);
        else
            setFavoriteIndicator(true);

        notifyOthersToUpdate();
    }

    private void setFavoriteIndicator(boolean isFavorite) {
        if (isFavorite)
            favoriteIndicator.setImageResource(android.R.drawable.btn_star_big_on);
        else
            favoriteIndicator.setImageResource(android.R.drawable.btn_star_big_off);
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

    @Override
    public void update() {

    }
}
