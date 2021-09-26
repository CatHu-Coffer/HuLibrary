package com.example.catl.hulibarry.ulite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 123 on 2017/11/29.
 */

public class GridRcyDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDividerDarwable;
    private int mDividerHight = 1;
    private int lineNumber = 0;


    public final int[] ATRRS = new int[]{android.R.attr.listDivider};

    public GridRcyDecoration(Context context, int size, int lineNumber) {
        if (lineNumber == 0) {
            throw new NullPointerException("数量不能为空");
        }
        this.lineNumber = lineNumber;
        this.mDividerHight = size;
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDividerDarwable = ta.getDrawable(0);
        ta.recycle();
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //画水平和垂直分割线
        drawHorizontalDivider(c, parent);
        drawVerticalDivider(c, parent);
    }

    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int left = child.getLeft() - params.leftMargin - mDividerHight;
            final int right = child.getRight() + params.rightMargin;
            int top = 0;
            int bottom = 0;
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDividerHight;
            //画分割线
            mDividerDarwable.setBounds(left, top, right, bottom);
            mDividerDarwable.draw(c);
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDividerHight;
            //画分割线
            mDividerDarwable.setBounds(left, top, right, bottom);
            mDividerDarwable.draw(c);

        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            int left = 0;
            int right = 0;
            if (i % lineNumber == 0) {
                left = child.getLeft();
                right = left + mDividerHight;
                mDividerDarwable.setBounds(left, top, right, bottom);
                mDividerDarwable.draw(c);


                left = child.getRight() + params.rightMargin - mDividerHight / 2;  //
                right = left + mDividerHight;
            } else if ((i + 1) % lineNumber == 0) {
                left = child.getLeft();
                right = left + mDividerHight / 2;
                mDividerDarwable.setBounds(left, top, right, bottom);
                mDividerDarwable.draw(c);

                left = child.getRight() + params.rightMargin - mDividerHight;  //
                right = left + mDividerHight;
            } else {
                left = child.getLeft();
                right = left + mDividerHight / 2;
                mDividerDarwable.setBounds(left, top, right, bottom);
                mDividerDarwable.draw(c);

                left = child.getRight() + params.rightMargin - mDividerHight / 2;  //
                right = left + mDividerHight;
            }
            mDividerDarwable.setBounds(left, top, right, bottom);
            mDividerDarwable.draw(c);
        }
    }

}
