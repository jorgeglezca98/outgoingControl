package com.example.jorgegonzalezcabrera.outgoing.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jorgegonzalezcabrera.outgoing.R;

public class editTextWithButton extends AppCompatEditText implements View.OnTouchListener, TextWatcher {

    private Drawable icon;
    private OnClearListener onClearListener;
    private boolean iconVisibility;

    public interface OnClearListener {
        void onClear();
    }

    public editTextWithButton(final Context context) {
        super(context);
        init(context);
    }

    public editTextWithButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public editTextWithButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnClearListener(OnClearListener onClearListener) {
        this.onClearListener = onClearListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int allowedHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        icon.setBounds(0, 0, allowedHeight, allowedHeight);
    }

    private void init(final Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.clear);
        if (drawable != null) {
            icon = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(icon, getCurrentHintTextColor());
            setClearIconVisible(false);
        }
        setOnTouchListener(this);
        addTextChangedListener(this);
        onClearListener = null;
        iconVisibility = false;
    }

    private void setClearIconVisible(final boolean visible) {
        if (icon != null) {
            setCompoundDrawables(
                    getCompoundDrawables()[0],
                    getCompoundDrawables()[1],
                    visible ? icon : null,
                    getCompoundDrawables()[3]
            );
        }
        iconVisibility = visible;
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (iconVisibility && x > getWidth() - getPaddingRight() - icon.getBounds().width()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setText("");
                if (onClearListener != null) {
                    onClearListener.onClear();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        setClearIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
