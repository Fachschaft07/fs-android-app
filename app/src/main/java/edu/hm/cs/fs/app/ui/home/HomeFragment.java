package edu.hm.cs.fs.app.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.fk07.R;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.HomePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.PerActivity;

@PerActivity
public class HomeFragment extends BaseFragment<HomeComponent, HomePresenter> implements HomeView,
        Toolbar.OnMenuItemClickListener, OnDismissCallback,
        RecyclerItemClickListener.OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.listView)
    MaterialListView mListView;

    @Inject
    SharedPreferences mPrefs;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(this);

        mListView.setOnDismissCallback(this);
        mListView.addOnItemTouchListener(this);

        getPresenter().loadHappenings();
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                MainActivity.getNavigator().goTo(new HomePreferenceFragment());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDismiss(@NonNull Card card, int position) {
        //noinspection ConstantConditions
        mPrefs.edit().putBoolean(card.getTag().toString(), false).apply();
    }

    @Override
    public void onRefresh() {
        getPresenter().loadHappenings();
    }

    @Override
    public void showCard(@NonNull Card card) {
        mListView.getAdapter().addAtStart(card);
    }

    @Override
    public void onItemClick(@NonNull final Card card, final int i) {

    }

    @Override
    public void onItemLongClick(@NonNull final Card card, final int i) {

    }

    @Override
    public void clear() {
        mListView.getAdapter().clearAll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected HomeComponent onCreateNonConfigurationComponent() {
        return DaggerHomeComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }
}
