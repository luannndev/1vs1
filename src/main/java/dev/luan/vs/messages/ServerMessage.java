package dev.luan.vs.messages;

import dev.luan.vs.messages.utilities.Pattern;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class ServerMessage {

    private static final Pattern pattern = new Pattern.GradientPattern();

    @Getter
    private static String
            prefix = "§8§l┃ " + getString("1vs1", MessageColor.PASTEL_PURPLE) + " §8┃ §7";

    public static String getString(final String string) {
        return ChatColor.translateAlternateColorCodes('&', pattern.getString(string));
    }

    public static String getString(final String string, final MessageColor messageColor) {
        final String finalString = messageColor.getHexColorCode()[0] + string + (messageColor.getHexColorCode().length == 1 ? messageColor.getHexColorCode()[0] : messageColor.getHexColorCode()[1]);
        return ChatColor.translateAlternateColorCodes('&', pattern.getString(finalString));
    }

    @Getter
    public enum MessageColor {

        /* CUSTOM */
        RED(new String[]{"<ED4245>", "<ED4245>"}),
        DARK_RED(new String[]{"<AA1C1C>", "<9F1A1A>"}),
        YELLOW(new String[]{"<FFE259>", "<FFA751>"}),
        PURPLE(new String[]{"<AA00AA>", "<7E05C0>"}),
        PASTEL_PURPLE(new String[]{"<A582FB>", "<A582FB>"}),
        LIGHT_PURPLE(new String[]{"<EB6EF2>", "<8E65F2>"}),
        LIGHT_GREEN(new String[]{"<57F287>", "<57F287>"}),
        BLUE(new String[]{"<2080FF>", "<3089FF>"}),
        LIGHT_BLUE(new String[]{"<FFE259>", "<FFA751>"}),

        /* REGULAR */
        REGULAR_BLACK(new String[]{"<000000>"}),
        REGULAR_GRAY(new String[]{"<AAAAAA>"}),
        REGULAR_DARK_GRAY(new String[]{"<555555>"}),
        REGULAR_BLUE(new String[]{"<5555FF>"}),
        REGULAR_DARK_BLUE(new String[]{"<0000AA>"}),
        REGULAR_AQUA(new String[]{"<55FFFF>"}),
        REGULAR_DARK_AQUA(new String[]{"<00AAAA>"}),
        REGULAR_RED(new String[]{"<FF5555>"}),
        REGULAR_DARK_RED(new String[]{"<AA0000>"}),
        REGULAR_GREEN(new String[]{"<55FF55>"}),
        REGULAR_DARK_GREEN(new String[]{"<00AA00>"}),
        REGULAR_LIGHT_PURPLE(new String[]{"<FF55FF>"}),
        REGULAR_DARK_PURPLE(new String[]{"<AA00AA>"}),
        REGULAR_YELLOW(new String[]{"<FFFF55>"}),
        REGULAR_GOLD(new String[]{"<FFAA00>"}),
        REGULAR_WHITE(new String[]{"<FFFFFF>"});
        final String[] hexColorCode;
        MessageColor(final String[] hexColorCode) {
            this.hexColorCode = hexColorCode;
        }
    }
}
