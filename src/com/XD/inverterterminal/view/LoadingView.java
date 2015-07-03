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
import android.widget.TextView;

public class LoadingView extends FrameLayout {
	private Animation mSlideInAnimation;
	private Animation mSlideOutAnimation;
	public boolean isLoading;
	
	public LoadingView(Context context) {
		super(context);
		load();
	}
	
	private void load() {
		Context context = getContext();
		LayoutInflater.from(context).inflate(R.layout.loading_view, this, true);
		mSlideInAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_in);
		mSlideOutAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_out);
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
		isLoading = false;
		if (this.getVisibility() == View.VISIBLE) {
			this.startAnimation(mSlideOutAnimation);
		}
//		this.startAnimation(mSlideOutAnimation);
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent ev){  
        return true;  
    } 
}
