package com.piotapps.blendle.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.piotapps.blendle.pojo.PopularItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses BLOB storage to save and retrieve popular items.
 *
 * TODO use database instead, but skip for demo app for now because PopularItem its structure and time
 */
public class StorageUtils {

    private static final String STORAGE_FILENAME = "popular_items";

    public static void clearPopularItems(@NonNull final Context context) {
        savePopularItems(context, null);
    }

    public static void savePopularItems(@NonNull final Context context, final List<PopularItem> pi) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(pi);
            oos.flush();
            oos.close();
            FileOutputStream fos = context.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE);
            fos.write(bos.toByteArray());
            fos.close();
        } catch (Exception e) { //FileNotFoundException or IOException
            e.printStackTrace();
        }
    }

    public static List<PopularItem> getSavedPopularItems(@NonNull final Context context) {
        InputStream is;
        try {
            is = context.openFileInput(STORAGE_FILENAME);
            File f = new File(context.getFilesDir(), STORAGE_FILENAME);
            byte[] bytes = new byte[(int) f.length()];
            is.read(bytes);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return (List<PopularItem>) objectInputStream.readObject();
        } catch (Exception e) {  //FileNotFoundException or IOException
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
