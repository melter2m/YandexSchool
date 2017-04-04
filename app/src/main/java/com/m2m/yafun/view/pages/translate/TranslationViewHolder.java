package com.m2m.yafun.view.pages.translate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.m2m.yafun.R;

public class TranslationViewHolder extends RecyclerView.ViewHolder{

    public TextView translationContent;

    public TranslationViewHolder(View itemView) {
        super(itemView);
        translationContent = (TextView) itemView.findViewById(R.id.translationContent);
    }
}