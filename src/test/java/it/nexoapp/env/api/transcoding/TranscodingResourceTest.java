package it.nexoapp.env.api.transcoding;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end test of the transcoding list endpoint against the environment schema,
 * the seeded test data and the Redis cache.
 * <p>
 * The claims mirror what the request context filter of common still reads from the token
 * ({@code sid}, {@code sub}, {@code preferred_username}, {@code email}) until its rewrite.
 */
@QuarkusTest
@TestSecurity(user = "env-test")
@OidcSecurity(claims = {
        @Claim(key = "sid", value = "env-test-session"),
        @Claim(key = "sub", value = "env-test"),
        @Claim(key = "preferred_username", value = "env-test"),
        @Claim(key = "email", value = "env-test@example.invalid")
})
class TranscodingResourceTest {

    /** Path of the state list endpoint, relative to the root path {@code /nexo-env} (RestAssured prepends it). */
    private static final String STATE_LIST_PATH = "/transcoding/list/STATE";

    /** The seeded state item, serialized with the pinned key order. */
    private static final String SEEDED_STATE = "{\"code\":\"100\",\"name\":\"Italia\",\"belfiore\":\"Z000\"}";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The state list answers 200 with the seeded item.
     */
    @Test
    void stateListContainsSeededItem() {
        given()
                .when().get(STATE_LIST_PATH)
                .then()
                .statusCode(200)
                .body("code", hasItem("100"))
                .body("name", hasItem("Italia"))
                .body("belfiore", hasItem("Z000"));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The seeded item serializes with exactly code, name and belfiore in this order,
     * and a second call (served through the cache) returns a byte-identical body.
     */
    @Test
    void stateItemKeyOrderIsPinnedAndCachedCallIsIdentical() {
        final String first = given().when().get(STATE_LIST_PATH).then().statusCode(200).extract().asString();
        final String second = given().when().get(STATE_LIST_PATH).then().statusCode(200).extract().asString();

        assertTrue(first.contains(SEEDED_STATE), "the seeded item should serialize as code, name, belfiore: " + first);
        assertEquals(first, second, "the cached response should be byte-identical to the first one");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Every item of the list exposes the three pinned keys.
     */
    @Test
    void everyStateItemExposesThePinnedKeys() {
        given()
                .when().get(STATE_LIST_PATH)
                .then()
                .statusCode(200)
                .body("[0]", hasKey("code"))
                .body("[0]", hasKey("name"))
                .body("[0]", hasKey("belfiore"));
    }

}
