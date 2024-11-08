package npnlab.smart.algriculture.kiosskdashboard;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.util.DisplayMetrics;


import androidx.appcompat.widget.AppCompatImageView;

public class ParaImageView extends AppCompatImageView {
    int mAngle = 90;

    int mColor = Color.GREEN;

    private boolean isFocus = false;
    Context mContext;

    public ParaImageView(Context context) {
        super(context);
        mContext = context;
    }

    public void setFocusStatus(boolean status){
        isFocus = status;
    }

    public ParaImageView(Context context, int angle, int color) {
        super(context);
        mAngle = angle;
        mColor = color;
        mContext = context;
    }

    public ParaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float h = getHeight();
        float posX = h / (float)Math.tan(Math.toRadians(mAngle));

        Path path = new Path();
        path.moveTo(getWidth(),0);
        path.lineTo(posX, 0);
        path.lineTo(0, getHeight());
        path.lineTo(getWidth() - posX,getHeight());
        path.lineTo(getWidth(), 0);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);


        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int itemH = (int)((float)displayMetrics.heightPixels * 0.15f);
        //int itemW = (int)((float)itemH * 1.3f);
        int currentHeight = getHeight();

        if(getWidth() > getHeight() * 1.5){

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setStrokeWidth(10);
            canvas.drawPath(path, paint);
        }



        //Log.d("TESTUBC", "Item height: " + itemH + " and cover height: " + currentHeight);


        super.onDraw(canvas);
    }

    public void setFillColor(int color) {
        mColor = color;
    }
}
