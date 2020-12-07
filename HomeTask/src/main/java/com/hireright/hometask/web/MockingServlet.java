package com.hireright.hometask.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hireright.hometask.service.ServiceRegistry;
import com.hireright.hometask.service.ServiceRequest;
import com.hireright.hometask.util.Log;


/**
 * Servlet receives requests and calls the Mocking Service to retrieve data
 * according to the received query criteria.
 */
@WebServlet(description = "Servlet to call mocking service", urlPatterns = { "/*" })
public class MockingServlet extends HttpServlet {
	
	private static final long serialVersionUID = -2963473875431356722L;
	
	public static final String CONTENT_TYPE = "application/json";
	public static final String ENCODING = "UTF-8";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAll(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAll(request, response);
	}
	
	private void doAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Log.TO.info("request received: " + request.getPathInfo());

		response.setContentType(MockingServlet.CONTENT_TYPE);
		response.setCharacterEncoding(MockingServlet.ENCODING);
		
		int status = ServiceRegistry.getInstance().get(request.getPathInfo()).map((service) -> {
			try {
				switch (service.processAndWrite(
						new ServiceRequest(
								request.getMethod(), 
								request.getParameterMap()), 
								response.getWriter())) {
					case ERROR: {
						return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
					}
					case INVALID_REQUEST: {
						return HttpServletResponse.SC_BAD_REQUEST;
					}
					case NOTHING: {
						return HttpServletResponse.SC_NOT_FOUND;
					}
					case OK: {
						return HttpServletResponse.SC_OK;
					}
					default:
						return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
			} catch (IOException e) {
				Log.TO.error(e);
			}
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			
		}).orElse(HttpServletResponse.SC_NOT_FOUND); 
		
		response.setStatus(status);
	}
	
}
