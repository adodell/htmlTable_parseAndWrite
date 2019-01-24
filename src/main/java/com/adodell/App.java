package com.adodell;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

    	String myURL;

    	try{
    		myURL = args[0].toString();
		} catch (Exception e) {
			System.out.println("No URL argument was input, using default...");
			myURL = "https://en.wikipedia.org/wiki/Historical_components_of_the_Dow_Jones_Industrial_Average";
		}

    	/*
		String myHTML = "<html><title>does this work?</title><p>TEST!</p></html>";
		System.out.println(StringEscapeUtils.escapeHtml(myHTML) + "\nare we live?");
		*/

		try {
			Document myDocumentObj = Jsoup.connect(myURL).get();
			Elements myTables_elements = myDocumentObj.select("table");

			// <table> iterator
			for (Element table_element : myTables_elements) {
				int table_i = myTables_elements.indexOf(table_element);
				System.out.println("\n" + table_i + "\n~~~~~~~~~~~~~\n\n");

				// <tSection> iterator
				for (Element tSection_element : table_element.select("thead,tbody,tfoot")) {
					// <tr > iterator
					for (Element tr_element : tSection_element.select("tr")) {

						if (tSection_element.select("tr").indexOf(tr_element) < 3){
							System.out.println(tr_element.toString());
						}

						// <td > iterator
						for (Element td_element : tr_element.select("td")) {
							//System.out.println(td_element.toString());
						}
					}
				}
				if (table_i == 5){
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Jsoup connect FAILED :(\n\n");
			e.printStackTrace();
		}

	}
}
