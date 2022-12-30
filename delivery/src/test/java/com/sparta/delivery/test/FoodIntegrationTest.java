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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FoodIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private RestaurantDto registeredRestaurant;

    private FoodDto food1 = FoodDto.builder()
            .id(null)
            .name("쉑버거 더블")
            .price(10900)
            .build();

    private FoodDto food2 = FoodDto.builder()
            .id(null)
            .name("치즈 감자튀김")
            .price(4900)
            .build();

    private FoodDto food3 = FoodDto.builder()
            .id(null)
            .name("쉐이크")
            .price(5900)
            .build();

    private FoodDto food4 = FoodDto.builder()
            .id(null)
            .name("스트로베리베리")
            .price(11400)
            .build();

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(1)
    @DisplayName("음식점1 등록")
    void test1() throws JsonProcessingException {
        // given
        RestaurantDto restaurantRequest = RestaurantDto.builder()
                .id(null)
                .name("쉐이크쉑 청돔점")
                .minOrderPrice(5000)
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
        assertEquals(HttpStatus.OK, response.getStatusCode());

        RestaurantDto restaurantResponse = response.getBody();
        assertNotNull(restaurantResponse);
        assertTrue(restaurantResponse.id > 0);
        assertEquals(restaurantRequest.name, restaurantResponse.name);
        assertEquals(restaurantRequest.minOrderPrice, restaurantResponse.minOrderPrice);
        assertEquals(restaurantRequest.deliveryFee, restaurantResponse.deliveryFee);

        // 음식점 등록 성공 시, registeredRestaurant 에 할당
        registeredRestaurant = restaurantResponse;
    }

    @Nested
    @DisplayName("음식점에 음식 3개 등록 및 메뉴판 조회")
    class RegisterRestaurants {
        @Test
        @Order(1)
        @DisplayName("음식 1개 등록")
        void test1() throws JsonProcessingException {
            // given
            List<FoodDto> foodsRequest = new ArrayList<>();
            // 음식2 추가
            foodsRequest.add(food2);

            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        @Order(2)
        @DisplayName("음식 2개 등록")
        void test2() throws JsonProcessingException {
            // given
            List<FoodDto> foodsRequest = new ArrayList<>();

            // 음식3 추가
            foodsRequest.add(food3);

            // 음식4 추가
            foodsRequest.add(food4);

            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        @Order(3)
        @DisplayName("음식점 메뉴판 조회")
        void test3() throws JsonProcessingException {
            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<FoodDto[]> response = restTemplate.getForEntity(
                    "/restaurant/" + restaurantId + "/foods",
                    FoodDto[].class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            FoodDto[] foodsResponse = response.getBody();
            assertNotNull(foodsResponse);
            assertEquals(4, foodsResponse.length);
            // 음식 1 확인
            FoodDto food1Response = Arrays.stream(foodsResponse)
                    .filter(food -> food1.getName().equals(food.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(food1Response);
            assertNotNull(food1Response.getId());
            assertEquals(food1.getName(), food1Response.getName());
            assertEquals(food1.getPrice(), food1Response.getPrice());

            // 음식 2 확인
            FoodDto food2Response = Arrays.stream(foodsResponse)
                    .filter(food -> food2.getName().equals(food.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(food2Response);
            assertNotNull(food2Response.getId());
            assertEquals(food2.getName(), food2Response.getName());
            assertEquals(food2.getPrice(), food2Response.getPrice());

            // 음식 3 확인
            FoodDto food3Response = Arrays.stream(foodsResponse)
                    .filter(food -> food3.getName().equals(food.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(food3Response);
            assertNotNull(food3Response.getId());
            assertEquals(food3.getName(), food3Response.getName());
            assertEquals(food3.getPrice(), food3Response.getPrice());

            // 음식 4 확인
            FoodDto food4Response = Arrays.stream(foodsResponse)
                    .filter(food -> food4.getName().equals(food.getName()))
                    .findAny()
                    .orElse(null);
            assertNotNull(food4Response);
            assertNotNull(food4Response.getId());
            assertEquals(food4.getName(), food4Response.getName());
            assertEquals(food4.getPrice(), food4Response.getPrice());
        }
    }

    @Nested
    @DisplayName("음식명")
    class FoodName {
        @Test
        @Order(1)
        @DisplayName("음식 1개 등록")
        void test1() throws JsonProcessingException {
            // given
            List<FoodDto> foodsRequest = new ArrayList<>();
            // 음식1 추가
            foodsRequest.add(food1);

            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        @Order(2)
        @DisplayName("기존 저장된 음식명과 중복")
        void test2() throws JsonProcessingException {
            // given
            List<FoodDto> foodsRequest = new ArrayList<>();
            // 음식1 추가
            foodsRequest.add(food1);
            // 음식2 추가
            foodsRequest.add(food2);

            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @Test
        @Order(3)
        @DisplayName("입력된 음식명에 중복 에러")
        void test3() throws JsonProcessingException {
            // given
            List<FoodDto> foodsRequest = new ArrayList<>();
            // 음식2 추가 (음식명: "치즈 감자튀김")
            foodsRequest.add(food2);
            // 음식3 추가 (음식명: "쉐이크")
            foodsRequest.add(food3);
            // 음식2 중복 추가 (음식명: "치즈 감자튀김")
            foodsRequest.add(food2);

            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @Test
        @Order(4)
        @DisplayName("음식점 2에 음식명 1에 등록되어 있는 음식명 입력 가능")
        void test4() throws JsonProcessingException {
            // given
            // 음식점2 추가
            RestaurantDto restaurantRequest = RestaurantDto.builder()
                    .id(null)
                    .name("베스킨라빈스 이태원점")
                    .minOrderPrice(13000)
                    .deliveryFee(5000)
                    .build();
            String requestBody = mapper.writeValueAsString(restaurantRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<RestaurantDto> response = restTemplate.postForEntity(
                    "/restaurant/register",
                    request,
                    RestaurantDto.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            RestaurantDto restaurantDto2 = response.getBody();

            //  추가 시도
            List<FoodDto> foodsRequest = new ArrayList<>();
            // 음식점1에 등록되어 있는 음식1 을 음식점2에 추가
            foodsRequest.add(food1);


            String foodRequestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> foodRequest = new HttpEntity<>(foodRequestBody, headers);

            // when
            Long restaurant2Id = restaurantDto2.id;
            ResponseEntity<Object> foodResponse = restTemplate.postForEntity(
                    "/restaurant/" + restaurant2Id + "/food/register",
                    foodRequest,
                    Object.class);

            // then
            assertEquals(HttpStatus.OK, foodResponse.getStatusCode());
        }
    }

    @Nested
    @DisplayName("음식 가격")
    class FoodPrice {
        @Test
        @DisplayName("100원 미만 에러")
        void test1() throws JsonProcessingException {
            // given
            List<FoodDto> foodsRequest = new ArrayList<>();

            foodsRequest.add(
                    FoodDto.builder()
                            .id(null)
                            .name("치즈 감자튀김")
                            .price(0)
                            .build()
            );


            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

            // then
            assertTrue(
                    response.getStatusCode() == HttpStatus.BAD_REQUEST
                            || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        @Test
        @DisplayName("1,000,000원 초과 에러")
        void test2() throws JsonProcessingException {
            // given
            List<FoodDto> foodsRequest = new ArrayList<>();

            foodsRequest.add(
                    FoodDto.builder()
                            .id(null)
                            .name("치즈 감자튀김")
                            .price(1_000_100)
                            .build()
            );


            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

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
            List<FoodDto> foodsRequest = new ArrayList<>();

            foodsRequest.add(
                    FoodDto.builder()
                            .id(null)
                            .name("치즈 감자튀김")
                            .price(770)
                            .build()
            );


            String requestBody = mapper.writeValueAsString(foodsRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            Long restaurantId = registeredRestaurant.id;
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/restaurant/" + restaurantId + "/food/register",
                    request,
                    Object.class);

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

    @Getter
    @Setter
    @Builder
    static class FoodDto {
        private Long id;
        private String name;
        private int price;
    }
}