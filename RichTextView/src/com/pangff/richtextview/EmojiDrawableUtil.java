// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

// Decompiler options: fullnames definits braces deadcode fieldsfirst

package com.pangff.richtextview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public final class EmojiDrawableUtil {

  private static EmojiDrawableUtil emojiDrawableUtil = null;

  private String code[] = null;

  private int positions[] = null;

  /**
   * 构造方法
   * 
   * @param context
   */
  private EmojiDrawableUtil(Context context) {
    code = context.getResources().getStringArray(R.array.emoji_code);
    String as[] = context.getResources().getStringArray(R.array.emoji_file);
    positions = new int[as.length];
    int i = 0;
    do {
      if (i >= as.length) {
        return;
      }
      positions[i] = Integer.parseInt(as[i]);
      i++;
    } while (true);
  }

  // 内存缓存图片
  private static Map<Integer, Drawable> activityViewCache = new LinkedHashMap<Integer, Drawable>(
      50, 0.75f, true) {

    private static final long serialVersionUID = 1L;

    @Override
    protected boolean removeEldestEntry(Entry<Integer, Drawable> eldest) {
      return true;
    }
  };

  /**
   * 根据位置获取图片
   * 
   * @param context
   * @param pos
   * @return
   */
  public static Drawable getEmojiDrawable(Context context, int pos) {
    if (activityViewCache.containsKey(pos)) {
      return activityViewCache.get(pos);
    }
    int j =
        context.getResources().getIdentifier("emoji_" + pos, "drawable", context.getPackageName());
    if (j == 0) {
      return null;
    }

    BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(j);
    activityViewCache.put(pos, drawable);
    return drawable;
  }

  public static int getEmojiId(Context context, int pos) {
    int j =
        context.getResources().getIdentifier("emoji_" + pos, "drawable", context.getPackageName());
    return j;
  }

  /**
   * 获取位置数组
   * 
   * @param context
   * @return
   */
  public static int[] getPositions(Context context) {
    return EmojiDrawableUtil.getInstance(context).positions;
  }

  /**
   * 根据code获取图片
   * 
   * @param context
   * @param code
   * @return
   */
  public static Drawable getEmojiDrawableByCode(Context context, String code) {
    return getEmojiDrawable(context, getPositionByCode(context, code));
  }

  /**
   * 根据位置获取code
   * 
   * @param context
   * @param i
   * @return
   */
  public static String getCodeByPosition(Context context, int i) {
    String as[] = context.getResources().getStringArray(R.array.emoji_code);
    String s;
    if (i < as.length) {
      s = as[i];
    } else {
      s = "";
    }
    return s;
  }

  /**
   * 根据code获取位置
   * 
   * @param context
   * @param code
   * @return
   */
  public static int getPositionByCode(Context context, String code) {
    int index = -1;
    for (int i = 0; i < getCodeArray(context).length; i++) {
      if (code.equals(getCodeArray(context)[i])) {
        index = i;
        break;
      }
    }
    return index;
  }

  /**
   * 获取code数组
   * 
   * @param context
   * @return
   */
  public static String[] getCodeArray(Context context) {
    return EmojiDrawableUtil.getInstance(context).code;
  }

  /**
   * 单例
   * 
   * @param context
   * @return
   */
  private static EmojiDrawableUtil getInstance(Context context) {
    if (emojiDrawableUtil == null) {
      emojiDrawableUtil = new EmojiDrawableUtil(context);
    }
    return emojiDrawableUtil;
  }

  /**
   * 解析字符串，将表情喝内容分离
   * 
   * @param context
   * @param str
   * @return
   * @throws Exception
   */
  public static List<WeiboText> dealExpression(Context context, String str) throws Exception {
    List<WeiboText> weiboTextList = new ArrayList<WeiboText>();
    String currentS = "";
    int _index = 0;
    for (char c : str.toCharArray()) {
      _index++;
      int index = getPositionByCode(context, c + "");
      if (index != -1) {

        if (!currentS.equals("")) {
          WeiboText weiboText2 = new WeiboText();
          weiboText2.setClickable(false);
          weiboText2.setType(WeiboText.TYPE_TEXT);
          weiboText2.setKey("");
          weiboText2.setContent(currentS);
          weiboTextList.add(weiboText2);
        }

        WeiboText weiboText = new WeiboText();
        weiboText.setClickable(false);
        weiboText.setType(WeiboText.TYPE_ICON);
        weiboText.setKey(index + "");
        weiboText.setContent(c + "");
        weiboTextList.add(weiboText);

        currentS = "";
      } else {
        currentS += c;
      }
      if (!currentS.equals("") && _index == str.length()) {
        WeiboText weiboText2 = new WeiboText();
        weiboText2.setClickable(false);
        weiboText2.setType(WeiboText.TYPE_TEXT);
        weiboText2.setKey("");
        weiboText2.setContent(currentS);
        weiboTextList.add(weiboText2);
      }
    }
    return weiboTextList;
  }

}
