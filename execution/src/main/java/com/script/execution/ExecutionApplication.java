package com.script.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class ExecutionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecutionApplication.class, args);
		List<dsa.program.VulnerabilityScript> scripts = Arrays.asList(
				new dsa.program.VulnerabilityScript(1, Arrays.asList(2, 3)),
				new dsa.program.VulnerabilityScript(2, Arrays.asList(4)),
				new dsa.program.VulnerabilityScript(3, Arrays.asList(4)),
				new dsa.program.VulnerabilityScript(4, Collections.emptyList())
		);

		MyExecutionPlan myExecutionPlan = new MyExecutionPlan();
		List<Integer> executionPlan = myExecutionPlan.generateExecutionPlan(scripts);
		System.out.println("Execution Plan: " + executionPlan);
	}

}
