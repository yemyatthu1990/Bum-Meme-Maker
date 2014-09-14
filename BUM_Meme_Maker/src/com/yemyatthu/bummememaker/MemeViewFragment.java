package com.yemyatthu.bummememaker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class MemeViewFragment extends Fragment{	
	private String saveName;
	private int fontColor;
	private int borderColor;
	private int shadowColor;
	private View memeContainer;
	private CheckBox favoriteCheckBox;
	private Button exampleMeme;
	private Button helpMeme;
	private Button makeMeme;
	private Button fontPlus1;
	private Button fontPlus2;
	private Button fontMinus1;
	private Button fontMinus2;
	private EditText topEdit;
	private int topViewSize;
	private int bottomViewSize;
	private EditText bottomEdit;
	private TextView topView;
	private TextView bottomView;;
	private ImageView memeView;
	private Bitmap bitmap;
	private ArrayList<Integer> restoredSize = new ArrayList<Integer>();
	private ArrayList<String> restoredText = new ArrayList<String>();
	private String topEditText,bottomEditText,topViewText,bottomViewText=null;
	private String selectedImagePath;
	private static final String ID_TAG = "com.yemyatthu.mememaker.SAVED_STATE";
	private static final String CONFIRM = "CONFIRM";
	public static final int REQUEST_CODE1 = 1;
	public static final int REQUEST_CODE2 = 2;
	public static final int REQUEST_CODE3 = 3;
	public static final String NAME_TAG = "com.yemyatthu.mememaker.NAME";
	private static final int SELECT_PICTURE = 5;
	public static final String FAVORITE_RESULT = "com.yemyatthu.bummememaker.FAVORITE";
	private static final Object selctedImagePath = null;
	
	
	private SharedPreferences prefs;
	
	public static MemeViewFragment getNewInstance(String memeName){
		Bundle args = new Bundle();
		args.putSerializable(NAME_TAG, memeName);
		MemeViewFragment memeViewFragment = new MemeViewFragment();
		memeViewFragment.setArguments(args);
		return memeViewFragment;}
	

	@Override
	public void onResume(){
		super.onResume();
		switch(Integer.parseInt(prefs.getString("fontColor", "1"))){
		case 1: fontColor= Color.WHITE;
			break;
		case 2: fontColor = Color.BLACK;
			break;
		case 3: fontColor = Color.RED;
			break;
		case 4: fontColor = Color.GRAY;
			break;
		case 5: fontColor = Color.GREEN;
			break;
		case 6: fontColor = Color.YELLOW;
			break;
		case 7: fontColor = Color.BLUE;
			break;
		default:
			fontColor = Color.WHITE;
		}
		
		topView.setTextColor(fontColor);
		bottomView.setTextColor(fontColor);
		
		switch(Integer.parseInt(prefs.getString("borderColor", "1"))){
		case 1: borderColor= Color.WHITE;
			break;
		case 2: borderColor = Color.BLACK;
			break;
		case 3: borderColor = Color.RED;
			break;
		case 4: borderColor = Color.GRAY;
			break;
		case 5: borderColor = Color.GREEN;
			break;
		case 6: borderColor = Color.YELLOW;
			break;
		case 7: borderColor = Color.BLUE;
			break;
		default:
			borderColor = Color.WHITE;
		}
		memeContainer.setBackgroundColor(borderColor);
		
		switch(Integer.parseInt(prefs.getString("shadowColor", "2"))){
		case 1: shadowColor= Color.WHITE;
			break;
		case 2: shadowColor = Color.BLACK;
			break;
		case 3: shadowColor = Color.RED;
			break;
		case 4: shadowColor = Color.GRAY;
			break;
		case 5: shadowColor = Color.GREEN;
			break;
		case 6: shadowColor = Color.YELLOW;
			break;
		case 7: shadowColor = Color.BLUE;
			break;
		default:
			shadowColor = Color.BLACK;
		}
		topView.setShadowLayer((float) 0.01, 2, 2,shadowColor);
		bottomView.setShadowLayer((float) 0.01, 2, 2,shadowColor);
		
		if(prefs.getBoolean("capCheckBox", true)){
			topView.setAllCaps(true);
		}
		if(!(prefs.getBoolean("capCheckBox", true))){
			topView.setAllCaps(false); 
		}
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(getArguments().getString(NAME_TAG));
		
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		topViewSize = 25;
		bottomViewSize = 25;
		if(savedInstanceState != null){
			topViewSize = restoredSize.get(0);
			bottomViewSize = restoredSize.get(1);
			topEditText = restoredText.get(0);
			bottomEditText=restoredText.get(1);
			topViewText=restoredText.get(2);
			bottomViewText=restoredText.get(3);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup root,Bundle savedInstanceState){
		final View v = inflater.inflate(R.layout.fragment_meme_view,root,false);
		selectedImagePath = getArguments().getString(NAME_TAG);
		memeView = (ImageView)v.findViewById(R.id.meme_view);
		registerForContextMenu(memeView);
		if(((MemeLab.get(getActivity())).getMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath))){
			Bitmap bitmapImage= BitmapFactory.decodeResource(getActivity().getResources(), getActivity().getResources().getIdentifier(selectedImagePath, "drawable", getActivity().getPackageName()));
			memeView.setImageBitmap(bitmapImage);
		}
		else{
			Bitmap bitmapImage = BitmapFactory.decodeFile(selectedImagePath);
			memeView.setImageBitmap(bitmapImage);
			
		}
		
		
		topView = (TextView)v.findViewById(R.id.top_text);
		Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/impact.ttf"); 
		topView.setTypeface(type);
		topView.setText(topViewText);
		topView.setTextSize(TypedValue.COMPLEX_UNIT_SP ,topViewSize);
		bottomView=(TextView)v.findViewById(R.id.bottom_text);		
		bottomView.setTypeface(type);
		bottomView.setText(bottomViewText);
		bottomView.setTextSize(TypedValue.COMPLEX_UNIT_SP,bottomViewSize);
		topEdit = (EditText)v.findViewById(R.id.top_edit_text);
		topEdit.setText(topEditText);
		topEdit.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					topView.setText(p1);
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					// TODO: Implement this method
				}


			}	);
		bottomEdit = (EditText)v.findViewById(R.id.bottom_edit_text);
		bottomEdit.setText(bottomEditText);
		bottomEdit.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					bottomView.setText(p1);
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					// TODO: Implement this method
				}


			});
		helpMeme = (Button)v.findViewById(R.id.help_meme);
		helpMeme.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),HelpMemeActivity.class);
				i.putExtra(HelpMemeFragment.HELP_TAG, getArguments().getString(NAME_TAG));
				startActivity(i);
			}
			
		});
		exampleMeme = (Button)v.findViewById(R.id.example_meme);
		exampleMeme.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				String topText = MemeLab.get(getActivity()).getMeme(selectedImagePath).getTopExample();
				topView.setText(topText);
				String bottomText = MemeLab.get(getActivity()).getMeme(selectedImagePath).getBottomExample();
				bottomView.setText(bottomText);}
				catch(NullPointerException e){
					topView.setText(R.string.top_custom_example);
					bottomView.setText("");
				}
			}
			
		});		
		
		
		favoriteCheckBox = (CheckBox)v.findViewById(R.id.favorite_checkBox);
		favoriteCheckBox.setChecked(MemeLab.get(getActivity()).getFavoriteMemes().contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)));;
		favoriteCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				// TODO Auto-generated method stub
				if(MemeLab.get(getActivity()).getMemes().contains((MemeLab.get(getActivity()).getMeme(selectedImagePath)))){
					if(isChecked){
						Toast.makeText(getActivity(), "added to Favorites", Toast.LENGTH_SHORT).show();
						sendResult(true);
					}
					if(!isChecked){
						Toast.makeText(getActivity(), "removed from Favorites", Toast.LENGTH_SHORT).show();
						sendResult(false);
					}
				}
				else{
					Toast.makeText(getActivity(), "cannot add Custom images to Favorites", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		memeContainer=v.findViewById(R.id.memeView_container);
		makeMeme = (Button)v.findViewById(R.id.make_meme);
		makeMeme.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					FragmentManager fm = getFragmentManager();
					ConfirmDialogFragment cfDialog = new ConfirmDialogFragment(R.string.save_confirm);
					cfDialog.setTargetFragment(MemeViewFragment.this, REQUEST_CODE1);
					cfDialog.show(fm, CONFIRM);
					
				}

			
		});
		fontPlus1=(Button)v.findViewById(R.id.font_plus1);
		fontPlus1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				topViewSize +=1;
				topView.setTextSize(topViewSize);
				
			}
			
		});
		
		fontPlus2=(Button)v.findViewById(R.id.font_plus2);
		fontPlus2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				bottomViewSize += 1;
				bottomView.setTextSize (bottomViewSize);
				
			}
			
		});
		
		fontMinus1=(Button)v.findViewById(R.id.font_minus1);
		fontMinus1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				topViewSize -= 1;
				topView.setTextSize(topViewSize);
				
			}
			
		});
		
		fontMinus2=(Button)v.findViewById(R.id.font_minus2);
		fontMinus2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				bottomViewSize -= 1;
				bottomView.setTextSize(bottomViewSize);
			}
			
		});
	

			
		return v;
	}
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		restoredSize.add(topViewSize);
		restoredSize.add(bottomViewSize);
		restoredText.add(topEdit.getText().toString());
		restoredText.add(bottomEdit.getText().toString());
		restoredText.add(topView.getText().toString());
		restoredText.add(bottomView.getText().toString());
		outState.putStringArrayList(ID_TAG,restoredText);
		outState.putIntegerArrayList(ID_TAG,restoredSize);
	}
	
	
	public void getName(){
		FragmentManager fm = getFragmentManager();
		View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_save_name, null);
		EditText edText = (EditText)view.findViewById(R.id.save_name_edit);
		if(MemeLab.get(getActivity()).getMemes().contains((MemeLab.get(getActivity()).getMeme(selectedImagePath)))){
		edText.setText(selectedImagePath);
		saveName = selectedImagePath;
		}
		else{
			edText.setText(R.string.save_custom_meme);
			saveName = getResources().getString(R.string.save_custom_meme);
		}
		SaveNameDialogFragment snDialog = new SaveNameDialogFragment(view);
		snDialog.show(fm,CONFIRM);
		snDialog.setTargetFragment(MemeViewFragment.this, REQUEST_CODE3);
		edText.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				saveName = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	public File getFile(View image){
		

		bitmap = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		image.draw(c);
		
		String root = Environment.getExternalStorageDirectory().toString();
		File newDir = new File(root + "/memesimages/");
		newDir.mkdirs();

		File file=new File(newDir,saveName+".jpg");
		return file;
	}
	
	public void checkName(File file){
		if(file.exists()){
			FragmentManager fm = getFragmentManager(); 
			ConfirmDialogFragment cfDialog = new ConfirmDialogFragment(R.string.name_exist);
			cfDialog.setTargetFragment(MemeViewFragment.this,REQUEST_CODE2 );
			cfDialog.show(fm, CONFIRM);
		}
		else
			saveFile(file);

	}
	public void saveFile(File file){
			try{
				FileOutputStream out = new FileOutputStream(file);
				
				bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
				out.flush();
				out.close();
				Uri uri = Uri.fromFile(file);
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.setDataAndType(uri,"image/*");
				startActivity(i);
				Toast.makeText(getActivity(),"Saved as " + file.getPath(),Toast.LENGTH_LONG).show();}
				catch(Exception e){
					
				}
			}
	@Override
	 public void onActivityResult(int requestCode,int resultCode,Intent data){
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_CODE1){
			getName();
		}
		if (requestCode == REQUEST_CODE3){
			File file = getFile(memeContainer);
			checkName(file);
		}
		if (requestCode == REQUEST_CODE2){
			File file= getFile(memeContainer);
			saveFile(file);
		}
		if (requestCode == SELECT_PICTURE){
			Uri selectedImageUri = data.getData();
			String path = getPath(selectedImageUri);
			Intent i = new Intent(getActivity(),MemeViewActivity.class);
			i.putExtra(NAME_TAG, path);
			startActivity(i);}
		
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

	 
	 public void sendResult(boolean result){
		 Intent i = new Intent();
		 i.putExtra(FAVORITE_RESULT, result);
		 getActivity().setResult(Activity.RESULT_OK, i);
	 }
	
}
