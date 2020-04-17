package com.HTT.ViewUtil;

import java.awt.Color;
import java.awt.Font;

public interface IMecView {
	Font topicFont = new Font("宋体", Font.BOLD, 30);
	Font normalFont = new Font("宋体", Font.PLAIN, 16);
	Font smallFont = new Font("宋体", Font.PLAIN, 14);
	Font XSmallFont = new Font("宋体", Font.PLAIN, 12);
	
	Color topicColor = new Color(21,3,155);
	
	int normalSize = normalFont.getSize();
	
	int PADDING = 5;
	
	default void initView() {
		init();
		reinit();
		delAction();
	}
	
	void init();
	void reinit();
	void delAction();
	void showView();
	void closeView();
	
}
