package com.atenea.core.api;

import java.io.IOException;
import java.util.List;

import com.atenea.core.api.bos.Article;

public interface Filter {

	public List<Article> filterArticles(List<Article> articles);

	public void loadConfig(String path) throws IOException;

}
