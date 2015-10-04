package com.atenea.core;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.atenea.components.EvaluatorImpl;
import com.atenea.components.FeedReaderImpl;
import com.atenea.components.FileLoaderImpl;
import com.atenea.components.FilterImpl;
import com.atenea.components.PrinterImpl;
import com.atenea.core.api.Evaluator;
import com.atenea.core.api.FeedReader;
import com.atenea.core.api.FileLoader;
import com.atenea.core.api.Filter;
import com.atenea.core.api.Printer;
import com.atenea.core.api.bos.Article;

/**
 * Loader class which starts all the process.
 * @author jliebana
 *
 */
public class Main {

	private static List<String> likedWords;
	private static List<Article> articles;
	private static List<File> files;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final FeedReader feed = new FeedReaderImpl();
		final Filter filter = new FilterImpl();
		final Evaluator evaluator = new EvaluatorImpl();
		final Printer printer = new PrinterImpl();
		final FileLoader loader = new FileLoaderImpl();
		
		Main m = new Main();
		Main.display = new Display();
		m.createLoading();
			 
		Thread t = new Thread(){
			@Override
			public void run(){
				
				//This part must be improved
				try {
					filter.loadConfig(System.getProperty("user.dir")+System.getProperty("file.separator")+"stop.txt");
					display.asyncExec(updateBar(10,"Cargando configuración"));
					likedWords = loader.loadLikedWords(System.getProperty("user.dir")+System.getProperty("file.separator")+"liked.txt");
					display.asyncExec(updateBar(40,"Descargando feeds (puede tardar)"));
					articles = feed.loadRSSData(System.getProperty("user.dir")+System.getProperty("file.separator")+"feeds.txt");
					display.asyncExec(updateBar(70,"Filtrando..."));
					filter.filterArticles(articles);
					display.asyncExec(updateBar(80,"Evaluando..."));
					articles=evaluator.evaluate(articles,likedWords);
					display.asyncExec(updateBar(90,"Imprimiendo..."));
					files = printer.print(articles);
					display.asyncExec(updateBar(100,"Listo!"));
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Imposible leer fichero de feeds");
				}
			}
		};
		t.start();
		while (t.isAlive()){
			if (!Main.display.readAndDispatch()) {
				Main.display.sleep();
			}
		}
		m.loadingShell.dispose();
		
		m.createMainShell();
		m.show(System.getProperty("user.dir")+System.getProperty("file.separator")+"liked.txt");
	}

	protected static Runnable updateBar(final int i,final String message) {
		return new Runnable(){ public void run() {
				if(progressBar.isDisposed()||loadingMessage.isDisposed()){
					return;
				}
				progressBar.setSelection(i);
				loadingMessage.setText(message);
			}
		};
	}

	public static final String VERSION = "v0.0.2";

	private int index = 0;
	private boolean pressed[];
	private static Display display = null;
	private String path;
	

	private Shell loadingShell = null;
	private static Label loadingMessage = null;
	private static ProgressBar progressBar = null;

	private Shell mainShell = null;
	private Composite composite = null;
	private Composite composite1;
	private Composite composite2 = null;
	private Button botonAnterior = null;
	private Button botonSiguiente = null;
	private Button botonMola = null;
	private Button botonNoMola = null;
	private Browser browser = null;
	
	
	public void createLoading(){
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginWidth = 55;
		gridLayout.marginHeight = 15;
		gridLayout.verticalSpacing = 25;
		gridLayout.makeColumnsEqualWidth = true;
		
		loadingShell = new Shell(display);
		
		loadingShell.setText("Atenea "+Main.VERSION+" - Cargando...");
		loadingShell.setLayout(gridLayout);
		loadingShell.setSize(300, 150);
		loadingShell.setMinimumSize(300, 150);
		loadingShell.setLocation(centerOfScreen(loadingShell));
		loadingMessage = new Label(loadingShell, SWT.NONE);
		loadingMessage.setText("Cargando...");
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		loadingMessage.setLayoutData(gridData);
		progressBar = new ProgressBar(loadingShell, SWT.NONE);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setSelection(1);
		loadingShell.setVisible(true);
		loadingShell.open();
	}

	public void show(String path) {

		pressed = new boolean[Main.articles.size()];
		this.path = path;

		while (!mainShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * This method initializes sShell
	 */
	private void createMainShell() {
		mainShell = new Shell(display);
		RowLayout rowLayout1 = new RowLayout();
		rowLayout1.justify = true;
		mainShell.setText("Atenea " + Main.VERSION);
		createComposite();
		mainShell.setLayout(rowLayout1);
		mainShell.setSize(new Point(800, 600));
		mainShell.setMinimumSize(800, 600);
		mainShell.setLocation(centerOfScreen(mainShell));
		mainShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
			@Override
			public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
				saveLikedWords();
				clean();
				System.out.println("Exiting without error");
				System.exit(0);
			}
		});
		mainShell.setActive();
		mainShell.setVisible(true);
	}

	private Point centerOfScreen(Shell shell) {
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		return new Point(x, y);
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.makeColumnsEqualWidth = true;
		gridLayout1.verticalSpacing = 20;
		composite = new Composite(mainShell, SWT.NONE);
		createComposite1();
		createComposite2();
		composite.setLayout(gridLayout1);
	}

	/**
	 * This method initializes browser
	 * 
	 */
	private void createComposite1() {
		GridData gridData = new GridData();
		gridData.heightHint = 500;
		gridData.widthHint = 750;
		composite1 = new Composite(composite, SWT.NONE);
		composite1.setLayout(new FillLayout());
		composite1.setLayoutData(gridData);
		createBrowser();
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite2() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		RowLayout rowLayout = new RowLayout();
		rowLayout.fill = true;
		rowLayout.spacing = 150;
		rowLayout.justify = true;
		composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayout(rowLayout);
		composite2.setLayoutData(gridData1);
		botonAnterior = new Button(composite2, SWT.NONE);
		botonAnterior.setText("Anterior");
		botonAnterior.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					@Override
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						index = (index > 0) ? index - 1 : index;
						updateUrl();
					}
				});
		botonMola = new Button(composite2, SWT.NONE);
		botonMola.setText("Mola");
		botonMola.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				if (!pressed[index]) {
					for (String current : articles.get(index)
							.getFilteredContent()) {
						boolean found = false;
						for (int i = 0; i < likedWords.size() && !found; i++) {
							if (current.equalsIgnoreCase(likedWords.get(i))) {
								found = true;
							}
						}
						if (!found) {
							likedWords.add(current);
						}
					}
					pressed[index] = true;
				}
			}
		});
		botonNoMola = new Button(composite2, SWT.NONE);
		botonNoMola.setText("No Mola");
		botonNoMola.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				if (!pressed[index]) {
					pressed[index] = true;
				}
			}
		});
		botonSiguiente = new Button(composite2, SWT.NONE);
		botonSiguiente.setText("Siguiente");
		botonSiguiente.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					@Override
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						index = (index < articles.size() - 1) ? index + 1
								: index;
						updateUrl();
					}
				});
	}

	private void createBrowser() {
		browser = new Browser(composite1, SWT.NONE);
		updateUrl();
	}

	private void updateUrl() {
		browser.setUrl("file:///" + System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "articles"
				+ System.getProperty("file.separator") + index + ".html");
	}

	private void saveLikedWords() {
		if (likedWords.size() > 0) {
			String words = "";
			for (String current : likedWords) {
				words += current + ",";
			}
			words = words.substring(0, words.length() - 1);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(path,
						false));
				bw.write(words);
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void clean() {
		for (File file : files) {
			file.delete();
		}
	}

}
