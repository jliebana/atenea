package com.atenea.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.atenea.core.api.FeedReader;
import com.atenea.core.api.bos.Article;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * Class used for retrieving the articles from the especified RSS urls
 * @author jliebana
 *
 */
public class FeedReaderImpl implements FeedReader {

	@Override
	public List<Article> loadRSSData(String path) throws IOException {
		List<Article> articles = new ArrayList<Article>();
		List<String> urls = readUrls(path);
		for (String url : urls) {
			List<Article> readed = feedFrom(url);
			for (Article current : readed) {
				articles.add(current);
			}
		}
		return articles;
	}

	@SuppressWarnings("unchecked")
	private List<Article> feedFrom(String url) {
		List<Article> articles = new ArrayList<Article>();
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed;
		try {
			feed = input.build(new XmlReader(new URL(url)));
			List<SyndEntry> entries = feed.getEntries();
			for (SyndEntry entry : entries) {
				Article article = new Article();
				article.setDate(entry.getPublishedDate());
				article.setAuthor(entry.getAuthor());
				article.setUri(entry.getLink());
				article.setTitle(entry.getTitleEx().getValue());
				article.setContent(getContent(entry));
				articles.add(article);
			}
			return articles;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Article>();
	}

	private String getContent(SyndEntry entry) {
		if(entry.getContents().size()>0){
			return ((SyndContent)entry.getContents().get(0)).getValue();
		} else return entry.getDescription().getValue();
		
	}

	private List<String> readUrls(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = "";
		List<String> urls = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			urls.add(line);
		}
		br.close();
		return urls;
	}

}
