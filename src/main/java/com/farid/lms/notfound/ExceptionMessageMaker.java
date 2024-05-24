package com.farid.lms.notfound;

public class ExceptionMessageMaker {
	public static String makeExceptionMessageForEntityWithId(String entityName, Long id) {
		return entityName + " with ID " + id + " not found";
	}
}
