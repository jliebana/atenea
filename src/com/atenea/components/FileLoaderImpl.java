package com.atenea.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.atenea.core.api.FileLoader;

/**
 * Simple class to load the liked words
 * @author jliebana
 *
 */
public class FileLoaderImpl implements FileLoader {

	@Override
	public List<String> loadLikedWords(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		List<String> likedWords = new ArrayList<String>();
		String line = br.readLine();
		String[] words = (line!=null)?line.split(","):new String[0];
		for(String current : words){
			likedWords.add(current);
		}
		br.close();
		return likedWords;
	}

}
