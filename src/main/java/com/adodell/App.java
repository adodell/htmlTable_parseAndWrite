package com.adodell;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

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
			System.out.println("No URL argument was input, using default Wikipedia DJIA Components History page...");
			myURL = "https://en.wikipedia.org/wiki/Historical_components_of_the_Dow_Jones_Industrial_Average";

			// another good example "http://www.eaglemarineservices.com/tmsweb/unsecure/UNSVSLRPT.tms"
		}

		try {
			Document myDocumentObj = Jsoup.connect(myURL).get();

			// creating Tables Elements lst
			Elements tables_elements = myDocumentObj.select("table");

			// <table> iterator
			for (Element tmp_table_element : tables_elements) {

				// print location of this tmp_table
				int table_i = tables_elements.indexOf(tmp_table_element);
				System.out.println("\n" + table_i + "\n~~~~~~~~~~~~~\n\n");

				// creation of Table StrBuilder
				StringBuilder tmp_tableStrBuilder = new StringBuilder();

				// creating list of TSection Elements
				Elements tSection_elements = tmp_table_element.select("thead,tbody,tfoot");

				// <tSection> iterator
				for (Element tmp_tSection_element : tSection_elements) {

					// creating list of TR (and also TH) Elements
					Elements tr_elements = tmp_tSection_element.select("tr,th");

					// how many TR elements in this TSection?
					int tmp_tSection_trSizeInt = tr_elements.size();

					// <tr > iterator
					for (int tmp_tr_i = 0; tmp_tr_i < tmp_tSection_trSizeInt; tmp_tr_i++) {
						Element tmp_tr_element = tr_elements.get(tmp_tr_i);
						// getting the TD Elements
						Elements td_elements = tmp_tr_element.select("td");

						// how many TD elements in this TRow?
						int tmp_tr_tdSizeInt = td_elements.size();

						// <td > iterator
						for (int tmp_td_i = 0; tmp_td_i < tmp_tr_tdSizeInt; tmp_td_i++) {
							// pulling <td> element
							Element tmp_td_element = td_elements.get(tmp_td_i);

							// if this is not the last TD, append with ","
							if (tmp_td_i + 1 != tmp_tr_tdSizeInt) {
								tmp_tableStrBuilder.append(tmp_td_element.text()).append(",");
							}
							// otherwise this is LastTD, append with "\n"
							else {
								tmp_tableStrBuilder.append(tmp_td_element.text()).append("\n");
							}
						}
					}

					//all of the TR and TDs have been iterated through
					// now print out StrBuilder for table
					System.out.println(tmp_tableStrBuilder.toString());
				}
				// stopping after first 3 tables
				if (table_i > 3){
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Jsoup connect FAILED :(\n\n");
			e.printStackTrace();
		}

	}
}

// STUFF TO DO
// check through table for <TR> as well, without a Tbody...
// create function that takes in TR Elements and outputs StringBuilder of these elements
// for each table, iterate through elements, checking for TR, or TBody/foot/head(s)