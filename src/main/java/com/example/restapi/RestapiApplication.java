package com.example.restapi;

import com.example.restapi.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

	public String sessionId() {
		ResponseEntity<String> response = restTemplate.exchange(URL_USER, HttpMethod.GET, httpEntity, String.class);
		List<String> cookies = response.getHeaders().get("Set-Cookie");
		if (cookies != null && !cookies.isEmpty()) {
			return cookies.stream().findFirst().orElseThrow();
		} else {
			throw new IllegalStateException("В заголовках ответа не найден идентификатор.");
		}
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

	public String deleteUser(long id) {
		httpEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate
				.exchange(URL_USER + "/" + id, HttpMethod.DELETE, httpEntity, String.class);
		return response.getBody();
	}

	public static void main(String[] args) {
		HttpHeaders headers1 = new HttpHeaders();
		RestTemplate restTemplate1 = new RestTemplate();
		HttpEntity<String> httpEntity = new HttpEntity<>(headers1);
		User user = new User(3, "James", "Brown", (byte) 21);
		User updateUser = new User(3, "Thomas", "Shelby", (byte) 21);

		RestapiApplication restApiApplication = new RestapiApplication(headers1, restTemplate1, httpEntity);
		System.out.println(restApiApplication.getAllUsers());
		System.out.println(restApiApplication.addUser(user));
		System.out.println(restApiApplication.getAllUsers());
		System.out.println(restApiApplication.updateUser(updateUser));
		System.out.println(restApiApplication.getAllUsers());
		System.out.println(restApiApplication.deleteUser(3));
		System.out.println(restApiApplication.getAllUsers());
	}
}