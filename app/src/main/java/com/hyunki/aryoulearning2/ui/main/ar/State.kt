package com.hyunki.aryoulearning2.ui.main.ar

import com.hyunki.aryoulearning2.db.model.Category
import com.hyunki.aryoulearning2.model.Model
import com.hyunki.aryoulearning2.model.ModelResponse
import java.util.*

sealed class State {

    object Loading : State()

    object Error : State()

    sealed class Success : State() {

        data class OnModelResponsesLoaded(
                val responses: ArrayList<ModelResponse>
        ) : Success()

        data class OnModelsLoaded(
                val models: ArrayList<Model>
        ) : Success()

        data class OnCategoriesLoaded(
                val categories: ArrayList<Category>
        ) : Success()

        data class OnCurrentCategoryStringLoaded(
                val currentCategoryString: String
        ) : Success()

    }
}

//private MediatorLiveData<MainResource<List<ModelResponse>>> modelResponsesData = new MutableLiveData<>();
//private MutableLiveData<List<Model>> modelLiveData = new MutableLiveData<>();
//private MutableLiveData<List<Category>> catLiveData = new MutableLiveData<>();
//private MutableLiveData<String> curCatLiveData = new MutableLiveData<>();