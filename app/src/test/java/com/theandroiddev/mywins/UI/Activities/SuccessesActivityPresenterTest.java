package com.theandroiddev.mywins.UI.Activities;

import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;
import com.theandroiddev.mywins.UI.repositories.SuccessesRepository;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jakub on 28.10.17.
 */
public class SuccessesActivityPresenterTest {

    @Test
    public void shouldPassSuccessesToView() {

        //given
        SuccessesActivityView view = new MockView();
        SuccessesRepository successesRepository = new MockSuccessesRepository(true);

        //when
        SuccessesActivityPresenter presenter = new SuccessesActivityPresenter(view, successesRepository);
        presenter.loadSuccesses();

        //then
        Assert.assertEquals(true, ((MockView) view).displaySuccessesWithSuccessesCalled);


    }

    @Test
    public void shouldHandleNoSuccessesFound() {

        SuccessesActivityView view = new MockView();
        SuccessesRepository successesRepository = new MockSuccessesRepository(false);

        SuccessesActivityPresenter presenter = new SuccessesActivityPresenter(view, successesRepository);
        presenter.loadSuccesses();

        Assert.assertEquals(true, ((MockView) view).displaySuccessesWithNoSuccessesCalled);

    }

    private class MockView implements SuccessesActivityView {

        boolean displaySuccessesWithSuccessesCalled;
        boolean displaySuccessesWithNoSuccessesCalled;

        @Override
        public void displaySuccesses(List<Success> successList) {
            if (successList.size() == 3) displaySuccessesWithSuccessesCalled = true;
        }

        @Override
        public void displayNoSuccesses() {
            displaySuccessesWithNoSuccessesCalled = true;
        }
    }

    private class MockSuccessesRepository implements SuccessesRepository {

        private boolean returnSomeBooks;

        public MockSuccessesRepository(boolean returnSomeBooks) {
            this.returnSomeBooks = returnSomeBooks;
        }

        @Override
        public List<Success> getSuccesses() {

            if (returnSomeBooks) {
                return Arrays.asList(new Success(), new Success(), new Success());
            } else {
                return Collections.emptyList();
            }
        }
    }


}