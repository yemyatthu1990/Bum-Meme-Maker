package com.yemyatthu.bummememaker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

 class MemeListFragment extends ListFragment{
	private Meme mainMeme;
	private Bitmap mPlaceHolderBitmap;
	private ArrayList<Meme> mMemes;
	private ArrayList<Meme> mFavoriteMemes;
	private ArrayList<Meme> mMyanmarMemes;
	private ArrayList<Meme> mCustomMemes;
	private LruCache<String,Bitmap> mMemoryCache;

	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;
	private static final String DISK_CACHE_SUBDIR="memethumbnails";
	private static final int DISK_CACHE_SIZE = 1024*1024*80;
	private static final int SELECT_PICTURE = 6;
	private String selectedImagePath;
	public static final String IMAGE_PATH = "com.yemyatthu.mememaker.IMAGE";
	public static final String TAB_ID = "com.yemyatthu.bummememaker.TAB";
	public static final int FAVORITE_REQUEST =10;
	public static final int CUSTOM_ID = 1;
	public int pos;
	
	
	public static MemeListFragment getNewInstance(int position){
		Bundle args = new Bundle();
		args.putSerializable(TAB_ID, position);
		MemeListFragment memeList = new MemeListFragment();
		memeList.setArguments(args);
		return memeList;
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		File cacheDir = new File(getActivity().getCacheDir()+File.separator+DISK_CACHE_SUBDIR);
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
		final int cacheSize = maxMemory/2;
		mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bitmap){
				return bitmap.getByteCount()/1024;
			}
		};
			
		
		mMemes = MemeLab.get(getActivity()).getMemes();
		mFavoriteMemes = MemeLab.get(getActivity()).getFavoriteMemes();
		mMyanmarMemes = MemeLab.get(getActivity()).getMyanmarMemes();
		mCustomMemes = MemeLab.get(getActivity()).getCustomMemes();
		if(getArguments().getInt(TAB_ID)==0){
			ArrayAdapter<Meme> adapter = new MemeAdapter(mMemes);
		setListAdapter(adapter);}
		if(getArguments().getInt(TAB_ID)==2){
			ArrayAdapter<Meme> adapter1 = new MemeAdapter(mFavoriteMemes);
			setListAdapter(adapter1);
		}
		if(getArguments().getInt(TAB_ID)==1){
			ArrayAdapter<Meme> adapter1 = new MemeAdapter(mMyanmarMemes);
			setListAdapter(adapter1);
		}
		if(getArguments().getInt(TAB_ID)==3){
			ArrayAdapter<Meme> adapter1 = new MemeAdapter(mCustomMemes);
			setListAdapter(adapter1);
		}
	
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.list_view, null);
		return v;
	}
	@Override 
	public void onPause(){
		super.onPause();
		try {
			MemeLab.get(getActivity()).saveMemes((MemeLab.get(getActivity())).getFavoriteMemes(),MemeLab.FAVORITE_FILE_NAME);
			MemeLab.get(getActivity()).saveMemes((MemeLab.get(getActivity())).getCustomMemes(),MemeLab.CUSTOM_FILE_NAME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		((MemeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	
	
	@Override
	public void onListItemClick (ListView l, View v, int position, long id){
		Intent i = new Intent(getActivity(),MemeViewActivity.class);
		Meme meme = ((MemeAdapter)getListAdapter()).getItem(position);
		i.putExtra(MemeViewFragment.NAME_TAG,meme.getName());
		startActivityForResult(i, FAVORITE_REQUEST);
		pos =position;
	}
	
	public class MemeAdapter extends ArrayAdapter<Meme>{
		public MemeAdapter(ArrayList<Meme> memes){
			super(getActivity(),0,memes);
		}
		
		@Override
		public View getView(int position,View convertView,ViewGroup container){
			class ViewHolder{
				public TextView text;
				public ImageView image;
			}
			
			if (convertView == null){
				
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_meme_list, null);
			}
			mainMeme = getItem(position);
			ViewHolder holder = new ViewHolder();
			holder.text = (TextView)convertView.findViewById(R.id.meme_name_view);
			holder.text.setTextColor(Color.DKGRAY);
			String memeName = (mainMeme.getName()).substring(0, 1).toUpperCase(Locale.ENGLISH);
			memeName += (mainMeme.getName()).substring(1).replace("_", " ").toLowerCase(Locale.ENGLISH);
			holder.text.setText(memeName);

			holder.image = (ImageView)convertView.findViewById(R.id.thumbnail_view);
		
			loadBitmap(mainMeme.getName(),holder.image);
					
			
			
			
					
			
			return convertView;
		}
		
		private void loadBitmap(String path,ImageView view){
			final String imageKey = path;
			final Bitmap bitmap = getBitmapFromMemCache(imageKey);
			if(bitmap != null){
				view.setImageBitmap(bitmap);
			}
			else{
			if(cancelPotentialWork(path,view)){
				final BitmapWorkerTask task = new BitmapWorkerTask(view);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(),mPlaceHolderBitmap,task);
				view.setImageDrawable(asyncDrawable);
				task.execute(path);
			}
			
			}
		}
	
		
			}
	
		
		
			
		
			 
	
@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
	 	super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.meme_view, menu);
	}
@Override
	public boolean onOptionsItemSelected(MenuItem item){
	 switch(item.getItemId()){
	 case R.id.settings_menu:
		 Intent i = new Intent(getActivity(),SettingsActivity.class);
		 startActivity(i);
		 return true;
	 case R.id.about_menu:
		 Intent d = new Intent(getActivity(),AboutViewActivity.class);
		 startActivity(d);
		 return true;
	 case R.id.open_image:
		 Intent e = new Intent();
			e.setType("image/*");
			e.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(e, getActivity().getResources().getString(R.string.image_picker_title)), SELECT_PICTURE);
		 return true;
	 default:
		 return super.onOptionsItemSelected(item);
	 }
}
	public String getPath(Uri uri){
		String res = null;
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = getActivity().getContentResolver().query(uri,projection,null,null,null);
		if(cursor.moveToFirst()){;
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		res=cursor.getString(column_index);
		}
		cursor.close();
		return res;
	 	
	}
	
	
	@Override
	 public void onActivityResult(int requestCode,int resultCode,Intent data){
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == SELECT_PICTURE){
			Uri selectedImageUri = data.getData();
			selectedImagePath = getPath(selectedImageUri);
			Intent i = new Intent(getActivity(),MemeViewActivity.class);
			i.putExtra(MemeViewFragment.NAME_TAG,selectedImagePath );
			startActivityForResult(i,CUSTOM_ID);
		}
		if(requestCode == CUSTOM_ID){
			boolean addMeme = data.getBooleanExtra(MemeViewFragment.MEME_RESULT, false);
			if(addMeme){
				
				Meme newMeme = new Meme();
				newMeme.setName(data.getStringExtra((MemeViewFragment.MEME_NAME)).toLowerCase(Locale.ENGLISH).replace(" ", "_"));
				MemeLab.get(getActivity()).getCustomMemes().add(newMeme);
				
			}
		}
		if(requestCode == FAVORITE_REQUEST){
			
			Toast.makeText(getActivity(),"just have enought sence",Toast.LENGTH_LONG).show();
			
			boolean isChecked = data.getBooleanExtra(MemeViewFragment.FAVORITE_RESULT, false);
			Meme meme = ((MemeAdapter)getListAdapter()).getItem(pos);
			
			if(isChecked){
				if(!(MemeLab.get(getActivity()).getFavoriteMemes().contains(meme))){
				
					MemeLab.get(getActivity()).getFavoriteMemes().add(meme);
				}
			}
			if(!isChecked){
				if(MemeLab.get(getActivity()).getFavoriteMemes().contains(meme)){
					
					MemeLab.get(getActivity()).getFavoriteMemes().remove(meme);
				}
			}
		
		
		}
		
		
	}
	
	public void addBitmapToMemeoryCache(String key,Bitmap bitmap){
		if(getBitmapFromMemCache(key)==null){
			mMemoryCache.put(key, bitmap);
		}
	}
	
	public Bitmap getBitmapFromMemCache(String key){
		return mMemoryCache.get(key);
	}
	private class BitmapWorkerTask extends AsyncTask<String,Void,Bitmap>{
		private final WeakReference<ImageView> imageViewReference;
		private String path = null;
		
		public BitmapWorkerTask(ImageView imageView){
			
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			path = params[0];
			Bitmap bitmap = null;
			 if(mMemes.contains(MemeLab.get(getActivity()).getMeme(path))||mMyanmarMemes.contains(MemeLab.get(getActivity()).getMeme(path))){
				 InputStream is = getResources().openRawResource(getResources().getIdentifier(path , "drawable",getActivity().getPackageName()));
				    Bitmap imageBitmap = BitmapFactory.decodeStream(is);

				   
				    imageBitmap = Bitmap.createScaledBitmap(imageBitmap,160 ,160, false);

				    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				    imageBitmap.compress(Bitmap.CompressFormat.PNG, 2, baos);
				    bitmap = imageBitmap;
			 }
			 
			 if (mCustomMemes.contains(MemeLab.get(getActivity()).getMeme(path))){

				 Bitmap imageBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+MemeViewFragment.templateMeme+
						path+".jpg");
				
				 imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 160, 160, false);
				 ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				    imageBitmap.compress(Bitmap.CompressFormat.PNG, 2, baos);
				    bitmap = imageBitmap;}
			addBitmapToMemeoryCache(path, bitmap);
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap){
			if(isCancelled()){
				bitmap = null;
			}
			if(imageViewReference != null && bitmap != null){
				final ImageView imageView = imageViewReference.get();
				final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
				if(this == bitmapWorkerTask && imageView != null){
					imageView.setImageBitmap(bitmap);
				}
			}
			
		}
		
	}
	static class AsyncDrawable extends BitmapDrawable{
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
		public AsyncDrawable(Resources res,Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask){
			super(res,bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}
		
		public BitmapWorkerTask getBitmapWorkerTask(){
			return bitmapWorkerTaskReference.get();
		}
		
	}
	public static boolean cancelPotentialWork(String path, ImageView imageView) {
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final String bitmapData = bitmapWorkerTask.path;
	        // If bitmapData is not yet set or it differs from the new data
	        if (bitmapData == null || !(bitmapData.equals( path))) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncDrawable) {
		           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		           return asyncDrawable.getBitmapWorkerTask();
		       }
		    }
		    return null;
		}
	private class InitDiskCacheTask extends AsyncTask<File, Void, Void>{

		@Override
		protected Void doInBackground(File... params) {
			// TODO Auto-generated method stub
			synchronized (mDiskCacheLock) {
				
			}
			return null;
		}
		
	}
}
