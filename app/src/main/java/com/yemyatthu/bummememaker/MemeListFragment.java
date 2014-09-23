package com.yemyatthu.bummememaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

 class MemeListFragment extends ListFragment{
	private EditText txtSearch;
	private Meme mainMeme;
     private ArrayAdapter<Meme> adapter;
	private ArrayList<Meme> mMemes;
	private ArrayList<Meme> mFavoriteMemes;
	private ArrayList<Meme> mMyanmarMemes;
	private ArrayList<Meme> mCustomMemes;
	private static final int SELECT_PICTURE = 6;
	private String selectedImagePath;
	public static final String TAB_ID = "com.yemyatthu.bummememaker.TAB";
	public static final int FAVORITE_REQUEST =10;
	public static final int CUSTOM_ID = 1;
	private int tabPos;
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
		mCustomMemes = MemeLab.get(getActivity()).getCustomMemes();
		tabPos = getArguments().getInt(TAB_ID);
		if(tabPos==0){
			adapter = new MemeAdapter(mMemes);
		setListAdapter(adapter);}
		if(tabPos==2){
			adapter = new MemeAdapter(mFavoriteMemes);
			setListAdapter(adapter);

		}
		if(tabPos==1){
			adapter = new MemeAdapter(mMyanmarMemes);
			setListAdapter(adapter);
		
		}
		if(tabPos==3){
			adapter = new MemeAdapter(mCustomMemes);
			setListAdapter(adapter);}
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		return inflater.inflate(R.layout.list_view, null);
		
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
		Intent i = new Intent(getActivity(),MemeViewPagerActivity.class);
		Meme meme = ((MemeAdapter)getListAdapter()).getItem(position);
		i.putExtra(MemeViewFragment.NAME_TAG,meme.getName());
		i.putExtra(MemeViewPagerActivity.TAB_TAG, tabPos);
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
		
	
		public void notifyDataSetChanged() {
		    super.notifyDataSetChanged();
			
		}
	}
		private void loadBitmap(String path,ImageView view){
		
			if(mMemes.contains(MemeLab.get(getActivity()).getMeme(path))||mMyanmarMemes.contains(MemeLab.get(getActivity()).getMeme(path))){
				Picasso.with(getActivity())
				.load( getResources().getIdentifier(path , "drawable",getActivity().getPackageName()))
				.placeholder(android.R.drawable.picture_frame)
				.resize(160, 160)
				.into(view);
				    
			 }
			 
			 if (mCustomMemes.contains(MemeLab.get(getActivity()).getMeme(path))){
				 File file = null;
					try{
						file = new File(Environment.getExternalStorageDirectory().toString()+MemeViewFragment.
								templateMeme+path+".jpg");
					}catch(Exception e){
						Toast.makeText(getActivity(), "file not found", Toast.LENGTH_LONG).show();
					}
				 Picasso.with(getActivity())
					.load( file)
					.placeholder(android.R.drawable.picture_frame)
					.resize(160, 160)
					.into(view);
			 }
			}
		
	
		
	
	
		
		
			
		
			 
	
@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
	 	super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.meme_view, menu);

		View v = (View) menu.findItem(R.id.search_menu).getActionView();
		 
		txtSearch = ( EditText ) v.findViewById(R.id.search_edit);
		 
        /** Setting an action listener */
        txtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				ArrayList<Meme> temp = new ArrayList<Meme>();
			for(int i =0;i<adapter.getCount();i++){
				if(adapter.getItem(i).getName().contains(s.toString().toLowerCase(Locale.ENGLISH))){
					
					temp.add(adapter.getItem(i));
					
				}
				
			}
			ArrayAdapter<Meme> tempAdapter = new MemeAdapter(temp);
			setListAdapter(tempAdapter);
			
		
			}
		
			
	});
        txtSearch.addOnAttachStateChangeListener(new OnAttachStateChangeListener(){

			@Override
			public void onViewAttachedToWindow(View v) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onViewDetachedFromWindow(View v) {
				// TODO Auto-generated method stub
				txtSearch.setText("");
			}
        	
        });
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
		if(cursor.moveToFirst()){
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
			Intent i = new Intent(getActivity(),MemeViewPagerActivity.class);
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
		
