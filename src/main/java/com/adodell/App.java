package com.adodell;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
		String myHTML = "<html><title>does this work?</title><p>TEST!</p></html>";
		System.out.println(StringEscapeUtils.escapeHtml(myHTML) + "\nare we live?");

		String myURL = "https://en.wikipedia.org/wiki/Historical_components_of_the_Dow_Jones_Industrial_Average";
		try {
			Document myDocumentObj = Jsoup.connect(myURL).get();

			for (Element row : myDocumentObj.select("table.wikitable th")){
				// this doesnt work...
				System.out.println(row.toString());
			}

			//this works!
			System.out.println(myDocumentObj.outerHtml().substring(0,50000));

		} catch (IOException e) {
			System.out.println("Jsoup connect FAILED :(");
		}

	}
}
