package com.yemyatthu.bumc.utils;

import android.content.Context;
import com.yemyatthu.bumc.R;
import com.yemyatthu.bumc.model.Meme;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class MemeLab {
  public static final String FAVORITE_FILE_NAME = "memes.jason";
  public static final String CUSTOM_FILE_NAME = "custommemes.jason";
  private static MemeLab sMemeLab;
  private ArrayList<Meme> mMemes;
  private ArrayList<Meme> mMyanmarMemes;
  private ArrayList<Meme> mCustomMemes;
  private Meme mMeme;
  private Context mAppContext;
  private ArrayList<Meme> mFavoriteMemes;

  private MemeLab(Context c) {
    String[] myanmarMemes = c.getResources().getStringArray(R.array.myanmar_meme_list);

    mAppContext = c;
    mMemes = new ArrayList<Meme>();
    mMyanmarMemes = new ArrayList<Meme>();
    String[] memeNameList = mAppContext.getResources().getStringArray(R.array.meme_list);

    for (String name : memeNameList) {

      mMeme = new Meme();
      mMeme.setName(name);
      mMemes.add(mMeme);
    }

    for (String name : myanmarMemes) {

      mMeme = new Meme();
      mMeme.setName(name);

      mMyanmarMemes.add(mMeme);
    }
    mCustomMemes = new ArrayList<Meme>();
    try {
      for (String name : loadFavoriteMemes(CUSTOM_FILE_NAME)) {
        Meme meme = new Meme();
        meme.setName(name);
        mCustomMemes.add(meme);
      }
    } catch (Exception ignored) {

    }
    mFavoriteMemes = new ArrayList<Meme>();
    try {
      for (String name : loadFavoriteMemes(FAVORITE_FILE_NAME)) {
        mFavoriteMemes.add(getMeme(name));
      }
    } catch (Exception ignored) {

    }
  }

  public static MemeLab get(Context c) {
    if (sMemeLab == null) {
      sMemeLab = new MemeLab(c.getApplicationContext());
    }
    return sMemeLab;
  }

  public ArrayList<Meme> getCustomMemes() {
    return mCustomMemes;
  }

  public ArrayList<Meme> getFavoriteMemes() {
    return mFavoriteMemes;
  }

  public ArrayList<Meme> getMemes() {
    return mMemes;
  }

  public ArrayList<Meme> getMyanmarMemes() {
    return mMyanmarMemes;
  }

  public Meme getMeme(String name) {
    for (Meme meme : mMemes) {
      if (meme.getName().equals(name)) return meme;
    }

    for (Meme myanmar : mMyanmarMemes) {
      if (myanmar.getName().equals(name)) return myanmar;
    }
    for (Meme custom : mCustomMemes) {
      if (custom.getName().equals(name)) return custom;
    }

    return null;
  }

  public ArrayList<String> loadFavoriteMemes(String fileName) throws IOException, JSONException {
    ArrayList<String> favoriteMemes = new ArrayList<String>();
    BufferedReader reader = null;
    try {
      InputStream in = mAppContext.openFileInput(fileName);
      reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder jsonString = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        jsonString.append(line);
      }
      JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
      for (int i = 0; i < array.length(); i++) {
        favoriteMemes.add(array.getString(i));
      }
    } catch (Exception ignored) {

    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return favoriteMemes;
  }

  public void saveMemes(ArrayList<Meme> favoriteMemes, String fileName)
      throws JSONException, IOException {
    JSONArray array = new JSONArray();
    for (Meme meme : favoriteMemes) {
      array.put(meme.getName());
    }
    Writer writer = null;
    try {
      OutputStream out = mAppContext.openFileOutput(fileName, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(out);
      writer.write(array.toString());
    } finally {
      if (writer != null) writer.close();
    }
  }
}
