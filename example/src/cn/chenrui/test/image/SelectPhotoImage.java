package cn.chenrui.test.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.rae.core.image.loader.ImageLoader;
import com.rae.core.image.photoview.PhotoSystemGridView;

public class SelectPhotoImage extends PhotoSystemGridView
{
	
	public SelectPhotoImage(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	protected View getItemView(int position, View convertView,
			ViewGroup parent, ImageLoader imageLoader)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_photo_selet, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.loading);
			
			holder.checkViewGroup = (ViewGroup) convertView
					.findViewById(R.id.ll_select_content);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		displayImage(holder, position);
		initCheckedItem(holder, position);
		return convertView;
	}
	
}
