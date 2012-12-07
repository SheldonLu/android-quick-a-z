package com.mzone.android.tools;

import java.util.HashMap;
import java.util.Map;

import com.mzone.android.tools.adapter.SectionIndexerListAdapter;

import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class DemoActivity extends CustemSectionAbstractActivity {
	
	@Override
	protected Map<String, String> getCustemSection() {
		Map<String,String> map=new HashMap<String, String>();
		map.put("#", "local");
		map.put("$", "hot");
		return map;
	}

	@Override
	public String[] getListData() {
		return new String[] { "#杭州", "Abbaye de Belloc:1", "$杭州", "$北京", "$上海",
				"Yorkshire Blue", "Zamorano", "Zanetti Grana Padano", "mak",
				"tom", "zhangsan", "北风:8", "张山", "李四", "欧阳锋", "郭靖", "黄蓉", "杨过", };
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		if( view.getTag(SectionIndexerListAdapter.SECTION_EVERY_DATA_POSITION)!=null && view.getTag(SectionIndexerListAdapter.SECTION_EVERY_DATA_POSITION).toString().length()>0)
			Toast.makeText(this, view.getTag(SectionIndexerListAdapter.SECTION_EVERY_DATA_POSITION).toString(), Toast.LENGTH_SHORT).show();
	}
}
