package com.web.core.converter;

import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Clob;

/**
 * 字符串转换器（对象转换成字符串）
 *
 * @author
 *  */
public class LocalStringConverter extends AbstractConverter {

	public LocalStringConverter() {
		super();
	}

	public LocalStringConverter(Object defaultValue) {
		super(defaultValue);
	}

	@Override
	protected Class<?> getDefaultType() {
		return String.class;
	}

	@Override
	protected String convertToString(Object value) throws Throwable {
		if (value == null) {
			return null;
		} else if (value instanceof File) {
			return IOUtils.toString(new FileInputStream((File) value));
		} else if (value instanceof Clob) {
			Clob clob = (Clob) value;
			return IOUtils.toString(clob.getCharacterStream());
		}
		return value.toString();
	}

	@Override
	protected <T> T convertToType(Class<T> type, Object value) {
		if (String.class.equals(type) || Object.class.equals(type)) {
			return type.cast(value.toString());
		}
		throw conversionException(type, value);
	}

}
