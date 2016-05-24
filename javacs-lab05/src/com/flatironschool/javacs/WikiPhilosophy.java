package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
   * 2. Ignoring external links, links to the current page, or red links
   * 3. Stopping when reaching "Philosophy", a page with no links or a page
   *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started
    ArrayList urlList = new ArrayList<String>();
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
    urlList.add(url);
		Elements paragraphs = wf.fetchWikipedia(url);

		Element firstPara = paragraphs.get(0);
    int iterate = 0;
		Iterable<Node> iter = new WikiNodeIterable(firstPara);
    boolean validPath = true;
    boolean linkFound = false;
    boolean destinationReached = false;
    while(!destinationReached) {
      int leftParens = 0;
      int rightParens = 0;
      int count = 0;
      for (Node node: iter) {
        linkFound = false;
        if (node instanceof TextNode) {
//          System.out.print(node);
          TextNode text = (TextNode) node;
          String str = text.text();
          for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '(') {
              leftParens++;
            }
            if(str.charAt(i) == ')') {
              rightParens++;
            }
          }
        }
        if(node instanceof Element && count > 0) {
          Element element = (Element)node;
          Elements links = element.select("a[href]");
          
          if(!links.isEmpty() && leftParens == rightParens) {
            url = links.get(0).attr("abs:href");
            urlList.add(url);
            paragraphs = wf.fetchWikipedia(url);
            firstPara = paragraphs.get(0);
            iter = new WikiNodeIterable(firstPara);
            linkFound = true;
            if(links.get(0).text().equals("philosophy")) {
              destinationReached = true;
            }
            break;
          }
        }
        count++;
      }
      if(!linkFound || destinationReached) {
        validPath = false;
      }
    }
    for(Object val : urlList) {
      System.out.println(val);
    }
  }

        // the following throws an exception so the test fails
        // until you update the code
        //throw new UnsupportedOperationException(msg);
}
