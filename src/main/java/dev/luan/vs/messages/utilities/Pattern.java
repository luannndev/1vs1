package dev.luan.vs.messages.utilities;

import java.awt.*;
import java.util.regex.Matcher;

public interface Pattern {

    String getString(final String string);

    class GradientPattern implements Pattern {

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<([0-9A-Fa-f]{6})>(.*?)<([0-9A-Fa-f]{6})>");

        public String getString(String string) {
            final Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                final String start = matcher.group(1);
                final String end = matcher.group(3);
                final String content = matcher.group(2);
                string = string.replace(matcher.group(), ColorManager.color(content, new Color(Integer.parseInt(start, 16)), new Color(Integer.parseInt(end, 16))));
            }
            return string;
        }
    }
}
