package cn.chenrui.test.image;

import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.rae.core.image.loader.ImageLoader;
import com.rae.core.image.photoview.PhotoGridView.ViewHolder;
import com.rae.core.image.utils.CameraUtils;
import com.rae.core.image.utils.CameraUtils.CameraCallbackListener;

/**
 * 拍照测试
 * 
 * @author MrChenrui
 * 
 */
public class TakePhotoActivity extends Activity implements OnClickListener,
		CameraCallbackListener
{
	private ImageView			img;
	CameraUtils					cm	= null;
	private SelectPhotoImage	aav;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takephoto);
		findViewById(android.R.id.button1).setOnClickListener(this);
		this.img = (ImageView) findViewById(R.id.img);
		cm = new CameraUtils(this);
		cm.setCameraListener(this);
		
		
		
		aav = ((SelectPhotoImage) findViewById(android.R.id.list));
		
		for (int i = 0; i < 5; i++)
		{
			aav.setCheckedUri(aav.getImageUri().get(i));
		}

		
		aav.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3)
			{
				Log.i("tag", position + " -- clicked");
				ViewHolder holder = (ViewHolder) view.getTag();
				aav.setChecked(holder , position, !aav.isChecked(position));
			}
		});
	}
	
	@Override
	public void onClick(View v)
	{
		cm.takePhoto();
	}
	
	@Override
	public void onCameraFaild(int code, String msg)
	{
		Log.e("tt", msg);
	}
	
	@Override
	public void onTakePhotoSuccess(String path)
	{
		ImageLoader.getInstance().displayImage(
				Uri.fromFile(new File(path)).toString(), img);
	}
	
}
