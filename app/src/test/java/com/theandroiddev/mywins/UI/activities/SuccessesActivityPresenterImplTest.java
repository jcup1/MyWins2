package com.theandroiddev.mywins.UI.activities;

import com.theandroiddev.mywins.UI.models.Success;
import com.theandroiddev.mywins.UI.views.SuccessesActivityView;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */
public class SuccessesActivityPresenterImplTest {

    private final ArrayList<Success> successList = new ArrayList<>();
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SuccessesRepository successesRepository;
    @Mock
    SuccessesActivityView view;
    SuccessesActivityPresenterImpl presenter;

    @Before
    public void setUp() {
        //presenter = new SuccessesActivityPresenterImpl(view, successesRepository);
        successList.add(new Success());
        successList.add(new Success());
    }

//    @Test
//    public void shouldNotPassDefaultSuccessesToView() {
//
//        when(successesRepository.getSuccesses("", SORT_DATE_ADDED, true)).thenReturn(new ArrayList<Success>());
//
//        presenter.loadSuccesses(getSearchText());
//
//        verify(view).displayNoDefaultSuccesses();
//
//    }

//    @Test
//    public void shouldPassDefaultSuccessesToView() {
//
//        when(successesRepository.getSuccesses("", SORT_DATE_ADDED, true)).thenReturn(successList);
//
//        presenter.loadSuccesses(getSearchText());
//
//        verify(view).displayDefaultSuccesses(successList);
//
//    }


}