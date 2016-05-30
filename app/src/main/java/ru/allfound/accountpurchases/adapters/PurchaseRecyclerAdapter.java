package ru.allfound.accountpurchases.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.allfound.accountpurchases.model.Purchase;
import ru.allfound.accountpurchases.R;

/*
 * PurchaseRecyclerAdapter.java    v.1.2 10.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class PurchaseRecyclerAdapter extends RecyclerView.Adapter<PurchaseRecyclerAdapter.ViewHolder> {
    private List<Purchase> purchases;
    private int position;
    private Context context;

    public PurchaseRecyclerAdapter(List<Purchase> purchases, Context context) {
        this.purchases = purchases;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textViewDescription.setText(purchases.get(position).getDescription());
        holder.textViewCategory.setText(purchases.get(position).getCategory());
        holder.textViewPrice.setText(purchases.get(position).getPrice() + context.getString(R.string.rub));
        holder.textViewDate.setText(purchases.get(position).getDate());
        holder.textViewTime.setText(purchases.get(position).getTime());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView textViewDescription;
        TextView textViewCategory;
        TextView textViewPrice;
        TextView textViewDate;
        TextView textViewTime;

        public ViewHolder(View view) {
            super(view);
            textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
            textViewCategory = (TextView) view.findViewById(R.id.textViewCategory);
            textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            textViewTime = (TextView) view.findViewById(R.id.textViewTime);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.ctx_menu_delete, Menu.NONE, R.string.delete_record);
            menu.add(Menu.NONE, R.id.ctx_menu_copy, Menu.NONE, R.string.copy_record);
        }
    }
}
