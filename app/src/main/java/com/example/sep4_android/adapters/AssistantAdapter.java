package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sep4_android.R;
import com.example.sep4_android.models.User;
import com.example.sep4_android.models.UserLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssistantAdapter extends RecyclerView.Adapter<AssistantAdapter.ViewHolder> {

    private List<String> assistants;
    private OnAssistantItemActionListener listener;

    public AssistantAdapter(OnAssistantItemActionListener listener){
        assistants = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_assistant, parent, false);
        return new AssistantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        listener.loadAssistantInfo(assistants.get(position)).observe((LifecycleOwner) holder.itemView.getContext(), assistant -> {
            Glide.with(holder.itemView).load(assistant.getAvatarUrl()).into(holder.assistantAvatar);
            holder.assistantName.setText(assistant.getName());
        });

        holder.approveButton.setOnClickListener(v -> {
            listener.approveAssistant(assistants.get(position));
        });

        holder.removeButton.setOnClickListener(v -> {
            listener.removeAssistant(assistants.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return assistants.size();
    }

    public void setAssistants(List<String> assistants) {
        this.assistants = assistants;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView assistantName;
        ImageView assistantAvatar;
        ImageView approveButton;
        ImageView removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assistantName = itemView.findViewById(R.id.text_assistant_item_name);
            assistantAvatar = itemView.findViewById(R.id.img_assistant_item_avatar);
            approveButton = itemView.findViewById(R.id.img_assistant_item_approve);
            removeButton = itemView.findViewById(R.id.img_assistant_item_remove);
        }
    }

    public interface OnAssistantItemActionListener {
        void approveAssistant(String assistantGoogleId);
        void removeAssistant(String assistantGoogleId);
        UserLiveData loadAssistantInfo(String assistantGoogleId);
    }
}
