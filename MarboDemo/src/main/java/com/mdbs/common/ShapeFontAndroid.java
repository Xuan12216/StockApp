package com.mdbs.common;

import android.app.Activity;
import android.graphics.Bitmap;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Typeface;
import android.graphics.PorterDuff;
  
public class ShapeFontAndroid
{
	Bitmap mBitmap;
	Canvas mCanvas;
	Paint mPaint;
	Typeface mExternalFont;
	int mFontStyle = 0;
	int mFontColor = 0xffffffff;
	int mFontShadowColor = 0xc0000000;
	
	public ShapeFontAndroid()
	{
	}
	
	public boolean createCanvas(int width, int height)
	{
		try
		{
			mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			mBitmap.eraseColor(0);
			
			mPaint = new Paint();  
            mPaint.setColor(0xffffffff);
            mPaint.setTextAlign(Paint.Align.LEFT);
            mPaint.setAntiAlias(true);
            mPaint.setShadowLayer(0, 0, 0, Color.BLACK);//no shadow
            mPaint.setStrokeWidth(4);
            mFontStyle = 0;
            
			return true;
		}
		catch (Exception e) { }

		return false;
	}
	
	public boolean setProperty(String face_name, int size, int style)
	{
		try
		{
			mFontStyle = style;
			int s = Typeface.NORMAL;
			if((style & 3)==3) s = Typeface.BOLD_ITALIC;
			else if((style & 2)!=0) s = Typeface.ITALIC;
			else if((style & 1)!=0) s = Typeface.BOLD;
			
			if(mExternalFont != null)
			{
				mPaint.setTypeface(mExternalFont);
			}
			else
			{
				mPaint.setTypeface(Typeface.create(face_name, s));
			}
			
			mPaint.setTextSize(size * 16 / 12);
			mPaint.setUnderlineText((style & 4)!=0);
			return true;
		}
		catch (Exception e) { }

		return false;
	}
	
	public void setFontColor(int color, int shadow_color)
	{
		mFontColor = color | 0xff000000;
		mFontShadowColor = shadow_color | 0xc0000000;

		// shadow
		if((mFontStyle & 0x10) !=0)
		{
			mPaint.setShadowLayer(2.0f, 2.0f, 2.0f, mFontShadowColor);
		}
		else mPaint.setShadowLayer(0, 0, 0, mFontShadowColor);		
	}
	
    public void textOut(String text, int x, int y)
    {  
        try  
        {
        	mCanvas.drawColor((mFontColor & 0x00fffff) | 0x01000000, PorterDuff.Mode.SRC);
        	
			// stroke
			if((mFontStyle & 8) != 0)
			{
				mPaint.setStyle(Paint.Style.STROKE);
				mPaint.setColor(mFontShadowColor);
	        	mCanvas.drawText(text, x, y, mPaint);  				
			}
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(mFontColor);
        	mCanvas.drawText(text, x, y, mPaint);
        }  
        catch (Exception e) { }
    }
    
    // get text's width, height and ascent to size[3]
    public boolean getTextExtent(String text, int[] size)
    {
    	try
    	{
    		FontMetrics fm = mPaint.getFontMetrics();
	        
	        size[0] = (int)mPaint.measureText(text);
	        size[1] = (int)(fm.descent - fm.ascent + 0.5);
	        size[2] = (int)(-fm.ascent + 0.5);
	        return true;
		}
		catch (Exception e) { }

		return false;
    }
    
    public Bitmap getBitmap(){ return mBitmap; }
    
    public void getPixels(int[] pixels)  
    {  
        int w = mBitmap.getWidth();  
        int h = mBitmap.getHeight();
        mBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
    }    
}

