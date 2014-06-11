package com.rae.core.image.photoview;

import android.content.Context;
import android.util.AttributeSet;

import com.rae.core.image.utils.CameraUtils;

/**
 * ϵͳ��������
 * 
 * @author MrChenrui
 * 
 */
public class PhotoSystemGridView extends PhotoGridView
{
	
	public PhotoSystemGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		String[] imageUris = CameraUtils.getSystemAlubmList(getContext());
		setImageUri(imageUris);
		setup();
	}
	
}
