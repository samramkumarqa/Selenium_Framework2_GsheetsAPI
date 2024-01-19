package com.graphql.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.pojos.GraphQLQuery;

import io.restassured.RestAssured;

public class GraphQLQueryTest {
	/* Developed by facebook.
	 * User an specify exactly they want in the query hi
	 * Result displays only the desired values as output
	 * hi test hj c
	 * */ 
	
	@Test
	public void getAllFilmsTest() {
		
		// https://swapi-graphql.netlify.app/.netlify/functions/index
		RestAssured.baseURI = "https://swapi-graphql.netlify.app";
		String query = "{\"query\":\"{\\n   allFilms {\\n films {\\n title\\n }\\n }\\n}\\n\",\"variables\":null}";
		
		given().log().all().contentType("application/json")
			.body(query)
				.when().log().all()
					.post("/.netlify/functions/index")
						.then().log().all()
							.assertThat()
								.statusCode(200)
									.and()
										.body("data.allFilms.films[0].title", equalTo("A New Hope"));
		
	}

	@Test
	public void getAllUsersTest() {
		RestAssured.baseURI = "https://hasura.io";
		String query = "{\"query\":\"{\\n  users (limit:10){\\n    id\\n    name\\n  }\\n}\\n\",\"variables\": null}";
		given().log().all()
			.contentType("application/json")
			.header("Authourization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik9FWTJSVGM1UlVOR05qSXhSRUV5TURJNFFUWXdNekZETWtReU1EQXdSVUV4UVVRM05EazFNQSJ9.eyJodHRwczovL2hhc3VyYS5pby9qd3QvY2xhaW1zIjp7IngtaGFzdXJhLWRlZmF1bHQtcm9sZSI6InVzZXIiLCJ4LWhhc3VyYS1hbGxvd2VkLXJvbGVzIjpbInVzZXIiXSwieC1oYXN1cmEtdXNlci1pZCI6ImF1dGgwfDY0NTMzZTNmODUxNTg0Y2QwZThmY2I4NyJ9LCJuaWNrbmFtZSI6InJhbWt1bWFyc2FtcWEiLCJuYW1lIjoicmFta3VtYXJzYW1xYUBnbWFpbC5jb20iLCJwaWN0dXJlIjoiaHR0cHM6Ly9zLmdyYXZhdGFyLmNvbS9hdmF0YXIvMGJjMjE0OTgyZTRlOThjMzAwZjA5ODZhNjgwNTEyYzU_cz00ODAmcj1wZyZkPWh0dHBzJTNBJTJGJTJGY2RuLmF1dGgwLmNvbSUyRmF2YXRhcnMlMkZyYS5wbmciLCJ1cGRhdGVkX2F0IjoiMjAyMy0wNS0xN1QwMzo1MDozOS41ODBaIiwiaXNzIjoiaHR0cHM6Ly9ncmFwaHFsLXR1dG9yaWFscy5hdXRoMC5jb20vIiwiYXVkIjoiUDM4cW5GbzFsRkFRSnJ6a3VuLS13RXpxbGpWTkdjV1ciLCJpYXQiOjE2ODQyOTU0NDAsImV4cCI6MTY4NDMzMTQ0MCwic3ViIjoiYXV0aDB8NjQ1MzNlM2Y4NTE1ODRjZDBlOGZjYjg3IiwiYXRfaGFzaCI6IlBZMmFHQTltOWl6eXU2TGJETWZfVHciLCJzaWQiOiJjUlRaVUxoVnNoSVc0Q2JaUUJGa0R1NHVRc3FxOW02ZSIsIm5vbmNlIjoiUDhLanF5fn5-UW92ZGNreFMwbFZFempRX3F1QmhhNzgifQ.RN--geW2jQcaIeZrDiqvhksmoU9ZBUzfvskr6qFFyUhyCaBusWwhmolQzmE5WlCjO1xKm55r2-YFRtI_ZfVoujIpNqGU18IBIsE0RjZqlQ20e5ySCLgMKDRJL9c-HXTSzPtMymQ_ZAsXTso3KxO2nqdKVpz0LYOy8yv-kyRmo2tUcYhnh-W_3T6dXmT-urYe7W-oNxhwZz2nJgK3PzC5taWUnoSLSNse6hFcN2RXPaE7hBEL3GHoYN_7CCbR1YFLBBnPyhGnEqJAUq8mZkFbItVtAuq9QZypjPUUNE2wtMDU7Vdip8uHA64E2371BIm-npidlXRwUf5FF3DihWbfUA")
				.body(query)
					.when().log().all()
						.post("/learn/graphql")
							.then().log().all()
								.assertThat()
									.statusCode(200)
										.body("data.users[0].name", equalTo("tui-glen"));
	}
	
	
	@DataProvider
	public Object[][] getQueryData() {
		return new Object[][] {{"10","joscool.o2"},
								{"5","me.darilla"}};
	}
	
	@Test(dataProvider = "getQueryData")
	public void getAllUsersTestWithDataTest(String limit, String name) {
		RestAssured.baseURI = "https://hasura.io";
		String query = "{\"query\":\"{\\n  users(limit:"+limit+", where: {name: {_eq: \\\""+name+"\\\"}}) {\\n    id\\n    name\\n  }\\n}\\n\",\"variables\":null}";
		given().log().all()
			.contentType("application/json")
			.header("Authourization","Bearer Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik9FWTJSVGM1UlVOR05qSXhSRUV5TURJNFFUWXdNekZETWtReU1EQXdSVUV4UVVRM05EazFNQSJ9.eyJodHRwczovL2hhc3VyYS5pby9qd3QvY2xhaW1zIjp7IngtaGFzdXJhLWRlZmF1bHQtcm9sZSI6InVzZXIiLCJ4LWhhc3VyYS1hbGxvd2VkLXJvbGVzIjpbInVzZXIiXSwieC1oYXN1cmEtdXNlci1pZCI6ImF1dGgwfDY0NTMzZTNmODUxNTg0Y2QwZThmY2I4NyJ9LCJuaWNrbmFtZSI6InJhbWt1bWFyc2FtcWEiLCJuYW1lIjoicmFta3VtYXJzYW1xYUBnbWFpbC5jb20iLCJwaWN0dXJlIjoiaHR0cHM6Ly9zLmdyYXZhdGFyLmNvbS9hdmF0YXIvMGJjMjE0OTgyZTRlOThjMzAwZjA5ODZhNjgwNTEyYzU_cz00ODAmcj1wZyZkPWh0dHBzJTNBJTJGJTJGY2RuLmF1dGgwLmNvbSUyRmF2YXRhcnMlMkZyYS5wbmciLCJ1cGRhdGVkX2F0IjoiMjAyMy0wNS0xN1QwMzo1MDozOS41ODBaIiwiaXNzIjoiaHR0cHM6Ly9ncmFwaHFsLXR1dG9yaWFscy5hdXRoMC5jb20vIiwiYXVkIjoiUDM4cW5GbzFsRkFRSnJ6a3VuLS13RXpxbGpWTkdjV1ciLCJpYXQiOjE2ODQyOTU0NDAsImV4cCI6MTY4NDMzMTQ0MCwic3ViIjoiYXV0aDB8NjQ1MzNlM2Y4NTE1ODRjZDBlOGZjYjg3IiwiYXRfaGFzaCI6IlBZMmFHQTltOWl6eXU2TGJETWZfVHciLCJzaWQiOiJjUlRaVUxoVnNoSVc0Q2JaUUJGa0R1NHVRc3FxOW02ZSIsIm5vbmNlIjoiUDhLanF5fn5-UW92ZGNreFMwbFZFempRX3F1QmhhNzgifQ.RN--geW2jQcaIeZrDiqvhksmoU9ZBUzfvskr6qFFyUhyCaBusWwhmolQzmE5WlCjO1xKm55r2-YFRtI_ZfVoujIpNqGU18IBIsE0RjZqlQ20e5ySCLgMKDRJL9c-HXTSzPtMymQ_ZAsXTso3KxO2nqdKVpz0LYOy8yv-kyRmo2tUcYhnh-W_3T6dXmT-urYe7W-oNxhwZz2nJgK3PzC5taWUnoSLSNse6hFcN2RXPaE7hBEL3GHoYN_7CCbR1YFLBBnPyhGnEqJAUq8mZkFbItVtAuq9QZypjPUUNE2wtMDU7Vdip8uHA64E2371BIm-npidlXRwUf5FF3DihWbfUA")
				.body(query)
					.when().log().all()
						.post("/learn/graphql")
							.then().log().all()
								.assertThat()
									.statusCode(200)
										.body("data.users[0].name", equalTo("tui-glen"));
	}
	
	@Test
	public void getAllUsers_WithPojoTest() {
		RestAssured.baseURI = "https://hasura.io";
		GraphQLQuery query = new GraphQLQuery();
		query.setQuery("query($limit:Int!){\n"
				+"	users(limit: $limit) {\n"
				+"	id\n"
				+"  name\n"
				+"}\n"
				+"}");
	}
}
