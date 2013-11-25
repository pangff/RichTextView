package com.pangff.richtextview;

import android.graphics.RectF;

public class WeiboInfoRect extends RectF{
	/**
	 * the start index of the KXLinkInfo's content string<p>
	 * if the content is null, such as Face bitmap or location bitmap, start = -1
	 */
 	public int start = -1;
 	/**
	 * the end index of the KXLinkInfo's content string<p>
	 * if the content is null, such as Face bitmap or location bitmap, end = -1
	 */
 	public int end = -1;
}
