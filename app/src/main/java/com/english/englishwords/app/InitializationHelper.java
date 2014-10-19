package com.english.englishwords.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InitializationHelper {
  /**
   * Copy the asset at the specified currentFolderOrFile to this app's data directory. If the
   * asset is a directory, its contents are also copied.
   *
   * @param currentFolderOrFile Path to asset, relative to app's assets directory.
   */
  public static void copyAsset(Context context, String out_dir, String currentFolderOrFile) {
    AssetManager manager = context.getAssets();

    // If we have a directory, we make it and recurse. If a file, we copy its
    // contents.
    try {
      String[] contents = manager.list(currentFolderOrFile);

      // The documentation suggests that list throws an IOException, but doesn't
      // say under what conditions. It'd be nice if it did so when the currentFolderOrFile was
      // to a file. That doesn't appear to be the case. If the returned array is
      // null or has 0 length, we assume the currentFolderOrFile is to a file. This means empty
      // directories will get turned into files.
      if (contents == null || contents.length == 0)
        throw new IOException();

      // Make the directory.
      File dir = new File(out_dir, currentFolderOrFile);
      dir.mkdirs();

      // Recurse on the contents.
      for (String entry : contents) {
        copyAsset(context, out_dir, currentFolderOrFile + "/" + entry);
      }
    } catch (IOException e) {
      copyFileAsset(context, out_dir, currentFolderOrFile);
    }
  }

  /**
   * Copy the asset file specified by path to app's data directory. Assumes
   * parent directories have already been created.
   *
   * @param path Path to asset, relative to app's assets directory.
   */
  private static void copyFileAsset(Context context, String out_dir, String path) {
    File file = new File(out_dir, path);
    try {
      InputStream in = context.getAssets().open(path);
      OutputStream out = new FileOutputStream(file);
      byte[] buffer = new byte[1024 * 256];
      int read = in.read(buffer);
      while (read != -1) {
        out.write(buffer, 0, read);
        read = in.read(buffer);
      }
      out.close();
      in.close();
    } catch (IOException e) {
      Log.e("ERROR", e.toString());
    }
  }
}
