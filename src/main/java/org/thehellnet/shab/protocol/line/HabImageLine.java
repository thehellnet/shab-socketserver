package org.thehellnet.shab.protocol.line;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.thehellnet.shab.protocol.Command;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.exception.ParseLineException;

/**
 * Created by sardylan on 03/08/16.
 */
public class HabImageLine extends Line {

    private static final Command COMMAND = Command.HAB_IMAGE;
    public static final String COMMAND_TAG = "HI";

    private int sliceTot;
    private int sliceNum;
    private byte[] data;

    public HabImageLine() {
        super(COMMAND);
    }

    public HabImageLine(String rawLine) throws AbstractProtocolException {
        super(COMMAND, rawLine);
    }

    @Override
    public String serializeLine() {
        return String.format("%s|%d|%d|%s",
                COMMAND_TAG,
                sliceTot,
                sliceNum,
                StringUtils.newStringUtf8(Base64.encodeBase64(data, false)));
    }

    @Override
    protected void parse(String[] items) throws AbstractProtocolException {
        if (!items[1].equals(COMMAND_TAG) || items.length != 5) {
            throw new ParseLineException();
        }

        sliceTot = Integer.parseInt(items[2]);
        sliceNum = Integer.parseInt(items[3]);
        data = Base64.decodeBase64(items[4]);
    }

    public int getSliceTot() {
        return sliceTot;
    }

    public void setSliceTot(int sliceTot) {
        this.sliceTot = sliceTot;
    }

    public int getSliceNum() {
        return sliceNum;
    }

    public void setSliceNum(int sliceNum) {
        this.sliceNum = sliceNum;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
