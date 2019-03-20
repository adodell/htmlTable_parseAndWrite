package com.adodell;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableInfo {
    /*
    ATTRIBUTES:
     */
    // data attributes
    private Element tableTag_element;
    private Elements tSection_elements_lst;

    // output attribute
    private StringBuilder output_strBuilder;

    // constructor
    public TableInfo(Element tableElement_in){
        tableTag_element = tableElement_in;
        populate_tSection_elements_lst();
    }

    /*
    METHODS: Populate Attributes
     */

    // populate TSection Elements List
    private void populate_tSection_elements_lst(){
        tSection_elements_lst = tableTag_element.select("thead,tbody,tfoot");
    }

    /*
    METHODS: GETTERS
     */
    public Element get_tableTag_element(){
        return tableTag_element;
    }

    public Elements get_tSection_elements_lst(){
        return tSection_elements_lst;
    }

}
