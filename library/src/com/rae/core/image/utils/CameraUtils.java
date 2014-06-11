package com.rae.core.image.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 照相机和媒体库工具类
 * 
 * @author MrChenrui
 * 
 */
public class CameraUtils
{
	public static final int			REQUEST_CODE_TAKE_PHOTO	= 100;
	public static final int			REQUEST_CODE_GET_PHOTOS	= 200;
	
	private Activity				context					= null;
	private CameraCallbackListener	mListener				= null;
	private static Uri				uri						= null;
	private static String			TAG						= "CameraUtils";
	
	public interface CameraCallbackListener
	{
		/**
		 * 接口调用失败
		 * 
		 * @param code
		 * @param msg
		 */
		void onCameraFaild(int code, String msg);
		
		/**
		 * 拍照成功回调接口
		 * 
		 * @param imageFile
		 */
		void onTakePhotoSuccess(String path);
	}
	
	public CameraUtils(Activity context)
	{
		this.context = context;
	}
	
	public void setCameraListener(CameraCallbackListener l)
	{
		this.mListener = l;
	}
	
	/**
	 * 拍照,
	 * 请重写Activity的onActivityResult方法并调用该类onTakePhotoActivityResult。注意该方法需要权限<br />
	 * android.permission.WRITE_EXTERNAL_STORAGE<br/>
	 * android.permission.MOUNT_UNMOUNT_FILESYSTEMS <br/>
	 * android.permission.RECORD_AUDIO<br/>
	 * android.permission.WRITE_EXTERNAL_STORAGE<br/>
	 * android.permission.CAMERA<br/>
	 * <uses-feature android:name="android.hardware.camera" /><br>
	 * <uses-feature android:name="android.hardware.camera.autofocus" />
	 */
	public void takePhoto()
	{
		// 检查内存卡
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			mListener.onCameraFaild(0, "没有检测到内存卡");
			return;
		}
		
		uri = context.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new ContentValues());
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		context.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
	}
	
	/**
	 * 拍照回调
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public String onTakePhotoActivityResult(int requestCode, int resultCode,
			Intent data)
	{
		if (requestCode != REQUEST_CODE_TAKE_PHOTO
				|| resultCode != Activity.RESULT_OK)
		{
			this.mListener.onCameraFaild(0, "放弃拍照！");
			return null;
		}
		if (uri == null)
		{
			this.mListener.onCameraFaild(0, "获取照片地址失败，请重试下吧！");
			return null;
		}
		
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor == null)
		{
			this.mListener.onCameraFaild(0, "获取照片地址失败！");
			return null;
		}
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		String picPath = cursor.getString(columnIndex);
		Log.d("tt", "图片地址：" + picPath);
		File file = new File(picPath);
		if (file.exists())
		{
			this.mListener.onTakePhotoSuccess(picPath);
		}
		else
		{
			this.mListener.onCameraFaild(0, "获取照片地址失败，请重试下吧！");
		}
		cursor.close();
		return picPath;
	}
	
	public static String[] getSystemAlubmList(Context context)
	{
		List<String> result = new ArrayList<String>();
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
		while (cursor.moveToNext())
		{
			int index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			String path = cursor.getString(index); // 文件地址
			File file = new File(path);
			if (file.exists())
			{
				String uriPath = Uri.fromFile(file).toString();
				path = Uri.decode(uriPath);
				result.add(path);
				Log.i(TAG, path);
			}
		}
		String[] res = new String[result.size()];
		result.toArray(res);
		return res;
	}
}
