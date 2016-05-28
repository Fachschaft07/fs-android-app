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
import edu.hm.cs.fs.app.database.RoomSearchModel;
import edu.hm.cs.fs.app.view.IRoomSearchView;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Room;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RoomSearchPresenterTest {
    @Test
    public void testGetFreeRoomsSuccess() {
        final IRoomSearchView view = mock(IRoomSearchView.class);

        final RoomSearchModel model = mock(RoomSearchModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Room>> callback = (ICallback<List<Room>>) invocation.getArguments()[2];
                callback.onSuccess(new ArrayList<Room>());
                return null;
            }
        }).when(model).getFreeRooms(any(Day.class), any(Time.class), any(ICallback.class));

        final RoomSearchPresenter presenter = new RoomSearchPresenter(view, model);
        presenter.loadCurrentFreeRooms();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getFreeRooms(any(Day.class), any(Time.class), any(ICallback.class));
        verify(view, atLeastOnce()).showContent(anyList());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetFreeRoomsError() {
        final IRoomSearchView view = mock(IRoomSearchView.class);

        final RoomSearchModel model = mock(RoomSearchModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Room>> callback = (ICallback<List<Room>>) invocation.getArguments()[2];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getFreeRooms(any(Day.class), any(Time.class), any(ICallback.class));

        final RoomSearchPresenter presenter = new RoomSearchPresenter(view, model);
        presenter.loadCurrentFreeRooms();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getFreeRooms(any(Day.class), any(Time.class), any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
