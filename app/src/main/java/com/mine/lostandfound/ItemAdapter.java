package com.mine.lostandfound;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter
    extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public interface OnItemClick {
        void openDetail(int itemId);
    }

    private final List<Item> itemList;
    private final OnItemClick clickListener;

    public ItemAdapter(List<Item> items, OnItemClick listener) {
        this.itemList     = items;
        this.clickListener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item it = itemList.get(position);
        holder.tvName.setText(it.getName());
        holder.tvDesc.setText(it.getDescription());

        holder.itemView.setOnClickListener(v ->
            clickListener.openDetail(it.getId())
        );
    }

    @Override public int getItemCount() {
        return itemList.size();
    }

    public void removeItemAt(int pos) {
        itemList.remove(pos);
        notifyItemRemoved(pos);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc;
        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_name);
            tvDesc = itemView.findViewById(R.id.item_description);
        }
    }
}
