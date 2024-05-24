package com.farid.lms.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class OperationLogger {
	private Logger logger;
	
	@Around("com.farid.lms.logging.LoggingPointcuts.apiCall()")
	public Object test(ProceedingJoinPoint pjp) throws Throwable {
		logger = LoggerFactory.getLogger(pjp.getTarget().getClass());
		String apiMethod = pjp.getSignature().getName();
		Object[] args = pjp.getArgs();
		
		logMethodCall(apiMethod, args);
		logger.info("Started executing " + apiMethod + "...");
		
		long startTime = System.currentTimeMillis();
		
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			logger.info("Threw exception: " + e.getClass() + ", message: " + e.getMessage());
			throw e;
		} finally {
			logger.info("Finished executing " +
						apiMethod +
						" in " +
						(System.currentTimeMillis() - startTime) +
						" milliseconds");
		}
	}
	
	private void logMethodCall(String name, Object[] args) {
		logger.info("Calling API method " +
				name +
				(args.length > 0? " with the following arguments:" : ""));
		
		for (int i = 0; i < args.length; i++)
			logger.info("Argument " + (i + 1) + " (type: " + args[i].getClass() + "): " + args[i]);
	}
}
