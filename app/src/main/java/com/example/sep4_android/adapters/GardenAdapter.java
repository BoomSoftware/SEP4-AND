package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sep4_android.R;
import com.example.sep4_android.models.Garden;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.annotations.NotNull;

public class GardenAdapter extends FirebaseRecyclerAdapter<Garden, GardenAdapter.ViewHolder> {

    private final OnItemClickListener listener;

    public GardenAdapter(@NonNull FirebaseRecyclerOptions<Garden> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public GardenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_garden, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull GardenAdapter.ViewHolder holder, int position, @NonNull Garden model) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(@NotNull View itemView){
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnItemClickListener {
        void onSendRequestClick(int clickedItemIndex);
    }
}
