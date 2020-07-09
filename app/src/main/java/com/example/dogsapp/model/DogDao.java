package com.example.dogsapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DogDao {

    @Insert
    List<Long> insertAll(DogBreed... dogs);

    @Query("select * from dogbreed ")
    List<DogBreed> getAllDogs();

    @Query("select * from dogbreed where uuid= :dogId")
    DogBreed getDog(int dogId);

    @Query("delete from dogbreed")
    void deleteAllDogs();
}
