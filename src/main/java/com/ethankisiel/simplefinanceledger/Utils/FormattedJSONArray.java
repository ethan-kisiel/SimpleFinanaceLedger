package com.ethankisiel.simplefinanceledger.Utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.Iterator;
import java.util.List;

public class FormattedJSONArray extends JSONArray
{
    public static String toJSONString(List list) {
        if (list == null) {
            return "null";
        } else {
            boolean first = true;
            StringBuffer sb = new StringBuffer();
            Iterator iter = list.iterator();
            
            sb.append('\n');
            sb.append('[');

            while(iter.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                    sb.append('\n');
                }

                Object value = iter.next();
                if (value == null) {
                    sb.append("null");
                } else {
                    sb.append(JSONValue.toJSONString(value));
                }
            }

            sb.append('\n');
            sb.append(']');
            return sb.toString();
        }
    }
}
