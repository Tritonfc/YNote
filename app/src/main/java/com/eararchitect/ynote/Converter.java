package com.eararchitect.ynote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


//Class to convert types into usable formats for the roomDatabase

public class Converter {

    //Function to  convert Bitmap to String
    @TypeConverter
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        if (temp == null) {
            return null;
        } else {
            return temp;
        }


    }

    //Function to  convert String to Bitmap
    @TypeConverter
    public static Bitmap StringtoBitmap(String encodedString) {
        try {
            byte[] encodedByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
            if (bitmap == null) {
                return null;
            } else {
                return bitmap;
            }
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    //Function to  convert ArrayList to String
    @TypeConverter
    public static String imageUriToString(ArrayList<String> uri) {
        if (uri == null) {
            return null;
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(uri);
            return json;

        }

    }

    //Function to  convert String to ArrayList

    @TypeConverter
    public static ArrayList<String> StringtoImageUri(String value) {
        if (value == null) {
            return null;
        } else {
            Type uri = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(value, uri);

        }
    }


}
