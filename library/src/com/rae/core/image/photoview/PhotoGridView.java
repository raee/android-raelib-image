package com.rae.core.image.photoview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.rae.core.image.loader.ImageLoader;
import com.rae.core.image.loader.assist.FailReason;
import com.rae.core.image.loader.listener.ImageLoadingProgressListener;
import com.rae.core.image.loader.listener.SimpleImageLoadingListener;

@SuppressLint("UseSparseArrays")
public class PhotoGridView extends GridView
{
	public class ImageGridViewAdapter extends BaseAdapter
	{
		
		@Override
		public int getCount()
		{
			return mImageUris.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return mImageUris.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			return getItemView(position, convertView, parent, mImageLoader);
		}
		
	}
	
	/**
	 * @author Chenrui
	 * 
	 */
	public final class ViewHolder
	{
		/**
		 * 选中的状态的视图
		 */
		public ViewGroup	checkViewGroup;
		/**
		 * 图片
		 */
		public ImageView	imageView;
		
		/**
		 * 进度条
		 */
		public ProgressBar	progressBar;
	}
	
	private HashMap<Integer, Boolean>	mCheckedMap				= new HashMap<Integer, Boolean>();
	private ImageLoader					mImageLoader			= null;
	private List<String>				mImageUris				= null;
	
	private boolean						mRemoveFaildImageView	= true;
	private ListAdapter					mAdapter;
	
	public PhotoGridView(Context context)
	{
		this(context, null);
	}
	
	public PhotoGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mImageLoader = ImageLoader.getInstance();
		this.mAdapter = new ImageGridViewAdapter();
	}
	
	/**
	 * 显示正在操作
	 * 
	 * @param progressBar
	 *            进度条
	 * @param progress
	 *            进度
	 */
	public void showInProgress(ProgressBar progressBar, int progress)
	{
		
		progressBar.setProgress(progress);
		progressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 显示正在操作
	 * 
	 * @param position
	 *            所在位置
	 * @param progress
	 *            进度
	 */
	public void showInProgress(int position, int progress)
	{
		View view = this.getChildAt(position);
		if (view != null)
		{
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder != null)
			{
				showInProgress(holder.progressBar, progress);
				holder.imageView.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * 显示正在操作
	 * 
	 * @param uri
	 *            路径
	 * @param progress
	 *            进度
	 */
	public void showInProgress(String uri, int progress)
	{
		int position = this.mImageUris.indexOf(uri);
		showInProgress(position, progress);
	}
	
	public void showImage(int position)
	{
		View view = this.getChildAt(position);
		if (view != null)
		{
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder != null)
			{
				holder.imageView.setVisibility(View.VISIBLE);
				holder.progressBar.setVisibility(View.GONE);
				if (holder.checkViewGroup != null) holder.checkViewGroup
						.setVisibility(View.GONE);
			}
		}
	}
	
	public void showImage(String uri)
	{
		int position = this.mImageUris.indexOf(uri);
		showImage(position);
	}
	
	/**
	 * 显示图片
	 * 
	 * @param imageView
	 * @param progressBar
	 * @param position
	 */
	protected void displayImage(final ViewHolder holder, int position)
	{
		mImageLoader.displayImage(mImageUris.get(position), holder.imageView,
				null, new SimpleImageLoadingListener()
				{
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage)
					{
						holder.progressBar.setVisibility(View.GONE);
						view.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason)
					{
						holder.progressBar.setVisibility(View.GONE);
						if (mRemoveFaildImageView)
						{
							Log.e("imageLoader", "错误的图片：" + imageUri);
						}
					}
					
					@Override
					public void onLoadingStarted(String imageUri, View view)
					{
						showInProgress(holder.progressBar, 0);
					}
				}, new ImageLoadingProgressListener()
				{
					
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total)
					{
						int progress = (int) Math
								.rint(((double) current / total) * 100);
						showInProgress(holder.progressBar, progress);
					}
				});
		
	}
	
	/**
	 * 获取所有选中的项
	 * 
	 * @return
	 */
	public HashMap<Integer, Boolean> getCheckedListMap()
	{
		return this.mCheckedMap;
	}
	
	/**
	 * 获取所有选中的项
	 * 
	 * @return
	 */
	public List<String> getCheckedList()
	{
		List<String> result = new ArrayList<String>();
		HashMap<Integer, Boolean> checkedListMap = getCheckedListMap();
		for (Entry<Integer, Boolean> item : checkedListMap.entrySet())
		{
			int key = item.getKey();
			result.add(this.mImageUris.get(key));
		}
		return result;
	}
	
	public List<String> getImageUri()
	{
		return this.mImageUris;
	}
	
	protected View getItemView(int position, View convertView,
			final ViewGroup parent, ImageLoader imageLoader)
	{
		ViewHolder mViewHolder = null;
		if (convertView == null)
		{
			mViewHolder = new ViewHolder();
			FrameLayout layout = new FrameLayout(getContext());
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					300);
			layout.setLayoutParams(params);
			
			mViewHolder.imageView = new ImageView(getContext());
			mViewHolder.imageView.setAdjustViewBounds(true);
			mViewHolder.imageView.setScaleType(ScaleType.CENTER_CROP);
			mViewHolder.imageView.setLayoutParams(params);
			
			mViewHolder.progressBar = new ProgressBar(getContext());
			FrameLayout.LayoutParams progparams = new FrameLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			progparams.gravity = Gravity.CENTER;
			mViewHolder.progressBar.setLayoutParams(progparams);
			mViewHolder.progressBar.setIndeterminate(false);
			
			layout.addView(mViewHolder.imageView);
			layout.addView(mViewHolder.progressBar);
			convertView = layout;
			convertView.setTag(mViewHolder);
		}
		else
		{
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		displayImage(mViewHolder, position);
		
		return convertView;
	}
	
	/**
	 * 初始化被点击的项
	 * 
	 * @param holder
	 *            视图
	 * @param position
	 *            所在位置
	 */
	protected void initCheckedItem(ViewHolder holder, int position)
	{
		if (mCheckedMap.containsKey(position))
		{
			setChecked(holder, position, true); // 初始化的时候设置为不选中
		}
		else
		{
			setChecked(holder, position, false);
		}
	}
	
	/**
	 * 检查某一项是否已经选中
	 * 
	 * @param position
	 *            所在的位置
	 * @return
	 */
	public boolean isChecked(int position)
	{
		return mCheckedMap.containsKey(position);
	}
	
	/**
	 * 标志选中状态
	 * 
	 * @param position
	 *            所在位置
	 */
	private void putChecked(int position)
	{
		if (!mCheckedMap.containsKey(position))
		{
			mCheckedMap.put(position, true);
		}
	}
	
	/**
	 * 移除选中状态
	 * 
	 * @param position
	 *            所在位置
	 */
	private void removeChecked(int position)
	{
		if (mCheckedMap.containsKey(position))
		{
			mCheckedMap.remove(position);
		}
	}
	
	/**
	 * 设置选中
	 * 
	 * @param holder
	 *            视图，可以通过view.getTag()的方式获取，并传递进来
	 * @param position
	 *            所在位置
	 * @param selected
	 *            设置选中状态
	 */
	public void setChecked(ViewHolder holder, int position, boolean selected)
	{
		if (holder == null) return;
		if (selected)
		{
			holder.checkViewGroup.setVisibility(View.VISIBLE);
			putChecked(position);
		}
		else
		{
			holder.checkViewGroup.setVisibility(View.GONE);
			removeChecked(position);
		}
		
	}
	
	/**
	 * 设置某一张图片被选中
	 * 
	 * @param uri
	 *            图片的地址
	 */
	public void setCheckedUri(String uri)
	{
		List<String> uris = getImageUri();
		if (uris != null && uris.contains(uri))
		{
			int position = uris.indexOf(uri);
			putChecked(position); // 设置点击的参数
		}
	}
	
	/**
	 * 设置图片地址集合
	 * 
	 * @param imageUris
	 */
	public void setImageUri(List<String> list)
	{
		this.mImageUris = list;
	}
	
	/**
	 * 设置图片地址集合
	 * 
	 * @param imageUris
	 */
	public void setImageUri(String[] imageUris)
	{
		this.mImageUris = Arrays.asList(imageUris);
	}
	
	/**
	 * 是否需要移除错误的图片,默认为移除。
	 * 
	 * @param val
	 */
	public void setRemoveFaildImageView(boolean val)
	{
		mRemoveFaildImageView = val;
	}
	
	public void setup()
	{
		setAdapter(mAdapter);
	}
	
}
