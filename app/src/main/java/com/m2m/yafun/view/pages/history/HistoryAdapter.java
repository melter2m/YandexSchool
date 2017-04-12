package com.m2m.yafun.view.pages.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2m.yafun.R;
import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.view.TranslateApplication;
import com.m2m.yafun.view.pages.translate.TranslationViewHolder;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<TranslationViewHolder> {

    private final HistoryPage historyPage;
    private List<HistoryItem> historyItems;

    public HistoryAdapter(HistoryPage historyPage, List<HistoryItem> historyItems) {
        this.historyPage = historyPage;
        this.historyItems = historyItems;
        sortItems();
    }

    public void update(List<HistoryItem> historyItems) {
        this.historyItems = historyItems;
        notifyDataSetChanged();
    }

    @Override
    public TranslationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_item, parent, false);
        return new TranslationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TranslationViewHolder holder, int position) {
        HistoryItem item = historyItems.get(position);
        holder.translationContent.setText(item.getTranslationTotalString());
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavoriteState(holder.getAdapterPosition());

            }
        });

        if (item.isFavorite())
            holder.favorite.setImageResource(android.R.drawable.btn_star_big_on);
        else
            holder.favorite.setImageResource(android.R.drawable.btn_star_big_off);
    }

    private void changeFavoriteState(int position) {
        HistoryItem item = historyItems.get(position);
        IHistoryGateway gateway = historyPage.getDatabaseContext().createHistoryGateway();
        item = gateway.setFavorite(item, !item.isFavorite());
        historyItems.set(position, item);
        sortItems();
        notifyDataSetChanged();
    }

    private void sortItems() {
        Collections.sort(historyItems, new Comparator<HistoryItem>() {
            @Override
            public int compare(HistoryItem o1, HistoryItem o2) {
                if (o1.isFavorite() == o2.isFavorite())
                    return 0;
                return o1.isFavorite() && !o2.isFavorite() ? -1 : 1;
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
