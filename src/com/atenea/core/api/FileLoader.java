package com.atenea.core.api;

import java.io.IOException;
import java.util.List;

public interface FileLoader {

	List<String> loadLikedWords(String path) throws IOException;

}
