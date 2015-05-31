package com.honesty.pool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Http {
	
	public static void downImage(String urlPath){
		try {
			URL url = new URL(urlPath);
			URLConnection conn = url.openConnection();
			conn.connect();
			int fileSize = conn.getContentLength();
			System.out.println("fileSize = " + fileSize);
			InputStream is = conn.getInputStream();
			
			byte[] bytes = new byte[1024];
            int len = -1;  
            while((len = is.read(bytes))!=-1){  
                
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过url获取图片
	 * 
	 * @param urlPath
	 * @return
	 */
	public static Bitmap getBitmapFromURL(String urlPath) {
		try {
			URL url = new URL(urlPath);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * http获取服务器数据
	 * 
	 * @param urlString
	 * @param outputStream
	 * @return
	 */
	public static boolean downloadUrlToStream(String urlString,
			OutputStream outputStream) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(),
					8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
