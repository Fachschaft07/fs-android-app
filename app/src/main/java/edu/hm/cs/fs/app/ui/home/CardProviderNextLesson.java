package edu.hm.cs.fs.app.ui.home;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.fk07.R;

/**
 * @author Fabio
 */
public class CardProviderNextLesson extends CardProvider<CardProviderNextLesson> {
    private String mTextTime;
    private String mTextPlace;

    public CardProviderNextLesson setTime(@NonNull final String time) {
        mTextTime = time;
        return this;
    }

    public CardProviderNextLesson setPlace(@NonNull final String place) {
        mTextPlace = place;
        return this;
    }

    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        ((TextView) view.findViewById(R.id.textTime)).setText(mTextTime);
        ((TextView) view.findViewById(R.id.textPlace)).setText(mTextPlace);
    }
}
