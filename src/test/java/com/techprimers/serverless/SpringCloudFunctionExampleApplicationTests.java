package com.techprimers.serverless;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringCloudFunctionExampleApplicationTests {

	@Test
	public void successfulResponse() {
		App app = new App();
		APIGatewayProxyResponseEvent result = app.handleRequest(null, null);
		assertEquals(result.getStatusCode().intValue(), 200);
		assertEquals(result.getHeaders().get("Content-Type"), "application/json");
		String content = result.getBody();
		assertNotNull(content);
		assertTrue(content.contains("\"message\""));
		assertTrue(content.contains("\"hello from lambda 777\""));
		assertTrue(content.contains("\"location\""));
	}
}
