package com.example.nano.supportclasses;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class AssetsLoaderSupport {
	private Context context;

	public AssetsLoaderSupport(Context c) {
		context = c;
	}

	public Drawable loadImageFromAssets(String path) {
		// load image
		try {
			// get input stream
			InputStream ims = context.getAssets().open(path);//path=avatar.jpg
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);
			// set image to ImageView
			// mImage.setImageDrawable(d);
			return d;
		} catch (IOException ex) {
			return null;
		}
	}

	public String loadTextBufferFromAssets(String path) {
		// load text
		try {
			// get input stream for text
			InputStream is = context.getAssets().open(path);
			// check size
			int size = is.available();
			// create buffer for IO
			byte[] buffer = new byte[size];
			// get data to buffer
			is.read(buffer);
			// close stream
			is.close();
			// set result to TextView
			return new String(buffer);
		} catch (IOException ex) {
			return null;
		}
	}
}
