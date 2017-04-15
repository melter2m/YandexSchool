package com.m2m.yafun.view.pages.history.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2m.yafun.R;

class HistoryItemViewHolder extends RecyclerView.ViewHolder{

    TextView originalText;
    public TextView direction;
    TextView translationContent;

    ImageView favorite;

    HistoryItemViewHolder(View itemView) {
        super(itemView);
        originalText = (TextView) itemView.findViewById(R.id.originalText);
        direction = (TextView) itemView.findViewById(R.id.translateDirection);
        translationContent = (TextView) itemView.findViewById(R.id.translationContent);
        favorite = (ImageView) itemView.findViewById(R.id.imageViewFavorite);
    }
}
