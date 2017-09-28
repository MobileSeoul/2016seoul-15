package kr.edcan.buspolis.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import kr.edcan.buspolis.R;


/**
 * Created by JunseokOh on 2016. 8. 6..
 */
public class CartaTagView extends TextView {
    boolean fullMode = false;
    boolean textColorEnabled = false;
    int color = Color.BLACK;
    int textColor = Color.WHITE;
    int height, width;

    private Paint innerPaint, bgPaint;

    public CartaTagView(Context context) {
        super(context);
    }

    public CartaTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CartaTagView);
        setTypedArray(array);
    }

    private void setTypedArray(TypedArray array) {
        fullMode = array.getBoolean(R.styleable.CartaTagView_fullMode, false);
        color = array.getColor(R.styleable.CartaTagView_themeColor, Color.BLACK);
        textColor = array.getColor(R.styleable.CartaTagView_textThemeColor, Color.BLACK);
        textColorEnabled = array.getBoolean(R.styleable.CartaTagView_textThemeColorEnabled, false);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();
    }

    public void setView() {
        if (!textColorEnabled) setTextColor((fullMode) ? Color.WHITE : color);
        else setTextColor(textColor);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setView();
        Point center = new Point(width / 2, height / 2);
        int strokeWidth = getResources().getDimensionPixelSize(R.dimen.stroke_width);
        int innerH = height - strokeWidth;
        int innerW = width - strokeWidth;
        bgPaint = new Paint();
        bgPaint.setColor(color);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeWidth(strokeWidth);
        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        innerPaint.setColor(color);
        innerPaint.setStyle(Paint.Style.FILL);

        int left = center.x - (innerW / 2);
        int top = center.y - (innerH / 2);
        int right = center.x + (innerW / 2);
        int bottom = center.y + (innerH / 2);

        RectF bgRect = new RectF(0.0f + strokeWidth, 0.0f + strokeWidth, width - strokeWidth, height - strokeWidth);
        RectF innerRect = new RectF(left, top, right, bottom);
        if (fullMode)
            canvas.drawRoundRect(innerRect, innerH / 2, innerH / 2, innerPaint);
        else canvas.drawRoundRect(bgRect, height / 2, height / 2, bgPaint);

        super.onDraw(canvas);
    }

    public void setShapeStyle(boolean fullMode, int color) {
        this.color = color;
        this.fullMode = fullMode;
        requestLayout();
    }


    public void setFullMode(boolean fullMode) {
        this.fullMode = fullMode;
        requestLayout();
    }

    public void setShapeStyle(boolean fullMode, String colorStr) {
        this.color = Color.parseColor(colorStr);
        requestLayout();
    }
    public void setTextColorForceFully(int color){
        this.textColorEnabled = true;
        this.textColor = color;
        requestLayout();
    }

}