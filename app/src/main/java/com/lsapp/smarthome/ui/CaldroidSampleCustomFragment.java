package com.lsapp.smarthome.ui;

import com.lsapp.smarthome.ui.adapter.CaldroidSampleCustomAdapter;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		CaldroidSampleCustomAdapter	adapter = new CaldroidSampleCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData,new ArrayList<>());
		return adapter;
	}

}
