package com.gminds.employee_service;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.gminds.employee_service.steps", plugin = "pretty")
@ActiveProfiles("test")
public class CucumberIntegrationTest {
}
