package com.hyunki.aryoulearning2.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.db.model.CurrentCategory;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.model.ModelResponse;
import com.hyunki.aryoulearning2.ui.main.ar.util.CurrentWord;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    public static final String TAG = "MainViewModel";

    private final MainRepository mainRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<State> modelResponsesData = new MutableLiveData<>();
    private MutableLiveData<State> modelLiveData = new MutableLiveData<>();
    private MutableLiveData<State> catLiveData = new MutableLiveData<>();
    private MutableLiveData<State> curCatLiveData = new MutableLiveData<>();
    private List<CurrentWord> wordHistory = new ArrayList<>();

    @Inject
    MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    void loadModelResponses() {

        modelResponsesData.setValue(State.Loading.INSTANCE);

        compositeDisposable.add(
                mainRepository.getModelResponses()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(modelResponses -> {
                            if (modelResponses.size() > 0) {
                                saveModelResponseData(modelResponses);
                                modelResponsesData.setValue(
                                        new State.Success.OnModelResponsesLoaded(modelResponses));
                            }

                        }, throwable -> modelResponsesData.setValue(State.Error.INSTANCE))
        );
    }

    public void saveModelResponseData(ArrayList<ModelResponse> modelResponses) {
        for (int i = 0; i < modelResponses.size(); i++) {
            mainRepository.insertCat(new Category(
                    modelResponses.get(i).getCategory(),
                    modelResponses.get(i).getBackground()
            ));
            for (int j = 0; j < modelResponses.get(i).getList().size(); j++) {
                Log.d(TAG, "observeModelResponses: " + modelResponses.get(i).getList().get(j).getName());
                mainRepository.insertModel(new Model(
                        modelResponses.get(i).getCategory(),
                        modelResponses.get(i).getList().get(j).getName(),
                        modelResponses.get(i).getList().get(j).getImage()
                ));
            }
        }
    }

    public void loadModelsByCat(String cat) {
        modelLiveData.setValue(State.Loading.INSTANCE);
        Log.d(TAG, "loadModelsByCat: loading models by cat");

        Disposable modelDisposable =
                mainRepository.getModelsByCat(cat)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onModelsFetched, this::onError);
        compositeDisposable.add(modelDisposable);
    }

    public void loadCategories() {
        catLiveData.setValue(State.Loading.INSTANCE);

        Disposable catDisposable = mainRepository.getAllCats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCatsFetched, this::onError);
        compositeDisposable.add(catDisposable);
    }

    public void loadCurrentCategoryName() {
        curCatLiveData.setValue(State.Loading.INSTANCE);

        Disposable curCatDisposable = mainRepository.getCurrentCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCurCatsFetched, this::onError);
        compositeDisposable.add(curCatDisposable);
    }

    public LiveData<State> getModelLiveData() {
        return modelLiveData;
    }

    public LiveData<State> getCatLiveData() {
        return catLiveData;
    }

    public LiveData<State> getCurCatLiveData() {
        return curCatLiveData;
    }

    LiveData<State> getModelResponsesData() {
        return modelResponsesData;
    }

    void setCurrentCategory(Category category) {
        mainRepository.setCurrentCategory(new CurrentCategory(category.getName()));
    }

    public List<CurrentWord> getWordHistory() {
        return wordHistory;
    }

    public void setWordHistory(List<CurrentWord> wordHistory) {
        this.wordHistory = wordHistory;
    }

    private void onError(Throwable throwable) {
        Log.d("MainViewModel", throwable.getMessage());
    }

    private void onModelsFetched(List<Model> models) {
        Log.d(TAG, "onModelsFetched: " + models.size());
        modelLiveData.setValue(new State.Success.OnModelsLoaded(models));
    }

    private void onCatsFetched(List<Category> categories) {
        Log.d(TAG, "onCatsFetched: " + categories.size());
        catLiveData.setValue(new State.Success.OnCategoriesLoaded(categories));
    }

    private void onCurCatsFetched(CurrentCategory category) {
        Log.d(TAG, "onCurCatsFetched: " + category.getCurrentCategory());
        curCatLiveData.setValue(
                new State.Success.OnCurrentCategoryStringLoaded(category.getCurrentCategory()));
        Log.d(TAG, "onCurCatsFetched: " + new State.Success.OnCurrentCategoryStringLoaded(category.getCurrentCategory()).getClass());
    }

    public void clearEntireDatabase() {
        mainRepository.clearEntireDatabase();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
//        clearEntireDatabase();
    }
}
