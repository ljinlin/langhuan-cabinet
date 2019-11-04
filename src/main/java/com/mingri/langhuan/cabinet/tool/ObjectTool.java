package com.mingri.langhuan.cabinet.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectTool {

	private ObjectTool() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectTool.class);

	@SuppressWarnings("unchecked")
	public static <T> T serializeObject(Object src) {
		T dest = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			dest = (T) in.readObject();
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return dest;

	}


}
