package com.example.dogsapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogsapp.R;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.util.Util;
import com.example.dogsapp.viewmodel.DetailViewModel;
import com.example.dogsapp.viewmodel.ListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class detailsFragment extends Fragment {

    private DetailViewModel viewModel;
    ImageView dogImage;
    TextView dogName;TextView dogPurpose;TextView dogTemperament;TextView dogLifeSpan;

    private int dogUUID;
    public detailsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_details, container, false);
        dogImage=view.findViewById(R.id.dogImage);
        dogName=view.findViewById(R.id.dogName);
        dogPurpose=view.findViewById(R.id.dogPurpose);
        dogTemperament=view.findViewById(R.id.dogTemperament);
        dogLifeSpan=view.findViewById(R.id.dogLifespan);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null){
            dogUUID=detailsFragmentArgs.fromBundle(getArguments()).getDogUUID();
        }
        Log.i("info123",String.valueOf(dogUUID));

        viewModel=new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(DetailViewModel.class);
        viewModel.fetch(dogUUID);
        observeViewModel();
    }
    private void observeViewModel(){
        viewModel.dogLiveData.observe(getViewLifecycleOwner(), new Observer<DogBreed>() {
            @Override
            public void onChanged(DogBreed dogBreed) {
                if(dogBreed!=null&&dogBreed instanceof DogBreed){
                    dogName.setText(dogBreed.dogBreed);
                    dogPurpose.setText(dogBreed.bredFor);
                    dogLifeSpan.setText(dogBreed.lifeSpan);
                    dogTemperament.setText(dogBreed.temperament);

                    if(dogBreed.imageUrl!=null&&getContext()!=null){
                        Util.loadImage(dogImage,dogBreed.imageUrl,new CircularProgressDrawable(getContext()));
                    }
                }
            }
        });
    }

    //    void onGoToDetails(){
//        NavDirections action=detailsFragmentDirections.actionDetailsFragmentToListFragment();
//        Navigation.findNavController(fab).navigate(action);
//    }
}
