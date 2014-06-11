package cn.chenrui.test.image;

import com.rae.core.image.cache.disc.naming.Md5FileNameGenerator;
import com.rae.core.image.loader.DisplayImageOptions;
import com.rae.core.image.loader.ImageLoader;
import com.rae.core.image.loader.ImageLoaderConfiguration;
import com.rae.core.image.loader.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

public class ImageApplication extends Application
{
	@Override
	public void onCreate()
	{
		initImageLoader(getApplicationContext());
	}
	
	public static void initImageLoader(Context context)
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // TODO:发布时，移除调试模式
				.defaultDisplayImageOptions(options) //默认配置
				.build();
		ImageLoader.getInstance().init(config);
	}
}
