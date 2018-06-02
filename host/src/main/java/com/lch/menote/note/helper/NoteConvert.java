package com.lch.menote.note.helper;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;


public class NoteConvert {

    public static String convertNoteContentToHtml(String jsonContent) {
        if (TextUtils.isEmpty(jsonContent)) {
            return null;
        }

        try {
            StringBuilder sb = new StringBuilder();
            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String htmlTag = convertElementToHtmlTag(jsonObject);
                sb.append(htmlTag);
            }

            return sb.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    private static String convertElementToHtmlTag(JSONObject jsonObject) {
        if (jsonObject == null) {
            return "";
        }
        String type = jsonObject.optString("type");
        if (type == null) {
            return "";
        }
        switch (type) {
            case "text":
                String text = jsonObject.optString("text");
                if (text != null) {
                    text = text.replaceAll("\n", "<br>");
                }
                return String.format("<p>%s</p>", text);
            case "img":
                return String.format("<img src=\"%s\"/>", jsonObject.optString("path"));
            // return String.format("<img src=\"%s\"/>","https://123p0.sogoucdn.com/imgu/2018/05/20180508193928_297.jpg");
            default:
                return "";
        }
    }
}
