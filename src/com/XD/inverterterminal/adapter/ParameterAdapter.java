package com.XD.inverterterminal.adapter;

import java.util.ArrayList;

import com.XD.inverterterminal.R;
import com.XD.inverterterminal.utils.Parameter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ParameterAdapter extends BaseAdapter{
	public ArrayList<Parameter> array;
	private LayoutInflater mInflater;

	public ParameterAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(mContext);//²¼¾ÖÌî³äÆ÷
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return array.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		try 
		{			
			if (convertView == null)
				convertView = mInflater.inflate(R.layout.item_parameter, null);			
			Parameter mPara = array.get(position);
			TextView paraId = (TextView)convertView.findViewById(R.id.parameter_id);
			TextView paraName = (TextView)convertView.findViewById(R.id.parameter_name);
			TextView paraFac = (TextView)convertView.findViewById(R.id.parameter_factory_setting);
			TextView paraRange = (TextView)convertView.findViewById(R.id.parameter_range);
			TextView paraValue = (TextView)convertView.findViewById(R.id.parameter_value);

			paraId.setText(mPara.id);
			paraName.setText(mPara.name);
			paraFac.setText(mPara.factorySetting);
			paraRange.setText(mPara.range);
			paraValue.setText(mPara.value);
			
			return convertView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
