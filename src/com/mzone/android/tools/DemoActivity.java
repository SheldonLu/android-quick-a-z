package com.mzone.android.tools;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.mzone.android.tools.adapter.SectionIndexerListAdapter;
import com.mzone.android.tools.util.PingYinUtil;
import com.mzone.android.tools.util.PinyinComparator;
import com.mzone.android.tools.view.MyLetterView;
import com.mzone.android.tools.view.MyLetterView.OnTouchingLetterChangedListener;
import com.mzone.android.tools.view.SectionIndexerListView;
/**
 * use #,$ to replace local,hot 
 * @author Administrator
 *
 */
@SuppressLint({ "DefaultLocale", "HandlerLeak" })
public class DemoActivity extends Activity implements
		OnTouchingLetterChangedListener {

	private TextView overlay;
	private MyLetterView myView;
	private OverlayThread overlayThread = new OverlayThread();

	private SectionIndexerListView mIndexerListView;

	private SectionIndexerListAdapter mIndexerListAdapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Arrays.sort(mNicks, new PinyinComparator());

		mIndexerListView = (SectionIndexerListView) findViewById(R.id.list);
		mIndexerListAdapter = new SectionIndexerListAdapter(
				getLayoutInflater(), initPairs());
		mIndexerListView.setAdapter(mIndexerListAdapter);
		mIndexerListView.setTextFilterEnabled(true);
		myView = (MyLetterView) findViewById(R.id.myView);

		myView.setOnTouchingLetterChangedListener(this);
		overlay = (TextView) findViewById(R.id.tvLetter);
		overlay.setVisibility(View.INVISIBLE);

	}

	ArrayList<Pair<String, ArrayList<String>>> initPairs() {
		ArrayList<Pair<String, ArrayList<String>>> pairs = new ArrayList<Pair<String, ArrayList<String>>>();
		addTitle(pairs);
		for (int j = 65; j <= 90; j++) {
			char c = (char) j;
			ArrayList<String> list = new ArrayList<String>();
			Pair<String, ArrayList<String>> pair = new Pair<String, ArrayList<String>>(
					String.valueOf(c), list);

			for (int i = 0; i < mNicks.length; i++) {
				String catalog = PingYinUtil.getPingYin(mNicks[i]).substring(0,
						1).toUpperCase();
				if (catalog.startsWith(String.valueOf(c))) {
					list.add(mNicks[i]);
				}
			}
			if (list.size() > 0)
				pairs.add(pair);
		}
		return pairs;
	}

	void addTitle(ArrayList<Pair<String, ArrayList<String>>> pairs) {
		ArrayList<String> title1 = new ArrayList<String>();
	
		for (int i = 0; i < mNicks.length; i++) {
			String catalog = PingYinUtil.getPingYin(mNicks[i]).substring(0,
					1).toUpperCase();
			if (catalog.startsWith("#")) {
				title1.add(mNicks[i]);
			}
		}
		addTitleList(pairs, "local", title1);

		ArrayList<String> title2 = new ArrayList<String>();
		for (int i = 0; i < mNicks.length; i++) {
			String catalog = PingYinUtil.getPingYin(mNicks[i]).substring(0,
					1).toUpperCase();
			if (catalog.startsWith("$")) {
				title2.add(mNicks[i]);
			}
		}
		addTitleList(pairs, "hot", title2);
	}

	void addTitleList(ArrayList<Pair<String, ArrayList<String>>> pairs,
			String title, ArrayList<String> list) {
		Pair<String, ArrayList<String>> pair = new Pair<String, ArrayList<String>>(
				title, list);
		pairs.add(pair);
	}

	public void onTouchingLetterChanged(String s) {
		// TODO Auto-generated method stub
		int position = alphaIndexer(s);
		if (position >= 0) {
			mIndexerListView.setSelection(position);

			overlay.setText(s);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			handler.postDelayed(overlayThread, 1500);
		}
	}

	@SuppressLint("DefaultLocale")
	public static int alphaIndexer(String s) {
		if (s.startsWith("local")) {
			s = "#";
		} else if (s.startsWith("hot")) {
			s = "$";
		}
		int i = 0;
		for (; i < mNicks.length; i++) {
			String catalog = PingYinUtil.getPingYin(mNicks[i]).substring(0, 1)
					.toUpperCase();
			if (catalog.startsWith(s)) {
				Log.w("position", i+"=="+s);
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

	public static final String[] mNicks = {"#杭州", "Abbaye de Belloc","$杭州","$北京","$上海",
			"Abbaye du Mont des Cats", 
			"Airag", "Airedale", "Aisy Cendre", "Brie de Meaux",
			"Brie de Melun", "Brillat-Savarin", "Brin", "Brin d' Amour",
			"Brin d'Amour", "Brinza (Burduf Brinza)", "Briquette de Brebis",
			"Briquette du Forez", "Broccio", "Yarra Valley Pyramid",
			"Yorkshire Blue", "Zamorano", "Zanetti Grana Padano",
			"Zanetti Parmigiano Reggiano", "门旭", "刘帅", "刘盼", "付久红", "徐贤鱼",
			"menxu", "liushuai", "liupan", "fujiuhong", "xuxianyu", "阿雅",
			"mak", "tom", "zhangsan", "北风", "张山", "李四", "欧阳锋", "郭靖", "黄蓉",
			"杨过", "凤姐", "芙蓉姐姐", "移联网", "樱木花道", "风清扬", "张三丰", "梅超风" };


}
