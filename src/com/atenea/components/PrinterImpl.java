package com.atenea.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.atenea.core.api.Printer;
import com.atenea.core.api.bos.Article;

/**
 * Class used for printing the articles to an HTML file which later would be shown in a webview
 * @author jliebana
 *
 */
public class PrinterImpl implements Printer {

	@Override
	public List<File> print(List<Article> articles) {
		List<File> files = new ArrayList<File>();
		for (int i = 0 ; i < articles.size() ; i++) {
			Article article = articles.get(i);
			File file = new File(System.getProperty("user.dir")+System.getProperty("file.separator")
					+"articles"+System.getProperty("file.separator")+i+".html");
			files.add(file);
			try {
				file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write("<html><header></header><body><h1>"+article.getTitle()+" - "+article.getUri()+"</h1></br>"+article.getContent()+"</body></html>");
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return files;
	}

}
