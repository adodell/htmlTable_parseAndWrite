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
    public static void main( String[] args )
    {
        String myHTML = "<html><title>does this work?</title><p>TEST!</p></html>";
        System.out.println(StringEscapeUtils.escapeHtml(myHTML) + "\nare we live?");

        try {
			Document doc = Jsoup.connect("https://www.adodell.com/").get();

			Element content = doc.getElementById("myNavigation");

			System.out.println(content.toString());

		} catch(IOException e){
        	System.out.println("WTF!");
		}

    }
}
