package com.mzone.android.tools;

import java.util.HashMap;
import java.util.Map;

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
		return new String[] { "#杭州", "Abbaye de Belloc", "$杭州", "$北京", "$上海",
				"Yorkshire Blue", "Zamorano", "Zanetti Grana Padano", "mak",
				"tom", "zhangsan", "北风", "张山", "李四", "欧阳锋", "郭靖", "黄蓉", "杨过", };
	}
}
