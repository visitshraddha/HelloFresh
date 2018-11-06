package HelloFresh;



import org.json.JSONObject;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.*;
import java.util.List;

public class RestApi {

	/*
	 * This Test to Get all countries and validate that US, DE and GB were
	 * returned in the response
	 */

	@Test
	public static void VerifyISOCodesInResponse() {

		Response response = when().get("http://services.groupkt.com/country/get/all").then()
				.contentType(ContentType.JSON). // check that the content type
												// return from the API is JSON
				extract().response(); // extract the response

		List<String> ISOCodes = response.path("RestResponse.result.alpha2_code");
		for (String iso : ISOCodes) {
			if (iso.equals("DE")) {
				System.out.println("DE City found");
			} else if (iso.equals("GB")) {
				System.out.println("GB City found");
			} else if (iso.equals("US")) {
				System.out.println("US city found");
			}
		}

	}

	/*
	 * This Test to Get each country (US, DE and GB) individually and validate
	 * the response
	 */

	public class CountriesSearchTests {

		/* This Test to Get US individually and validate the response */

		@Test
		public void verifyResponseUsingTheCodeUS() {
			RestAssured.when().get("http://services.groupkt.com/country/get/iso2code/US").then().assertThat()
					.statusCode(200).and().body("RestResponse.messages", contains("Country found matching code [US]."))
					.and().body("RestResponse.result.name", is("United States of America")).and()
					.body("RestResponse.result.alpha2_code", is("US")).and()
					.body("RestResponse.result.alpha3_code", is("USA"));
		}

		/* This Test to Get DE individually and validate the response */
		@Test
		public void verifyResponseUsingTheCodeDE() {
			RestAssured.when().get("http://services.groupkt.com/country/get/iso2code/DE").then().assertThat()
					.statusCode(200).and().body("RestResponse.messages", contains("Country found matching code [DE]."))
					.and().body("RestResponse.result.name", is("Germany")).and()
					.body("RestResponse.result.alpha2_code", is("DE")).and()
					.body("RestResponse.result.alpha3_code", is("DEU"));
		}

		/* This Test to Get GB individually and validate the response */
		@Test
		public void verifyResponseUsingTheCodeGB() {
			RestAssured.when().get("http://services.groupkt.com/country/get/iso2code/GB").then().assertThat()
					.statusCode(200).and().and()
					.body("RestResponse.messages", contains("Country found matching code [GB].")).and()
					.body("RestResponse.result.name", is("United Kingdom of Great Britain and Northern Ireland")).and()
					.body("RestResponse.result.alpha2_code", is("GB")).and()
					.body("RestResponse.result.alpha3_code", is("GBR"));
		}
	}

	/*
	 * This Test to get information for inexistent countries and validate the
	 * response
	 */

	@Test
	public void verifyResponseUsingNonExistantCode() {
		RestAssured.when().get("http://services.groupkt.com/country/get/iso2code/CP").then().assertThat()
				.statusCode(200).and().and()
				.body("RestResponse.messages", contains("No matching country found for requested code [CP]."));
	}

	/*
	 * This Test to validate new country addition using POST and validate the
	 * response
	 */

	@Test
	public void verifyResponseUsingPOST() {
		JSONObject jsonObj = new JSONObject().put("name", "Test Country").put("alpha2_code", "XY").put("alpha3_code",
				"TCY");

		given().contentType("application/json") // another way to specify
												// content type
				.body(jsonObj.toString()) // use jsonObj toString method
				.when().post("http://services.groupkt.com/country/get/all");

		RestAssured.when().get("http://services.groupkt.com/country/get/iso2code/XY").then().assertThat()
				.statusCode(200).and().and()
				.body("RestResponse.messages", contains("Country found matching code [XY]."));

	}
}


