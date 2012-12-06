package com.mzone.android.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.mzone.android.tools.adapter.SectionIndexerListAdapter;
import com.mzone.android.tools.util.PingYinUtil;
import com.mzone.android.tools.util.PinyinComparator;
import com.mzone.android.tools.view.MyLetterView;
import com.mzone.android.tools.view.MyLetterView.OnTouchingLetterChangedListener;
import com.mzone.android.tools.view.SectionIndexerListView;
public abstract class CustemSectionAbstractActivity extends Activity implements
		OnTouchingLetterChangedListener {

	private TextView overlay;
	private MyLetterView myView;
	private SectionIndexerListView mIndexerListView;

	private OverlayThread overlayThread = new OverlayThread();
	private SectionIndexerListAdapter mIndexerListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mIndexerListView = (SectionIndexerListView) findViewById(R.id.list);
		myView = (MyLetterView) findViewById(R.id.myView);
		overlay = (TextView) findViewById(R.id.tvLetter);
		doSortForStringArray();

		mIndexerListAdapter = new SectionIndexerListAdapter(
				getLayoutInflater(), initPairs());
		mIndexerListView.setAdapter(mIndexerListAdapter);
		mIndexerListView.setTextFilterEnabled(true);

		myView.setOnTouchingLetterChangedListener(this);

		overlay.setVisibility(View.INVISIBLE);

	}

	private boolean needSort = true;

	/**
	 * 是否需要排序，false:以排序，无需排序， true:需要排序
	 * 
	 * @param needSort
	 */
	public void setNeedSort(boolean needSort) {
		this.needSort = needSort;
	}

	@SuppressWarnings("unchecked")
	private void doSortForStringArray() {
		mNicks = getListData();
		if (needSort)
			Arrays.sort(mNicks, new PinyinComparator());
	}

	/**
	 * 获取列表显示数据，可以使用自定义数据分类，如自定义分类 "hot",则可以自定义标志"#"，
	 * 
	 * @return {"#杭州"，"Asee"，"#上海"}
	 */
	
	public abstract String[] getListData();

	ArrayList<Pair<String, ArrayList<String>>> initPairs() {
		ArrayList<Pair<String, ArrayList<String>>> pairs = new ArrayList<Pair<String, ArrayList<String>>>();

		doCustemSection(pairs);

		for (int j = 65; j <= 90; j++) {
			char c = (char) j;
			ArrayList<String> list = new ArrayList<String>();
			Pair<String, ArrayList<String>> pair = new Pair<String, ArrayList<String>>(
					String.valueOf(c), list);

			for (int i = 0; i < mNicks.length; i++) {
				String catalog = PingYinUtil.getPingYin(mNicks[i])
						.substring(0, 1).toUpperCase();
				if (catalog.startsWith(String.valueOf(c))) {
					list.add(mNicks[i]);
				}
			}
			if (list.size() > 0)
				pairs.add(pair);
		}
		return pairs;
	}

	/**
	 * 除去A-Z之外分类必须自定义分组
	 */
	private void doCustemSection(
			ArrayList<Pair<String, ArrayList<String>>> pairs) {

		Map<String, String> maps = getCustemSection();
		if (maps != null) {
			Set<String> keySet = maps.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String str = it.next();
				ArrayList<String> list = new ArrayList<String>();
				Pair<String, ArrayList<String>> pair = new Pair<String, ArrayList<String>>(
						maps.get(str), list);
				for (int i = 0; i < mNicks.length; i++) {
					String catalog = PingYinUtil.getPingYin(mNicks[i])
							.substring(0, 1).toUpperCase();
					if (catalog.startsWith(str)) {
						list.add(mNicks[i]);
					}
				}
				if (list.size() > 0)
					pairs.add(pair);
			}
		}
	}

	/**
	 * 自定义分类类型  
	 * map.put("#", "local");
	 * @return 
	 */
	protected abstract Map<String, String> getCustemSection();

	public void onTouchingLetterChanged(String s) {
		int position = alphaIndexer(s);
		if (position >= 0) {
			mIndexerListView.setSelection(position);

			overlay.setText(s);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			handler.postDelayed(overlayThread, 1500);
		}
	}

	
	public int alphaIndexer(String s) {
		Map<String, String> maps = getCustemSection();
		if (maps != null) {
			Set<String> keySet = maps.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String str=it.next();
				if(s.startsWith(maps.get(str))){
					s=str;
					break;
				}
			}
		}
	
		int i = 0;
		for (; i < mNicks.length; i++) {
			String catalog = PingYinUtil.getPingYin(mNicks[i]).substring(0, 1)
					.toUpperCase();
			if (catalog.startsWith(s)) {
				return i;
			}
		}

		return -1;
	}

	private Handler handler = new Handler() {
	};

	private class OverlayThread implements Runnable {

		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	private String[] mNicks;

}
