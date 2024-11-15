package com.example.busbook.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {

    private Paint paint;
    private BitmapShader shader;
    private Matrix matrix;

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = getBitmapFromDrawable();
        if (bitmap != null) {
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            float scale = Math.max((float) getWidth() / bitmap.getWidth(), (float) getHeight() / bitmap.getHeight());
            matrix.setScale(scale, scale);
            shader.setLocalMatrix(matrix);
            paint.setShader(shader);

            float radius = Math.min(getWidth() / 2.0f, getHeight() / 2.0f) - 4; // Adjust for the 4dp border
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, radius, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getBitmapFromDrawable() {
        if (getDrawable() == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        getDrawable().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        getDrawable().draw(canvas);
        return bitmap;
    }
}
