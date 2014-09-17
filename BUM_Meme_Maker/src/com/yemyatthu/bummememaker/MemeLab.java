package com.yemyatthu.bummememaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;


public class MemeLab {
	private ArrayList<Meme> mMemes;
	private ArrayList<Meme> mMyanmarMemes;
	private Meme mMeme;
	private static MemeLab sMemeLab;
	private Context mAppContext;
	private String[] memeHelpList;
	private String topExample = null;
	private String bottomExample = null;
	private ArrayList<Meme> mFavoriteMemes;
	private final String FILE_NAME="memes.jason";
	
	private MemeLab(Context c){
		mAppContext = c;
		mMemes = new ArrayList<Meme>();
	
		String[] memeNameList = c.getResources().getStringArray(R.array.meme_list);
		String[] myanmarMeme = c.getResources().getStringArray(R.array.myanmar_meme_list);
		for(String name: memeNameList){
			
			mMeme = new Meme();
			mMeme.setName(name);
				
	
			
				
			mMemes.add(mMeme);}
		
		mMyanmarMemes = new ArrayList<Meme>();
		for(String name: myanmarMeme){
			
			mMeme = new Meme();
			mMeme.setName(name);
				
	
			
				
			mMyanmarMemes.add(mMeme);}
		mFavoriteMemes = new ArrayList<Meme>();
		try{
			for (String name: loadFavoriteMemes()){
				mFavoriteMemes.add(getMeme(name));}
		}catch(Exception e){
			
		}
	}
 	
	
	
	

	
	public ArrayList<Meme> getFavoriteMemes() {
		return mFavoriteMemes;
	}






	public void setFavoriteMemes(ArrayList<Meme> favoriteMemes) {
		mFavoriteMemes = favoriteMemes;
	}






	public String[] getMemeHelp(){
		return memeHelpList;
	}
	public static MemeLab get(Context c){
		if (sMemeLab==null){
		sMemeLab = new MemeLab(c.getApplicationContext()) ;}
		return sMemeLab;
	}
	
	public ArrayList<Meme> getMemes(){
		return mMemes;
	}
	
	public ArrayList<Meme> getMyanmarMemes(){
		return mMyanmarMemes;
	}
	
	public Meme getMeme(String name){
		for(Meme meme: mMemes){
			if(meme.getName().equals(name))
				return meme;}
		
		for(Meme myanmar: mMyanmarMemes){
			if(myanmar.getName().equals(name))
				return myanmar;}
		return null;
	}
	
	
	public ArrayList<String> loadFavoriteMemes() throws IOException,JSONException{
		ArrayList<String> favoriteMemes = new ArrayList<String>();
		BufferedReader reader = null;
		try{
			InputStream in = mAppContext.openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line=reader.readLine())!=null){
				  jsonString.append(line); 
			}
			 JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue(); 
			 for(int i =0;i<array.length();i++){
				 favoriteMemes.add(array.getString(i));}
			 }catch(Exception e){
			
		}finally{
			if(reader != null){
				reader.close();
			}
		}
		return favoriteMemes;
	}
	

	public void saveFavoriteMemes(ArrayList<Meme> favoriteMemes) throws JSONException,IOException{
		JSONArray array = new JSONArray();
		for(Meme meme: favoriteMemes){
			array.put(meme.getName());
		}
		Writer writer = null;
		try{
			OutputStream out = mAppContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		}finally{
			if(writer != null)
				writer.close();
		}
	}
}
