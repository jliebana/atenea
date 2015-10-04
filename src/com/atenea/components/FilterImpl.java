package com.atenea.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.atenea.core.api.Filter;
import com.atenea.core.api.bos.Article;

/**
 * Class used for removing the HTML and keep only the relevant words
 * @author jliebana
 *
 */
public class FilterImpl implements Filter {

	private final List<String> stopWords = new ArrayList<String>();
	
	@Override
	public List<Article> filterArticles(List<Article> articles) {
		for(Article current : articles){
			current.setFilteredContent(filterStopWords(toWords(removeHTML(current.getContent()))));
		}
		return articles;
	}

	/**
	 * Method that transforms the content to words, filtering those elements which are not formed by letters or numbers.
	 * 
	 * @param content
	 * @return
	 */
	private List<String> toWords(String content) {
		content = content.toLowerCase();
		ArrayList<String> words = new ArrayList<String>();
		int j = 0;
		int status = 1;
		for(int i = 0; i < content.length(); i++){
			switch(status){
			case 1:
				if(isLetterOrNumber(content.charAt(i))){
					status = 2;
					words.add(new String(""+content.charAt(i)));
				}
				break;
			case 2:
				if(isLetterOrNumber(content.charAt(i))){
					status = 2;
					words.set(j,words.get(j)+content.charAt(i));
				}else{
					status = 1;
					j++;
				}
				break;
			}
		}
		return words;
	}

	private boolean isLetterOrNumber(char letter) {
		return (letter>='a'&&letter<='z')||(letter>='0'&&letter<='9')
		||(letter=='á')||(letter=='é')||(letter=='í')||(letter=='ó')||(letter=='ú')
		||(letter=='ü')||(letter=='ñ');
	}

	private String removeHTML(String content) {
		Pattern pattern = Pattern.compile("<!--.*?-->",Pattern.DOTALL);
		content = pattern.matcher(content).replaceAll(""); //First remove the HTML comments
		Pattern pattern1 = Pattern.compile("<script.*?>.*?</script>",Pattern.DOTALL+Pattern.CASE_INSENSITIVE);//Now remove the JS DOTALL
		content = pattern1.matcher(content).replaceAll("");//Remove the scripts
		Pattern pattern2 = Pattern.compile("<.*?>",Pattern.DOTALL+Pattern.CASE_INSENSITIVE);
		content = pattern2.matcher(content).replaceAll(""); //Remove all other tags
		content = content.replaceAll("\\s*\n","\n"); //Remove empty lines
		return content;
	}

	private List<String> filterStopWords(List<String> content) {
		List<String> rst = new ArrayList<String>();
		for(int i = 0 ; i < content.size() ; i++){
			if(!stopWords.contains(content.get(i))){
				rst.add(content.get(i));
			}
		}
		return rst;
	}

	@Override
	public void loadConfig(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		String[] words = line.split(",");
		for(String current : words){
			stopWords.add(current);
		}
		br.close();
	}

}
