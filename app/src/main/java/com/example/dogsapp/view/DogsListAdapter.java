package com.example.dogsapp.view;

import android.net.LinkAddress;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsapp.R;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> {

    private ArrayList<DogBreed> dogsList;

    public DogsListAdapter(ArrayList<DogBreed> dogsList) {
        this.dogsList = dogsList;
    }

    public void updateDogsList(List<DogBreed> newdogsList){
        dogsList.clear();
        dogsList.addAll(newdogsList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog,parent,false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        //this is the place where you actually populate the variables
        ImageView imageView=holder.itemView.findViewById(R.id.imageView);
        TextView name=holder.itemView.findViewById(R.id.name);
        TextView lifespan=holder.itemView.findViewById(R.id.lifespan);

        name.setText(dogsList.get(position).dogBreed);
        lifespan.setText(dogsList.get(position).lifeSpan);
        Util.loadImage(imageView,dogsList.get(position).imageUrl,Util.getProgressDrawable(imageView.getContext()));

        LinearLayout linearLayout=holder.itemView.findViewById(R.id.linear_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragmentDirections.ActionListFragmentToDetailsFragment action=ListFragmentDirections.actionListFragmentToDetailsFragment();
                action.setDogUUID(dogsList.get(position).uuid);
                Navigation.findNavController(linearLayout).navigate(action);
            }
        });



    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
        }
    }






}
