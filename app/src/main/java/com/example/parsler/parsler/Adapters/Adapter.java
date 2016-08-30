package com.example.parsler.parsler.Adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Adapter<X,Y> extends RecyclerView.Adapter<ObjectHolder<X,Y>> {

    private ArrayList<X> items;
    private ViewDataBinding binding;
    private int placeholder;
    private String case_constant;

    public Adapter(ArrayList<X> data, int _placeholder, String _case) {
        super();
        items = data;
        placeholder = _placeholder;
        case_constant = _case;
    }

    @Override
    public ObjectHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        binding =  DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                placeholder, viewGroup, false);

        return new ObjectHolder(binding);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onBindViewHolder(ObjectHolder objectHolder, final int position) {

        objectHolder.bindConnection(items.get(position), binding, case_constant);
    }

}
