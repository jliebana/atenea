package com.atenea.components;

import java.util.LinkedList;
import java.util.List;

import com.atenea.core.api.Evaluator;
import com.atenea.core.api.bos.Article;

/**
 * Class responsible of evaluating the articles
 * 
 * @author jliebana
 *
 */
public class EvaluatorImpl implements Evaluator {

	@Override
	public List<Article> evaluate(List<Article> filteredArticles, List<String> likedWords) {
		List<Article> evaluatedArticles = new LinkedList<Article>();
		for (Article current : filteredArticles) {
			current.setScore((float) liked(current.getFilteredContent(), likedWords) / (float) current.getFilteredContent().size());
			evaluatedArticles = addOrderedByScore(current, evaluatedArticles);
		}
		return evaluatedArticles;
	}

	private long liked(List<String> filteredContent, List<String> likedWords) {
		long i = 0;
		for (String current : filteredContent) {
			if (likedWords.contains(current))
				i++;
		}
		return i;
	}

	private List<Article> addOrderedByScore(Article current, List<Article> evaluatedArticles) {
		int i = 0;
		while (i < evaluatedArticles.size() && current.getScore() < evaluatedArticles.get(i).getScore()) {
			i++;
		}
		evaluatedArticles.add(i, current);
		return evaluatedArticles;
	}

}
