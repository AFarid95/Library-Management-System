package com.farid.lms.logging;

import org.aspectj.lang.annotation.Pointcut;

public class LoggingPointcuts {
	@Pointcut("within(com.farid.lms.controllers.*)")
	public void apiCall() {}
}
