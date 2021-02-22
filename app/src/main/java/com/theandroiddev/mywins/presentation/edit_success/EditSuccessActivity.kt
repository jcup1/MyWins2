package com.theandroiddev.mywins.presentation.edit_success

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.*
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.camera.CameraModule
import com.esafirm.imagepicker.features.camera.ImmediateCameraModule
import com.esafirm.imagepicker.model.Image
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.core.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.core.mvp.startActivity
import com.theandroiddev.mywins.presentation.image.CustomImagePickerAdapter
import com.theandroiddev.mywins.presentation.image.SuccessImageAdapter
import com.theandroiddev.mywins.presentation.importance_popup.ImportancePopupActivity
import com.theandroiddev.mywins.presentation.importance_popup.ImportancePopupBundle
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.Importance
import com.theandroiddev.mywins.utils.Constants.Companion.REQUEST_CODE_IMPORTANCE
import com.theandroiddev.mywins.utils.DateHelper
import com.theandroiddev.mywins.utils.DrawableSelector
import kotlinx.android.synthetic.main.activity_edit_success.*
import kotlinx.android.synthetic.main.activity_insert_success.*
import kotlinx.android.synthetic.main.content_edit_success.*
import java.util.*

class EditSuccessActivity : MvpDaggerAppCompatActivity<EditSuccessView,
        EditSuccessBundle, EditSuccessPresenter>(), SuccessImageAdapter.OnSuccessImageClickListener, View.OnLongClickListener, EditSuccessView {

    private var drawableSelector: DrawableSelector? = null
    private var dateHelper: DateHelper? = null
    private var imagePicker: ImagePicker? = null

    private var currentImportance = Importance.NONE
    private var currentCategory = Category.NONE

    private var animShow: Animation? = null
    private var animHide: Animation? = null
    private var cameraModule: CameraModule? = null
    private var noDistractionMode: Boolean = false
    private var successImageAdapter: SuccessImageAdapter? = null
    private var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP or ItemTouchHelper.DOWN) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.adapterPosition
            presenter.onSwiped(position, successImageAdapter?.successImages?.get(position))

        }

        override fun getSwipeDirs(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
            if(viewHolder?.adapterPosition == 0) return 0
            return super.getSwipeDirs(recyclerView, viewHolder)
        }
    }

    private var mImageUri: Uri? = null

    private fun initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show)
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide)
    }

    private fun undoToRemove(position: Int, successImage: SuccessImageModel) {

        presenter.onUndoToRemove(position, successImage)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_success)
        val toolbar = findViewById<Toolbar>(R.id.edit_toolbar)
        setSupportActionBar(toolbar)

        noDistractionMode = false
        initAnimation()
        initFab()
        initRecycler()
        initObjects()
        initListeners()
    }


    override fun onBackPressed() {
        if (edit_description.hasFocus()) {
            edit_description.clearFocus()
        } else {
            super.onBackPressed()

        }
    }

    private fun initObjects() {
        this.drawableSelector = DrawableSelector(this)
        this.dateHelper = DateHelper(this)

    }

    private fun initListeners() {

        edit_date_started.setOnClickListener { dateHelper?.setDate(getString(R.string.date_started_empty), edit_date_started, edit_date_ended) }

        edit_date_ended.setOnClickListener { dateHelper?.setDate(getString(R.string.date_ended_empty), edit_date_started, edit_date_ended) }

        edit_importance_iv.setOnClickListener { startSetImportanceActivity() }

        edit_date_started.setOnLongClickListener(this)

        edit_date_ended.setOnLongClickListener(this)

        edit_description.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {

                edit_card_basic.startAnimation(animHide)
                edit_card_basic.visibility = View.GONE
                edit_card_images.startAnimation(animHide)
                edit_card_images.visibility = View.GONE
                noDistractionMode = true

            } else {

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                edit_card_images.visibility = View.VISIBLE
                edit_card_images.startAnimation(animShow)
                edit_card_basic.visibility = View.VISIBLE
                edit_card_basic.startAnimation(animShow)

                noDistractionMode = false
            }
        }

        edit_fab.setOnClickListener {
            if (noDistractionMode) {
                edit_description.clearFocus()

            } else {
                saveChanges()
            }
        }
    }

    override fun displaySuccessData(success: SuccessModel, successImages: MutableList<SuccessImageModel>) {

        edit_title.setText(success.title)
        edit_category.text = getString(success.category.res)
        edit_description.setText(success.description)
        edit_date_started.text = success.dateStarted
        checkDate(success.dateStarted, success.dateEnded)

        currentImportance = success.importance
        currentCategory = success.category

        drawableSelector?.selectCategoryImage(edit_category_iv, success.category, edit_category)
        drawableSelector?.selectImportanceImage(edit_importance_iv, success.importance)

        successImageAdapter?.successImages = successImages
        successImageAdapter?.notifyDataSetChanged()

    }

    override fun displaySuccessImageRemovedSnackbar(position: Int, successImage: SuccessImageModel) {

        successImageAdapter?.successImages?.remove(successImage)
        successImageAdapter?.notifyItemRemoved(position)

        val snackbar = Snackbar
                .make(edit_success_layout, getString(R.string.snack_image_removed), Snackbar.LENGTH_LONG)

        snackbar.setAction(getString(R.string.snack_undo)) { undoToRemove(position, successImage) }
        snackbar.show()
    }

    override fun displayUndoRemovingSuccessImage(position: Int, successImage: SuccessImageModel) {
        successImageAdapter?.successImages?.add(position, successImage)
        successImageAdapter?.notifyItemInserted(position)
        edit_image_recycler_view.scrollToPosition(position)
    }

    private fun saveChanges() {

        val dateStarted: String
        val dateEnded: String

        if (dateHelper?.validateData(edit_title, edit_date_started, edit_date_ended, insert_repeat_count) == true) {

            dateStarted = dateHelper?.checkBlankDate(edit_date_started.text.toString()).orEmpty()
            dateEnded = dateHelper?.checkBlankDate(edit_date_ended.text.toString()).orEmpty()

            val success = SuccessModel(
                    null,
                    title = edit_title.text.toString(),
                    category = currentCategory,
                    description = edit_description.text.toString(),
                    dateStarted = dateStarted,
                    dateEnded = dateEnded,
                    importance = currentImportance)

            presenter.onSaveChanges(success, successImageAdapter?.successImages)

        }

    }

    override fun addSuccessImages(successImages: ArrayList<SuccessImageModel>) {

        for (successImage in successImages) {
            successImageAdapter?.successImages?.add(successImage)
        }
        successImageAdapter?.notifyDataSetChanged()
    }

    private fun initFab() {
        val id = R.drawable.ic_done
        val color = R.color.white
        val myDrawable = ResourcesCompat.getDrawable(resources, id, null)

        edit_fab.setImageDrawable(myDrawable)
        edit_fab.setColorFilter(ContextCompat.getColor(this, color))

    }

    private fun initRecycler() {

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        edit_image_recycler_view.layoutManager = linearLayoutManager
        edit_image_recycler_view.setHasFixedSize(true)

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(edit_image_recycler_view)

        successImageAdapter = SuccessImageAdapter(this, R.layout.success_image_layout, this)
        edit_image_recycler_view.adapter = successImageAdapter

    }

    private fun checkDate(dateStarted: String, dateEnded: String) {

        if (dateStarted == "") {
            edit_date_started.text = getString(R.string.date_started_empty)
        } else {
            edit_date_started.text = dateStarted
        }

        if (dateEnded == "") {
            edit_date_ended.text = getString(R.string.date_ended_empty)
        } else {
            edit_date_ended.text = dateEnded
        }

    }

    private fun startSetImportanceActivity() {
        startActivity<ImportancePopupActivity>(ImportancePopupBundle(currentImportance), REQUEST_CODE_IMPORTANCE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMPORTANCE) {
            if (resultCode == Activity.RESULT_OK) {
                //TODO move bundles to presenters
                if (data != null) {
                    val bundle = data.getSerializableExtra("bundle")

                    if (bundle is ImportancePopupBundle) {
                        currentImportance = bundle.importance
                    }

                    drawableSelector?.selectImportanceImage(edit_importance_iv, currentImportance)
                } else {
                    alerts.displayUnexpectedError()
                }
            }
        }

        if (requestCode == RC_CODE_PICKER && resultCode == Activity.RESULT_OK && data != null) {

            presenter.onImagePickerResultOk(successImageAdapter?.successImages, data, imagePicker == null)

            return
        }

        if (requestCode == RC_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            getCameraModule().getImage(this, data) { resultImages ->
                presenter.onCameraResultOk(successImageAdapter?.successImages, resultImages, imagePicker == null)

            }
        }

    }

    private fun captureImage() {
        startActivityForResult(
                getCameraModule().getCameraIntent(this@EditSuccessActivity), RC_CAMERA)
    }

    private fun getCameraModule(): ImmediateCameraModule {
        if (cameraModule == null) {
            cameraModule = ImmediateCameraModule()
        }
        return cameraModule as ImmediateCameraModule
    }

    override fun updateImagePath(selectedImagePosition: Int, successImage: SuccessImageModel) {
        successImageAdapter?.updateSuccessImage(selectedImagePosition, successImage)
        successImageAdapter?.notifyItemChanged(selectedImagePosition)
        val imagePickerAdapter = CustomImagePickerAdapter(this)
        imagePickerAdapter.removeAllSelectedSingleClick()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == RC_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mImageUri != null) {
            outState.putString("cameraImageUri", mImageUri?.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey("cameraImageUri")) {
            mImageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"))
        }
    }

    override fun onSuccessImageClick(successImage: SuccessImageModel, successImageIv: ImageView, position: Int, constraintLayout: ConstraintLayout, cardView: CardView) {

        presenter.onSuccessImageClick(position)

    }

    override fun onSuccessImageLongClick(successImage: SuccessImageModel, successImageIv: ImageView, position: Int, constraintLayout: ConstraintLayout, cardView: CardView) {

        presenter.onSuccessImageLongClick(position, cardView)

    }

    override fun displayGallery(images: ArrayList<Image>) {

        imagePicker = ImagePicker.create(this)
                .theme(R.style.ImagePickerTheme)
                .returnAfterFirst(true) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(false) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select")
                .single()
        //TODO make it .multi() without selection bug
        imagePicker?.limit(10) // max imageList can be selected (99 by default)
                ?.showCamera(true) // show camera or not (true by default)
                ?.imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                ?.imageFullDirectory(Environment.getExternalStorageDirectory().path) // can be full path
                ?.origin(images) // original selected imageList, used in multi mode
                ?.start(RC_CODE_PICKER) // start image picker activity with request code
    }

    override fun displayDeleteMenu(position: Int, cardView: CardView) {
        val popupMenu = PopupMenu(this@EditSuccessActivity, cardView)

        popupMenu.menuInflater.inflate(R.menu.menu_images, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.remove_image_menu) {
                presenter.onSuccessImageRemoved(position, successImageAdapter?.successImages?.get(position))
            }
            true
        }
    }

    override fun onLongClick(view: View): Boolean {

        when (view.id) {
            R.id.edit_date_started -> openDatePopupMenu(getString(R.string.date_started_empty))
            R.id.edit_date_ended -> openDatePopupMenu(getString(R.string.date_ended_empty))
            else -> {
            }
        }
        return true
    }

    private fun openDatePopupMenu(s: String) {

        val popupMenu: PopupMenu? =
        if (s == getString(R.string.date_started_empty)) {
            PopupMenu(this@EditSuccessActivity, edit_date_started)
        } else if (s == getString(R.string.date_ended_empty)) {
            PopupMenu(this@EditSuccessActivity, edit_date_ended)
        } else {
            null
        }

        if (popupMenu != null) {
            popupMenu.menuInflater.inflate(R.menu.menu_date, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.remove_date_menu) {


                    if (s == getString(R.string.date_started_empty)) {
                        edit_date_started.text = getString(R.string.date_started_empty)
                    } else if (s == getString(R.string.date_ended_empty)) {
                        edit_date_ended.text = getString(R.string.date_ended_empty)
                    }

                }

                true
            }
        }

    }

    override fun displaySlider() {

        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    override fun displaySuccessImages(successImages: ArrayList<SuccessImageModel>) {

        successImageAdapter?.successImages = successImages
        successImageAdapter?.notifyDataSetChanged()
        successImageAdapter?.successImages?.firstOrNull()
    }

    companion object {
        private val RC_CODE_PICKER = 2000
        private val RC_CAMERA = 3000
    }
}
