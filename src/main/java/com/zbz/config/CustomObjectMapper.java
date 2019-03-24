package com.zbz.config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = -5422983207643006489L;

	public CustomObjectMapper() {
		
		//禁止使用时间戳
		this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		//设置为中国时区
		this.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		//设置日期格式
		this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));		
	}

}
