package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sep4_android.R;
import com.example.sep4_android.models.Garden;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import org.w3c.dom.Text;

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
        String assistantGoogleId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.gardenName.setText(model.getName());
        holder.requestInfo.setVisibility(View.GONE);
//        Glide.with(holder.itemView).load(FirebaseAuth.getInstance().get).into(holder.ownerAvatar);
        if(model.getAssistantList().containsKey(assistantGoogleId)){
            if(!model.getAssistantList().get(assistantGoogleId)){
                holder.requestInfo.setVisibility(View.VISIBLE);
                holder.requestInfo.setText("Waiting for approve");
            }else{
                holder.requestInfo.setVisibility(View.GONE);
            }

            holder.requestButton.setText("Cancel");
            holder.requestButton.setOnClickListener(v-> {
                listener.onCancelAssistance(model.getName(), assistantGoogleId);
            });
        }else{
            holder.requestButton.setText("Send request");
            holder.requestButton.setOnClickListener(v-> {
                listener.onSendRequestClick(model.getName());
            });
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView gardenName;
        TextView requestInfo;
        ImageView ownerAvatar;
        Button requestButton;

        public ViewHolder(@NotNull View itemView){
            super(itemView);
            gardenName = itemView.findViewById(R.id.text_garden_item_name);
            ownerAvatar = itemView.findViewById(R.id.img_garden_item_owner);
            requestButton = itemView.findViewById(R.id.button_garden_item_request);
            requestInfo = itemView.findViewById(R.id.text_garden_item_waiting);
        }

    }

    public interface OnItemClickListener {
        void onSendRequestClick(String gardenName);
        void onCancelAssistance(String gardenName, String assistantGoogleId);
    }
}
