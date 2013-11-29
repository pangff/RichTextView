package com.pangff.richtextview;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;

public class WeiBoTextViewHelper {


  /**
   * html分离正则
   */
  public static String TAGS = "[^<>]+|<(\\/?)([A-Za-z]+)([^<>]*)>";

  /**
   * html正则
   */
  public static String HTML_TAG = "<(?:.|\\s)*?>";

  /**
   * 单例
   */
  public static WeiBoTextViewHelper weiBoTextViewHelper;

  private WeiBoTextViewHelper() {}
  public static WeiBoTextViewHelper getInstance() {
    if (weiBoTextViewHelper == null) {
      weiBoTextViewHelper = new WeiBoTextViewHelper();
    }
    return weiBoTextViewHelper;
  }

  /**
   * 获取html转换后的data
   * 
   * @param html
   * @return
   */
  public List<WeiboText> paraeHTML(Context context,String html) {
    List<WeiboText> dataList = new ArrayList<WeiboText>();
    Matcher m = Pattern.compile(TAGS).matcher(html);
    while (m.find()) {
      String r = m.group();
      if (r.matches(HTML_TAG)) {
        WeiboText text = new WeiboText();
        if (r.startsWith("<stock")) {
          text.setClickable(true);
          text.setType(WeiboText.TYPE_STOCK);
          text.setKey(getAttr(r, "stock", "code"));
          text.setContent(getAttr(r, "stock", "name"));
          dataList.add(text);
        } else if (r.startsWith("<user")) {
          text.setClickable(true);
          text.setType(WeiboText.TYPE_USER);
          text.setKey(getAttr(r, "user", "uid"));
          text.setContent(getAttr(r, "user", "nick"));
          dataList.add(text);
        } else if (r.startsWith("<atuser")) {
          text.setClickable(true);
          text.setType(WeiboText.TYPE_ATUSER);
          text.setKey(getAttr(r, "atuser", "uid"));
          text.setContent(getAttr(r, "atuser", "nick"));
          dataList.add(text);
        } else if (r.startsWith("<a") && !r.startsWith("<atuser")) {
          text.setClickable(true);
          text.setType(WeiboText.TYPE_LINK);
          text.setKey(getAttr(r, "a", "href"));
          text.setContent(getAttr(r, "a", "alt"));
          dataList.add(text);
        } else if (r.startsWith("<lbs")) {
          // 位置前缀
          text.setClickable(false);
          text.setType(WeiboText.TYPE_LOCATION_PRE);
          text.setKey(getAttr(r, "lbs", "lon") + "|" + getAttr(r, "lbs", "lat"));
          text.setContent("\n我在:");
          dataList.add(text);

          // 位置图标
          WeiboText icon = new WeiboText();
          icon.setType(WeiboText.TYPE_LOCATION_ICON);
          icon.setKey(getAttr(r, "lbs", "lon") + "|" + getAttr(r, "lbs", "lat"));
          icon.setContent("");
          dataList.add(icon);

          // 位置信息
          WeiboText text_location = new WeiboText();
          text_location.setType(WeiboText.TYPE_LOCATION_TEXT);
          text_location.setKey(getAttr(r, "lbs", "lon") + "|" + getAttr(r, "lbs", "lat"));
          text_location.setContent(getAttr(r, "lbs", "location"));
          dataList.add(text_location);
        }else if(r.startsWith("<font")){
        	  	WeiboText text_font = new WeiboText();
        	  	text_font.setType(WeiboText.TYPE_STYLE_TEXT);
        	  	text_font.setContent(getAttr(r, "font", "text"));
        	  	
        	  	String style = getAttr(r, "font", "style");
        	  	if(style!=null&&style.indexOf("bold")!=-1){
        	  		text_font.setBold(true);
        	  	}
        	  	String color = getAttr(r, "font", "color");
        	  	if(color!=null&&!color.equals("")){
        	  		text_font.setColor(Color.parseColor(color));
        	  	}
        	  	String size =  getAttr(r, "font", "size");
        		if(size!=null&&!size.equals("")){
        			if(size.toLowerCase().endsWith("px")){
        				size = size.substring(0, size.length()-2);
        			}
        			if(size.toLowerCase().endsWith("dp")){
        				size = size.substring(0, size.length()-2);
        			}
        			text_font.setSize(dipToPixels(context,Integer.parseInt(size)));
        	  	}
            dataList.add(text_font);
        }

      } else {
        try {
          List<WeiboText> list =
              EmojiDrawableUtil.dealExpression(context, r);
          dataList.addAll(list);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return dataList;
  }
  
  public List<WeiboText> paraeHTMLWithOutUser(Context context,String html) {

	    List<WeiboText> dataList = new ArrayList<WeiboText>();
	    Matcher m = Pattern.compile(TAGS).matcher(html);
	    while (m.find()) {
	      String r = m.group();
	      if (r.matches(HTML_TAG)) {
	        WeiboText text = new WeiboText();
	        if (r.startsWith("<stock")) {
	          text.setClickable(true);
	          text.setType(WeiboText.TYPE_STOCK);
	          text.setKey(getAttr(r, "stock", "code"));
	          text.setContent(getAttr(r, "stock", "name"));
	          dataList.add(text);
	        } else if (r.startsWith("<user")) {
	          text.setClickable(true);
	          text.setType(WeiboText.TYPE_USER);
	          text.setKey(getAttr(r, "user", "uid"));
	          text.setContent("");
	          dataList.add(text);
	        } else if (r.startsWith("<atuser")) {
	          text.setClickable(true);
	          text.setType(WeiboText.TYPE_ATUSER);
	          text.setKey(getAttr(r, "atuser", "uid"));
	          text.setContent(getAttr(r, "atuser", "nick"));
	          dataList.add(text);
	        } else if (r.startsWith("<a") && !r.startsWith("<atuser")) {
	          text.setClickable(true);
	          text.setType(WeiboText.TYPE_LINK);
	          text.setKey(getAttr(r, "a", "href"));
	          text.setContent(getAttr(r, "a", "alt"));
	          dataList.add(text);
	        } else if (r.startsWith("<lbs")) {
	          // 位置前缀
	          text.setClickable(false);
	          text.setType(WeiboText.TYPE_LOCATION_PRE);
	          text.setKey(getAttr(r, "lbs", "lon") + "|" + getAttr(r, "lbs", "lat"));
	          text.setContent("\n我在:");
	          dataList.add(text);

	          // 位置图标
	          WeiboText icon = new WeiboText();
	          icon.setType(WeiboText.TYPE_LOCATION_ICON);
	          icon.setKey(getAttr(r, "lbs", "lon") + "|" + getAttr(r, "lbs", "lat"));
	          icon.setContent("");
	          dataList.add(icon);

	          // 位置信息
	          WeiboText text_location = new WeiboText();
	          text_location.setType(WeiboText.TYPE_LOCATION_TEXT);
	          text_location.setKey(getAttr(r, "lbs", "lon") + "|" + getAttr(r, "lbs", "lat"));
	          text_location.setContent(getAttr(r, "lbs", "location"));
	          dataList.add(text_location);
	        }
	      } else {
	        try {
	          List<WeiboText> list =
	              EmojiDrawableUtil.dealExpression(context, r);
	          dataList.addAll(list);
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	    }
	    return dataList;
  }

  /**
   * 获取标签内属性
   * 
   * @param source
   * @param element
   * @param attr
   * @return
   */
  public String getAttr(String source, String element, String attr) {
    String result = "";
    String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"].*?>";
    Matcher m = Pattern.compile(reg).matcher(source);
    while (m.find()) {
      result = m.group(1);
    }
    return result;
  }

  // dip转像素
  public static int dipToPixels(Context context,int dip) {

    final float SCALE = context.getResources().getDisplayMetrics().density;
    float valueDips = dip;
    int valuePixels = (int) (valueDips * SCALE + 0.5f);
    return valuePixels;
  }

  // 像素转dip
  public static float pixelsToDip(Context context,int pixels) {

    final float SCALE = context.getResources().getDisplayMetrics().density;
    float dips = pixels / SCALE;
    return dips;
  }
}
