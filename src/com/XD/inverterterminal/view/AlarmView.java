package com.XD.inverterterminal.view;


import com.XD.inverterterminal.R;
import com.XD.inverterterminal.utils.RecvUtils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;


public class AlarmView extends RelativeLayout{

	private Context mContext;
	private Animation mSlideInAnimation;
	private Animation mSlideOutAnimation;
	
	private Button mAlarmHide;
	private ImageButton mButtonNext;
	private ImageButton mButtonPrev;
	
	private TextView mAlarmTitle;
	private TextView mAlarmName;
	private TextView mAlarmContent;
	private TextView mAlarmCheckPoint;
	private TextView mAlarmHandle;

	private View mAlarmBg;
	
	private boolean isLoading;
	
	private int alarmNum;
	
	public static synchronized AlarmView getInstance(Context c) {
		AlarmView instance = new AlarmView(c);
		instance.mContext = c;
		instance.load();
		return instance;
	}

	public AlarmView(Context context) {
		super(context);
	}
	
	private void load() {
		// TODO Auto-generated method stub
		LayoutInflater.from(mContext).inflate(R.layout.alarm_view, this, true);
		mSlideInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_top);
		mSlideOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_top);
		mSlideOutAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				AlarmView.this.setVisibility(View.GONE);
			}
		});
		
		mAlarmTitle = (TextView)findViewById(R.id.alarmTitle);
		mAlarmName = (TextView)findViewById(R.id.alarmName);
		mAlarmContent = (TextView)findViewById(R.id.alarmContent);
		mAlarmCheckPoint = (TextView)findViewById(R.id.alarmCheckPoint);
		mAlarmHandle = (TextView)findViewById(R.id.alarmHandle);
		mAlarmHide = (Button)findViewById(R.id.alarm_view_hide);
		mAlarmHide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				hide();
			}
		});
		
		mButtonNext = (ImageButton)findViewById(R.id.alarm_next_btn);
		mButtonNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showNext();
			}
		});
		
		mButtonPrev = (ImageButton)findViewById(R.id.alarm_prev_btn);
		mButtonPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showPrev();
			}
		});
		
		mAlarmBg = (View)findViewById(R.id.alarm_view_bg2);
		mAlarmBg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hide();
			}
		});
		
		((Activity)getContext()).addContentView(this, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setVisibility(View.INVISIBLE);
		isLoading = false;
	}
	
	private void showPrev() {
		// TODO Auto-generated method stub
		if(alarmNum == 1)
			return;
		alarmNum--;
		show(alarmNum);
	}

	private void showNext() {
		// TODO Auto-generated method stub
		if(alarmNum == 22)
			return;
		alarmNum++;
		show(alarmNum);
	}

	public void show() {
		this.setVisibility(View.VISIBLE);
//		if(!isLoading)
//			this.startAnimation(mSlideInAnimation);
//		isLoading = true;
	}
	
	public void hide() {
		isLoading = false;
		if (this.getVisibility() == View.GONE) {
			return;
		}
		this.startAnimation(mSlideOutAnimation);		
	}
	
	public void show(int a) {
		alarmNum = a;
		mAlarmTitle.setText(RecvUtils.alarms_title[a]);
		mAlarmName.setText(RecvUtils.alarms_name[a]);
		mAlarmContent.setText(RecvUtils.alarms_content[a]);
		mAlarmCheckPoint.setText(RecvUtils.alarms_checkPoint[a]);
		mAlarmHandle.setText(RecvUtils.alarms_handle[a]);
		show();
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent ev){  
        return true;  
    } 
}
