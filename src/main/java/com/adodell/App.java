package com.adodell;

import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	public static void main( String[] args ) {
		/*
		~~~~~~~~
		STEP 1:
		find argument
		~~~~~~~~
		 */

		// declaring My Arg to grab
		String my_arg;

		// grabbing arg
		try{
			// for testing local File
			//my_arg = "C:/Users/Aaron/Documents/2Programming/java/htmlTable_parseAndWrite/tableExample.html";

			my_arg = args[0].toString();
		}
		// if no arg use default
		catch (IndexOutOfBoundsException e) {
			System.out.println("No argument was input, using default Wikipedia DJIA Components History page...");
			my_arg = "https://en.wikipedia.org/wiki/Historical_components_of_the_Dow_Jones_Industrial_Average";

			// problems with "https://en.wikipedia.org/wiki/Mike_Tyson", debug this!
		}

		/*
		~~~~~~~~
		STEP 2:
		create Document obj from arg URL or File
		~~~~~~~~
		 */

		// declaring Document to get from My_arg
		Document myDocumentObj;

		// checking if it is a URL or File
		try{
			URL my_url = new URL(my_arg);

			// parse URL using JSOUP
			myDocumentObj = Jsoup.connect(my_arg).get();

		}
		// if not URL, try to see if it is a path
		catch (MalformedURLException tmp_badURL_exception) {
			File my_file = new File(my_arg);

			// if the file is not readable, throw an error
			if (!my_file.canRead()){
				throw new IllegalArgumentException("The argument input: " + my_arg + " is not a URL or the File cannot be read");
			}
			// parse using JSOUP
			try{
				myDocumentObj = Jsoup.parse(my_file, null);
			} catch (Exception tmp_parse_exception) {
				tmp_parse_exception.printStackTrace();
				throw new IllegalArgumentException("The argument file input: " + my_arg + " could not be parsed");
			}

		}
		// catching a URL Exception
		catch (IOException tmp_badIO_exception) {
			tmp_badIO_exception.printStackTrace();

			throw new IllegalArgumentException("The argument URL input: " + my_arg+ " does not work");
		}

		/*
		~~~~~~~~
		STEP 3:
		iterate through Table tags in Document
		~~~~~~~~
		 */

		// creating Tables Elements lst
		Elements tables_elements = myDocumentObj.select("table");

		// creating a TableInfo Array
		ArrayList<TableInfo> tableInfo_objLst = new ArrayList<TableInfo>();

		// populating the Arraylist with Elements as TableInfo objs
		for (Element tmp_table_element : tables_elements) {
			// creating a TableInfo obj
			TableInfo tmp_tblInfo_obj = new TableInfo(tmp_table_element);
			// adding this TblInfo Obj into Arraylist
			tableInfo_objLst.add(tmp_tblInfo_obj);
		}

		System.out.println(String.format("\n~~~~~~~~~~~~~~~~~\n%d tables were found!!\n~~~~~~~~~~~~~~~~~\n\n", tableInfo_objLst.size()));

		// iterating through every TableInfo obj
		for (TableInfo tmp_tableInfo_obj : tableInfo_objLst) {
			// print location of this tmp_tableInfoObj
			int table_i = tableInfo_objLst.indexOf(tmp_tableInfo_obj);
			System.out.println("\n" + table_i + "\n~~~~~~~~~~~~~\n\n");

			// creation of Table StrBuilder
			StringBuilder tmp_tableStrBuilder = new StringBuilder();

			// getting list of list of TSection Elements
			Elements tSection_elements = tmp_tableInfo_obj.get_tSection_elements_lst();

			// <tSection> iterator
			for (Element tmp_tSection_element : tSection_elements) {

				// creating list of TR (and also TH) Elements
				Elements tr_elements = tmp_tSection_element.select("tr");

				// how many TR elements in this TSection?
				int tmp_tSection_trSizeInt = tr_elements.size();

				// <tr > iterator
				for (int tmp_tr_i = 0; tmp_tr_i < tmp_tSection_trSizeInt; tmp_tr_i++) {
					Element tmp_tr_element = tr_elements.get(tmp_tr_i);
					// getting the TD Elements
					Elements td_elements = tmp_tr_element.select("td,th");

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
			}

			//all of the T Sections have been iterated through
			// now print out StrBuilder for the Table
			System.out.println(tmp_tableStrBuilder.toString());

			// stopping after first 3 tables
			if (table_i > 3){
				break;
			}
		}
	}
}

// STUFF TO DO
// implement all below into TableInfo.java Class....
// check through table for <TR> as well, without a Tbody
// create function that takes in TR Elements and outputs StringBuilder of these elements
// for each table, iterate through elements, checking for TR, or TBody/foot/head(s)