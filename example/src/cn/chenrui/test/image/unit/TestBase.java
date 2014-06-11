package cn.chenrui.test.image.unit;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestBase extends AndroidTestCase
{
	private String	tag;
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		this.tag = getClass().getName();
	}

	public void i(Object msg)
	{
		Log.i(tag, msg.toString());
	}
}
