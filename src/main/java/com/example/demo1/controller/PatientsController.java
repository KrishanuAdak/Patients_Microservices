package com.example.demo1.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import com.example.demo1.config.UserDetailsServiceImpl;
import com.example.demo1.exception.ResourceNotFoundException;
import com.example.demo1.model.Available_Doctor_Respponse;
import com.example.demo1.model.Doctor;
import com.example.demo1.model.Patients;
import com.example.demo1.openFeign.OpenFeign;
import com.example.demo1.service.PatientsService;
import com.example.demo1.util.JwtUtil;

@RestController
@RequestMapping("/patients/v1")
public class PatientsController {
    
	
	@Autowired
	private PatientsService patientsService;
	

	
	@Autowired(required=true)
	private OpenFeign openFeign;  
	
	@Autowired
	private Environment env;
	
	
	@GetMapping("/home")
	public ResponseEntity<?> home(@RequestHeader("X-User-Id") String userid,
	        @RequestHeader("X-Role") String role) {
		System.out.println(env.getProperty("server.port"));
		if(role.equalsIgnoreCase("patient") || role.equalsIgnoreCase("admin")) {
			return ResponseEntity.status(HttpStatus.OK).body("welcome to Patients portal!! Mr. "+userid); 
	
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You dont have access to view");
	}
	

	
	
	@GetMapping("/get-doctors") 
	public List<Doctor> getAll()
	{
		return this.openFeign.getAllDoctors();
	}
	
	
	@GetMapping("/available/now")
	public List<Available_Doctor_Respponse> getAllDoctors()
	{
		List<Available_Doctor_Respponse> li=this.openFeign.getAvailableDoctorsFromPatients();
		if(li.size()==0)
		{
			throw new ResourceNotFoundException("No Doctors available now!! \n set your alert once they are available let you know:)");
		}
		return li;
	}



	
	


}
