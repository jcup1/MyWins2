package com.theandroiddev.mywins.UI.activities;

import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;
import com.theandroiddev.mywins.successes.SuccessesContract;
import com.theandroiddev.mywins.successes.SuccessesPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */
public class SuccessesPresenterTest {

    private final ArrayList<Success> successList = new ArrayList<>();
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SuccessesRepository successesRepository;
    @Mock
    SuccessesContract.View view;
    SuccessesPresenter presenter;

    @Before
    public void setUp() {
        //presenter = new SuccessesPresenter(view, successesRepository);
        successList.add(new Success());
        successList.add(new Success());
    }
//
//    @Test
//    public void shouldDisplayDefaultSuccesses() {
//        presenter.preferencesHelper.setFirstSuccessAdded();
//        Mockito.when(view.displayDefaultSuccesses(successesRepository.getDefaultSuccesses()));
//    }

//    @Test
//    public void shouldNotPassDefaultSuccessesToView() {
//
//        when(successesRepository.getSuccesses("", SORT_DATE_ADDED, true)).thenReturn(new ArrayList<Success>());
//
//        presenter.onExtrasLoaded(getSearchText());
//
//        verify(view).displayNoDefaultSuccesses();
//
//    }

//    @Test
//    public void shouldPassDefaultSuccessesToView() {
//
//        when(successesRepository.getSuccesses("", SORT_DATE_ADDED, true)).thenReturn(successList);
//
//        presenter.onExtrasLoaded(getSearchText());
//
//        verify(view).displayDefaultSuccesses(successList);
//
//    }


}