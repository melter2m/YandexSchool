package com.m2m.yafun.view.pages.translate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2m.yafun.R;

import java.util.List;

class TranslationsAdapter extends RecyclerView.Adapter<TranslationViewHolder> {

    private List<String> translations;

    TranslationsAdapter(List<String> translations) {
        this.translations = translations;
    }

    void update(List<String> translations) {
        this.translations = translations;
        notifyDataSetChanged();
    }

    @Override
    public TranslationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_item, parent, false);
        return new TranslationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TranslationViewHolder holder, int position) {
        holder.translationContent.setText(translations.get(position));
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }
}
