package com.adodell;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class TableInfo {
    /*
    ATTRIBUTES:
     */
    // data attributes
    private Element tableTag_element;
    private ArrayList<Element> tSection_element_arrayList;

    // output attribute
    private StringBuilder csvFormat_strBuilder;

    // constructor
    public TableInfo(Element tableElement_in){
        // assign TableTab Elem,ent
        tableTag_element = tableElement_in;

        // population TSection Array List
        populate_tSection_element_arrayList();
        // populate CSVFormat StrBuilder ArrayList
        populate_csvFormat_strBuilder();
    }

    /*
    METHODS: Populate Attributes
     */

    // populate TSection Elements List
    private void populate_tSection_element_arrayList(){
        // casting the Elements obj into an ArrayLIst of Element
        tSection_element_arrayList = new ArrayList<Element>( tableTag_element.select("thead,tbody,tfoot") );
    }

    //populate Output StrBuilder Array
    private void populate_csvFormat_strBuilder(){
        // creating StringBuilder obj
        csvFormat_strBuilder = new StringBuilder();

        // Iterate through every TSection
        for (Element tmp_tSection_element : tSection_element_arrayList) {

            // creating list of TR Elements
            Elements tr_elements = tmp_tSection_element.select("tr");

            // <tr > iterator
            for (Element tmp_tr_element : tr_elements) {
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
                        csvFormat_strBuilder.append(tmp_td_element.text()).append(",");
                    }
                    // otherwise this is LastTD, append with "\n"
                    else {
                        csvFormat_strBuilder.append(tmp_td_element.text()).append("\n");
                    }
                }
            }
        }
    }

    /*
    METHODS: GETTERS
     */
    public Element get_tableTag_element(){
        return tableTag_element;
    }

    public ArrayList<Element> get_tSection_element_arrayList(){
        return tSection_element_arrayList;
    }

    public StringBuilder get_csvFormat_strBuilder(){
        return csvFormat_strBuilder ;
    }
}
