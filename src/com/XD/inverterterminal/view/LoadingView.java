package com.XD.inverterterminal.view;

import com.XD.inverterterminal.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class LoadingView extends FrameLayout {
	private Animation mSlideInAnimation;
	private Animation mSlideOutAnimation;
	public boolean isLoading;
	private Context mContext;
	
	public static synchronized LoadingView getInstance(Context c) {
		LoadingView instance = new LoadingView(c);
		instance.mContext = c;
		instance.load();
		return instance;
	}
	
	public LoadingView(Context context) {
		super(context);
	}
	
	private void load() {
		LayoutInflater.from(mContext).inflate(R.layout.loading_view, this, true);
		mSlideInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_in);
		mSlideOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_out);
		mSlideOutAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				LoadingView.this.setVisibility(View.GONE);
			}
		});
		
		((Activity)getContext()).addContentView(this, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setVisibility(View.GONE);
		isLoading = false;
	}
	
	public void show() {
		isLoading = true;
		this.setVisibility(View.VISIBLE);
		this.startAnimation(mSlideInAnimation);
	}
	
	public void hide() {
		if (isLoading) {
			this.startAnimation(mSlideOutAnimation);
		}
		isLoading = false;
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent ev){  
        return true;  
    } 
}
