package com.atenea.core.api;

import java.io.IOException;
import java.util.List;

import com.atenea.core.api.bos.Article;

public interface FeedReader {

	public List<Article> loadRSSData(String filepath) throws IOException;

}
