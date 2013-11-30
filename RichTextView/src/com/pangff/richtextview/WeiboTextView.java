package com.pangff.richtextview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class WeiboTextView extends TextView {

  List<WeiboText> dataList = new ArrayList<WeiboText>();
  private int mDisplayWidth = 0;

  private float mDisplayHeight = 0;
  private static final String FLAG_NEW_LINE = "\n";
  private static final String FLAG_NEW_LINE_R_N = "\r\n";
  private static final String FLAG_NEW_LINE_R = "\r";


  private static final float linkExtraSpace = 10;
  private static final float linkInnerPadding = 5;
  /**
   * 可以设置的属性
   */
  private float lineSpace = 10;
  public int COLOR_USER = Color.parseColor("#01A7E5");// 用户颜色
  public int COLOR_USER_PRESS = Color.parseColor("#b10000");// 用户按下颜色
  public int COLOR_STOCK = Color.parseColor("#FDAF45");// 股票颜色
  public int COLOR_STOCK_PRESS = Color.parseColor("#0069a6");// 股票按下颜色
  public int COLOR_LOCATION_PRE = Color.DKGRAY;// 位置前缀颜色
  public int COLOR_LOCATION_TEXT = Color.DKGRAY;// 位置 信息颜色
  public int COLOR_TEXT = Color.DKGRAY;// 位置 信息颜色

  Paint paint;
  Paint paintBmp;
  private float mDensity = 1.0f;
  private float textSize ;
  Bitmap locationIcon;
  Bitmap weiboLinkbtnIcon;
  Bitmap weiboLinkbtnIconPressed;
  NinePatchDrawable link_bg;
  Bitmap firstFaceBmp;
  
  public WeiboTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public void setContent(String html) {
    dataList.clear();
    List<WeiboText> list = WeiBoTextViewHelper.getInstance().paraeHTML(getContext().getApplicationContext(),html);
    dataList.addAll(list);
    mDisplayHeight = 0;
    requestLayout();
  }
  
  public void setContentWithOutUser(String html) {
    dataList.clear();
    List<WeiboText> list = WeiBoTextViewHelper.getInstance().paraeHTMLWithOutUser(getContext().getApplicationContext(),html);
    dataList.addAll(list);
    mDisplayHeight = 0;
    requestLayout();
  }

  private void init(Context context, AttributeSet attrs) {
    mDensity = this.getContext().getResources().getDisplayMetrics().density;
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeiboTextView);
    COLOR_USER = a.getColor(R.styleable.WeiboTextView_userColor, Color.parseColor("#01A7E5"));
    COLOR_USER_PRESS = a.getColor(R.styleable.WeiboTextView_userPressedColor, Color.parseColor("#b10000"));
    COLOR_STOCK = a.getColor(R.styleable.WeiboTextView_stockColor, Color.parseColor("#FDAF45"));
    COLOR_STOCK_PRESS = a.getColor(R.styleable.WeiboTextView_stockPressedColor, Color.parseColor("#0069a6"));
    COLOR_LOCATION_PRE = a.getColor(R.styleable.WeiboTextView_locationPreColor, Color.parseColor("#939393"));
    COLOR_LOCATION_TEXT = a.getColor(R.styleable.WeiboTextView_locationTextColor, Color.parseColor("#939393"));
    COLOR_TEXT = a.getColor(R.styleable.WeiboTextView_textColor, Color.parseColor("#454545"));
    lineSpace = a.getDimension(R.styleable.WeiboTextView_lineSpace, 10f);
    textSize = this.getTextSize();
    locationIcon = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.position_pic)).getBitmap();
    link_bg = (NinePatchDrawable) this.getResources().getDrawable(R.drawable.weibo_linkbtn_bg);
    weiboLinkbtnIcon = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.weibo_linkbtn_icon)).getBitmap();
    weiboLinkbtnIconPressed = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.weibo_linkbtn_icon_pressed)).getBitmap();
    firstFaceBmp = ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.emoji_0)).getBitmap();
  }

  /**
   * 重写onMeasure方法
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    try {
      measureView(widthMeasureSpec, heightMeasureSpec);
    } catch (Exception ex) {
      // Log.e(TAG, "onMeasure", ex);
    }
    if (mDisplayHeight == 0) {
      mDisplayHeight = 0;
    }
    try {
      setMeasuredDimension(mDisplayWidth, (int) mDisplayHeight);
    } catch (Exception ex) {
      // Log.e(TAG, "onMeasure", ex);
      ex.printStackTrace();
    }

  }

  /**
   * 重写onDraw方法
   */
  @Override
  protected void onDraw(Canvas canvas) {
    try {
      if (dataList == null || dataList.size() == 0) {
        return;
      }
      if (paint == null) {
        paint = new Paint();
      }
      // clear background
      paint.setColor(0x00000000);
      Rect rc = new Rect();
      rc.set(0, 0, getWidth(), getHeight());
      canvas.drawRect(rc, paint);

      // draw content
      paint.setTextSize(getTextSize());
      paint.setAntiAlias(true);
      paint.setStyle(Style.FILL);
      drawList(canvas, paint, dataList, 0);
    } catch (Exception ex) {
      // KXLog.e(TAG, "onDraw", ex);
    }
  }

  /**
   * 画数据rect
   * 
   * @param canvas
   * @param paint
   * @param infoList
   * @param startY
   * @return
   */
  private float drawList(Canvas canvas, Paint paint, List<WeiboText> infoList, float startY) {

    if (infoList == null || infoList.size() == 0) {
      return startY;
    }

    float y = startY;

    float ascent = paint.ascent();
    float fontHeight = paint.descent() - ascent;
    WeiboInfoRect rect = null;
    // paint for DrawBitmap
    if (paintBmp == null) {
      paintBmp = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
    }

    for (WeiboText info : infoList) {
      int rectIndex = 0;
      List<WeiboInfoRect> rectList = info.getRectList();
      if (null == rectList || rectList.size() == 0) {
        continue;
      }
      String strContent = info.getContent();
      if (null == strContent) {
        strContent = "";
      }
      paint.setColor(COLOR_TEXT);

      /**
       * 如果是表情
       */
      if (info.getType() == WeiboText.TYPE_ICON) {
        Bitmap faceBmp = info.getFaceBmp();
        rect = rectList.get(rectIndex);
        if (faceBmp != null && null != rect) {
          float padding_v = (rect.bottom - rect.top - fontHeight) / 2;
          float resultWidth = (rect.right - rect.left) / (rect.bottom - rect.top) * fontHeight;
          float padding_h = (rect.right - rect.left - resultWidth) / 2;

          Rect dst = new Rect();
          dst.set((int) (rect.left + padding_h), (int) (rect.top), (int) (rect.right - padding_h),
              (int) (rect.bottom - padding_v));
          canvas.drawBitmap(faceBmp, null, dst, paintBmp);
        }
      }

      /**
       * 如果是位置
       */
      if (info.getType() == WeiboText.TYPE_LOCATION_ICON) {
        rect = rectList.get(rectIndex);
        if (locationIcon != null && null != rect) {
          Rect dst = new Rect();
          float padding_v = (rect.bottom - rect.top - fontHeight) / 2;
          float resultWidth = (rect.right - rect.left) / (rect.bottom - rect.top) * fontHeight;
          float padding_h = (rect.right - rect.left - resultWidth) / 2;

          dst.set((int) (rect.left + padding_h), (int) (rect.top + padding_v),
              (int) (rect.right - padding_h), (int) (rect.bottom - padding_v));
          canvas.drawBitmap(locationIcon, null, dst, paintBmp);
        }
      }

      /**
       * 如果是位置 前缀 设置属性
       */
      if (info.getType() == WeiboText.TYPE_LOCATION_PRE) {
        paint.setColor(COLOR_LOCATION_PRE);
        paint.setTextSize(textSize * 0.9f);
      }
      /**
       * 如果是位置 信息 设置属性
       */
      if (info.getType() == WeiboText.TYPE_LOCATION_TEXT) {
        paint.setColor(COLOR_LOCATION_TEXT);
        paint.setTextSize(textSize * 0.9f);
      }

      /**
       * 如果是链接
       */
      if (info.getType() == WeiboText.TYPE_LINK) {
        Bitmap icon = null;
        if (!info.isPressed()) {
          paint.setColor(COLOR_USER);
          icon = weiboLinkbtnIcon;
        } else {
          paint.setColor(COLOR_USER_PRESS);
          icon = weiboLinkbtnIconPressed;
        }
        rect = rectList.get(rectIndex);
        float padding_v = 0;
        float resultWidth = 0;
        float padding_h = 0;
        if (rect != null) {
          padding_v = (rect.bottom - rect.top - fontHeight) / 2;
          resultWidth = (rect.right - rect.left) / (rect.bottom - rect.top) * fontHeight;
          padding_h = (rect.right - rect.left - resultWidth) / 2;
        }
        if (link_bg != null) {
        		link_bg.setBounds((int) (rect.left), (int) (rect.top + padding_v / 4), (int) (rect.right),
              (int) (rect.bottom - padding_v / 4));
        		link_bg.draw(canvas);
        }

        float icon_height = fontHeight * 0.8f;
        float padding_v_iocn = (rect.bottom - rect.top - icon_height) / 2;
        float icon_width = 0;
        if (icon != null && null != rect) {
          icon_width = icon_height * icon.getWidth() / icon.getHeight();
          Rect dst = new Rect();
          dst.set((int) (rect.left + linkInnerPadding), (int) (rect.top + padding_v_iocn),
              (int) (rect.left + linkInnerPadding + icon_width),
              (int) (rect.bottom - padding_v_iocn));
          canvas.drawBitmap(icon, null, dst, paintBmp);
        }
        paint.setTextSize(textSize * 0.8f);
        canvas.drawText("网页链接", 0, 4, rect.left + linkInnerPadding + icon_width + padding_h / 2,
            rect.top - ascent - ascent / 8, paint);
      }
      /**
       * 如果是用户
       */
      if (info.getType() == WeiboText.TYPE_USER) {
        if (!info.isPressed()) {
          paint.setColor(COLOR_USER);
        } else {
          paint.setColor(COLOR_USER_PRESS);
        }
      }

      /**
       * 如果是AT用户
       */
      if (info.getType() == WeiboText.TYPE_ATUSER) {
        if (!info.isPressed()) {
          paint.setColor(COLOR_USER);
        } else {
          paint.setColor(COLOR_USER_PRESS);
        }
      }

      /**
       * 如果是股票
       */
      if (info.getType() == WeiboText.TYPE_STOCK) {
        if (!info.isPressed()) {
          paint.setColor(COLOR_STOCK);
        } else {
          paint.setColor(COLOR_STOCK_PRESS);
        }
      }
      if(info.getType() == WeiboText.TYPE_STYLE_TEXT){
    	  	  if(info.getColor()!=-1){
    	  		 paint.setColor(info.getColor());
    	  	  }
    	  	  if(info.getSize()!=-1){
    	  		paint.setTextSize(info.getSize());
    	  	  }
    	  	  if(info.isBold()){
    	  		paint.setFakeBoldText(true);
    	  	  }
    	  	 ascent = paint.ascent();
    	     fontHeight = paint.descent() - ascent;
      }
      int nCount = rectList.size();
      while (rectIndex < nCount) {
        rect = rectList.get(rectIndex);
        rectIndex++;
        if (rect.start < 0 || rect.end < 0) {
          continue;
        }
        canvas.drawText(strContent, rect.start, rect.end, rect.left,rect.centerY()+fontHeight/4, paint);
      }
      paint.setTextSize(textSize);
      paint.setFakeBoldText(false);
    }

    return y + fontHeight;
  }

  /**
   * 计算view高度
   * 
   * @param widthMeasureSpec
   * @param heightMeasureSpec
   */
  private void measureView(int widthMeasureSpec, int heightMeasureSpec) {
    if (dataList.size() == 0) {
      mDisplayHeight = getPaddingTop() + getPaddingBottom();
      mDisplayWidth = getPaddingLeft() + getPaddingRight();
      return;
    }
    int specMode = MeasureSpec.getMode(widthMeasureSpec);
    int specWidth = MeasureSpec.getSize(widthMeasureSpec);

    // view has been measured and the size did not change.
    if (specMode == MeasureSpec.EXACTLY && specWidth == mDisplayWidth && 0 != mDisplayHeight) {
      return;
    }

    mDisplayWidth = specWidth;

    float x = 0;
    float y = getPaddingTop();
    Paint paint = getPaint();
    // font height
    final float fontHeight = -paint.ascent() + paint.descent();// fontMetrics.bottom

    // fontMetrics.top;
    // line height
    float lineHeight = fontHeight + lineSpace;

    WeiboInfoRect rect = null;
    // the max width of current line
    final int maxRight = specWidth - getPaddingRight();
    int nLineRightLimit = maxRight;
    final int paddingLeft = getPaddingLeft();

    for (int m=0;m<dataList.size();m++) {
      WeiboText info =  dataList.get(m);
      if(m==0){
    	  	x=paddingLeft;
      }
      nLineRightLimit = maxRight;
      info.clearRect();

      if (info.getType() == WeiboText.TYPE_ICON) {// 表情
        if (firstFaceBmp != null) {
          float bmpWidth = firstFaceBmp.getWidth();
          float bmpHeight = firstFaceBmp.getHeight();
          if (mDensity > 1) {
            bmpWidth = bmpWidth / mDensity;
            bmpHeight = bmpHeight / mDensity;
          }
          // face icon has left and right margins.
          if (x + bmpWidth >= nLineRightLimit) {
            y += lineHeight;
            // set fontHeight to lineHeight as default.
            lineHeight = fontHeight + lineSpace;
            x = paddingLeft;
          }
          rect = new WeiboInfoRect();
          bmpWidth = lineHeight / bmpHeight * bmpWidth;
          rect.set(x, y, x + bmpWidth, y + lineHeight);
          info.addRect(rect);
          x += bmpWidth;
        }
        continue;
      }

      if (info.getType() == WeiboText.TYPE_LOCATION_ICON) {// 位置图标
        if (locationIcon != null) {
          float bmpWidth = locationIcon.getWidth();
          float bmpHeight = locationIcon.getHeight();
          if (mDensity > 1) {
            bmpWidth = bmpWidth / mDensity;
            bmpHeight = bmpHeight / mDensity;
          }
          // face icon has left and right margins.
          if (x + bmpWidth >= nLineRightLimit) {
            y += lineHeight;
            // set fontHeight to lineHeight as default.
            lineHeight = fontHeight + lineSpace;
            x = paddingLeft;
          }
          rect = new WeiboInfoRect();
          bmpWidth = lineHeight / bmpHeight * bmpWidth;
          rect.set(x, y, x + bmpWidth, y + lineHeight);
          info.addRect(rect);
          x += bmpWidth;
        }
        continue;
      }


      if (info.getType() == WeiboText.TYPE_LINK) {// 链接
    	  	
        if (weiboLinkbtnIcon != null) {
          float bmpWidth = weiboLinkbtnIcon.getWidth();
          float bmpHeight = weiboLinkbtnIcon.getHeight();
          if (mDensity > 1) {
            bmpWidth = bmpWidth / mDensity;
            bmpHeight = bmpHeight / mDensity;
          }
          paint.setTextSize(textSize * 0.8f);
          float textWidth = paint.measureText("网页链接");
          paint.setTextSize(textSize);
          // face icon has left and right margins.
          if (x + bmpWidth + textWidth + linkExtraSpace >= nLineRightLimit) {
            y += lineHeight;
            lineHeight = fontHeight + lineSpace;
            x = paddingLeft;
          }
          rect = new WeiboInfoRect();

          bmpWidth = lineHeight / bmpHeight * bmpWidth;
          rect.set(x + linkExtraSpace / 2, y, x + bmpWidth + textWidth + linkInnerPadding * 2
              + linkExtraSpace / 2, y + lineHeight);
          info.addRect(rect);
          x += bmpWidth + textWidth + linkExtraSpace + linkInnerPadding * 2;
        }
        continue;
      }
      if(info.getType() == WeiboText.TYPE_STYLE_TEXT){
    	  	if(info.getSize()!=-1){
    	  		paint.setTextSize(info.getSize());
    	  	}
      }
      String strContent = info.getContent();
      if (null == strContent) {
        strContent = "";
      }
      if (strContent.startsWith("\n")) {
        y += lineHeight;
        lineHeight = fontHeight + lineSpace;
        x = paddingLeft;
      }
      // Log.i("linkinfo", "strContent 1:" + strContent);
      strContent = strContent.replaceAll(FLAG_NEW_LINE_R_N, FLAG_NEW_LINE);
      strContent = strContent.replaceAll(FLAG_NEW_LINE_R, FLAG_NEW_LINE);
      // Log.i("linkinfo", "strContent 2:" + strContent);
      info.setContent(strContent);
      String strLines[] = strContent.split(FLAG_NEW_LINE);
      // Log.i("linkinfo", "strContent strLines:" + strLines.length);
      int strStart = 0;
      for (int line = 0; line < strLines.length; line++) {// 文字
        String strLine = strLines[line];
        if (!TextUtils.isEmpty(strLine)) {
          float xLen = paint.measureText(strLine);
          // if the whole text is beyond the width, split the text
          if (x + xLen >= nLineRightLimit) {
            float startX = x;
            int strLen = strLine.length();
            int lineHeadIndex = strStart;
            for (int i = 0; i < strLen; i++) {
              float width = paint.measureText(strLine, i, i + 1);
              if (x + width >= nLineRightLimit) {
                rect = new WeiboInfoRect();
                rect.set(startX, y, x, y + lineHeight);
                rect.start = lineHeadIndex;
                rect.end = strStart + i;
                info.addRect(rect);
                lineHeadIndex = rect.end;
                y += lineHeight;
                lineHeight = fontHeight + lineSpace;
                x = paddingLeft + width;
                startX = paddingLeft;
              } else {
                x += width;
              }
            }
            rect = new WeiboInfoRect();
            rect.set(startX, y, x, y + lineHeight);
            rect.start = lineHeadIndex;
            rect.end = strStart + strLen;
            info.addRect(rect);
          } else {
            rect = new WeiboInfoRect();
            rect.set(x, y, x + xLen, y + lineHeight);
            rect.start = strStart;
            rect.end = strStart + strLine.length();
            info.addRect(rect);
            x += xLen;
          }
          // jump to next line
          if (line < strLines.length - 1) {
            // if reach the maxLines, break
            x = paddingLeft;
            y += lineHeight;
            lineHeight = fontHeight + lineSpace;
          }
        }
        strStart += strLine.length() + FLAG_NEW_LINE.length();
      }
      paint.setTextSize(textSize);
    }
    float specHeight = y + getPaddingBottom();
    if (x > paddingLeft) {
      specHeight += lineHeight;
    }
    mDisplayHeight = specHeight + 2;
  }

  long lastEventTime = 0;

  public void resetTouchSate() {
    this.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (lastEventTime != 0 && java.lang.System.currentTimeMillis() - lastEventTime >= 1000) {
          if (currentTouchedInfo != null) {
            currentTouchedInfo.setPressed(false);
            invalidate();
          }
        } else {
          resetTouchSate();
        }
      }
    }, 1000);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    super.onTouchEvent(event);

    if (!this.isEnabled()) {
      return false;
    }
    if (event.getAction() == MotionEvent.ACTION_UP) {
      float x = event.getX();
      float y = event.getY();
      if (click(dataList, x, y, MotionEvent.ACTION_UP)) {
        return true;
      }
    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
      float x = event.getX();
      float y = event.getY();
      if (click(dataList, x, y, MotionEvent.ACTION_DOWN)) {
        return true;
      }
    } else {
      lastEventTime = java.lang.System.currentTimeMillis();
    }
    return false;
  }

  WeiboText currentTouchedInfo = null;

  private boolean click(List<WeiboText> infoList, float x, float y, int action) {
    if (infoList == null) {
      return false;
    }
    for (int i = 0; i < infoList.size(); i++) {
      WeiboText info = infoList.get(i);
      if (!info.isClickable()) {
        continue;
      }
      if (info.contains(x, y)) {
        if (action == MotionEvent.ACTION_DOWN) {
          resetTouchSate();
          currentTouchedInfo = info;
          info.setPressed(true);
          invalidate();
        } else if (action == MotionEvent.ACTION_UP) {
          currentTouchedInfo = null;
          info.setPressed(false);
          if (info.getType() == WeiboText.TYPE_USER || info.getType() == WeiboText.TYPE_ATUSER) {
        	  	Toast.makeText(getContext(), "显示用户", Toast.LENGTH_SHORT).show();
          } else if (info.getType() == WeiboText.TYPE_STOCK) {
        	  	Toast.makeText(getContext(), "显示股票", Toast.LENGTH_SHORT).show();
          } else if (info.getType() == WeiboText.TYPE_LINK) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(info.getKey());
            intent.setData(content_url);
            ((Activity) getContext()).startActivity(intent);
          }
          invalidate();
        }
        return true;
      }
    }
    return false;
  }

}
