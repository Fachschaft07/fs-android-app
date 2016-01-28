package edu.hm.cs.fs.app.ui.timetable.structure;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;

import edu.hm.cs.fs.app.ui.timetable.TimetableAdapter;

/**
 * @author Fabio
 */
public class Cell {
    private final int mRow;
    private final int mColumn;
    @DrawableRes
    private final int mBackgroundId;

    public Cell(int row, int column, @DrawableRes int backgroundId) {
        mRow = row;
        mColumn = column;
        mBackgroundId = backgroundId;
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }

    public void render(TimetableAdapter.ViewHolder holder) {
        // Empty cell
        holder.mCell.setBackgroundResource(mBackgroundId);
        holder.mCount.setImageDrawable(null);
        holder.mCount.setVisibility(View.GONE);
        holder.mLayoutSubject.setVisibility(View.GONE);
        setText(holder.mSubject, null);
        setText(holder.mRoom, null);
        setText(holder.mInfo, null);
    }

    public void setText(final TextView view, final String text) {
        if (text != null && text.length() > 0) {
            view.setText(text);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
