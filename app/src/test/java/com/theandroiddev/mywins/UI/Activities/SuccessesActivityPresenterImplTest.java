package com.theandroiddev.mywins.UI.Activities;

import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;
import com.theandroiddev.mywins.UI.repositories.SuccessesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static com.theandroiddev.mywins.UI.Helpers.Constants.SORT_DATE_ADDED;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        presenter = new SuccessesActivityPresenterImpl(view, successesRepository);
        successList.add(new Success());
        successList.add(new Success());
    }

    @Test
    public void shouldNotPassDefaultSuccessesToView() {

        when(successesRepository.getSuccessesWithNewSorting("", SORT_DATE_ADDED, true)).thenReturn(new ArrayList<Success>());

        presenter.loadSuccesses();

        verify(view).displayNoDefaultSuccesses();

    }

    @Test
    public void shouldPassDefaultSuccessesToView() {

        when(successesRepository.getSuccessesWithNewSorting("", SORT_DATE_ADDED, true)).thenReturn(successList);

        presenter.loadSuccesses();

        verify(view).displayDefaultSuccesses(successList);

    }


}