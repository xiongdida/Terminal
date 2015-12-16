package com.XD.inverterterminal;

import java.util.ArrayList;

import com.XD.inverterterminal.adapter.ParameterAdapter;
import com.XD.inverterterminal.model.SciModel;
import com.XD.inverterterminal.utils.Parameter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SettingActivity extends Activity{

	private ParameterAdapter mAdapter;
	
	String[] names = {"转矩提升", "上限频率", "下限频率", "加速时间", "减速时间", "电子过电流保护", "适用负荷选择", "适用电机选择"};
	String[] ids = {"0", "1", "2", "7", "8", "9", "14", "71"};
	String[] factorys = {"6%", "120Hz", "0Hz", "5s", "5s", "2.6A", "0", "14", "0"};
	String[] ranges = {"0~30%", "0~120Hz", "0~120Hz", "0~3600s", "0~3600s", "0~500A", "0~3", "0，1，3，5，6，13，15，16，23，100，101，103，105，106，113，115，116，123"};
	String[] values = {"0", "60", "0", "5", "5", "1.4", "1", "0"};
	int[] rangess = {30, 120, 120, 3600, 3600, 500, 3, 17};
	
	String[] nums = {"0", "1", "3", "5", "6", "13", "15", "16", "23", "100", "101", "103", "105", "106", "113", "115", "116", "123"};
	private ArrayList<Parameter> pas = new ArrayList<Parameter>();
	
	private ListView listView;
	
	private SciModel sciModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mAdapter = new ParameterAdapter(this);
		
		setContentView(R.layout.activity_setting);
		
		sciModel = SciModel.getInstance(this);
		listView = (ListView)findViewById(R.id.parameters_list);
		
		String[] nValue = sciModel.getSavePara();
		for(int i = 0; i <= 7; i++) {
			Parameter p = new Parameter();
			p.id = ids[i];
			p.name = names[i];
			p.factorySetting = factorys[i];
			p.range = ranges[i];
			p.value = nValue[i];
			pas.add(p);
		}
		mAdapter.array = pas;
        listView.setAdapter(mAdapter);
	
        
	    listView.setOnItemClickListener(new OnItemClickListener()
	    {
	    	public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
	    	{
	    		if(sciModel.getRun() != 0)
					Toast.makeText(getApplicationContext(), "请先停止电机再设置参数", Toast.LENGTH_SHORT).show();
	    		else if(sciModel.isSciOpened()) showAlertDialog(position);
	    	}
	    });
	    registerBroadcast();
	}

	private void registerBroadcast() {
		try {
			IntentFilter filter = new IntentFilter();

			filter.addAction(SciModel.Data_ACK);
			filter.addAction(SciModel.Data_NAK);
			
			
			filter.addAction(SciModel.Conn_Error);
			registerReceiver(mBroadcastReceiver, filter);
		} catch (Exception e) {
			// mBroadcastReceiver register failed
			e.printStackTrace();
		}
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			//数据代码正确
			if (action.equals(SciModel.Data_ACK)) {
				Toast.makeText(getApplicationContext(), "操作正确", Toast.LENGTH_SHORT).show();
			}
			//数据代码错误
			else if (action.equals(SciModel.Data_NAK)) {
				String daErr = intent.getStringExtra("dataError");
				Toast.makeText(getApplicationContext(), "数据错误为：" + daErr, Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private void showAlertDialog(final int a) {

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(  
                R.layout.dlg_setting, null);  
//      TextView code = (TextView)view.findViewById(R.id.dialogCode);
		TextView content = (TextView)view.findViewById(R.id.dialogContent);
        TextView factory = (TextView)view.findViewById(R.id.dialogFactory);
//        TextView range = (TextView)view.findViewById(R.id.dialogRange);
        final NumberPicker picker = (NumberPicker)view.findViewById(R.id.numPicker);
        
//      code.setText(ids[a]);
		content.setText(names[a]);
		factory.setText(factorys[a]);
//		range.setText(ranges[a]);
		
        picker.setMaxValue(rangess[a]);
        if(a == 7) {
        	picker.setDisplayedValues(nums);
        	picker.setMinValue(0);
        }
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("参数设置");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setView(view);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int out;
						int s = picker.getValue();
						if(a == 7)
							out = Integer.parseInt(nums[picker.getValue()]);
						else out = picker.getValue();
						Toast.makeText(getApplicationContext(), out + "", Toast.LENGTH_SHORT).show();						
						sciModel.setPar(a, out);
					}
				});
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mBroadcastReceiver);
	}
	
	
}
