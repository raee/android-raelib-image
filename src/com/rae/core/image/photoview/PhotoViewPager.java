package com.rae.core.image.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.rae.core.image.loader.ImageLoader;
import com.rae.core.image.loader.assist.FailReason;
import com.rae.core.image.loader.listener.SimpleImageLoadingListener;

/**
 * ViewPager 图片查看器，需要调用setImageUri来设置图片路径，最后调用setup方法显示
 * 
 * @author ChenRui
 * 
 */
public class PhotoViewPager extends ViewPager
{
	protected static final String	LOG_TAG	= "PhotoViewpager";
	private String[]				mImageUris;
	private ImageLoader				imageLoader;
	private PhotoView				mPhotoView;
	
	public PhotoViewPager(Context context)
	{
		this(context, null);
	}
	
	public PhotoViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		imageLoader = ImageLoader.getInstance();
	}
	
	/**
	 * ViewPager显示图片的时候，如需要请重写该方法
	 * 
	 * @param view
	 * @param position
	 * @return
	 */
	protected View getView(ViewGroup view, int position)
	{
		android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		FrameLayout layout = new FrameLayout(getContext());
		layout.setLayoutParams(params);
		
		mPhotoView = new PhotoView(getContext());
		mPhotoView.setAdjustViewBounds(true);
		mPhotoView.setLayoutParams(params);
		
		final ProgressBar spinner = (ProgressBar) new ProgressBar(getContext());
		// params.width = LayoutParams.WRAP_CONTENT;
		// params.height = LayoutParams.WRAP_CONTENT;
		spinner.setLayoutParams(params);
		
		imageLoader.displayImage(mImageUris[position], mPhotoView, null,
				new SimpleImageLoadingListener()
				{
					@Override
					public void onLoadingStarted(String imageUri, View view)
					{
						spinner.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason)
					{
						String message = null;
						switch (failReason.getType())
						{
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
						}
						Log.e(LOG_TAG, message);
						spinner.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage)
					{
						spinner.setVisibility(View.GONE);
					}
				});
		
		layout.addView(mPhotoView);
		layout.addView(spinner);
		
		view.addView(layout, 0);
		return layout;
	}
	
	public PhotoView getPhotoView()
	{
		return this.mPhotoView;
	}
	
	/**
	 * 设置图片地址集合
	 * 
	 * @param imageUris
	 */
	public void setImageUri(String[] imageUris)
	{
		this.mImageUris = imageUris;
	}
	
	/**
	 * 装载图片
	 */
	public void setup()
	{
		if (mImageUris == null) throw new IllegalArgumentException(
				"图片地址为空，装载失败，请正确调用setImageUri");
		setAdapter(new ImagePagerAdapter(mImageUris));
	}
	
	public class ImagePagerAdapter extends PagerAdapter
	{
		
		private String[]	images;
		
		private ImagePagerAdapter(String[] images)
		{
			this.images = images;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}
		
		@Override
		public int getCount()
		{
			return images.length;
		}
		
		@Override
		public Object instantiateItem(ViewGroup view, int position)
		{
			return getView(view, position);
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view.equals(object);
		}
		
		@Override
		public void restoreState(Parcelable state, ClassLoader loader)
		{
		}
		
		@Override
		public Parcelable saveState()
		{
			return null;
		}
	}
}
