package io.pivotal.json.jsonview;

import io.pivotal.json.jsonview.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JsonViewApplicationTests {

    @Value("${local.server.port}")
    String port;

    @Test
    void test_V1Item_accepts() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(new MediaType("application", "vnd.jsonview.items.v1+json")));

        ResponseEntity<Item> response = restTemplate
                .exchange("http://localhost:" + port + "/items/1", HttpMethod.GET, new HttpEntity<>(headers), Item.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Item v1Item = response.getBody();

        assertThat(v1Item.getId()).isEqualTo(1);
        assertThat(v1Item.getItemName()).isNotBlank();
        assertThat(v1Item.getOwnerName()).isNull();
        assertThat(v1Item.getOwnerCreditCard()).isNull();
    }

    @Test
    void test_V2Item_accepts() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(new MediaType("application", "vnd.jsonview.items.v2+json")));

        ResponseEntity<Item> response = restTemplate
                .exchange("http://localhost:" + port + "/items/2", HttpMethod.GET, new HttpEntity<>(headers), Item.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Item v1Item = response.getBody();

        assertThat(v1Item.getId()).isEqualTo(2);
        assertThat(v1Item.getItemName()).isNotBlank();
        assertThat(v1Item.getOwnerName()).isNotBlank();
        assertThat(v1Item.getOwnerCreditCard()).isNull();
    }

    @Test
    void test_V3Item_accepts() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(new MediaType("application", "vnd.jsonview.items.v3+json")));

        ResponseEntity<Item> response = restTemplate
                .exchange("http://localhost:" + port + "/items/3", HttpMethod.GET, new HttpEntity<>(headers), Item.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Item v1Item = response.getBody();

        assertThat(v1Item.getId()).isEqualTo(3);
        assertThat(v1Item.getItemName()).isNotBlank();
        assertThat(v1Item.getOwnerName()).isNotBlank();
        assertThat(v1Item.getOwnerCreditCard()).isNotBlank();
    }

    @Test
    void test_Item_MissingAccepts() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            restTemplate
                    .exchange("http://localhost:" + port + "/items/1", HttpMethod.GET, new HttpEntity<>(headers), Item.class);
            fail("Did not get NOT_ACCEPTABLE");
        } catch (HttpStatusCodeException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @Test
    void test_V1Item_uri() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Item> response = restTemplate
                .exchange("http://localhost:" + port + "/v1/items/1", HttpMethod.GET, new HttpEntity<>(headers), Item.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Item v1Item = response.getBody();

        assertThat(v1Item.getId()).isEqualTo(1);
        assertThat(v1Item.getItemName()).isNotBlank();
        assertThat(v1Item.getOwnerName()).isNull();
        assertThat(v1Item.getOwnerCreditCard()).isNull();
    }

    @Test
    void test_V2Item_uri() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Item> response = restTemplate
                .exchange("http://localhost:" + port + "/v2/items/2", HttpMethod.GET, new HttpEntity<>(headers), Item.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Item v1Item = response.getBody();

        assertThat(v1Item.getId()).isEqualTo(2);
        assertThat(v1Item.getItemName()).isNotBlank();
        assertThat(v1Item.getOwnerName()).isNotBlank();
        assertThat(v1Item.getOwnerCreditCard()).isNull();
    }

    @Test
    void test_V3Item() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Item> response = restTemplate
                .exchange("http://localhost:" + port + "/v3/items/3", HttpMethod.GET, new HttpEntity<>(headers), Item.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Item v1Item = response.getBody();

        assertThat(v1Item.getId()).isEqualTo(3);
        assertThat(v1Item.getItemName()).isNotBlank();
        assertThat(v1Item.getOwnerName()).isNotBlank();
        assertThat(v1Item.getOwnerCreditCard()).isNotBlank();
    }

}
