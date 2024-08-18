package com.gminds.employee_service;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.gminds.employee_service.steps")
public class CucumberIntegrationTest {
}
