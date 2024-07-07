package com.inha.endgame.core.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return ArrayUtils.toArray(WebSocketConfig.class);
	}

	@Override
	protected String[] getServletMappings() {
		return ArrayUtils.toArray("/");
	}
}
