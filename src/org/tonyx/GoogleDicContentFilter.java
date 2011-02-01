package org.tonyx;


import org.tonyxzt.language.core.ContentFilter;
import org.tonyxzt.language.util.Utils;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Tonino
 * Date: 27/01/11
 * Time: 2.34
 * To change this template use File | Settings | File Templates.
 */
public class GoogleDicContentFilter implements ContentFilter {
    private static final int INDEX_START_FILTERING = 2;
    public String filter(String theResult) {
        Pattern p = Pattern.compile("<span class=\"dct-tt\">|<div class=\"wbtr_cnt\">");
        String splitted[] = p.split(theResult, Pattern.MULTILINE | Pattern.DOTALL);
        String toReturn="";
        toReturn = flattenAndClean(splitted, toReturn);
        return toReturn;
    }

    private String flattenAndClean(String[] splitted, String toReturn) {
        String toAdd;
        for (int i=INDEX_START_FILTERING ;i<splitted.length;i++)  {
            toAdd= splitted[i].substring(0,splitted[i].indexOf("</span>"));
            toAdd = removeAnchorsBlocksByTags(toAdd,"a");
            //toAdd = Utils.stripBlock(toAdd,"a");
            if (!"\n".equals(toAdd.replaceAll(" ",""))) {
                toReturn+=toAdd;
                toReturn+=", ";
            }
        }
        return toReturn.replaceAll("<[^>]*>","");
    }

    private String removeAnchorsBlocksByTags(String string, String tag) {
        String[] res = string.split("<"+tag+"a.[^>]*>|</"+tag+">");
        String strRes="";
        for (int i =0;i<res.length;i+=2) {
            strRes+=res[i];
        }
        return strRes;
    }
}
