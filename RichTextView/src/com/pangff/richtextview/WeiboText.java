package com.pangff.richtextview;

import java.util.ArrayList;
import java.util.List;

import android.graphics.RectF;


public class WeiboText{
	
	private int type;
	public static final int TYPE_TEXT = 0;
	public static final int TYPE_USER = 1;
	public static final int TYPE_ATUSER = 2;
	public static final int TYPE_STOCK = 3;
	public static final int TYPE_LINK = 4;
	public static final int TYPE_ICON = 5;
	public static final int TYPE_LOCATION_PRE = 6;
	public static final int TYPE_LOCATION_ICON = 7;
	public static final int TYPE_LOCATION_TEXT = 8;
	public static final int TYPE_STYLE_TEXT = 9;
	
	private String content;
	private String key;
	private int color;
	private boolean isBold;
	private int size=-1;
	
	private boolean clickable;
	private boolean isPressed = false;
	
	private List<WeiboInfoRect> rectList = new ArrayList<WeiboInfoRect>();

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<WeiboInfoRect> getRectList() {
		return rectList;
	}

	public void addRect(WeiboInfoRect rect){
		rectList.add(rect);
	}
	
	public void clearRect(){
		rectList.clear();
	}
	
	public boolean contains(float x, float y) {
        boolean ret = false;
        int len = rectList.size();
        for (int i = 0; i < len; i++) {
            RectF rectF = rectList.get(i);
            if (rectF.contains(x, y)) {
                return true;
            }
        }
        return ret;
    }

	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	public boolean isPressed() {
		return isPressed;
	}

	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isBold() {
		return isBold;
	}

	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
