package com.fastaccess.ui.widgets.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.ui.adapter.SimpleListAdapter;
import com.fastaccess.ui.base.BaseDialogFragment;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;
import com.fastaccess.ui.widgets.recyclerview.scroll.RecyclerViewFastScroller;

import net.grandcentrix.thirtyinch.TiPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Kosh on 31 Dec 2016, 3:19 PM
 */

public class ListDialogView<O extends Parcelable> extends BaseDialogFragment implements BaseViewHolder.OnItemClickListener<O> {

    public static final String TAG = ListDialogView.class.getSimpleName();

    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.fastScroller) RecyclerViewFastScroller fastScroller;

    public interface onSimpleItemSelection<O extends Parcelable> {
        void onItemSelected(O item);
    }

    public interface onSimpleItemLongSelection<O extends Parcelable> {
        void onItemLongSelected(O item);
    }

    @Nullable private onSimpleItemSelection onSimpleItemSelection;
    @Nullable private onSimpleItemLongSelection onSimpleItemLongSelection;

    @Override protected int fragmentLayout() {
        return R.layout.simple_list_dialog;
    }

    @Override protected void onFragmentCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList<O> objects = getArguments().getParcelableArrayList(BundleConstant.ITEM);
        String titleText = getArguments().getString(BundleConstant.EXTRA);
        title.setText(titleText);
        if (objects != null) {
            SimpleListAdapter<O> adapter = new SimpleListAdapter<>(objects, this);
            recycler.addDivider();
            recycler.setAdapter(adapter);
        } else {
            dismiss();
        }
        fastScroller.attachRecyclerView(recycler);
    }

    @Override public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof onSimpleItemSelection)
                onSimpleItemSelection = (onSimpleItemSelection) getParentFragment();
            if (getParentFragment() instanceof onSimpleItemLongSelection)
                onSimpleItemLongSelection = (onSimpleItemLongSelection) getParentFragment();
        } else {
            if (context instanceof onSimpleItemSelection)
                onSimpleItemSelection = (onSimpleItemSelection) context;
            if (context instanceof onSimpleItemLongSelection)
                onSimpleItemLongSelection = (onSimpleItemLongSelection) context;
        }

    }

    @Override public void onDetach() {
        super.onDetach();
        onSimpleItemSelection = null;
        onSimpleItemLongSelection = null;
    }

    @NonNull @Override public TiPresenter providePresenter() {
        return new BasePresenter();
    }

    @SuppressWarnings("unchecked") @Override public void onItemClick(int position, View v, O item) {
        if (onSimpleItemSelection != null) {
            onSimpleItemSelection.onItemSelected(item);
        }
        dismiss();
    }

    @SuppressWarnings("unchecked") @Override public void onItemLongClick(int position, View v, O item) {
        if (onSimpleItemLongSelection != null) {
            onSimpleItemLongSelection.onItemLongSelected(item);
        }
    }

    public void initArguments(@NonNull String title, @NonNull ArrayList<O> objects) {
        setArguments(Bundler.start()
                .put(BundleConstant.EXTRA, title)
                .putParcelableArrayList(BundleConstant.ITEM, objects)
                .end());
    }

    public void initArguments(@NonNull String title, @NonNull List<O> objects) {
        setArguments(Bundler.start()
                .put(BundleConstant.EXTRA, title)
                .putParcelableArrayList(BundleConstant.ITEM, (ArrayList<? extends Parcelable>) objects)
                .end());
    }
}
