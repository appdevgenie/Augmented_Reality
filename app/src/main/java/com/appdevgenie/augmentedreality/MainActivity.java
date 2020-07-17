package com.appdevgenie.augmentedreality;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends Activity {

    private Camera mCamera;
    private PreviewSurface mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mCamera = getCameraInstance();

        initializeCameraParameters();

        if (mCamera == null) {
            // setContentView(R.layout.activity_main);
        } else {
            mPreview = new PreviewSurface(this, mCamera);
            setContentView(mPreview);
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {

        }

        return c;
    }

    private void initializeCameraParameters() {
        Camera.Parameters parameters = mCamera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        Camera.Size size = getBestPreviewSize(640, 480, parameters);

        int currentWidth = 0;
        int currentHeight = 0;

        boolean foundDesiredWidth = false;

        for (Camera.Size s : sizes) {
            if (s.width == size.width) {
                currentWidth = s.width;
                currentHeight = s.height;
                foundDesiredWidth = true;
                break;
            }
        }

        if (foundDesiredWidth) {
            parameters.setPreviewSize(currentWidth, currentHeight);
        }

        mCamera.setParameters(parameters);
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }
        return (result);
    }
}

