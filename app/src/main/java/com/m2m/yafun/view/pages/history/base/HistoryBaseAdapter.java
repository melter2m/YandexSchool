package com.m2m.yafun.view.pages.history.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2m.yafun.R;
import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.view.pages.history.HistoryPage;
import com.m2m.yafun.view.pages.history.helper.ItemTouchHelperAdapter;

import java.util.List;

public abstract class HistoryBaseAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> implements ItemTouchHelperAdapter {
    protected final HistoryBasePage historyPage;
    protected List<HistoryItem> historyItems;

    protected HistoryBaseAdapter(HistoryBasePage historyPage, List<HistoryItem> historyItems) {
        this.historyPage = historyPage;
        this.historyItems = historyItems;
    }

    public void update(List<HistoryItem> historyItems) {
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
    public void onItemDismiss(int position, ItemTouchHelperAdapter.SwipeDirection direction) {
        try {
            if (direction == ItemTouchHelperAdapter.SwipeDirection.Left || direction == ItemTouchHelperAdapter.SwipeDirection.Right)
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

    protected abstract void changeFavoriteState(int position);

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
