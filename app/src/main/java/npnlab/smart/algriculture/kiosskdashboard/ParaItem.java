package npnlab.smart.algriculture.kiosskdashboard;

import android.widget.FrameLayout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ParaItem extends FrameLayout {
    boolean isCurrentRemoteFocus = false;
    IParaItemListener mListener;

    Context mContext;

    ParaImageView mBackgroundView;
    ImageView mIconView;
    TextView mTextView;
    LinearLayout mIconTextView;

    public int mColor = Color.GREEN;
    int mAngle = 90;
    public String mText = "UNKNOWN";
    public Drawable mIcon;


    private boolean isFocus = false;


    public ParaItem(Context context) {
        super(context);
        mContext = context;
        initializeItem();
    }

    public void refreshIcon(){
        this.setFocusable(true);

        mBackgroundView = setupBackgroundView();
        mIconTextView = setupIconAndTextView();

        this.removeAllViews();
        this.addView(mBackgroundView);
        this.addView(mIconTextView);
        mTextView.setVisibility(VISIBLE);
        displayFocus(true);
    }
    public ParaItem(Context context, int color, int angle, Drawable icon, String text, IParaItemListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        mColor = color;
        mAngle = angle;
        mText = text;
        mIcon = icon;

        initializeItem();
    }

    public ParaItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeItem();
    }


    protected void initializeItem() {

        this.setFocusable(true);

        mBackgroundView = setupBackgroundView();
        mIconTextView = setupIconAndTextView();

        this.removeAllViews();
        this.addView(mBackgroundView);
        this.addView(mIconTextView);

        this.setOnFocusChangeListener((view, b) -> {
            displayFocus(b);
            if (mListener != null) {
                mListener.onParaItemFocused(view, b);
            }
        });

        this.setOnHoverListener((view, motionEvent) -> {
//            if (isCurrentRemoteFocus) {
//                displayFocus(true);
//                return false;
//            }
            if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                view.requestFocus();
                displayFocus(true);
                if (mListener != null) {
                    mListener.onParaItemFocused(view, true);
                }
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                view.clearFocus();
                displayFocus(false);
                if (mListener != null) {
                    mListener.onParaItemFocused(view, false);
                }
            }
            return false;
        });

    }

    public void displayFocus(boolean isShow) {

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int itemH = (int)((float)displayMetrics.heightPixels * 0.15f);
        int iconSize = (int)(0.6 * itemH);

        LinearLayout.LayoutParams iconViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  isShow ? iconSize : iconSize);
        iconViewParams.gravity = Gravity.CENTER;

        mIconView.setLayoutParams(iconViewParams);

        mTextView.setTextSize(17);

        mTextView.setVisibility(isShow ? VISIBLE : GONE);
        isFocus = isShow;

        //Log.d("TEST UBC", "Item height: " + itemH);
    }

    protected ParaImageView setupBackgroundView() {
        ParaImageView view = new ParaImageView(mContext, mAngle, mColor);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams iconViewParams;
        iconViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        iconViewParams.setMargins(0,0,0,0);
        view.setLayoutParams(iconViewParams);

        view.setFocusStatus(isFocus);

        return view;
    }

    protected ImageView setupIconView() {

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int itemH = (int)((float)displayMetrics.heightPixels * 0.15f);
        int iconSize = (int)(0.6 * itemH);

        ImageView view = new ImageView(mContext);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //LayoutParams iconViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80);
        LayoutParams iconViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, iconSize);
        iconViewParams.gravity = Gravity.CENTER;
        view.setLayoutParams(iconViewParams);
        view.setImageDrawable(mIcon);

        return view;
    }

    protected TextView setupTextView() {
        TextView view = new TextView(mContext);


        view.setText(mText);


        view.setTextColor( mColor == -1 ? Color.BLACK : Color.WHITE);

        //view.setTextColor(mColor + Color.parseColor("#880000"));

        view.setTypeface(null, Typeface.BOLD);
        view.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(params);
        view.setVisibility(GONE);
        return view;
    }

    protected LinearLayout setupIconAndTextView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        layout.setLayoutParams(params);

        mIconView = setupIconView();
        mTextView = setupTextView();
        layout.addView(mIconView);
        layout.addView(mTextView);
        return layout;
    }

}
