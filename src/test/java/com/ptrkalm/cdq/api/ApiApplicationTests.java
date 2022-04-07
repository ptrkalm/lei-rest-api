package com.ptrkalm.cdq.api;

import com.ptrkalm.cdq.api.responses.RestResponse;
import com.ptrkalm.cdq.api.responses.RestResponseError;
import com.ptrkalm.cdq.api.responses.RestResponsePaged;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void getRelationshipsDefaultParamsTest() {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/relationships", RestResponsePaged.class))
				.hasFieldOrPropertyWithValue("status", 200)
				.hasFieldOrPropertyWithValue("pageNumber", 0L)
				.hasFieldOrPropertyWithValue("pageSize", 100L);
	}

	@Test
	void getRelationshipsPageSize0Test() {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/relationships?page_size=0", RestResponsePaged.class))
				.hasFieldOrPropertyWithValue("status", 200)
				.hasFieldOrPropertyWithValue("pageNumber", 0L)
				.hasFieldOrPropertyWithValue("pageSize", 0L)
				.hasFieldOrPropertyWithValue("result", new ArrayList<>());
	}

	@Test
	void getRelationshipsPageSizeMinus1Test() {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/relationships?page_size=-1", RestResponseError.class))
				.hasFieldOrPropertyWithValue("status", 404)
				.hasFieldOrPropertyWithValue("result", null)
				.hasFieldOrPropertyWithValue("errorMassage", "Unsupported endpoint or invalid param.");
	}

	@Test
	void getRelationshipsPageNumberMinus1Test() {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/relationships?page=-1", RestResponseError.class))
				.hasFieldOrPropertyWithValue("status", 404)
				.hasFieldOrPropertyWithValue("result", null)
				.hasFieldOrPropertyWithValue("errorMassage", "Unsupported endpoint or invalid param.");
	}

	@Test
	void getRelationshipsByNodeDefaultParamsTest() {
		assertThat(restTemplate.getForObject(
				"http://localhost:" + port + "/nodes/2138006TODRZ9YDK3H64/relationships", RestResponsePaged.class))
				.hasFieldOrPropertyWithValue("status", 200)
				.hasFieldOrProperty("result")
				.hasFieldOrPropertyWithValue("totalResults", 2L)
				.hasFieldOrPropertyWithValue("pageNumber", 0L)
				.hasFieldOrPropertyWithValue("pageSize", 100L);
	}

	@Test
	void getRelationshipsStatisticsTest() {
		assertThat(restTemplate.getForObject(
						"http://localhost:" + port + "/relationships/statistics", RestResponse.class))
				.hasFieldOrPropertyWithValue("status", 200);
	}

	@Test
	void getNotExistingEndpoint() {
		assertThat(restTemplate.getForObject(
				"http://localhost:" + port + "/nodes", RestResponseError.class))
				.hasFieldOrPropertyWithValue("status", 404)
				.hasFieldOrPropertyWithValue("result", null)
				.hasFieldOrPropertyWithValue("errorMassage", "Unsupported endpoint or invalid param.");
	}

}
