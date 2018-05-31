package reversi.project.tki.p0528_content_provider.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Deviation on 2018-03-12.
 */

public class SquareImageView extends ImageView {

    public SquareImageView(final Context context) {
        super(context);
    }

    public SquareImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}