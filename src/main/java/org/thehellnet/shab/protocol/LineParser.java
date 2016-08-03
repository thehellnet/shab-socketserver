package org.thehellnet.shab.protocol;

import org.thehellnet.shab.protocol.line.Line;

/**
 * Created by sardylan on 03/08/16.
 */
public class LineParser {

    public static Line parse(String line) {
        line = line.trim();
        String[] items = line.split("|");

        int providedChecksum = Integer.parseInt(items[0], 16);
        int strChecksum = checksum16(line.substring(5));

        if (providedChecksum != strChecksum) {
            return null;
        }

        switch(items[1]) {
            case "CC":
                break;
        }
    }

    public static int checksum16(String rawLine) {
        int ret = 0;

        for (int i = 0; i < rawLine.length(); i++) {
            ret += rawLine.charAt(i);
            ret %= 0xFFFF;
        }

        return ret;
    }
}
