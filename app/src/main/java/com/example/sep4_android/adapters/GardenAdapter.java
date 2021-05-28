package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sep4_android.R;
import com.example.sep4_android.models.Garden;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
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
        String assistantGoogleId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.gardenName.setText(model.getName());

        String gardenInfo = holder.itemView.getContext().getString(R.string.address) + " " + model.getStreet() + " " + model.getNumber() + "\n" +
                holder.itemView.getContext().getString(R.string.area) + " " + model.getLandArea() + "\n" +
                holder.itemView.getContext().getString(R.string.assistant_no) + " " + model.getAssistantList().size() + "\n";
        holder.gardenInfo.setText(gardenInfo);
        holder.requestStatus.setVisibility(View.GONE);
        if(model.getAssistantList().containsKey(assistantGoogleId)){
            if(!model.getAssistantList().get(assistantGoogleId)){
                holder.requestStatus.setVisibility(View.VISIBLE);
                holder.requestStatus.setText(holder.itemView.getContext().getString(R.string.waiting_for_approve));
            }else{
                holder.requestStatus.setVisibility(View.GONE);
            }

            holder.requestButton.setText(holder.itemView.getContext().getString(R.string.cancel));
            holder.requestButton.setOnClickListener(v-> {
                listener.onCancelAssistance(model.getName(), assistantGoogleId);
            });
        }else{
            holder.requestButton.setText(holder.itemView.getContext().getString(R.string.send_request));
            holder.requestButton.setOnClickListener(v-> {
                listener.onSendRequestClick(model.getName());
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gardenName;
        TextView gardenInfo;
        Button requestButton;
        TextView requestStatus;

        public ViewHolder(@NotNull View itemView){
            super(itemView);
            gardenName = itemView.findViewById(R.id.text_garden_item_name);
            requestButton = itemView.findViewById(R.id.button_garden_item_request);
            gardenInfo = itemView.findViewById(R.id.text_garden_item_info);
            requestStatus = itemView.findViewById(R.id.text_garden_item_request);
        }

    }

    public interface OnItemClickListener {
        void onSendRequestClick(String gardenName);
        void onCancelAssistance(String gardenName, String assistantGoogleId);
    }
}
