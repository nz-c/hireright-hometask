package com.hireright.hometask.web;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.hireright.hometask.service.ServiceRegistry;
import com.hireright.hometask.util.Log;

/**
 * Lister is used to load service configuration.
 * 
 * @author Aleksei Nazarov
 *
 */
@WebListener("ctx listener")
public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String propFile = sce.getServletContext().getInitParameter("service-configuration");
		if( propFile != null ) {
			for (String f : propFile.split(";")) {
				InputStream input = sce.getServletContext().getResourceAsStream(f);
				if (input != null) {
					Log.TO.info("Loading service configuration from: " + f);
					ServiceRegistry.getInstance().loadAndRegister(input);
				} else {
					Log.TO.info("Failed to load configuration form : " + f);
					Log.TO.info("Context located at: " + sce.getServletContext().getContextPath());
				}
			}
			
		}
	}
}
