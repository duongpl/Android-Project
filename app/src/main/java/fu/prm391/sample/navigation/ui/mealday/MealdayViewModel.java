package fu.prm391.sample.navigation.ui.mealday;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealdayViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MealdayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}