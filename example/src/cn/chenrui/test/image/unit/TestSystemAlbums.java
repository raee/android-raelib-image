package cn.chenrui.test.image.unit;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class TestSystemAlbums extends TestBase
{
	private Uri	uri;
	
	public void testAll()
	{
		uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		
		ContentResolver contentResolver = getContext().getContentResolver();
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		i("--开始--" + cursor.getCount());
		while (cursor.moveToNext())
		{
			int index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			String name = cursor.getString(index);
			i(name);
		}
		i("--结束--");
	}
}
