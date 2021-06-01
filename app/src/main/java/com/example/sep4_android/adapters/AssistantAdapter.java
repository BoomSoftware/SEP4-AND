package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sep4_android.R;
import com.example.sep4_android.models.UserLiveData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssistantAdapter extends RecyclerView.Adapter<AssistantAdapter.ViewHolder> {

    private List<String> assistants;
    private OnAssistantItemActionListener listener;
    private boolean status;

    public AssistantAdapter(OnAssistantItemActionListener listener) {
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
            if(assistant != null){
                Glide.with(holder.itemView).load(assistant.getAvatarUrl()).into(holder.assistantAvatar);
                holder.assistantEmail.setText(assistant.getEmail());
            }
        });


        if (status) {
            holder.approveButton.setVisibility(View.VISIBLE);
            holder.approveButton.setOnClickListener(v -> {
                listener.approveAssistant(assistants.get(position), position);
            });
        } else {
            holder.approveButton.setVisibility(View.GONE);
        }


        holder.removeButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle(holder.itemView.getContext().getString(R.string.are_you_sure))
                    .setMessage(holder.itemView.getContext().getString(R.string.remove_assistant))
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        listener.removeAssistant(assistants.get(position), position);
                    })
                    .setNegativeButton(android.R.string.no, null).show();
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

    public void removeAssistantAtPosition(int position){
        assistants.remove(position);
        notifyDataSetChanged();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView assistantEmail;
        CircleImageView assistantAvatar;
        ConstraintLayout approveButton;
        ConstraintLayout removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assistantAvatar = itemView.findViewById(R.id.img_assistant_item_avatar);
            approveButton = itemView.findViewById(R.id.button_assistant_item_accept);
            removeButton = itemView.findViewById(R.id.button_assistant_item_decline);
            assistantEmail = itemView.findViewById(R.id.text_assistant_item_email);
        }
    }

    public interface OnAssistantItemActionListener {
        void approveAssistant(String assistantGoogleId, int position);

        void removeAssistant(String assistantGoogleId, int position);

        UserLiveData loadAssistantInfo(String assistantGoogleId);
    }
}
