package com.daw.contafin.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ErrorMessage {
	
	private String message = "The web page doesn't exist or you don't have permission.";

	public ErrorMessage(){

	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorMessage(String message) {
		this.message = message;
	}
		
}
