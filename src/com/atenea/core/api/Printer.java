package com.atenea.core.api;

import java.io.File;
import java.util.List;

import com.atenea.core.api.bos.Article;

public interface Printer {

	public List<File> print(List<Article> articles);

}
