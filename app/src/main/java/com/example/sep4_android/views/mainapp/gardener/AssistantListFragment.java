package com.example.sep4_android.views.mainapp.gardener;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sep4_android.R;
import com.example.sep4_android.adapters.AssistantAdapter;
import com.example.sep4_android.models.GardenLiveData;
import com.example.sep4_android.models.UserLiveData;
import com.example.sep4_android.viewmodels.gardener.AssistantListViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssistantListFragment extends Fragment implements AssistantAdapter.OnAssistantItemActionListener {

    private View view;
    private AssistantListViewModel viewModel;
    private AssistantAdapter adapter;
    private String listType;
    private TextView emptyAssistant;
    private String gardenName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assistant_list, container, false);
        viewModel = new ViewModelProvider(this).get(AssistantListViewModel.class);
        listType = getArguments().getString("listType");
        emptyAssistant = view.findViewById(R.id.empty_assistant);
        loadData();
        return view;
    }

    @Override
    public void approveAssistant(String assistantGoogleId, int position) {
        viewModel.approveAssistant(gardenName, assistantGoogleId);
        adapter.removeAssistantAtPosition(position);
    }

    @Override
    public void removeAssistant(String assistantGoogleId, int position) {
        viewModel.removeAssistant(gardenName, assistantGoogleId);
        adapter.removeAssistantAtPosition(position);
    }

    @Override
    public UserLiveData loadAssistantInfo(String assistantGoogleId) {
        return viewModel.getAssistant(assistantGoogleId);
    }

    private void loadData() {
        adapter = new AssistantAdapter(this);
        RecyclerView assistantList = view.findViewById(R.id.assistant_recycler);
        assistantList.hasFixedSize();
        assistantList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        assistantList.setAdapter(adapter);

        GardenLiveData gardenLive = viewModel.getLiveGarden();
        if (gardenLive != null) {
            gardenLive.observe(getViewLifecycleOwner(), garden -> {
                if (garden != null) {
                    gardenName = garden.getName();
                    List<String> assistants = new ArrayList<>();
                    if (listType.equals("requests")) {
                        for (Map.Entry<String, Boolean> entry : garden.getAssistantList().entrySet()) {
                            if (!entry.getValue()) {
                                assistants.add(entry.getKey());
                            }
                        }
                        adapter.setStatus(true);
                    } else {
                        for (Map.Entry<String, Boolean> entry : garden.getAssistantList().entrySet()) {
                            if (entry.getValue()) {
                                assistants.add(entry.getKey());
                            }
                        }
                        adapter.setStatus(false);
                    }
                    displayInformation(assistants);
                }
            });
        }
    }

    private void displayInformation(List<String> assistants) {
        if (!assistants.isEmpty()) {
            emptyAssistant.setVisibility(View.GONE);
            adapter.setAssistants(assistants);
            return;
        }
        emptyAssistant.setVisibility(View.VISIBLE);
    }
}