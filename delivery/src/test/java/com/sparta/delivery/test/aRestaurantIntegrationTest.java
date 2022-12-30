package com.sparta.delivery.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class aRestaurantIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private final List<RestaurantDto> registeredRestaurants = new ArrayList<>();

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Nested
    @DisplayName("음식점 3개 등록 및 조회")
    class RegisterRestaurants {
        @Test
        @Order(1)
        @DisplayName("음식점1 등록")
        void test1() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(1_000)
                    .deliveryFee(0)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            RestaurantDto restaurantResponse = response.getBody();
            assertNotNull(restaurantResponse);
            assertTrue(restaurantResponse.id > 0);
            assertEquals(restaurantRequest.name, restaurantResponse.name);
            assertEquals(restaurantRequest.minOrderPrice, restaurantResponse.minOrderPrice);
            assertEquals(restaurantRequest.deliveryFee, restaurantResponse.deliveryFee);

            // 음식점 등록 성공 시, registeredRestaurants 에 추가
            registeredRestaurants.add(restaurantResponse);
        }

        @Test
        @Order(2)
        @DisplayName("음식점2 등록")
        void test2() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("자담치킨 강남점")
                    .minOrderPrice(100_000)
                    .deliveryFee(10_000)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            RestaurantDto restaurantResponse = response.getBody();
            assertNotNull(restaurantResponse);
            assertTrue(restaurantResponse.id > 0);
            assertEquals(restaurantRequest.name, restaurantResponse.name);
            assertEquals(restaurantRequest.minOrderPrice, restaurantResponse.minOrderPrice);
            assertEquals(restaurantRequest.deliveryFee, restaurantResponse.deliveryFee);

            // 음식점 등록 성공 시, registeredRestaurants 에 추가
            registeredRestaurants.add(restaurantResponse);
        }

        @Test
        @Order(3)
        @DisplayName("음식점3 등록")
        void test3() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("마라하오 위례점")
                    .minOrderPrice(100000)
                    .deliveryFee(10000)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            RestaurantDto restaurantResponse = response.getBody();
            assertNotNull(restaurantResponse);
            assertTrue(restaurantResponse.id > 0);
            assertEquals(restaurantRequest.name, restaurantResponse.name);
            assertEquals(restaurantRequest.minOrderPrice, restaurantResponse.minOrderPrice);
            assertEquals(restaurantRequest.deliveryFee, restaurantResponse.deliveryFee);

            // 음식점 등록 성공 시, registeredRestaurants 에 추가
            registeredRestaurants.add(restaurantResponse);
        }

        @Test
        @Order(4)
        @DisplayName("등록된 모든 음식점 조회")
        void test4() {
            // when
            ResponseEntity<RestaurantDto[]> response = restTemplate.getForEntity(
                    "/restaurants",
                    RestaurantDto[].class
            );

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            RestaurantDto[] responseRestaurants = response.getBody();
            assertNotNull(responseRestaurants);
            assertEquals(registeredRestaurants.size(), responseRestaurants.length);
            for (RestaurantDto responseRestaurant : responseRestaurants) {
                RestaurantDto registerRestaurant = registeredRestaurants.stream()
                        .filter(restaurant -> responseRestaurant.getId().equals(restaurant.getId()))
                        .findAny()
                        .orElse(null);

                assertNotNull(registerRestaurant);
                assertEquals(registerRestaurant.getName(), responseRestaurant.getName());
                assertEquals(registerRestaurant.getDeliveryFee(), responseRestaurant.getDeliveryFee());
                assertEquals(registerRestaurant.getMinOrderPrice(), responseRestaurant.getMinOrderPrice());
            }
        }
    }

    @Nested
    @DisplayName("최소주문 가격")
    class MinOrdersPrice {
        @Test
        @DisplayName("1,000원 미만 에러")
        void test1() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(500)
                    .deliveryFee(1000)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @Test
        @DisplayName("100,000원 초과 에러")
        void test2() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(100100)
                    .deliveryFee(1000)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @Test
        @DisplayName("100원 단위로 입력 안 됨 에러")
        void test3() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(2220)
                    .deliveryFee(1000)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Nested
    @DisplayName("기본 배달비")
    class DeliveryFee {
        @Test
        @DisplayName("0원 미만 에러")
        void test2() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(5000)
                    .deliveryFee(-500)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @Test
        @DisplayName("10,000원 초과 에러")
        void test3() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(5000)
                    .deliveryFee(20000)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @Test
        @DisplayName("500원 단위로 입력 안 됨 에러")
        void test4() throws JsonProcessingException {
            // given
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("쉐이크쉑 청담점")
                    .minOrderPrice(5000)
                    .deliveryFee(2200)
                    .build();

            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Getter
    @Setter
    @Builder
    static class RestaurantDto {
        private Long id;
        private String name;
        private int minOrderPrice;
        private int deliveryFee;
    }
}
