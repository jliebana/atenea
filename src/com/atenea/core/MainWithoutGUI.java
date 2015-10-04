package com.atenea.core;

import java.io.IOException;
import java.util.List;

import com.atenea.components.EvaluatorImpl;
import com.atenea.components.FeedReaderImpl;
import com.atenea.components.FileLoaderImpl;
import com.atenea.components.FilterImpl;
import com.atenea.components.GuiImpl;
import com.atenea.components.PrinterImpl;
import com.atenea.core.api.Evaluator;
import com.atenea.core.api.FeedReader;
import com.atenea.core.api.FileLoader;
import com.atenea.core.api.Filter;
import com.atenea.core.api.Gui;
import com.atenea.core.api.Printer;
import com.atenea.core.api.bos.Article;

/**
 * Loader class without GUI
 * @author jliebana
 *
 */
public class MainWithoutGUI {

	public static void main(String[] args) {

		FeedReader feed = new FeedReaderImpl();
		Filter filter = new FilterImpl();
		Evaluator evaluator = new EvaluatorImpl();
		Printer printer = new PrinterImpl();
		FileLoader loader = new FileLoaderImpl();
		final Gui gui = new GuiImpl();

		List<String> likedWords = null;
		List<Article> articles;
		// Start loading screen
		gui.createLoadingGuiImpl();
		
		// This part must be improved
		try {
			gui.updateLoadingBar(10, "Cargando stopwords...");
			filter.loadConfig(System.getProperty("user.dir") + System.getProperty("file.separator") + "stop.txt");
			gui.updateLoadingBar(20, "Cargando palabras gustadas...");
			likedWords = loader.loadLikedWords(System.getProperty("user.dir") + System.getProperty("file.separator") + "liked.txt");
			gui.updateLoadingBar(30, "Descargando feeds (esto puede tardar)...");
			articles = feed.loadRSSData(System.getProperty("user.dir") + System.getProperty("file.separator") + "feeds.txt");
			gui.updateLoadingBar(80, "Filtrando artículos...");
			filter.filterArticles(articles);
			gui.updateLoadingBar(90, "Ordenando artículos...");
			articles = evaluator.evaluate(articles, likedWords);
			printer.print(articles);
			gui.updateLoadingBar(99, "Completado!");
			gui.deleteLoading();
			gui.show(articles, likedWords, System.getProperty("user.dir") + System.getProperty("file.separator") + "liked.txt");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Imposible leer fichero de feeds");
		}

	}

}
