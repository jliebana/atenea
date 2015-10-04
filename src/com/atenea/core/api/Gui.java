package com.atenea.core.api;

import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.atenea.core.api.bos.Article;

public interface Gui {

	Display display = null;
	Shell mainShell = null;
	Shell loadingShell = null;
	
	void show(List<Article> articles, List<String> likedWords, String path);
	void updateLoadingBar(int state, String message);
	void createLoadingGuiImpl();
	void deleteLoading();
}
