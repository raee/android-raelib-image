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
 * �������ý��⹤����
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
		 * �ӿڵ���ʧ��
		 * 
		 * @param code
		 * @param msg
		 */
		void onCameraFaild(int code, String msg);
		
		/**
		 * ���ճɹ��ص��ӿ�
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
	 * ����,
	 * ����дActivity��onActivityResult���������ø���onTakePhotoActivityResult��ע��÷�����ҪȨ��<br />
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
		// ����ڴ濨
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			mListener.onCameraFaild(0, "û�м�⵽�ڴ濨");
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
	 * ���ջص�
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
			this.mListener.onCameraFaild(0, "�������գ�");
			return null;
		}
		if (uri == null)
		{
			this.mListener.onCameraFaild(0, "��ȡ��Ƭ��ַʧ�ܣ��������°ɣ�");
			return null;
		}
		
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor == null)
		{
			this.mListener.onCameraFaild(0, "��ȡ��Ƭ��ַʧ�ܣ�");
			return null;
		}
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		String picPath = cursor.getString(columnIndex);
		Log.d("tt", "ͼƬ��ַ��" + picPath);
		File file = new File(picPath);
		if (file.exists())
		{
			this.mListener.onTakePhotoSuccess(picPath);
		}
		else
		{
			this.mListener.onCameraFaild(0, "��ȡ��Ƭ��ַʧ�ܣ��������°ɣ�");
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
		if (cursor == null || cursor.getCount() <= 0) return null; // û��ͼƬ
		while (cursor.moveToNext())
		{
			int index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			String path = cursor.getString(index); // �ļ���ַ
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
