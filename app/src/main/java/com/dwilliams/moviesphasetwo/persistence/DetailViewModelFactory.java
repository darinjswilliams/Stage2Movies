package com.dwilliams.moviesphasetwo.persistence;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private AppRepository appRepository;
    private Integer movieId;

    public DetailViewModelFactory(AppRepository appRepository, Integer movieId) {
        this.appRepository = appRepository;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(appRepository, movieId);
    }
}
