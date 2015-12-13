package ru.ifmo.android_2015.search_song.list;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecylcerDividersDecorator extends RecyclerView.ItemDecoration {

    private final Paint paint;

    public RecylcerDividersDecorator(int color) {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView recyclerView,
                               RecyclerView.State state) {
        int position = recyclerView.getChildLayoutPosition(view);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int itemCount = adapter == null ? 0 : adapter.getItemCount();
        // Резервируем под разделитель один пиксель в высоту под каждым элементом
        // кроме последнего
        int bottomInset = position >= 0 && position < itemCount - 3
                        ? 3
                        : 0;
        outRect.set(0, 0, 0, bottomInset);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.State state) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int itemCount = adapter == null ? 0 : adapter.getItemCount();

        int width = recyclerView.getWidth();

        // Рисуем разделитель высотой в один пиксель под каждым элементом списка
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            int position = recyclerView.getChildLayoutPosition(child);
            if (position >= 0 && position < itemCount - 3) {
                float y = child.getBottom() + 3;
                c.drawLine(0, y, width, y, paint);
            }
        }
    }
}
