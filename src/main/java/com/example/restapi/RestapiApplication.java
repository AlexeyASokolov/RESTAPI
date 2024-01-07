package com.example.restapi;

import com.example.restapi.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
//5ebfebe7cb9786c496
@SpringBootApplication
public class RestapiApplication {

	private static final String URL_USER = "http://94.198.50.185:7081/api/users";
	private final HttpHeaders headers;
	private final RestTemplate restTemplate;
	private HttpEntity<?> httpEntity;

	public RestapiApplication(HttpHeaders headers, RestTemplate restTemplate,
							  HttpEntity<String> httpEntity) {
		this.headers = headers;
		this.restTemplate = restTemplate;
		this.httpEntity = httpEntity;
	}

	private HttpHeaders getHeaders(HttpHeaders headers) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		return headers;
	}

	private String sessionId() {
		ResponseEntity<String> response = restTemplate.exchange(URL_USER, HttpMethod.GET, httpEntity, String.class);
		return response.getHeaders().get("Set-Cookie").stream().findFirst().orElseThrow();
	}

	public String getAllUsers() {
		httpEntity = new HttpEntity<>(getHeaders(headers));
		ResponseEntity<String> response = restTemplate.exchange(URL_USER, HttpMethod.GET, httpEntity,  String.class);
		return response.getBody();
	}

	public String addUser(User user) {
		headers.add(HttpHeaders.COOKIE, sessionId());
		httpEntity = new HttpEntity<>(user, headers);
		ResponseEntity<String> response = restTemplate
				.exchange(URL_USER, HttpMethod.POST, httpEntity, String.class);
		return response.getBody();
	}

	public String updateUser(User user) {
		httpEntity = new HttpEntity<>(user, headers);
		ResponseEntity<String> response = restTemplate
				.exchange(URL_USER, HttpMethod.PUT, httpEntity, String.class);
		return response.getBody();
	}

	public String deleteUser(Long id) {
		httpEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate
				.exchange(URL_USER + "/" + id, HttpMethod.DELETE, httpEntity, String.class);
		return response.getBody();
	}

	public static void main(String[] args) {
		HttpHeaders headers1 = new HttpHeaders();
		RestTemplate restTemplate1 = new RestTemplate();
		HttpEntity<String> httpEntity = new HttpEntity<>(headers1);
		User user = new User(3L, "James", "Brown", (byte) 35);
		User updateUser = new User(3L, "Tomas", "Shelby", (byte) 55);

		RestapiApplication restApiApplication = new RestapiApplication(headers1, restTemplate1, httpEntity);
		System.out.println(restApiApplication.getAllUsers());
		System.out.println(restApiApplication.addUser(user));
		System.out.println(restApiApplication.getAllUsers());
		System.out.println(restApiApplication.updateUser(updateUser));
		System.out.println(restApiApplication.getAllUsers());
		System.out.println(restApiApplication.deleteUser(3L));
		System.out.println(restApiApplication.getAllUsers());
	}
}