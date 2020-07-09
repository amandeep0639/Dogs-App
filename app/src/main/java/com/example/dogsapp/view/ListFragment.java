package com.example.dogsapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dogsapp.R;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.viewmodel.ListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private DogsListAdapter dogsListAdapter=new DogsListAdapter(new ArrayList<>());
    RecyclerView dogsList;
    TextView listError;
    ProgressBar loadingView;
    SwipeRefreshLayout refreshLayout;
    public ListFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_list, container, false);
        dogsList=view.findViewById(R.id.dogslist);
        listError=view.findViewById(R.id.listerror);
        loadingView=view.findViewById(R.id.loadingview);
        refreshLayout=view.findViewById(R.id.refreshlayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dogsList.setVisibility(View.GONE);
                listError.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                viewModel.refresh_force();
                refreshLayout.setRefreshing(false);
            }
        });



        Log.i("info1234","aaa");
//noteViewModel=new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        viewModel=new  ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(ListViewModel.class);
        viewModel.refresh();
        Log.i("info1234",String.valueOf(viewModel.dogs.equals(null)));
        dogsList.setLayoutManager(new LinearLayoutManager(getContext()));
        dogsList.setAdapter(dogsListAdapter);
        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.dogs.observe(this, new Observer<List<DogBreed>>() {
            @Override
            public void onChanged(List<DogBreed> dogBreeds) {
                Log.i("info1234","dogsobserve");
                if(dogBreeds!=null){
                    dogsList.setVisibility(View.VISIBLE);
                    dogsListAdapter.updateDogsList(dogBreeds);
                }
            }
        });
        viewModel.dogLoadError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("info1234","errorobserve");
                if(aBoolean!=null&& aBoolean instanceof Boolean){
                    listError.setVisibility((aBoolean==true)?View.VISIBLE:View.GONE);
                }
            }
        });
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("info1234","loadingobserve");
                if(aBoolean!=null){
                    loadingView.setVisibility((aBoolean==true)?View.VISIBLE:View.GONE);
                    if(aBoolean){
                        listError.setVisibility(View.GONE);
                        dogsList.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //    void onGoToDetails(){
//        ListFragmentDirections.ActionListFragmentToDetailsFragment action=ListFragmentDirections.actionListFragmentToDetailsFragment();
//        action.setDogUUID(500);
//        Navigation.findNavController(fab).navigate(action);
//    }













}
