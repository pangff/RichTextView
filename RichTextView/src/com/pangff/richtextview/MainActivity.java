package com.pangff.richtextview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	WeiboTextView richTextView;
	WeiboTextView richTextView2;
	String testS = "";
	String testS2 = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		richTextView = (WeiboTextView) findViewById(R.id.richTextView);
		richTextView2 = (WeiboTextView) findViewById(R.id.richTextView2);
		 /**
		   * 测试数据
		   */
		 testS =
		      EmojiDrawableUtil.getCodeByPosition(this, 1)
                  + "<a href=\"http://www.baidu.com\" />目前还在内部测试.||<atuser uid=\"dafdaf\" nick=\"残月\" />:［晚间分享:巴菲特语录］1、如果你不愿意拥有一支股票十年，那就不要考虑拥有它十分钟；||<user uid=\"dafdaf\" nick=\"TONBBY\" />:这就是你说的那儿啊。||<user uid=\"dafdaf\" nick=\"许松\" />:明天会更好。||\n<user uid=\"dafdaf\" nick=\"梅咳咳\" />:股票 <stock code=\"600016\" name=\"民生银行(600016)\" /><font size=\"24\" color=\"#a12212\" text=\"文字\"  style=\"bold\"/><lbs lon=\"53.232\" lat=\"27.3432\" location=\"北京市信安大厦\" />";
		
		 testS2 ="<lbs lon=\"53.232\" lat=\"27.3432\" location=\"北京市信安大厦\" />";

	      testS =  "fffffffffffffdddddfffffffffffffdddddfffffffff<a href=\"http://www.baidu.com\" />";

		 
		richTextView.setContent(testS);
		richTextView2.setContent(testS2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
