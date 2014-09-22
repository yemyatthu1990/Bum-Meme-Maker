package com.yemyatthu.bummememaker;

import android.annotation.SuppressLint;
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
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

@SuppressLint("NewApi")
public class MemeViewFragment extends Fragment {
  private Typeface type;
  private String path;
  private String addName;
  private String saveName;
  private String memeName;
  private int fontColor;
  private int borderColor;
  private int shadowColor;
  private View memeContainer;
  private CheckBox favoriteCheckBox;
  private Button addTemplate;
  private Button makeMeme;
  private Button fontPlus1;
  private Button fontPlus2;
  private Button fontMinus1;
  private Button fontMinus2;
  private EditText topEdit;
  private int topViewSize;
  private int bottomViewSize;
  private EditText bottomEdit;
  private TextView waterMark;
  private TextView topView;
  private TextView bottomView;
  ;
  private ImageView memeView;
  private Bitmap bitmap;
  private ArrayList<Integer> restoredSize = new ArrayList<Integer>();
  private ArrayList<String> restoredText = new ArrayList<String>();
  private String topEditText, bottomEditText, topViewText, bottomViewText = null;
  private String selectedImagePath;
  private static final String ID_TAG = "com.yemyatthu.mememaker.SAVED_STATE";
  private static final String CONFIRM = "CONFIRM";
  public static final int REQUEST_CODE1 = 1;
  public static final int REQUEST_CODE2 = 2;
  public static final int REQUEST_CODE3 = 3;
  public static final int REQUEST_CODE4 = 4;
  public static final int REQUEST_CODE5 = 5;
  public static final int CUSTOM_ID = 10;
  public static final String NAME_TAG = "com.yemyatthu.mememaker.NAME";
  private static final int SELECT_PICTURE = 6;
  public static final String FAVORITE_RESULT = "com.yemyatthu.bummememaker.FAVORITE";
  public static final String MEME_RESULT = "com.yemyatthu.bummememaker.MEME";
  public static final String MEME_NAME = "com.yemyatthu.bummememaker.MEMENAME";
  public static final String finishedMeme = "/memesimages/";
  public static final String templateMeme = "/memestemplates/";
  private static final String selctedImagePath = null;


  private SharedPreferences prefs;

  public static MemeViewFragment getNewInstance(String memeName) {
    Bundle args = new Bundle();
    args.putSerializable(NAME_TAG, memeName);
    MemeViewFragment memeViewFragment = new MemeViewFragment();
    memeViewFragment.setArguments(args);
    return memeViewFragment;
  }


  @Override
  public void onResume() {
    super.onResume();
    type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/"+prefs.getString("font","Impact")+".ttf"); // This need to be fix
    switch (Integer.parseInt(prefs.getString("fontColor", "1"))) {
      case 1:
        fontColor = Color.WHITE;
        break;
      case 2:
        fontColor = Color.BLACK;
        break;
      case 3:
        fontColor = Color.RED;
        break;
      case 4:
        fontColor = Color.GRAY;
        break;
      case 5:
        fontColor = Color.GREEN;
        break;
      case 6:
        fontColor = Color.YELLOW;
        break;
      case 7:
        fontColor = Color.BLUE;
        break;
      default:
        fontColor = Color.WHITE;
    }

    topView.setTextColor(fontColor);
    topView.setTypeface(type);
    bottomView.setTextColor(fontColor);
    bottomView.setTypeface(type);
    switch (Integer.parseInt(prefs.getString("borderColor", "2"))) {
      case 1:
        borderColor = Color.WHITE;
        break;
      case 2:
        borderColor = Color.BLACK;
        break;
      case 3:
        borderColor = Color.RED;
        break;
      case 4:
        borderColor = Color.GRAY;
        break;
      case 5:
        borderColor = Color.GREEN;
        break;
      case 6:
        borderColor = Color.YELLOW;
        break;
      case 7:
        borderColor = Color.BLUE;
        break;
      default:
        borderColor = Color.BLACK;
    }
    memeContainer.setBackgroundColor(borderColor);

    switch (Integer.parseInt(prefs.getString("shadowColor", "2"))) {
      case 1:
        shadowColor = Color.WHITE;
        break;
      case 2:
        shadowColor = Color.BLACK;
        break;
      case 3:
        shadowColor = Color.RED;
        break;
      case 4:
        shadowColor = Color.GRAY;
        break;
      case 5:
        shadowColor = Color.GREEN;
        break;
      case 6:
        shadowColor = Color.YELLOW;
        break;
      case 7:
        shadowColor = Color.BLUE;
        break;
      default:
        shadowColor = Color.BLACK;
    }
    topView.setShadowLayer((float) 20, 0, 0, shadowColor);
    bottomView.setShadowLayer((float) 20, 0, 0, shadowColor);

    if (prefs.getBoolean("capCheckBox", true)) {
      topView.setAllCaps(true);
    }
    if (!(prefs.getBoolean("capCheckBox", true))) {
      topView.setAllCaps(false);
    }

    if (prefs.getBoolean("borderBarCheckBox", false)) {
      int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Integer.parseInt(prefs.getString("borderSize", "80")), getResources().getDisplayMetrics());
      int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
      memeView.setPadding(width, height, width, height);
      RelativeLayout.LayoutParams paramsTop = (RelativeLayout.LayoutParams) topView.getLayoutParams();
      paramsTop.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      topView.setLayoutParams(paramsTop);
      RelativeLayout.LayoutParams paramsBottom = (RelativeLayout.LayoutParams) bottomView.getLayoutParams();
      paramsBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      bottomView.setLayoutParams(paramsBottom);
    }
    if (!prefs.getBoolean("borderBarCheckBox", false)) {
      int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
      memeView.setPadding(width, width, width, width);
      RelativeLayout.LayoutParams paramsTop = (RelativeLayout.LayoutParams) topView.getLayoutParams();
      paramsTop.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
      topView.setLayoutParams(paramsTop);
      RelativeLayout.LayoutParams paramsBottom = (RelativeLayout.LayoutParams) bottomView.getLayoutParams();
      paramsBottom.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      bottomView.setLayoutParams(paramsBottom);
    }
    if (prefs.getBoolean("waterMarkCheckBox", false)) {
      waterMark.setVisibility(View.VISIBLE);
      waterMark.setText(prefs.getString("waterMarkEditText", "BUM"));
      waterMark.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(prefs.getString("waterMarkSize", "50")));
    }
    if (!prefs.getBoolean("waterMarkCheckBox", false)) {
      waterMark.setVisibility(View.INVISIBLE);
    }


  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    selectedImagePath = getArguments().getString(NAME_TAG);
    if (((MemeLab.get(getActivity())).getMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
        ((MemeLab.get(getActivity())).getMyanmarMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath))) {
      memeName = (getArguments().getString(NAME_TAG)).substring(0, 1).toUpperCase(Locale.ENGLISH);
      memeName += (getArguments().getString(NAME_TAG)).substring(1).replace("_", " ").toLowerCase(Locale.ENGLISH);
    } else {
      File file = new File(selectedImagePath);
      memeName = file.getName().substring(0, 1).toUpperCase(Locale.ENGLISH);
      memeName += file.getName().substring(1).toLowerCase(Locale.ENGLISH);

    }

    getActivity().setTitle(memeName);


    prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    topViewSize = 25;
    bottomViewSize = 25;
    if (savedInstanceState != null) {
      topViewSize = restoredSize.get(0);
      bottomViewSize = restoredSize.get(1);
      topEditText = restoredText.get(0);
      bottomEditText = restoredText.get(1);
      topViewText = restoredText.get(2);
      bottomViewText = restoredText.get(3);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
    final View v = inflater.inflate(R.layout.fragment_meme_view, root, false);
    memeView = (ImageView) v.findViewById(R.id.meme_view);

    if (((MemeLab.get(getActivity())).getMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
        ((MemeLab.get(getActivity())).getMyanmarMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath))) {
      Bitmap bitmapImage = BitmapFactory.decodeResource(getActivity().getResources(), getActivity().getResources().getIdentifier(selectedImagePath, "drawable", getActivity().getPackageName()));
      memeView.setImageBitmap(bitmapImage);
    } else {
      if (MemeLab.get(getActivity()).getCustomMemes().contains(MemeLab.get(getActivity()).getMeme(selectedImagePath))) {
        Bitmap bitmapImage = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + MemeViewFragment.templateMeme +
            selectedImagePath + ".jpg");
        memeView.setImageBitmap(bitmapImage);
      } else {
        Bitmap bitmapImage = BitmapFactory.decodeFile(selectedImagePath);
        memeView.setImageBitmap(bitmapImage);
      }


    }


    waterMark = (TextView) v.findViewById(R.id.waterMarkTextView);

    waterMark.setVisibility(View.INVISIBLE);
    topView = (TextView) v.findViewById(R.id.top_text);

    topView.setText(topViewText);
    topView.setTextSize(TypedValue.COMPLEX_UNIT_SP, topViewSize);

    bottomView = (TextView) v.findViewById(R.id.bottom_text);
    bottomView.setTypeface(type);
    bottomView.setText(bottomViewText);
    bottomView.setTextSize(TypedValue.COMPLEX_UNIT_SP, bottomViewSize);
    topEdit = (EditText) v.findViewById(R.id.top_edit_text);
    topEdit.setText(topEditText);
    topEdit.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
        // TODO: Implement this method
      }

      @Override
      public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
        topView.setText(p1);
      }

      @Override
      public void afterTextChanged(Editable p1) {
        // TODO: Implement this method
      }


    });
    bottomEdit = (EditText) v.findViewById(R.id.bottom_edit_text);
    bottomEdit.setText(bottomEditText);
    bottomEdit.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
        // TODO: Implement this method
      }

      @Override
      public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
        bottomView.setText(p1);
      }

      @Override
      public void afterTextChanged(Editable p1) {
        // TODO: Implement this method
      }


    });

    favoriteCheckBox = (CheckBox) v.findViewById(R.id.favorite_checkBox);
    if (!(((MemeLab.get(getActivity())).getMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
        ((MemeLab.get(getActivity())).getMyanmarMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
        (MemeLab.get(getActivity()).getCustomMemes().contains(MemeLab.get(getActivity()).getMeme(selectedImagePath))))) {
      favoriteCheckBox.setEnabled(false);
    }

    favoriteCheckBox.setChecked(MemeLab.get(getActivity()).getFavoriteMemes().contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)));
    ;
    favoriteCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        // TODO Auto-generated method stub
        if (((MemeLab.get(getActivity())).getMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
            ((MemeLab.get(getActivity())).getMyanmarMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
            (MemeLab.get(getActivity()).getCustomMemes().contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)))) {
          if (isChecked) {
            Toast.makeText(getActivity(), "added to Favorites", Toast.LENGTH_SHORT).show();
            sendResult(FAVORITE_RESULT, true);
          }
          if (!isChecked) {
            Toast.makeText(getActivity(), "removed from Favorites", Toast.LENGTH_SHORT).show();
            sendResult(FAVORITE_RESULT, false);
          }
        } else {
          Toast.makeText(getActivity(), "cannot add Custom images to Favorites", Toast.LENGTH_SHORT).show();
        }

      }
    });

    addTemplate = (Button) v.findViewById(R.id.add_template);
    if (((MemeLab.get(getActivity())).getMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
        ((MemeLab.get(getActivity())).getMyanmarMemes()).contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)) ||
        (MemeLab.get(getActivity()).getCustomMemes().contains(MemeLab.get(getActivity()).getMeme(selectedImagePath)))) {
      addTemplate.setEnabled(false);
    }
    addTemplate.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        FragmentManager fm = getFragmentManager();
        ConfirmDialogFragment cfDialog = new ConfirmDialogFragment(R.string.add_confirm, R.string.save_title);
        cfDialog.setTargetFragment(MemeViewFragment.this, REQUEST_CODE4);
        cfDialog.show(fm, CONFIRM);

      }

    });
    memeContainer = v.findViewById(R.id.memeView_container);
    makeMeme = (Button) v.findViewById(R.id.make_meme);
    makeMeme.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View p1) {
        FragmentManager fm = getFragmentManager();
        ConfirmDialogFragment cfDialog = new ConfirmDialogFragment(R.string.save_confirm, R.id.save_name_edit);
        cfDialog.setTargetFragment(MemeViewFragment.this, REQUEST_CODE1);
        cfDialog.show(fm, CONFIRM);

      }


    });
    fontPlus1 = (Button) v.findViewById(R.id.font_plus1);
    fontPlus1.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        topViewSize += 1;
        topView.setTextSize(topViewSize);

      }

    });

    fontPlus2 = (Button) v.findViewById(R.id.font_plus2);
    fontPlus2.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        bottomViewSize += 1;
        bottomView.setTextSize(bottomViewSize);

      }

    });

    fontMinus1 = (Button) v.findViewById(R.id.font_minus1);
    fontMinus1.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        topViewSize -= 1;
        topView.setTextSize(topViewSize);

      }

    });

    fontMinus2 = (Button) v.findViewById(R.id.font_minus2);
    fontMinus2.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        bottomViewSize -= 1;
        bottomView.setTextSize(bottomViewSize);
      }

    });


    return v;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    restoredSize.add(topViewSize);
    restoredSize.add(bottomViewSize);
    restoredText.add(topEdit.getText().toString());
    restoredText.add(bottomEdit.getText().toString());
    restoredText.add(topView.getText().toString());
    restoredText.add(bottomView.getText().toString());
    outState.putStringArrayList(ID_TAG, restoredText);
    outState.putIntegerArrayList(ID_TAG, restoredSize);
  }

  public void addName() {
    FragmentManager fm = getFragmentManager();
    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_save_name, null);
    EditText edText = (EditText) view.findViewById(R.id.save_name_edit);
    edText.setText(R.string.save_custom_meme);
    addName = getResources().getString(R.string.save_custom_meme);
    SaveNameDialogFragment snDialog = new SaveNameDialogFragment(view);
    snDialog.setTargetFragment(MemeViewFragment.this, REQUEST_CODE5);

    snDialog.show(fm, CONFIRM);
    edText.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        addName = s.toString();
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


  public void getName() {
    FragmentManager fm = getFragmentManager();
    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_save_name, null);
    EditText edText = (EditText) view.findViewById(R.id.save_name_edit);
    if (MemeLab.get(getActivity()).getMemes().contains((MemeLab.get(getActivity()).getMeme(selectedImagePath)))) {
      edText.setText(selectedImagePath);
      saveName = selectedImagePath;
    } else {
      edText.setText(R.string.save_custom_meme);
      saveName = getResources().getString(R.string.save_custom_meme);
    }
    SaveNameDialogFragment snDialog = new SaveNameDialogFragment(view);
    snDialog.show(fm, CONFIRM);
    snDialog.setTargetFragment(MemeViewFragment.this, REQUEST_CODE3);
    edText.addTextChangedListener(new TextWatcher() {

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


  public File getFile(View image, String dir, String name) {


    bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bitmap);
    image.draw(c);

    String root = Environment.getExternalStorageDirectory().toString();
    File newDir = new File(root + dir);
    newDir.mkdirs();

    File file = new File(newDir, name + ".jpg");
    return file;
  }

  public void checkName(File file) {
    if (file.exists()) {
      FragmentManager fm = getFragmentManager();
      ConfirmDialogFragment cfDialog = new ConfirmDialogFragment(R.string.name_exist, R.string.save_title);
      cfDialog.setTargetFragment(MemeViewFragment.this, REQUEST_CODE2);
      cfDialog.show(fm, CONFIRM);
    } else
      saveFile(file);

  }

  public void addFile(File file) {
    try {
      FileOutputStream out = new FileOutputStream(file);

      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
      out.flush();
      out.close();
      Toast.makeText(getActivity(), "Added as " + file.getPath(), Toast.LENGTH_LONG).show();
    } catch (Exception e) {
      Toast.makeText(getActivity(), "Exception catch " + file.getPath(), Toast.LENGTH_LONG).show();

    }
  }

  public void saveFile(File file) {
    try {
      FileOutputStream out = new FileOutputStream(file);

      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
      out.flush();
      out.close();
      Uri uri = Uri.fromFile(file);
      Intent i = new Intent();
      i.setAction(Intent.ACTION_VIEW);
      i.setDataAndType(uri, "image/*");
      startActivity(i);
      Toast.makeText(getActivity(), "Saved as " + file.getPath(), Toast.LENGTH_LONG).show();
      MediaScannerConnection.scanFile(getActivity(),
          new String[]{file.toString()}, null,
          new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
            }
          });
    } catch (Exception e) {

    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) return;
    if (requestCode == REQUEST_CODE1) {
      getName();
    }
    if (requestCode == REQUEST_CODE3) {
      File file = getFile(memeContainer, finishedMeme, saveName);
      checkName(file);
    }
    if (requestCode == REQUEST_CODE2) {
      File file = getFile(memeContainer, finishedMeme, saveName);
      saveFile(file);
    }
    if (requestCode == REQUEST_CODE4) {
      addName();

    }
    if (requestCode == REQUEST_CODE5) {

      File file = getFile(memeView, templateMeme, addName);
      addFile(file);
      sendResult(MEME_RESULT, true);

    }
    if (requestCode == SELECT_PICTURE) {

      Uri selectedImageUri = data.getData();
      path = getPath(selectedImageUri);
      Intent i = new Intent(getActivity(), MemeViewActivity.class);
      i.putExtra(NAME_TAG, path);
      startActivityForResult(i, CUSTOM_ID);
    }

    if (requestCode == CUSTOM_ID) {
      sendResult(MEME_RESULT, true);
    }
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Inflate the menu; this adds items to the action bar if it is present.
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.meme_view, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.settings_menu:
        Intent i = new Intent(getActivity(), SettingsActivity.class);
        startActivity(i);
        return true;
      case R.id.about_menu:
        Intent d = new Intent(getActivity(), AboutViewActivity.class);
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

  public String getPath(Uri uri) {
    String res = null;
    String[] projection = {MediaStore.Images.Media.DATA};
    Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
    if (cursor.moveToFirst()) {
      ;
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      res = cursor.getString(column_index);
    }
    cursor.close();
    return res;

  }


  public void sendResult(String tag, boolean result) {
    Intent i = new Intent();

    i.putExtra(tag, result);
    if (tag.equals(MEME_RESULT)) {
      i.putExtra(MEME_NAME, addName);
    }
    getActivity().setResult(Activity.RESULT_OK, i);
  }


}
