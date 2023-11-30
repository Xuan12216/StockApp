package com.example.ZhuJiaHong.object.favourite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.object.strategy.StrategyLinearLayoutManager;
import com.google.gson.Gson;
import com.mdbs.base.view.activity.ActivityFavoriteEdit;
import com.mdbs.base.view.activity.ActivityStocksAdd;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.domain.Group;
import com.mdbs.base.view.fragment.BaseFavoriteFragment;
import com.mdbs.base.view.fragment.BaseFragment;
import com.mdbs.base.view.object.base.FilterItemView;
import com.mdbs.base.view.object.dialog.AddGroupDialog;
import com.mdbs.base.view.object.dialog.EditFavoriteDialog;
import com.mdbs.base.view.object.dialog.EditTextDialog;
import com.mdbs.base.view.object.favorite.BaseFavoriteContract;
import com.mdbs.base.view.object.favorite.BaseFavoritePresenter;
import com.mdbs.base.view.object.favorite.BaseRecycleViewHolderBuilder;
import com.mdbs.base.view.object.favorite.FavoriteLayout;
import com.mdbs.base.view.object.favorite.FavoriteViewType;
import com.mdbs.base.view.object.favorite.GroupAdapter;
import com.mdbs.base.view.object.favorite.StockAdapter;
import com.mdbs.base.view.setting.GalaxySetting;
import com.mdbs.base.view.setting.ResourceTag;
import com.mdbs.base.view.utils.AlertDialogUtil;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.base.view.utils.FavoriteUtil;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class MyBaseFavoriteFragment extends BaseFragment implements BaseFavoriteContract.BaseFavoriteView {
    private final String List_Mode = "list";
    private final String Grid_Mode = "grid";
    private BaseFavoriteContract.BaseFavoritePresenter presenter;
    private FavoriteLayout view;
    private EditFavoriteDialog editFavoriteDialog;
    protected StrategyLinearLayoutManager layoutManager;

    private BaseFavoriteFragment.OpenSubViewClickListener openSubViewClickListener;
    private FavoriteLayout.Listener sortListener = (position, name, stockDataList, status) -> {
        if (status == FilterItemView.Status.None) {
            if (this.view.mGroupAdapter.getCurrentGroup().stockList != null && this.view.mGroupAdapter.getCurrentGroup().stockList.size() != 0) {
                this.view.stockLayout.setVisibility(View.VISIBLE);
                this.view.emptyStockImageLayout.setVisibility(View.GONE);
                this.view.mStockAdapter.set(true, this.view.mGroupAdapter.getCurrentGroup().stockList);
            } else {
                int mipmapResource = this.presenter.isFixedMode() ? GalaxySetting.Factory.getImageOrColor("自選老師空群組_m") : GalaxySetting.Factory.getImageOrColor("自選自選空群組_m");
                if (mipmapResource == 0) {
                    mipmapResource = GalaxySetting.Factory.getImageOrColor("自選自選空群組_m");
                }

                this.view.emptyStockImage.setImageResource(mipmapResource);
                this.view.emptyStockImageLayout.setVisibility(View.VISIBLE);
                this.view.stockLayout.setVisibility(View.GONE);
            }
        } else {
            boolean isAscending = status == FilterItemView.Status.Ascending;
            this.getStockListByFilterClick(position, name, stockDataList, isAscending);
        }

    };
    //1130
    private GroupAdapter.Listener groupListener = new GroupAdapter.Listener() {
        public void onClick(Group group) {
            if (view.mStockAdapter.getList() != group.stockList) {
                presenter.getStockList(group);
            } else {
                if (!presenter.isEditMode()) {
                    return;
                }

                int position = view.mGroupAdapter.getList().indexOf(group);
                EditTextDialog editGroupDialog = new EditTextDialog(mContext);
                editGroupDialog.setTitle(getString(R.string.favorite_group_edit));
                editGroupDialog.setText(group.name);
                editGroupDialog.setOnClickListener((editText) -> {
                    if (!editText.equals(group.name)) {
                        group.name = editText;
                        view.mGroupAdapter.notifyItemChanged(position);
                        editGroupDialog.dismiss();
                    }
                });
                editGroupDialog.show();
            }
        }

        public void onDelete(Group group) {
            presenter.deleteGroup(group);
        }
    };
    private StockAdapter.Listener stockListener = new StockAdapter.Listener() {
        public void onClick(BaseStockData stockData) {
            if (view.getFavoriteType() == FavoriteViewType.All_Page) {
                Intent intent = new Intent(String.format("%s.stock", ((Activity) mContext).getApplication().getPackageName()));
                intent.putExtra("stockData", stockData);
                Group group = new Group();
                group.stockList = view.mStockAdapter.getList();
                intent.putExtra("page_category", (new Gson()).toJson(group));
                mContext.startActivity(intent);
            } else if (openSubViewClickListener != null) {
                openSubViewClickListener.openSubView(stockData);
            }
        }

        public void onDelete(BaseStockData stockData) {
            try {
                if (view.mStockAdapter.getList() != null && view.mStockAdapter.getList().size() != 0) {
                    view.emptyStockImageLayout.setVisibility(View.GONE);
                    view.stockLayout.setVisibility(View.VISIBLE);
                    if (view.getFavoriteType() == FavoriteViewType.Half_Page) {
                        view.stockSubView.setVisibility(View.VISIBLE);
                    }
                } else {
                    int mipmapResource = presenter.isFixedMode() ? GalaxySetting.Factory.getImageOrColor("自選老師空群組_m") : GalaxySetting.Factory.getImageOrColor("自選自選空群組_m");
                    if (mipmapResource == 0) {
                        mipmapResource = GalaxySetting.Factory.getImageOrColor("自選自選空群組_m");
                    }

                    view.emptyStockImage.setImageResource(mipmapResource);
                    view.emptyStockImageLayout.setVisibility(View.VISIBLE);
                    view.stockLayout.setVisibility(View.GONE);
                    if (view.getFavoriteType() == FavoriteViewType.Half_Page) {
                        view.stockSubView.setVisibility(View.GONE);
                    }
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    };

    public MyBaseFavoriteFragment() {
    }

    public abstract BaseRecycleViewHolderBuilder createViewHolderBuilder();

    public abstract View createOverview();

    public abstract void getStockListByFilterClick(int var1, String var2, List<BaseStockData> var3, boolean var4);

    public abstract View createSubView();

    public abstract BaseFavoriteFragment.OpenSubViewClickListener initOpenSubViewClickListener();

    public void setStockListByFilterClick(List<BaseStockData> stockData) {

        this.view.mStockAdapter.set(true, stockData);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = this.createFavoriteView();
        layoutManager = new StrategyLinearLayoutManager(mContext);
        this.view.layoutManager = layoutManager;
        this.presenter = new BaseFavoritePresenter(this, this.mContext);
        this.initListener();
        this.initTouchHelper();
        this.setEditorMode(this.mTitleView.isEditMode());
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mTitleView.setTitle(this.mContext.getString(R.string.favorite_title));
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(-1, -1);
        LinearLayout layout = (LinearLayout)super.onCreateView(inflater, container, savedInstanceState);
        layout.addView(this.view, viewParams);
        return layout;
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        GalaxySetting.Factory.initResource(ResourceTag.自選);
        this.presenter.getGroupList();
    }

    public void onPause() {
        super.onPause();
        this.presenter.setCurrentGroupPosition(this.view.mGroupAdapter.getCurrentPosition());
    }

    private FavoriteLayout createFavoriteView() {
        View subView = this.createSubView();
        return new FavoriteLayout(this.mContext, this.createViewHolderBuilder(), this.createOverview(), subView);
    }

    protected void switchToTeacherMode(boolean isTeacherMode) {
        this.presenter.switchMode(isTeacherMode);
        if (isTeacherMode) {
            this.setEditorMode(false);
            this.mTitleView.setRightButtonVisible(false, 8);
        } else {
            this.mTitleView.setRightButtonVisible(false, 0);
        }

    }

    public void showSortButton(int resource, View.OnClickListener listener) {
        this.view.showSortButton(resource, listener);
    }

    public boolean isTeacherMode() {
        return this.presenter.isFixedMode();
    }

    private void initListener() {
        this.view.setSortListener(this.sortListener);
        this.view.setGroupListener(this.groupListener);
        this.view.setStockListener(this.stockListener);
        this.view.setFunctionListener((mode, position) -> {
            this.view.mStockAdapter.setCurrentType(position);
            if ("list".equals(mode.toLowerCase())) {
                this.view.switchToListMode();
            } else if ("grid".equals(mode.toLowerCase())) {
                this.view.switchToGridMode();
            }

            this.view.stockRecyclerView.setAdapter(this.view.mStockAdapter);
            int teacherPosition = GalaxySetting.Factory.getInteger("自選上方功能列老師視角位置_i");
            this.switchToTeacherMode(teacherPosition == position);
        });
        this.mTitleView.setRightImageButton(GalaxySetting.Factory.getImageOrColor("自選編輯_m"), false, (view) -> {
            if (BaseUtil.isSubscribe(this.mContext)) {
                this.setEditorMode(true);
            } else {
                this.setEditorMode(true);
                //BaseUtil.popUpPurchasingDialog(this.mContext);
            }

        });
        this.mTitleView.setRightTextButton(this.getString(R.string.favorite_completed), true, (v) -> {
            this.setEditorMode(false);
            this.presenter.updateGroupList(this.view.mGroupAdapter.getList());
        });
        this.mTitleView.setLeftImageButton(GalaxySetting.Factory.getImageOrColor("自選搜尋個股_m"), false, (v) -> {
            if (BaseUtil.isSubscribe(this.mContext)) {
                BaseUtil.showStockSearchDialog(this.mContext);
            } else {
                BaseUtil.showStockSearchDialog(this.mContext);
                //BaseUtil.popUpPurchasingDialog(this.mContext);
            }

        });
        this.mTitleView.setLeftTextButton(this.getString(R.string.favorite_cancel), true, (view) -> {
            this.setEditorMode(false);
            this.presenter.getGroupList();
        });
        this.mTitleView.initLeftButtonExperienceMode(false, GalaxySetting.Factory.getImageOrColor("自選搜尋個股上鎖_m"));
        this.mTitleView.initRightButtonExperienceMode(false, GalaxySetting.Factory.getImageOrColor("自選編輯上鎖_m"));
        this.editFavoriteDialog = new EditFavoriteDialog(this.mContext);
        this.editFavoriteDialog.setAddStockListener((v) -> {
            Intent intent = new Intent(this.mContext, ActivityStocksAdd.class);
            intent.putExtra("groupList", (new Gson()).toJson(this.presenter.getTempGroup()));
            this.mContext.startActivity(intent);
            this.editFavoriteDialog.dismiss();
            this.setEditorMode(false);
        });
        this.editFavoriteDialog.setEditListener((view) -> {
            Intent intent = new Intent(this.mContext, ActivityFavoriteEdit.class);
            intent.putExtra("groupList", (new Gson()).toJson(this.presenter.getTempGroup()));
            this.mContext.startActivity(intent);
            this.editFavoriteDialog.dismiss();
            this.setEditorMode(false);
        });
        this.editFavoriteDialog.setAddGroupListener((v) -> {
            AddGroupDialog addGroupDialog = new AddGroupDialog(this.mContext);
            addGroupDialog.setOnClickListener((view) -> {
                if (FavoriteUtil.groupList.size() >= 20) {
                    AlertDialogUtil.showErrorAlert(this.mContext, this.getString(R.string.alert_add_group_fulled_error), false, (View.OnClickListener)null);
                } else {
                    Iterator var3 = FavoriteUtil.groupList.iterator();

                    Group group;
                    do {
                        if (!var3.hasNext()) {
                            this.presenter.addGroup(addGroupDialog.getGroupName());
                            return;
                        }

                        group = (Group)var3.next();
                    } while(!group.name.equals(addGroupDialog.getGroupName()));

                    AlertDialogUtil.showErrorAlert(this.mContext, this.getString(R.string.alert_add_group_repeat_error), false, (View.OnClickListener)null);
                }
            });
            addGroupDialog.show();
        });
        this.view.addButton.setOnClickListener((view) -> {
            this.editFavoriteDialog.show();
        });
        this.openSubViewClickListener = this.initOpenSubViewClickListener();
    }

    private void setEditorMode(boolean isEditor) {
        try {
            this.presenter.setEditMode(isEditor);
            this.presenter.setCurrentGroupPosition(this.view.mGroupAdapter.getCurrentPosition());
            if (isEditor) {
                this.mTitleView.atEditMode();
            } else {
                this.mTitleView.atViewMode();
            }

            if (isEditor) {
                this.renderView(true);
                this.view.resetFilter();
                this.presenter.getAllList();
            } else {
                this.renderView(false);
            }
        } catch (Exception var3) {
        }

    }

    public void renderView(boolean canEdit) {
        this.view.mGroupAdapter.setEditMode(canEdit);
        this.view.mStockAdapter.setEditMode(canEdit);
        this.view.sortLayout.setVisibility(canEdit ? View.GONE : View.VISIBLE);
        this.view.addButton.setVisibility(canEdit ? View.VISIBLE : View.GONE);
        this.mTitleView.getRightCustomLayout().setVisibility(canEdit ? View.GONE : View.VISIBLE);
        this.mTitleView.getLeftCustomLayout().setVisibility(canEdit ? View.GONE : View.VISIBLE);
    }

    private void initTouchHelper() {
        this.view.mGroupAdapter.itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(15, 0);
            }

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    int target1 = viewHolder.getAdapterPosition();
                    int target2 = target.getAdapterPosition();
                    int i;
                    if (target1 < target2) {
                        for(i = target1; i < target2; ++i) {
                            Collections.swap(view.mGroupAdapter.mGroupList, i, i + 1);
                        }
                    } else if (target1 > target2) {
                        for(i = target1; i > target2; --i) {
                            Collections.swap(view.mGroupAdapter.mGroupList, i, i - 1);
                        }
                    }
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    Collections.swap(view.mGroupAdapter.mGroupList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                }

                view.mGroupAdapter.notifyMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (view.mGroupAdapter != null) {
                    view.mGroupAdapter.notifyDataSetChanged();
                }

            }

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            public boolean isLongPressDragEnabled() {
                return false;
            }
        });
        this.view.mGroupAdapter.itemTouchHelper.attachToRecyclerView(this.view.groupRecycleView);
        this.view.mStockAdapter.itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(15, 0);
            }

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    int target1 = viewHolder.getAbsoluteAdapterPosition();
                    int target2 = target.getAbsoluteAdapterPosition();
                    int i;
                    if (target1 < target2) {
                        for(i = target1; i < target2; ++i) {
                            Collections.swap(view.mStockAdapter.mStockDataList, i, i + 1);
                        }
                    } else if (target1 > target2) {
                        for(i = target1; i > target2; --i) {
                            Collections.swap(view.mStockAdapter.mStockDataList, i, i - 1);
                        }
                    }
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    Collections.swap(view.mStockAdapter.mStockDataList, viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());
                }

                view.mStockAdapter.setDefaultItem(target.getAbsoluteAdapterPosition());
                view.mStockAdapter.notifyItemMoved(viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());
                return true;
            }

            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                view.mGroupAdapter.getCurrentGroup().stockList = view.mStockAdapter.getList();
                ((Activity) mContext).runOnUiThread(() -> {
                    view.mStockAdapter.notifyDataSetChanged();
                });
            }

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            public boolean isLongPressDragEnabled() {
                return false;
            }
        });
        this.view.mStockAdapter.itemTouchHelper.attachToRecyclerView(this.view.stockRecyclerView);
    }

    public void onGetGroupListFinish(List<Group> groupList) {
        Group group = null;
        if (groupList.size() != 0) {
            try {
                group = (Group)groupList.get(this.presenter.getCurrentGroupPosition());
            } catch (Exception var4) {
                group = (Group)groupList.get(0);
                this.presenter.setCurrentGroupPosition(0);
            }
        }

        if (group != null && this.isTeacherMode()) {
            this.view.setUpdateTime(group.modify_time);
        } else {
            this.view.setUpdateTime("");
        }

        this.view.mGroupAdapter.set(groupList);
        this.view.mGroupAdapter.setCurrentPosition(this.presenter.getCurrentGroupPosition());
        this.view.groupRecycleView.scrollToPosition(this.presenter.getCurrentGroupPosition());
        this.presenter.getStockList(group);
    }

    public void onAddGroupFinish(String resultCode) {
        switch (resultCode) {
            case "0":
                AlertDialogUtil.showMessageAlert(this.mContext, this.getString(R.string.add_group_success), false, (view) -> {
                    Intent intent = new Intent(this.mContext, ActivityStocksAdd.class);
                    intent.putExtra("groupList", (new Gson()).toJson(this.presenter.getTempGroup()));
                    this.mContext.startActivity(intent);
                    this.editFavoriteDialog.dismiss();
                    this.setEditorMode(false);
                });
            default:
        }
    }

    public void onGetStockListFinish(List<BaseStockData> stockDataList) {
        if (stockDataList != null && stockDataList.size() != 0) {
            this.view.emptyStockImageLayout.setVisibility(View.GONE);
            this.view.stockLayout.setVisibility(View.VISIBLE);
            if (this.view.getFavoriteType() == FavoriteViewType.Half_Page) {
                this.view.stockSubView.setVisibility(View.VISIBLE);
            }
        } else {
            int mipmapResource = this.presenter.isFixedMode() ? GalaxySetting.Factory.getImageOrColor("自選老師空群組_m") : GalaxySetting.Factory.getImageOrColor("自選自選空群組_m");
            if (mipmapResource == 0) {
                mipmapResource = GalaxySetting.Factory.getImageOrColor("自選自選空群組_m");
            }

            this.view.emptyStockImage.setImageResource(mipmapResource);
            this.view.emptyStockImageLayout.setVisibility(View.VISIBLE);
            this.view.stockLayout.setVisibility(View.GONE);
            if (this.view.getFavoriteType() == FavoriteViewType.Half_Page) {
                this.view.stockSubView.setVisibility(View.GONE);
            }
        }

        this.view.mStockAdapter.clearItemSelected();
        this.view.mStockAdapter.set(false, stockDataList);
        this.view.notifyFilter();
    }

    public void onUpdateGroupFinish() {
        this.presenter.getGroupList();
    }

    public void onDeleteGroupFinish(String resultCode) {
        this.view.mGroupAdapter.setCurrentPosition(this.presenter.getCurrentGroupPosition());
        this.presenter.getStockList(this.view.mGroupAdapter.getCurrentGroup());
    }

    public void onGetAllFinish(List<Group> groupList) {
        this.view.mGroupAdapter.set(groupList);
        this.view.mGroupAdapter.setCurrentPosition(this.presenter.getCurrentGroupPosition());
        if (this.view.mGroupAdapter.getCurrentGroup() != null) {
            this.presenter.getStockList(this.view.mGroupAdapter.getCurrentGroup());
        }

    }

    public void onError(String message) {
        AlertDialogUtil.showErrorAlert(this.mContext, message, false, (View.OnClickListener)null);
    }

    public boolean isEditor() {
        return this.presenter.isEditMode();
    }

    public interface OpenSubViewClickListener {
        void openSubView(BaseStockData var1);
    }
}
