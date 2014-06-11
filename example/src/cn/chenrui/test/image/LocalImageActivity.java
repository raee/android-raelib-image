package cn.chenrui.test.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.rae.core.image.loader.ImageLoader;
import com.rae.core.image.photoview.PhotoGridView;

public class LocalImageActivity extends Activity
{
	private PhotoGridView		gridview;
	private String[]		images;
	protected ImageLoader	imageLoader	= ImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		images = new String[] {
				"http://www.zjjcts.com/uploads/allimg/120504/8-120504015Z9.jpg",
				"http://img.blog.163.com/photo/oeNGSebL9agfe-tznwRAoQ==/3970204546504136431.jpg",
				"http://www.hyrc.cn/upfile/3/200611/1123539053c7e.jpg",
				"file:///mnt/sdcard/test/a (1).jpg",
				"file:///mnt/sdcard/test/a (2).jpg"
		
		};
		
		gridview = (PhotoGridView) findViewById(R.id.gridview);
		gridview.setImageUri(images);
		gridview.setup();
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(LocalImageActivity.this,
						ImageViewPageActivity.class);
				intent.putExtra("images", images);
				startActivity(intent);
			}
		});
	}
}
