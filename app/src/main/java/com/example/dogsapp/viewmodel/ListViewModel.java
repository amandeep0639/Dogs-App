package com.example.dogsapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.model.DogDao;
import com.example.dogsapp.model.DogDatabase;
import com.example.dogsapp.model.DogsApiService;
import com.example.dogsapp.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogs=new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError=new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading=new MutableLiveData<Boolean>();
    public ListViewModel(@NonNull Application application) {
        super(application);
    }
    private DogsApiService dogsService=new DogsApiService();
    private CompositeDisposable disposable=new CompositeDisposable();
    private AsyncTask<List<DogBreed>,Void,List<DogBreed>> insertTask;
    private AsyncTask<Void,Void,List<DogBreed>> retrieveTask;
    private SharedPreferencesHelper prefHelper=SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime=5*60*1000*1000*1000L;
    public void refresh(){
/*        DogBreed dog1=new DogBreed("1","corgi","15 years","","","","");
//        DogBreed dog2=new DogBreed("2","corgi","15 years","","","","");
//        DogBreed dog3=new DogBreed("3","corgi","15 years","","","","");
//        ArrayList<DogBreed> doglist=new ArrayList<>();
//        doglist.add(dog1);
//        doglist.add(dog2);
//        doglist.add(dog3);
//        Log.i("info1234","aaa1");
//        dogs.setValue(doglist);
//        dogLoadError.setValue(false);
//        loading.setValue(false);
        */

        long updateTime=prefHelper.getUpdateTime();
        Log.i("info123",String.valueOf(updateTime));
        long currTime=System.nanoTime();
        if(updateTime!=0&&(currTime-updateTime)<refreshTime)
        {
            fetchFromDatabase();;
        }else{
            fetchFromRemote();
        }
    }

    public void refresh_force(){
        fetchFromRemote();
    }

    private void fetchFromDatabase(){
        loading.setValue(true);
        retrieveTask=new RetrieveDogsTask();
        retrieveTask.execute();
    }

    private void fetchFromRemote(){
            loading.setValue(true);
            disposable.add(
                    dogsService.getDogs()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                        @Override
                        public void onSuccess(List<DogBreed> dogBreeds) {
                            insertTask=new InsertDogsTask();
                            insertTask.execute(dogBreeds);
                            Toast.makeText(getApplication(), "data fetched from internet", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dogLoadError.setValue(true);
                            loading.setValue(false);
                            e.printStackTrace();
                        }
                    })


            );
    }

    private void dogsRetrieved(List<DogBreed> dogList){
        dogs.setValue(dogList);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if(insertTask!=null){
            insertTask.cancel(true);
            insertTask=null;
        }
        if(retrieveTask!=null){
            retrieveTask.cancel(true);
            retrieveTask=null;
        }
    }

    private class InsertDogsTask extends AsyncTask<List<DogBreed>,Void,List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list=lists[0];
            DogDao dao= DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> newList=new ArrayList<>(list);
            List<Long> result=dao.insertAll(newList.toArray(new DogBreed[0]));
            int i=0;
            while(i<list.size()){
                list.get(i).uuid=result.get(i).intValue();
                i++;
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            super.onPostExecute(dogBreeds);
            dogsRetrieved(dogBreeds);
            prefHelper.saveUpdateTime(System.nanoTime());
        }
    }

    private class RetrieveDogsTask extends AsyncTask<Void,Void,List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {

          return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            super.onPostExecute(dogBreeds);
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(), "database sent data", Toast.LENGTH_SHORT).show();
        }
    }
}
