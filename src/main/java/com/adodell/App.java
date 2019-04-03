package com.adodell;

import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.commons.io.FileUtils;

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
		create Array of TableInfo objects from table tags in Document

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

		// determining how many tables exist
		String totalTables_count_str = Integer.toString(tableInfo_objLst.size());

		System.out.println(String.format("\n~~~~~~~~~~~~~~~~~\n%s tables were found!!\n~~~~~~~~~~~~~~~~~\n\n", totalTables_count_str));

		// finding how many leading zeros are necessary for file names
		int leadingZeroForFileNames_count = totalTables_count_str.length();

		/*
		~~~~~~~~
		STEP 4:
		create Array of StringBuilder objects from the TableInfo objects
		~~~~~~~~
		 */

		// creating StrBuilder Array
		ArrayList<StringBuilder> csvFormat_strBuilder_arrayLst = new ArrayList<StringBuilder>();

		// iterating through every TableInfo obj
		for (TableInfo tmp_tableInfo_obj : tableInfo_objLst) {
			// add the stringBuilder to the ArrayList
			csvFormat_strBuilder_arrayLst.add(tmp_tableInfo_obj.get_csvFormat_strBuilder());
		}

		/*
		~~~~~~~~
		STEP 5:
		create Directory for Tables, then populate with CSV Files
		~~~~~~~~
		 */
		// find the Current Working directory
		String currentDir_str = System.getProperty("user.dir");

		// creating new TableOutputs directory
		File tablesOutput_dir = new File(currentDir_str, "TableOutputs");
		// if this Outputs dir already exists, delete it
		if (tablesOutput_dir.exists()){
			try{
				FileUtils.deleteDirectory(tablesOutput_dir);
			} catch (Exception E){
				;
			}
		}
		// now creating the TableOuputs Dir
		tablesOutput_dir.mkdir();

		// iterate through every StrBuilder, saving as a CSV in the TableOuputs dir
		for (int tmp_table_i = 0; tmp_table_i < csvFormat_strBuilder_arrayLst.size(); tmp_table_i++){
			// pulling the StrBuilder from the Array
			StringBuilder tmp_csvFormat_strBuilder = csvFormat_strBuilder_arrayLst.get(tmp_table_i);

			// FILE SETUP
			// creating new File location
			File tmp_table_fileObj = new File(tablesOutput_dir,
												String.format("Table_%0" + Integer.toString(leadingZeroForFileNames_count) +"d.csv",tmp_table_i + 1)
			);

			// create File itself, and popualte with StrBuilder
			try{
				// creating File
				tmp_table_fileObj.createNewFile();
				// create Writer
				FileWriter tmp_table_fileWriterObj = new FileWriter(tmp_table_fileObj);
				// write the StrBuilder
				tmp_table_fileWriterObj.write(tmp_csvFormat_strBuilder.toString());
				tmp_table_fileWriterObj.flush();
				tmp_table_fileWriterObj.close();
			} catch (Exception E){
				//pass
				;
			}
		}
	}
}