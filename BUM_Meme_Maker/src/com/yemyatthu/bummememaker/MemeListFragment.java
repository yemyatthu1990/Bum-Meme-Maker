package com.yemyatthu.bummememaker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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

 class MemeListFragment extends ListFragment {
	private TextView mMemeNameView;
	private ImageView mMemeThumbnail;
	private ArrayList<Meme> mMemes;
	private ArrayList<Meme> mFavoriteMemes;
	private ArrayList<Meme> mMyanmarMemes;
	private static final int SELECT_PICTURE = 5;
	private String selectedImagePath;
	public static final String IMAGE_PATH = "com.yemyatthu.mememaker.IMAGE";
	public static final String TAB_ID = "com.yemyatthu.bummememaker.TAB";
	public static final int FAVORITE_REQUEST =10;
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
		mMemes = MemeLab.get(getActivity()).getMemes();
		mFavoriteMemes = MemeLab.get(getActivity()).getFavoriteMemes();
		mMyanmarMemes = MemeLab.get(getActivity()).getMyanmarMemes();
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
	
	
	}
	
	@Override 
	public void onPause(){
		super.onPause();
		try {
			MemeLab.get(getActivity()).saveFavoriteMemes(MemeLab.get(getActivity()).getFavoriteMemes());
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
			if (convertView == null){
				
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_meme_list, null);
			}
			Meme meme = getItem(position);
			mMemeNameView = (TextView)convertView.findViewById(R.id.meme_name_view);
			mMemeNameView.setText((meme.getName()).replace("_", " ").toUpperCase(Locale.ENGLISH));

			mMemeThumbnail = (ImageView)convertView.findViewById(R.id.thumbnail_view);
		
			makeThumbnail(mMemeThumbnail,meme.getName());
					
			
			return convertView;
		}
	
		private void makeThumbnail(ImageView img,String name){
			 byte[] imageData = null;

			    try 
			    {
			    InputStream is = getResources().openRawResource(getResources().getIdentifier(name , "drawable",getActivity().getPackageName()));
			    Bitmap imageBitmap = BitmapFactory.decodeStream(is);

			   
			    imageBitmap = Bitmap.createScaledBitmap(imageBitmap,80 ,80, false);

			    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			    imageBitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
			    imageData = baos.toByteArray();
			    img.setImageBitmap(imageBitmap);
			    }
			    catch(Exception ex) {

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
			startActivity(i);
		}
		if(requestCode == FAVORITE_REQUEST){
			
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

}
