package com.m2m.yafun.view.pages.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2m.yafun.R;
import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.view.pages.history.helper.ItemTouchHelperAdapter;

import java.util.List;

class HistoryAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> implements ItemTouchHelperAdapter {

    private final HistoryPage historyPage;
    private List<HistoryItem> historyItems;

    HistoryAdapter(HistoryPage historyPage, List<HistoryItem> historyItems) {
        this.historyPage = historyPage;
        this.historyItems = historyItems;
    }

    void update(List<HistoryItem> historyItems) {
        this.historyItems = historyItems;
        notifyDataSetChanged();
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_item, parent, false);
        return new HistoryItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HistoryItemViewHolder holder, int position) {
        HistoryItem item = historyItems.get(position);
        holder.originalText.setText(item.getText());
        holder.direction.setText(item.getDirection());
        holder.translationContent.setText(item.getTranslationTotalString());
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavoriteState(holder.getAdapterPosition());
            }
        });

        setFavoriteView(holder, item.isFavorite());
    }

    private void setFavoriteView(HistoryItemViewHolder holder, boolean isFavorite) {
        if (isFavorite)
            holder.favorite.setImageResource(android.R.drawable.btn_star_big_on);
        else
            holder.favorite.setImageResource(android.R.drawable.btn_star_big_off);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position, SwipeDirection direction) {
        try {
            if (direction == SwipeDirection.Left || direction == SwipeDirection.Right)
                deleteItem(position);
            else
                notifyItemChanged(position);
        } catch (Exception ignored) {
        }
    }

    private void deleteItem(int position) {
        HistoryItem item = historyItems.get(position);
        IHistoryGateway gateway = historyPage.getDatabaseContext().createHistoryGateway();
        gateway.deleteItem(item.getId());
        historyItems.remove(position);
        notifyItemRemoved(position);
    }

    private HistoryItem changeFavoriteState(int position) {
        HistoryItem item = historyItems.get(position);
        IHistoryGateway gateway = historyPage.getDatabaseContext().createHistoryGateway();
        item = gateway.setFavorite(item, !item.isFavorite());
        historyItems.remove(position);
        historyItems.add(position, item);
        notifyItemChanged(position);
        return item;
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
