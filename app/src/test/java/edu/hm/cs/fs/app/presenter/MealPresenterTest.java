package edu.hm.cs.fs.app.presenter;

import com.fk07.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.MealModel;
import edu.hm.cs.fs.app.view.IMealView;
import edu.hm.cs.fs.common.model.Meal;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MealPresenterTest {
    @Test
    public void testGetMealSuccess() {
        final IMealView view = mock(IMealView.class);

        final MealModel model = mock(MealModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Meal>> callback = (ICallback<List<Meal>>) invocation.getArguments()[1];
                callback.onSuccess(new ArrayList<Meal>());
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));

        final MealPresenter presenter = new MealPresenter(view, model);
        presenter.loadMeals(true);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getAll(anyBoolean(), any(ICallback.class));
        verify(view, atLeastOnce()).showContent(anyList());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetMealError() {
        final IMealView view = mock(IMealView.class);

        final MealModel model = mock(MealModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback callback = (ICallback) invocation.getArguments()[1];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));

        final MealPresenter presenter = new MealPresenter(view, model);
        presenter.loadMeals(true);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getAll(anyBoolean(), any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
