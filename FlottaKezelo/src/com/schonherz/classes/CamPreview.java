package com.schonherz.classes;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("unused")
class CamPreview extends ViewGroup implements SurfaceHolder.Callback {
 


	private final String TAG = "Preview";

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    Size mPreviewSize;
    List<Size> mSupportedPreviewSizes;
    public Camera mCamera;
    SharedPreferences pref;

    CamPreview(Context context, SurfaceView sv) {
        super(context);

        mSurfaceView = sv;
  //     addView(mSurfaceView);
       
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
    	mCamera = camera;
    	if (mCamera != null) {
    		mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();

    		requestLayout();

    		// get Camera parameters
    		Camera.Parameters params = mCamera.getParameters();
    		
    		
    		List<String> focusModes = params.getSupportedFocusModes();
    		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
    			// set the focus mode
    			params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
    			
    			 pref = PreferenceManager
    						.getDefaultSharedPreferences(getContext());
    				String reqRes = pref.getString("cameraSize", "640x480");
    				String[] pSize = reqRes.split("x");
    				Size size = getOptimalPreviewSize(camera.getParameters().getSupportedPictureSizes(), Integer.parseInt(pSize[0]), Integer.parseInt(pSize[1]));
    				params.setPictureSize(size.width, size.height);
    			// set Camera parameters
    			mCamera.setParameters(params);
    		}
    	}
    }

    @Override
 	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), w);
        final int height = resolveSize(getSuggestedMinimumHeight(), h);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    	super.onSizeChanged(w, h, oldw, oldh);
 	}
    


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
    	if(mCamera != null) {

            
				//Camera.Parameters parameters = mCamera.getParameters();
				//parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
				//requestLayout();

				//mCamera.setParameters(parameters);

    	}

    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	if(mCamera != null) {

				try {
					mCamera.setPreviewDisplay(holder);

					Camera.Parameters parameters = mCamera.getParameters();
	        		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
	        		//requestLayout();

	        		mCamera.setParameters(parameters);
	        		//mCamera.setDisplayOrientation(0);

					mCamera.startPreview();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		

    		
    	}
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }



}