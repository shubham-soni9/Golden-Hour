package com.goldenhour.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goldenhour.R
import com.goldenhour.data.model.db.PinnedLocation
import com.goldenhour.databinding.DialogSavedPinBinding
import com.goldenhour.ui.base.BaseDialog
import com.goldenhour.ui.home.HomeActivity
import javax.inject.Inject

/**
 * Dialog for showing saved locations
 */
class SavedPinDialog : BaseDialog<SavedPinViewModel>(), LocationListCallback {
    val TAG = SavedPinDialog::class.java.name
    lateinit var mViewBinding: DialogSavedPinBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var mViewModel: SavedPinViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_saved_pin,
            container,
            false
        )
        val view = mViewBinding.root
        return view
    }

    companion object {
        fun newInstance(): SavedPinDialog {
            val fragment = SavedPinDialog()
            val bundle = Bundle()
            bundle.putParcelableArrayList(
                PinnedLocation::class.java.name,
                ArrayList<PinnedLocation>()
            )
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this, viewModelFactory).get(SavedPinViewModel::class.java)
        mViewBinding.viewModel = mViewModel
        mViewModel.getAllLocation().observe(this, Observer<List<PinnedLocation>> {
            mViewBinding.dialogRvSavedPinList.layoutManager = LinearLayoutManager(activity)
            mViewBinding.dialogRvSavedPinList.adapter = LocationListAdapter(it, this)
        })
        mViewBinding.btnAction.setOnClickListener {
            dismissDialog(TAG)
        }
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onLocationClicked(pinnedLocation: PinnedLocation) {
        if (mActivity != null && mActivity is HomeActivity) {
            (mActivity as HomeActivity).onSavedLocationClicked(pinnedLocation)
        }
        dismissDialog(TAG)
    }

}
