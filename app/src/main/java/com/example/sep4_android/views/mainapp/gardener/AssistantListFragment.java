package com.example.sep4_android.views.mainapp.gardener;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sep4_android.R;
import com.example.sep4_android.adapters.AssistantAdapter;
import com.example.sep4_android.viewmodels.gardener.AssistantListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssistantListFragment extends Fragment implements AssistantAdapter.OnAssistantItemActionListener {

    private View view;
    private AssistantListViewModel viewModel;
    private RecyclerView assistantList;
    private AssistantAdapter adapter;
    private String listType;
    private String gardenName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assistant_list, container, false);
        viewModel = new ViewModelProvider(this).get(AssistantListViewModel.class);
        listType = getArguments().getString("listType");
        loadData();
        return view;
    }

    private void loadData() {
        adapter = new AssistantAdapter(this);
        assistantList = view.findViewById(R.id.assistant_recycler);
        assistantList.hasFixedSize();
        assistantList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        assistantList.setAdapter(adapter);

        viewModel.getLiveGarden().observe(getViewLifecycleOwner(), garden -> {
            gardenName = garden.getName();
            List<String> assistants = new ArrayList<>();
            if (listType.equals("requests")) {
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX  requests " + listType);
                for (Map.Entry<String, Boolean> entry : garden.getAssistantList().entrySet()) {
                    if (!entry.getValue()) {
                        assistants.add(entry.getKey());
                    }

                }
            }
            else{
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX  all " + listType);
                for (Map.Entry<String, Boolean> entry : garden.getAssistantList().entrySet()) {
                    if (entry.getValue()) {
                        assistants.add(entry.getKey());
                    }
                }
            }
            adapter.setAssistants(assistants);
        });
    }

    @Override
    public void approveAssistant(String assistantGoogleId) {
        viewModel.approveAssistant(gardenName, assistantGoogleId);
    }

    @Override
    public void removeAssistant(String assistantGoogleId) {
        viewModel.removeAssistant(gardenName, assistantGoogleId);
    }

    @Override
    public void loadAssistantInformation(String assistantGoogleId) {

    }
}