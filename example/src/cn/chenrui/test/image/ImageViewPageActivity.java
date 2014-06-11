package cn.chenrui.test.image;

import android.app.Activity;
import android.os.Bundle;

import com.rae.core.image.photoview.PhotoViewPager;

public class ImageViewPageActivity extends Activity
{
	private PhotoViewPager	viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);
		viewPager = (PhotoViewPager) findViewById(R.id.pager);
		Bundle bundle = getIntent().getExtras();
		String[] imageUrls = bundle.getStringArray("images");
		viewPager.setImageUri(imageUrls);
		viewPager.setup();
		viewPager.setCurrentItem(2, true);
	}

}
