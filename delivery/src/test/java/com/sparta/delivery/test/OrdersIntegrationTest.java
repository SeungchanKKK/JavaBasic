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
class OrdersIntegrationTest {
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
                .name("쉐이크쉑 청딤점")
                .minOrderPrice(5000)
                .deliveryFee(2000)
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

    @Test
    @Order(2)
    @DisplayName("음식점에 음식 3개 등록")
    void test2() throws JsonProcessingException {
        // given
        List<FoodDto> foodsRequest = new ArrayList<>();
        foodsRequest.add(food1);
        foodsRequest.add(food2);
        foodsRequest.add(food3);

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
    void test3() {
        // when
        Long restaurantId = registeredRestaurant.id;
        ResponseEntity<FoodIntegrationTest.FoodDto[]> response = restTemplate.getForEntity(
                "/restaurant/" + restaurantId + "/foods",
                FoodIntegrationTest.FoodDto[].class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FoodIntegrationTest.FoodDto[] foodsResponse = response.getBody();
        assertNotNull(foodsResponse);
        assertEquals(3, foodsResponse.length);
        // 음식 1 확인
        FoodIntegrationTest.FoodDto food1Response = Arrays.stream(foodsResponse)
                .filter(food -> food1.getName().equals(food.getName()))
                .findAny()
                .orElse(null);
        assertNotNull(food1Response);
        assertNotNull(food1Response.getId());
        assertEquals(food1.getName(), food1Response.getName());
        assertEquals(food1.getPrice(), food1Response.getPrice());
        food1.id = food1Response.getId();

        // 음식 2 확인
        FoodIntegrationTest.FoodDto food2Response = Arrays.stream(foodsResponse)
                .filter(food -> food2.getName().equals(food.getName()))
                .findAny()
                .orElse(null);
        assertNotNull(food2Response);
        assertNotNull(food2Response.getId());
        assertEquals(food2.getName(), food2Response.getName());
        assertEquals(food2.getPrice(), food2Response.getPrice());
        food2.id = food2Response.getId();

        // 음식 3 확인
        FoodIntegrationTest.FoodDto food3Response = Arrays.stream(foodsResponse)
                .filter(food -> food3.getName().equals(food.getName()))
                .findAny()
                .orElse(null);
        assertNotNull(food3Response);
        assertNotNull(food3Response.getId());
        assertEquals(food3.getName(), food3Response.getName());
        assertEquals(food3.getPrice(), food3Response.getPrice());
        food3.id = food3Response.getId();
    }

    @Test
    @Order(4)
    @DisplayName("주문하기")
    void test4() throws JsonProcessingException {
        // given
        Long restaurantId = registeredRestaurant.id;

        FoodOrderRequestDto foodOrderRequest1 = FoodOrderRequestDto.builder()
                .id(food1.id)
                .quantity(1)
                .build();

        FoodOrderRequestDto foodOrderRequest2 = FoodOrderRequestDto.builder()
                .id(food2.id)
                .quantity(2)
                .build();

        FoodOrderRequestDto foodOrderRequest3 = FoodOrderRequestDto.builder()
                .id(food3.id)
                .quantity(3)
                .build();

        List<FoodOrderRequestDto> foodOrderRequestDtos = new ArrayList<>();
        foodOrderRequestDtos.add(foodOrderRequest1);
        foodOrderRequestDtos.add(foodOrderRequest2);
        foodOrderRequestDtos.add(foodOrderRequest3);

        OrderRequestDto orderRequest = OrderRequestDto.builder()
                .restaurantId(restaurantId)
                .foods(foodOrderRequestDtos)
                .build();

        String requestBody = mapper.writeValueAsString(orderRequest);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                "/order/request",
                request,
                OrderDto.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderDto orderDto = response.getBody();
        assertNotNull(orderDto);
        // 음식점 이름
        assertEquals(registeredRestaurant.name, orderDto.restaurantName);

        // 음식 주문 확인
        assertEquals(3, orderDto.foods.size());
        // 음식1 주문 확인
        FoodOrderDto foodOrder1 = orderDto.foods.stream()
                .filter(foodOrderDto -> foodOrderDto.name.equals(food1.getName()))
                .findAny().orElse(null);
        assertNotNull(foodOrder1);
        assertEquals(food1.name, foodOrder1.name);
        assertEquals(foodOrder1.quantity, foodOrder1.quantity);
        assertEquals(10900, foodOrder1.price);
        // 음식2 주문 확인
        FoodOrderDto foodOrder2 = orderDto.foods.stream()
                .filter(foodOrderDto -> foodOrderDto.name.equals(food2.getName()))
                .findAny().orElse(null);
        assertNotNull(foodOrder2);
        assertEquals(food2.name, foodOrder2.name);
        assertEquals(foodOrder2.quantity, foodOrder2.quantity);
        assertEquals(9800, foodOrder2.price);
        // 음식3 주문 확인
        FoodOrderDto foodOrder3 = orderDto.foods.stream()
                .filter(foodOrderDto -> foodOrderDto.name.equals(food3.getName()))
                .findAny().orElse(null);
        assertNotNull(foodOrder3);
        assertEquals(food3.name, foodOrder3.name);
        assertEquals(foodOrder3.quantity, foodOrder3.quantity);
        assertEquals(17700, foodOrder3.price);

        // 배달비 확인
        assertEquals(2000, orderDto.deliveryFee);

        // 총 결제 금액 확인
        assertEquals(40400, orderDto.totalPrice);
    }

    @Test
    @Order(5)
    @DisplayName("주문하기 - 음식 주문 수량 1 미만 에러")
    void test5() throws JsonProcessingException {
        // given
        Long restaurantId = registeredRestaurant.id;

        FoodOrderRequestDto foodOrderRequest1 = FoodOrderRequestDto.builder()
                .id(food1.id)
                .quantity(0)
                .build();

        List<FoodOrderRequestDto> foodOrderRequestDtos = new ArrayList<>();
        foodOrderRequestDtos.add(foodOrderRequest1);

        OrderRequestDto orderRequest = OrderRequestDto.builder()
                .restaurantId(restaurantId)
                .foods(foodOrderRequestDtos)
                .build();

        String requestBody = mapper.writeValueAsString(orderRequest);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                "/order/request",
                request,
                OrderDto.class);

        // then
        assertTrue(
                response.getStatusCode() == HttpStatus.BAD_REQUEST
                        || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Test
    @Order(6)
    @DisplayName("주문하기 - 음식 주문 수량 100 초과 에러")
    void test6() throws JsonProcessingException {
        // given
        Long restaurantId = registeredRestaurant.id;

        FoodOrderRequestDto foodOrderRequest1 = FoodOrderRequestDto.builder()
                .id(food1.id)
                .quantity(101)
                .build();

        List<FoodOrderRequestDto> foodOrderRequestDtos = new ArrayList<>();
        foodOrderRequestDtos.add(foodOrderRequest1);

        OrderRequestDto orderRequest = OrderRequestDto.builder()
                .restaurantId(restaurantId)
                .foods(foodOrderRequestDtos)
                .build();

        String requestBody = mapper.writeValueAsString(orderRequest);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                "/order/request",
                request,
                OrderDto.class);

        // then
        assertTrue(
                response.getStatusCode() == HttpStatus.BAD_REQUEST
                        || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Test
    @Order(7)
    @DisplayName("주문하기 - 음식점의 최소주문 가격 미만 시 에러")
    void test7() throws JsonProcessingException {
        // given
        Long restaurantId = registeredRestaurant.id;

        FoodOrderRequestDto foodOrderRequest1 = FoodOrderRequestDto.builder()
                .id(food2.id)
                .quantity(1)
                .build();

        List<FoodOrderRequestDto> foodOrderRequestDtos = new ArrayList<>();
        foodOrderRequestDtos.add(foodOrderRequest1);

        OrderRequestDto orderRequest = OrderRequestDto.builder()
                .restaurantId(restaurantId)
                .foods(foodOrderRequestDtos)
                .build();

        String requestBody = mapper.writeValueAsString(orderRequest);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                "/order/request",
                request,
                OrderDto.class);

        // then
        assertTrue(
                response.getStatusCode() == HttpStatus.BAD_REQUEST
                        || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Test
    @Order(8)
    @DisplayName("주문 조회하기")
    void test8() {
        // when
        ResponseEntity<OrderDto[]> response = restTemplate.getForEntity(
                "/orders",
                OrderDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);

        OrderDto orderDto = response.getBody()[0];
        // 음식점 이름
        assertEquals(registeredRestaurant.name, orderDto.restaurantName);

        // 음식 주문 확인
        assertEquals(3, orderDto.foods.size());
        // 음식1 주문 확인
        FoodOrderDto foodOrder1 = orderDto.foods.stream()
                .filter(foodOrderDto -> foodOrderDto.name.equals(food1.getName()))
                .findAny().orElse(null);
        assertNotNull(foodOrder1);
        assertEquals(food1.name, foodOrder1.name);
        assertEquals(foodOrder1.quantity, foodOrder1.quantity);
        assertEquals(10900, foodOrder1.price);
        // 음식2 주문 확인
        FoodOrderDto foodOrder2 = orderDto.foods.stream()
                .filter(foodOrderDto -> foodOrderDto.name.equals(food2.getName()))
                .findAny().orElse(null);
        assertNotNull(foodOrder2);
        assertEquals(food2.name, foodOrder2.name);
        assertEquals(foodOrder2.quantity, foodOrder2.quantity);
        assertEquals(9800, foodOrder2.price);
        // 음식3 주문 확인
        FoodOrderDto foodOrder3 = orderDto.foods.stream()
                .filter(foodOrderDto -> foodOrderDto.name.equals(food3.getName()))
                .findAny().orElse(null);
        assertNotNull(foodOrder3);
        assertEquals(food3.name, foodOrder3.name);
        assertEquals(foodOrder3.quantity, foodOrder3.quantity);
        assertEquals(17700, foodOrder3.price);

        // 배달비 확인
        assertEquals(2000, orderDto.deliveryFee);

        // 총 결제 금액 확인
        assertEquals(40400, orderDto.totalPrice);
    }

    @Getter
    @Setter
    @Builder
    static class OrderRequestDto {
        private Long restaurantId;
        private List<FoodOrderRequestDto> foods;
    }

    @Getter
    @Setter
    @Builder
    static class FoodOrderRequestDto {
        Long id;
        int quantity;
    }

    @Getter
    @Setter
    static class OrderDto {
        private String restaurantName;
        private List<FoodOrderDto> foods;
        private int deliveryFee;
        private int totalPrice;
    }

    @Getter
    @Setter
    static class FoodOrderDto {
        String name;
        int quantity;
        int price;
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