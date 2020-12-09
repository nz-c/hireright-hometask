package com.hr.hometask.service.validators;

import com.hr.hometask.service.ServiceRequest;
import com.hr.hometask.service.Validator;
import com.hr.hometask.util.Log;

/**
 * Validator is used to check whether a mandatory parameter is present
 * in the {@link ServiceRequest}
 * 
 * @author Aleksei Nazarov
 *
 */
public class MandantoryParameterValidator implements Validator {
	private String mandatoryParameter;
	

	public MandantoryParameterValidator() {
	}

	public MandantoryParameterValidator(String mandatoryParameter) {
		this.mandatoryParameter = mandatoryParameter;
	}

	public String getMandatoryParameter() {
		return mandatoryParameter;
	}

	public void setMandatoryParameter(String mandatoryParameter) {
		this.mandatoryParameter = mandatoryParameter;
	}

	@Override
	public boolean validate(ServiceRequest request) {
		boolean ret = false;
		if (request != null && request.getParams() != null) {
			ret = request.getParams().containsKey(this.mandatoryParameter);
		}
		
		if (!ret) Log.TO.info("Mandatory parameter is missing: " + this.mandatoryParameter);
		
		return ret;
	}

}
