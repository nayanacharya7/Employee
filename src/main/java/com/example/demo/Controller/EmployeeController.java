package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/addEmpolyee")
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
	    try {
	        if (employee.getEmployee_Name() == null || employee.getEmployee_Name().isEmpty()) {
	            return new ResponseEntity<>("Employee name is required", HttpStatus.BAD_REQUEST);
	        }
	        if (employee.getEmployee_Age() == 0) {
	            return new ResponseEntity<>("Employee age is required", HttpStatus.BAD_REQUEST);
	        }
	        if (employee.getEmployee_Salary() == null || employee.getEmployee_Salary().isEmpty()) {
	            return new ResponseEntity<>("Employee salary is required", HttpStatus.BAD_REQUEST);
	        }

	        Employee savedEmployee = employeeService.addEmployee(employee);
	        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);

	    } catch (Exception e) {
	        return new ResponseEntity<>("An error occurred while adding the employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@GetMapping("/allEmp")
	public ResponseEntity<?> getAllEmployees() {
	    try {
	        List<Employee> employees = employeeService.getEmployee();
	        if (employees.isEmpty()) {
	            return new ResponseEntity<>("No employees found", HttpStatus.NOT_FOUND);
	        } else {
	            return new ResponseEntity<>(employees, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error fetching employees: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/getOneEmp/{id}")
	public ResponseEntity<?> getEmployee(@PathVariable Integer id) {
	    try {
	        Optional<Employee> employee = employeeService.getOneEmployee(id);
	        if (employee.isPresent()) {
	            return new ResponseEntity<>(employee.get(), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@DeleteMapping("/deleteEmp/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) {
	    try {
	        boolean deleted = employeeService.deleteEmployeeById(id);
	        if (deleted) {
	            return new ResponseEntity<>("Employee deleted successfully with ID " + id , HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error deleting employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PatchMapping("/updateEmp/{id}")
	public ResponseEntity<?> updateEmployeePartially(@PathVariable Integer id, @RequestBody Employee employee) {
	    try {
	        Optional<Employee> existingEmployeeOpt = employeeService.getOneEmployee(id);
	        if (existingEmployeeOpt.isEmpty()) {
	            return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }

	        Employee existingEmployee = existingEmployeeOpt.get();

	        if (employee.getEmployee_Name() != null && !employee.getEmployee_Name().isEmpty()) {
	            existingEmployee.setEmployee_Name(employee.getEmployee_Name());
	        }

	        if (employee.getEmployee_Age() != 0) {
	            existingEmployee.setEmployee_Age(employee.getEmployee_Age());
	        }

	        if (employee.getEmployee_Salary() != null && !employee.getEmployee_Salary().isEmpty()) {
	            existingEmployee.setEmployee_Salary(employee.getEmployee_Salary());
	        }

	        Employee updated = employeeService.addEmployee(existingEmployee);
	        return new ResponseEntity<>(updated, HttpStatus.OK);

	    } catch (Exception e) {
	        return new ResponseEntity<>("Error updating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
/*	@RequestMapping(value = "/getELSSCalculatorResult", method = RequestMethod.POST)
		public void getELSSCalculatorResult(HttpServletRequest request, HttpServletResponse response) throws IOException
		{
			PrintWriter writer = null;		
			Gson gson = null;
			String ipAddr = null;
			String origin = null;
			MutualFundApiAndIpValidityResponse mutualFundApiAndIpValidityResponse = null;
			ApiCalculatorELSS result = null;
			String apirequest = "",apiresponse = "";
			Date d1;
			String client_name = "";
			
			try 
			{	
				d1 = new Date();
				writer = response.getWriter();
				gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).create();
				response.setContentType("text/html");
				ipAddr = CommonUtils.getIpAddr(request);
				if(ipAddr == null){ipAddr="";}
				origin = request.getHeader("Origin");
				if(origin == null){origin="";}
				result = new ApiCalculatorELSS();
				
				String amount = request.getParameter("investment_amount");
		     		String interest_rate = request.getParameter("interest_rate");
	            		String period = request.getParameter("period");
	            		String type = request.getParameter("type");
	            		String key = request.getParameter("key");
	            
	            String[][] param_and_pattern = {{amount,"Amount"},{interest_rate,"SafeString"},{period,"SafeString"},{type,"SafeString"}};
	            
	            int invalid_index_para = ESAPIValidator.isValidParameters(param_and_pattern); 
	            if(invalid_index_para > -1) 
	            {
	            	String[] parameters_name = {"investment_amount","interest rate","period","type"};
	            	String validationErrorMsg = "Invalid "+parameters_name[invalid_index_para];
	            	result.setStatus(StatusMessage.FailureCode);
	            	result.setStatus_msg(StatusMessage.FailureMessage);
	            	result.setMsg(validationErrorMsg);
	            	apiresponse = gson.toJson(result);                                       
	            	writer.print(apiresponse);/*Esapi FailureResponse*/
	    		writer.close();
	            	return;
	            }
	            
		    if(amount == null || StringHelper.isEmpty(amount)){amount = "";}
	            if(interest_rate == null || StringHelper.isEmpty(interest_rate)){interest_rate = "";}
	            if(period == null || StringHelper.isEmpty(period)){period = "";}
	            if(type == null || StringHelper.isEmpty(type)){type = "SIP";}
	            if(key == null || StringHelper.isEmpty(key)){key = "";}
	            
	            
	            amount = amount.trim();
	            interest_rate = interest_rate.trim();
	            period = period.trim();
	            type = type.trim();
				key = key.trim();
				
	            apirequest += "/calc/getELSSCalculatorResult?";
	            apirequest += "origin="+origin+"&";
	            apirequest += "ipAddr="+ipAddr+"&";
	            apirequest += "key="+key+"&";
	            apirequest += "investment_amount="+amount+"&";
	            apirequest += "interest_rate="+interest_rate+"&";
	            apirequest += "period="+period;
	            apirequest += "type="+type;
	            
	            String api = "/calc/getELSSCalculatorResult";
	            mutualFundApiAndIpValidityResponse = apiDao.isApiKeyAndIpValid(key, ipAddr, origin, api);
	            client_name = mutualFundApiAndIpValidityResponse.getClient_name();
	            
	            String apirequest1 = CommonUtils.formatApiRequest(apirequest);  
	            Integer id = apiDao.saveApiLogRequest(apirequest1, api, client_name, ipAddr, origin);
	            
	            if(mutualFundApiAndIpValidityResponse!=null && mutualFundApiAndIpValidityResponse.isValid())
	            {
	            	if(!NumberUtils.isParsable(period)) {
	            		result.setStatus(StatusMessage.FailureCode);
	                	result.setStatus_msg(StatusMessage.FailureMessage);
	                	result.setMsg("Invalid Period");	
	                	
	                	apiresponse = gson.toJson(result);
	                	apiDao.updateApiResponseById(id, apiresponse.length()>100?apiresponse.substring(0, 100):apiresponse, d1);                                       
	                	writer.print(apiresponse);
	        		    writer.close();
	                	return;
	            	}
	            	
	            	if(!NumberUtils.isParsable(amount) || Integer.parseInt(amount) < 1) {
	            		result.setStatus(StatusMessage.FailureCode);
	                	result.setStatus_msg(StatusMessage.FailureMessage);
	                	result.setMsg("Invalid Amount");	  
	                	
	                	apiresponse = gson.toJson(result);
	                	apiDao.updateApiResponseById(id, apiresponse.length()>100?apiresponse.substring(0, 100):apiresponse, d1);                                       
	                	writer.print(apiresponse);
	        		    writer.close();
	                	return;
	            	}
	            	
	            	if(!NumberUtils.isParsable(interest_rate)) {
	            		result.setStatus(StatusMessage.FailureCode);
	                	result.setStatus_msg(StatusMessage.FailureMessage);
	                	result.setMsg("Invalid Interest Rate");	 
	                	
	                	apiresponse = gson.toJson(result);
	                	apiDao.updateApiResponseById(id, apiresponse.length()>100?apiresponse.substring(0, 100):apiresponse, d1);                                       
	                	writer.print(apiresponse);
	        		    writer.close();
	                	return;
	            	}
	            	
	            	long investAmount = Long.parseLong(amount);
	            	double interestRate = Double.parseDouble(interest_rate);
	            	int years = Integer.parseInt(period); // in years
	            	long investedAmount = 0;
	            	long growthValue = 0;
	            	long maturityAmount = 0;

	            	if (type.equalsIgnoreCase("SIP")) {
	            		//convert year to month for SIP
	            	    int totalMonths = years * 12;
	            	    Double pv = 0.0;
	            	    Double finalAmount = TrackerUtils.getFutureValue(interestRate, totalMonths, investAmount * -1.0, pv, 1);
	            	    maturityAmount = Math.round(finalAmount);
	            	    investedAmount = investAmount * totalMonths;
	            	    growthValue = maturityAmount - investedAmount;
	            	}

	            	if (type.equalsIgnoreCase("Lumpsum")) {
	            		
	            	    double value1 = 1 + (interestRate / 100);
	            	    double value2 = Math.pow(value1, years);
	            	    double futureValue = investAmount * value2;
	            	    maturityAmount = Math.round(futureValue);
	            	    investedAmount = investAmount;
	            	    growthValue = maturityAmount - investedAmount;
	            	}
	            	
	            	result.setStatus(StatusMessage.SuccessCode);
	            	result.setStatus_msg(StatusMessage.SuccessMessage);
	            	result.setMsg(StatusMessage.SuccessMessage);
	            	result.setType(type);
	            	result.setInvested_amount(investedAmount);
	            	result.setGrowth_value(growthValue);
	            	result.setMaturity_amount(maturityAmount);
	            }
	            else
	            {
	            	result.setStatus(StatusMessage.FailureCode);
	            	result.setStatus_msg(StatusMessage.FailureMessage);
	            	result.setMsg(mutualFundApiAndIpValidityResponse.getValid_msg());
	            }
	            apiresponse = gson.toJson(result);
	            apiDao.updateApiResponseById(id, apiresponse.length()>100?apiresponse.substring(0, 100):apiresponse, d1);
	    		writer.print(apiresponse);
			    writer.close();
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
				writer.print(CommonUtils.getFailureResponse());
				writer.close();
			}
		}
*/
}
