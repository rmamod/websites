package fr.rahim.websites.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import fr.rahim.websites.R;


public class FastScroller extends FrameLayout {

    private static final int TRACK_SNAP_RANGE = 5;
    private final RecyclerView.OnScrollListener mScrollListener = new ScrollListener();

    private RecyclerView mRecyclerView;
    private View mScroller;
    private int mHeight;

    public FastScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    public FastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
      mRecyclerView = recyclerView;
      mRecyclerView.setOnScrollListener(mScrollListener);

    }

    private void initialise(Context context) {
        setClipChildren(false);
        setBackgroundColor(getResources().getColor(android.R.color.primary_text_dark_nodisable));
        LayoutInflater inflater = LayoutInflater.from(context);

        inflater.inflate(R.layout.view_fast_scroller, this, true);
        mScroller = findViewById(R.id.scroller);
        mScroller.setClickable(false);
    }

    private void setPosition(float y) {
        float position = y / mHeight;
        int handleHeight = mScroller.getHeight();
        mScroller.setY(getValueInRange(0, mHeight - handleHeight, (int) ((mHeight - handleHeight) * position)));
    }

    private int getValueInRange(int min, int max, int value) {
        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            setPosition(event.getY());
            setRecyclerViewPosition(event.getY());
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void setRecyclerViewPosition(float y) {
        if (mRecyclerView != null) {
            int itemCount = mRecyclerView.getAdapter().getItemCount();
            float proportion;
            if (mScroller.getY() == 0) {
                proportion = 0f;
            } else if (mScroller.getY() + mScroller.getHeight() >= mHeight - TRACK_SNAP_RANGE) {
                proportion = 1f;
            } else {
                proportion = y / (float) mHeight;
            }
            int targetPos = getValueInRange(0, itemCount - 1, (int) (proportion * (float) itemCount));
            mRecyclerView.scrollToPosition(targetPos);
        }
    }

    private  class ScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            View firstVisibleView = recyclerView.getChildAt(0);
            int firstVisiblePosition = recyclerView.getChildPosition(firstVisibleView);
            int visibleRange = recyclerView.getChildCount();
            int lastVisiblePosition = firstVisiblePosition + visibleRange;
            int itemCount = recyclerView.getAdapter().getItemCount();
            int position;
            if (firstVisiblePosition == 0) {
                position = 0;
            } else if (lastVisiblePosition == itemCount - 1) {
                position = itemCount - 1;
            } else {
                position = firstVisiblePosition;
            }
            float proportion = (float) position / (float) itemCount;
            setPosition(mHeight * proportion);
        }
    }


}
