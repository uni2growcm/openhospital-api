package org.isf.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json;charset=UTF-8");

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
		body.put("status", HttpServletResponse.SC_FORBIDDEN);
		body.put("error", "Forbidden");
		body.put("message", "You do not have permission to access this resource."
		);
		body.put("path", request.getRequestURI());

		mapper.writeValue(response.getOutputStream(), body);
	}
}
