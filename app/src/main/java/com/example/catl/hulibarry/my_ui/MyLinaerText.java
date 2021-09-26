package com.example.catl.hulibarry.my_ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.catl.hulibarry.R;


/**
 * Created by 123 on 2018/5/18.
 */

public class MyLinaerText extends LinearLayout{

    /**
     * 左侧文字
     */
    private TextView LeftText;

    /**
     * 右侧图片
     */
    private ImageView RightIg;



    private float text_size; //字体大小
    private String str;      //文本内容
    private int color;       //字体颜色
    private float ig_width;    //图片宽度
    private float ig_height;   //图片高度


    public MyLinaerText(Context context) {
        this(context, null);
    }

    public MyLinaerText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinaerText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyLinaerText);
        text_size = typedArray.getDimension(R.styleable.MyLinaerText_text_size, 50);
        str = typedArray.getString(R.styleable.MyLinaerText_text_str);
        color = typedArray.getColor(R.styleable.MyLinaerText_text_color, getResources().getColor(R.color.black));
        ig_width = typedArray.getDimension(R.styleable.MyLinaerText_ig_width, 50);
        ig_height = typedArray.getDimension(R.styleable.MyLinaerText_ig_height, 50);

    }

    /**
     * 初始化控件
     */
    private void setViewData() {
        LeftText = new TextView(getContext());
        RightIg = new ImageView(getContext());
        LeftText.setTextColor(color);
        LeftText.setTextSize(text_size);
        if (str != null) {
            LeftText.setText(str);
        } else LeftText.setText("");

        RightIg.setScaleType(ImageView.ScaleType.FIT_START);
        RightIg.setImageResource(R.drawable.right_ig);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            addLinaerView();
        }
    }

    /**
     * 添加内容
     */
    private void addLinaerView() {

        setViewData();

        this.setOrientation(HORIZONTAL);
        this.setBackgroundResource(R.drawable.mylinear_bg);
        this.setPadding(15, 5, 5, 5);
        this.setGravity(Gravity.CENTER_VERTICAL);


        LayoutParams pl = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        LeftText.setLayoutParams(pl);
        addView(LeftText);
        addView(RightIg);
    }

    /**
     * 设置父控件padding距离
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setLinearPadding(int left, int top, int right, int bottom) {
        this.setPadding(left, top, right, bottom);
        invalidate();
    }

    /**
     * 设置左侧文本
     *
     * @param str
     */
    public void setLeftText(String str) {
        if (LeftText != null) {
            LeftText.setText(String.valueOf(str));
        }
    }

    /**
     * 设置文本颜色
     * @param color
     */
    public void setTextColor(int color){
        if(LeftText!=null){
            LeftText.setTextColor(color);
        }
    }

    /**
     * 设置右侧图片资源ID
     *
     * @param resId
     */
    public void setRightIg(int resId) {
        if (RightIg != null) {
            RightIg.setBackgroundResource(resId);
        }
    }

    /**
     * 设置右侧图片
     *
     * @param drawable
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setRightIg(Drawable drawable) {
        if (RightIg != null) {
            RightIg.setBackground(drawable);
        }
    }

}
