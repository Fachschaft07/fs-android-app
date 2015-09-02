package edu.hm.cs.fs.app.ui.timetable;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.app.presenter.TimetableEditorPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.view.ITimetableEditorView;
import edu.hm.cs.fs.common.model.Group;

/**
 * @author Fabio
 */
public class TimetableEditorFragment extends BaseFragment<TimetableEditorPresenter> implements ITimetableEditorView, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    @Bind(R.id.textGroupLayout)
    TextInputLayout mTextGroup;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().reset();
                getMainActivity().getNavigator().goOneBack();
            }
        });
        mToolbar.inflateMenu(R.menu.timetable_editor);
        mToolbar.setOnMenuItemClickListener(this);

        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(false);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new TimetableEditorPresenter(getActivity(), this));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_timetable_editor;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                getPresenter().save();
                getMainActivity().getNavigator().goOneBack();
                return true;
        }
        return false;
    }

    @OnClick(R.id.search)
    @Override
    public void onRefresh() {
        final Group group = Group.of(mTextGroup.getEditText().getText().toString());
        if (group.getStudy() != null) {
            mTextGroup.setError("");
            getPresenter().loadModules(group);
        } else {
            mTextGroup.setError(getString(R.string.group_format));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
