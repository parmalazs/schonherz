package com.schonherz.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageUtil {
	public static String encodeImegeFromPath(String path) {
		String encodedImage = null;
		try {
			Bitmap bm = BitmapFactory.decodeFile(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 80, baos); 
			byte[] byteArrayImage = baos.toByteArray();
			bm.recycle();
			bm = null;
			baos.flush();
			baos.close();
			baos = null;
			encodedImage = Base64
					.encodeBytes(byteArrayImage, Base64.NO_OPTIONS);
			byteArrayImage = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return encodedImage;
	}
	
    // kicseréli az ékezet karakterekt nem ékezetesre
    public static String RemoveHUnCaracters(String Text)
    {
        Text = Text.replace("á", "a");
        Text = Text.replace("é", "e");
        Text = Text.replace("í", "i");
        Text = Text.replace("ú", "u");
        Text = Text.replace("ű", "u");
        Text = Text.replace("ü", "u");
        Text = Text.replace("ő", "o");
        Text = Text.replace("ó", "o");
        Text = Text.replace("ö", "o");

        Text = Text.replace("Á", "A");
        Text = Text.replace("É", "E");
        Text = Text.replace("Í", "I");
        Text = Text.replace("Ú", "U");
        Text = Text.replace("Ű", "U");
        Text = Text.replace("Ü", "U");
        Text = Text.replace("Ő", "O");
        Text = Text.replace("Ó", "O");
        Text = Text.replace("Ö", "O");
        

        return Text;        
    }


    public static String PrepareFolder(String folder)
    {
        folder = RemoveHUnCaracters(folder);
        folder = folder.replaceAll("[^-a-zA-Z0-9_ ]", "-");
        folder = folder.trim();
        folder = folder.replace("[-_ ]+", "-");
        folder = folder.replace("[. ]+", "-");

        return folder;
    }
    
    public static String PreparePicture(String picture)
    {
        picture = RemoveHUnCaracters(picture);
        picture = picture.replaceAll("[^-a-zA-Z0-9_ ]", "-");
        picture = picture.trim();
        picture = picture.replace("[-_ ]+", "-");
        picture = picture.replace("[. ]+", "-");

        return picture;
    }
	
}
