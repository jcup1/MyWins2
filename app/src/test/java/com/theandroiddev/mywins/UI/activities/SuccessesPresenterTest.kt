package com.theandroiddev.mywins.UI.activities

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Created by jakub on 28.10.17.
 */
class SuccessesPresenterTest : Spek({

    given("Test") {

        on("calculator") {
            it("should return 2") {
                2 shouldEqual 2
            }
        }
    }
//            private val successList = ArrayList<Success>()
//            @Rule
//            var mockitoRule = MockitoJUnit.rule()
//
//
//            var successesRepository = mock(SuccessesRepository::class.java)
//
//            var view = mock(SuccessesView::class.java)
//
//            internal var presenter = SuccessesPresenter(successesRepository)
//
//            @Before
//            fun setUp() {
//                presenter.attachView(view)
//                successList.add(Success())
//                successList.add(Success())
//            }
//
//            @Test
//            fun shouldDisplayDefaultSuccesses() {
//                presenter.preferencesHelper?.setFirstSuccessAdded()
//                on("display default successes")
//                Mockito.`when`(view?.displayDefaultSuccesses(successesRepository.getDefaultSuccesses())).thenReturn(successList)
//            }
//
//            //    @Test
//            //    public void shouldNotPassDefaultSuccessesToView() {
//            //
//            //        when(successesRepository.getSuccesses("", SORT_DATE_ADDED, true)).thenReturn(new ArrayList<Success>());
//            //
//            //        presenter.onExtrasLoaded(getSearchText());
//            //
//            //        verify(view).displayNoDefaultSuccesses();
//            //
//            //    }
//
//            //    @Test
//            //    public void shouldPassDefaultSuccessesToView() {
//            //
//            //        when(successesRepository.getSuccesses("", SORT_DATE_ADDED, true)).thenReturn(successes);
//            //
//            //        presenter.onExtrasLoaded(getSearchText());
//            //
//            //        verify(view).displayDefaultSuccesses(successes);
//            //
//            //    }

}
)