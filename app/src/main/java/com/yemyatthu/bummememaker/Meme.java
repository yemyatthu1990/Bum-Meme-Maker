package com.yemyatthu.bummememaker;

public class Meme {
	private String mName,mHelp;
	private String mTopExample;
	private String mBottomExample;
	
	public String getTopExample() {
		return mTopExample;
	}

	public void setTopExample(String topExample) {
		
		mTopExample = topExample;
	}
	public String getBottomExample() {
		return mBottomExample;
	}

	public void setBottomExample(String bottomExample) {
		
		mBottomExample = bottomExample;
	}


	public Meme(){
		mTopExample = "No Example Found";
		mBottomExample = "Please Contribute To The Project";
		mHelp = "No explanation found. Wanna contribute to the project? Email an explanation of this meme to thu.yemyat@gmail.com";
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getHelp() {
		return mHelp;
	}

	public void setHelp(String help) {
		mHelp = help;
	}

}
