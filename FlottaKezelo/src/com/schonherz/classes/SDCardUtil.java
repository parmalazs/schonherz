package com.schonherz.classes;

import android.os.Environment;
import android.os.StatFs;

public class SDCardUtil {
	
	public static Boolean checkAvailable() {
		String state=Environment.getExternalStorageState();		
		
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Boolean checkIsReadOnly () {
		String state=Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		} 
		else {
			return false;
		}
	}
	
	public static int getAvailableSpaceInMegaBytes() {
		int availableSpace=0;
		
		try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            availableSpace =stat.getAvailableBlocks() * stat.getBlockSize() / 1048576;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return availableSpace;
	}

}
