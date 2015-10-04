package com.atenea.core.api;

import java.util.List;

import com.atenea.core.api.bos.Article;

public interface Evaluator {

	public List<Article> evaluate(List<Article> filteredArticles, List<String> likedWords);

}
