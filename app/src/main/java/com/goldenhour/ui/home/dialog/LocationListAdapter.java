package com.goldenhour.ui.home.dialog;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.goldenhour.data.model.db.PinnedLocation;
import com.goldenhour.databinding.ItemEmptyLocationViewBinding;
import com.goldenhour.databinding.ItemLocationListViewBinding;
import com.goldenhour.ui.base.BaseAdapter;
import com.goldenhour.ui.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Adapter for showing saved location list inside SavedPinDialog
 */
public class LocationListAdapter extends BaseAdapter<BaseViewHolder, PinnedLocation> {

    // Saved location list
    private List<PinnedLocation> locationEntities;

    // Calling for location list events
    private final LocationListCallback locationListCallback;

    // Define constants for viewType in case of empty item
    private static final int VIEW_TYPE_EMPTY = 0;

    // Define constants for viewType in case of normal item
    private static final int VIEW_TYPE_NORMAL = 1;

    /**
     * Constructor for initializing location list adapter
     *
     * @param locationEntities     is saved location list
     * @param locationListCallback is getting events from location list
     */
    public LocationListAdapter(List<PinnedLocation> locationEntities, @NonNull LocationListCallback locationListCallback) {
        this.locationEntities = locationEntities;
        this.locationListCallback = locationListCallback;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ItemLocationListViewBinding openSourceViewBinding = ItemLocationListViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new LocationViewHolder(openSourceViewBinding);
            case VIEW_TYPE_EMPTY:
            default:
                ItemEmptyLocationViewBinding emptyViewBinding = ItemEmptyLocationViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new EmptyViewHolder(emptyViewBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return locationEntities.isEmpty() ? 1 : locationEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!locationEntities.isEmpty()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    public class LocationViewHolder extends BaseViewHolder {

        final ItemLocationListViewBinding mBinding;

        LocationViewHolder(ItemLocationListViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            mBinding.setPinnedLocation(locationEntities.get(position));
            mBinding.executePendingBindings();
            mBinding.getRoot().setOnClickListener(v -> locationListCallback.onLocationClicked(locationEntities.get(position)));
        }
    }

    static class EmptyViewHolder extends BaseViewHolder {
        final ItemEmptyLocationViewBinding mBinding;

        EmptyViewHolder(ItemEmptyLocationViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            mBinding.executePendingBindings();
        }
    }
}
