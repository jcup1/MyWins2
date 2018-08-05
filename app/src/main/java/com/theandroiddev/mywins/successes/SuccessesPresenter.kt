package com.theandroiddev.mywins.successes

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.models.SearchFilter
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.prefs.PreferencesHelper
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import com.theandroiddev.mywins.mvp.MvpPresenter
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.*
import io.codetail.animation.ViewAnimationUtils
import java.util.*
import javax.inject.Inject

/**
 * Created by jakub on 04.11.17.
 */

class SuccessesPresenter @Inject() constructor(
        private val successesRepository: SuccessesRepository
) : MvpPresenter<SuccessesView>() {
    private var successList: ArrayList<Success>? = null
    private val successToRemoveList: ArrayList<Success>
    private var backupSuccess: Success? = null
    private var preferencesHelper: PreferencesHelper? = null

    private var sortType: String? = null
    private var isSortingAscending: Boolean = false
    private var isSearchOpened = false
    private var searchTerm: String? = null


    val searchFilter: SearchFilter
        get() = SearchFilter(searchTerm, sortType, isSortingAscending)

    init {
        successList = ArrayList()
        successToRemoveList = ArrayList()
        sortType = Constants.SORT_DATE_ADDED
        isSortingAscending = true
    }


    fun loadSuccesses() {

        successList = successesRepository.getSuccesses(searchFilter)
        val successesToDisplay = successList

        if (successesToDisplay?.isEmpty() == true || successesToDisplay == null) {
            view.displayNoSuccesses()
        } else {
            view.displaySuccesses(successesToDisplay)
        }
    }

    fun removeSuccessesFromQueue() {
        successesRepository.removeSuccesses(successToRemoveList)
        successToRemoveList.clear()

    }


    fun undoToRemove(position: Int) {

        val successToUndoRemove = backupSuccess

        if (successToUndoRemove != null) {
            successList?.add(position, successToUndoRemove)
            view.undoToRemove(position)
            successToRemoveList.remove(successToUndoRemove)
        }

    }


    fun sendToRemoveQueue(position: Int) {

        val successSentToRemoveQueue = backupSuccess

        if (successSentToRemoveQueue != null) {
            successList?.removeAt(position)
            view.successRemoved(position)
            successToRemoveList.add(successSentToRemoveQueue)
            //TODO handle it
            if (successList?.isEmpty() == true) {
                //onExtrasLoaded();
                view.displayNoSuccesses()
            }
        }

    }


    fun backupSuccess(position: Int) {
        backupSuccess = successList?.get(position)
    }


    fun updateSuccess(clickedPosition: Int) {
        //TODO check it later
        val successList = successList
        val successesRepository = successesRepository
        if (successList != null && successesRepository != null) {
            if (preferencesHelper?.isFirstSuccessAdded == true && successList.size > clickedPosition) {

                if (clickedPosition != NOT_ACTIVE) {
                    val id = successList[clickedPosition].id
                    successList[clickedPosition] = successesRepository.getSuccess(id)
                    view.displaySuccessChanged()
                }
            }
        }

    }


    fun categoryPicked(category: String) {
        view.displayCategory(category)
    }


    fun setPrefHelper(preferencesHelper: PreferencesHelper) {
        this.preferencesHelper = preferencesHelper

    }


    fun handleOptionsItemSelected(item: MenuItem) {
        val id = item.itemId

        if (id == R.id.action_search) {
            handleMenuSearch()
        }//TODO it's so dumb
        if (id == R.id.action_date_started) {

            if (sortType != Constants.SORT_DATE_STARTED) {
                sortType = Constants.SORT_DATE_STARTED
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses()

        }
        if (id == R.id.action_date_ended) {

            if (sortType != Constants.SORT_DATE_ENDED) {
                sortType = Constants.SORT_DATE_ENDED
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses()

        }
        if (id == R.id.action_title) {

            if (sortType != Constants.SORT_TITLE) {
                sortType = Constants.SORT_TITLE
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses()

        }
        if (id == R.id.action_date_added) {

            if (sortType != Constants.SORT_DATE_ADDED) {
                sortType = Constants.SORT_DATE_ADDED
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses()

        }
        if (id == R.id.action_importance) {

            if (sortType != Constants.SORT_IMPORTANCE) {
                sortType = Constants.SORT_IMPORTANCE
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses()

        }
        if (id == R.id.action_description) {

            if (sortType != Constants.SORT_DESCRIPTION) {
                sortType = Constants.SORT_DESCRIPTION
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses()

        }
    }


    fun handleMenuSearch() {

        val successList = successList
        if (successList != null) {
            if (isSearchOpened) {
                view.hideSearchBar()
                isSearchOpened = false
                this.successList = successesRepository.getSuccesses(searchFilter)
                view.displaySuccesses(successList)
            } else {
                view.displaySearchBar()
                isSearchOpened = true
            }
        }
    }


    fun showSearch() {
        view.displaySearch()
    }


    fun onCreateActivity(context: Context, view: SuccessesView, prefs: PreferencesHelper) {

        setPrefHelper(prefs)
        setSearchTerm("")
        openDB()
        checkPreferences()
        loadSuccesses()
    }


    fun onDestroyActivity() {
        closeDB()
    }


    fun onResumeActivity(view: SuccessesView, clickedPosition: Int, searchBox: EditText) {

        updateSuccess(clickedPosition)

    }


    fun showSoftKeyboard(imm: InputMethodManager?, searchBox: EditText?) {

        if (searchBox != null) {
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    fun hideSoftKeyboard(searchBox: EditText?, imm: InputMethodManager?) {

        if (imm != null && searchBox != null) {
            imm.hideSoftInputFromWindow(searchBox.windowToken, 0)
        }

    }


    fun setSuccessListVisible(recyclerView: RecyclerView, emptyListTv: TextView) {
        recyclerView.visibility = android.view.View.VISIBLE
        emptyListTv.visibility = android.view.View.INVISIBLE
    }


    fun setSuccessListInvisible(recyclerView: RecyclerView, emptyListTv: TextView) {
        recyclerView.visibility = android.view.View.INVISIBLE
        emptyListTv.visibility = android.view.View.VISIBLE
    }


    fun selectCategory(id: Int) {

        when (id) {

            R.id.action_learn -> categoryPicked(CATEGORY_LEARN)
            R.id.action_sport -> categoryPicked(CATEGORY_SPORT)
            R.id.action_journey -> categoryPicked(CATEGORY_JOURNEY)
            R.id.action_money -> categoryPicked(CATEGORY_MONEY)
            R.id.action_video -> categoryPicked(CATEGORY_VIDEO)

            else -> {
            }
        }
    }


    fun showCircularReveal(myView: android.view.View) {
        myView.setBackgroundColor(Color.argb(0, 0, 0, 0))
        myView.visibility = android.view.View.VISIBLE
        myView.post {
            myView.setBackgroundColor(Color.argb(127, 0, 0, 0))
            val cx = myView.left + myView.right
            val cy = myView.top + myView.bottom
            val dx = Math.max(cx, myView.width - cx)
            val dy = Math.max(cy, myView.height - cy)
            val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = 375

            animator.start()
        }

    }

    fun hideCircularReveal(myView: android.view.View) {
        myView.visibility = android.view.View.VISIBLE
        myView.post {
            val cx = myView.left + myView.right
            val cy = myView.top + myView.bottom
            val dx = Math.max(cx, myView.width - cx)
            val dy = Math.max(cy, myView.height - cy)
            val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0f)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.setDuration(375).addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {
                    myView.visibility = android.view.View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })

            animator.start()
        }

    }


    fun setSearchTerm(searchTerm: String) {
        this.searchTerm = searchTerm
    }


    fun addSuccess(s: Success) {

        if (preferencesHelper?.isFirstSuccessAdded == true) {
            successesRepository.clearDatabase()
            preferencesHelper?.setFirstSuccessAdded()
        }
        successesRepository.addSuccess(s)
        loadSuccesses()
        view.displayUpdatedSuccesses()

    }


    fun checkPreferences() {
        if (preferencesHelper?.isFirstRun == true) {
            //TODO add to database
            successesRepository.saveSuccesses(successesRepository.defaultSuccesses)

            preferencesHelper?.setNotFirstRun()
        }
    }


    fun openDB() {
        successesRepository.openDB()
    }


    fun startSlider(success: Success, position: Int, titleTv: TextView, categoryTv: TextView, dateStartedTv: TextView, dateEndedTv: TextView, categoryIv: ImageView, importanceIv: ImageView, constraintLayout: ConstraintLayout, cardView: CardView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            view.displaySliderAnimation(successesRepository.getSuccesses(searchFilter), success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView)

        } else {
            view.displaySlider(successesRepository.getSuccesses(searchFilter))

        }
    }


    fun clearSearch() {
        searchTerm = ""
        //onExtrasLoaded();

    }


    fun closeDB() {
        successesRepository.closeDB()
    }

    private fun clearSuccesses() {
        successList?.clear()
    }

    override fun detachView() {

    }

    override fun destroy() {

    }

}
