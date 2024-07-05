//package com.sparta.javajyojo;
//
//import com.sparta.javajyojo.entity.*;
//import com.sparta.javajyojo.enums.OrderStatus;
//import com.sparta.javajyojo.enums.UserRoleEnum;
//import com.sparta.javajyojo.repository.*;
//import jakarta.annotation.PostConstruct;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private final MenuRepository menuRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final OrderRepository orderRepository;
//    private final ReviewRepository reviewRepository;
//    private final OrderDetailRepository orderDetailRepository;
//    private final PasswordHistoryRepository passwordHistoryRepository;
//
//    @PostConstruct
//    @Transactional
//    public void init() {
//        List<Menu> menuList =
//                List.of(
//                        new Menu("후라이드", 14900),
//                        new Menu("양념", 14900),
//                        new Menu("후라이드반/양념반", 14900),
//                        new Menu("떡볶이", 5000),
//                        new Menu("콜라", 2000),
//                        new Menu("사이다", 2000)
//                );
//        menuRepository.saveAll(menuList);
//
//        List<User> userList =
//                List.of(
//                        new User("admin", passwordEncoder.encode("A1234qwer!"), "손호찬", "한마디!", UserRoleEnum.valueOf("ADMIN")),
//                        new User("equis3351", passwordEncoder.encode("A1234qwer!"), "남현", "두마디!", UserRoleEnum.valueOf("WITHDRAW")),
//                        new User("gaeun7", passwordEncoder.encode("A1234qwer!"), "김가은", "세마디!", UserRoleEnum.valueOf("USER")),
//                        new User("kim2016", passwordEncoder.encode("A1234qwer!"), "김현성", "네마디!", UserRoleEnum.valueOf("USER")),
//                        new User("ael1197", passwordEncoder.encode("A1234qwer!"), "손아엘", "다섯마디!", UserRoleEnum.valueOf("USER"))
//                );
//        userRepository.saveAll(userList);
//
//        List<Order> orderList =
//                List.of(
//                        new Order(
//                                userRepository.findByUsername("gaeun7").get(),
//                                "문 앞에 놔주세요",
//                                "서울시 영등포구 여의도동 123-45",
//                                OrderStatus.NEW,
//                                14900),
//                        new Order(
//                                userRepository.findByUsername("gaeun7").get(),
//                                "문 앞에 놔주시오",
//                                "서울시 영등포구 여의도동 123-45",
//                                OrderStatus.COMPLETED,
//                                14900 * 2),
//                        new Order(
//                                userRepository.findByUsername("kim2016").get(),
//                                "문 앞에 놓아 주실래요?",
//                                "부산시 해운대구 우동 123-45",
//                                OrderStatus.UPDATED,
//                                14900),
//                        new Order(
//                                userRepository.findByUsername("kim2016").get(),
//                                "문 앞에 두고 가",
//                                "부산시 해운대구 우동 123-45",
//                                OrderStatus.COMPLETED,
//                                14900 + 2000 + 2000),
//                        new Order(
//                                userRepository.findByUsername("ael1197").get(),
//                                "문 앞에 두고 가주세요",
//                                "충남 보령시 신흑동 123-45",
//                                OrderStatus.COMPLETED,
//                                (14900 + 5000 + 2000) * 2),
//                        new Order(
//                                userRepository.findByUsername("ael1197").get(),
//                                "문 앞에 두세요",
//                                "충남 보령시 신흑동 123-45",
//                                OrderStatus.CANCELED,
//                                2000)
//                );
//        orderRepository.saveAll(orderList);
//
//        List<OrderDetail> orderDetailList =
//                List.of(
//                        new OrderDetail(
//                                orderRepository.findByOrderId(1L).get(),
//                                menuRepository.findById(1L).get(),
//                                1),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(2L).get(),
//                                menuRepository.findById(2L).get(),
//                                2),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(3L).get(),
//                                menuRepository.findById(3L).get(),
//                                1),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(4L).get(),
//                                menuRepository.findById(1L).get(),
//                                1),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(4L).get(),
//                                menuRepository.findById(5L).get(),
//                                1),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(4L).get(),
//                                menuRepository.findById(6L).get(),
//                                1),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(5L).get(),
//                                menuRepository.findById(2L).get(),
//                                2),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(5L).get(),
//                                menuRepository.findById(4L).get(),
//                                2),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(5L).get(),
//                                menuRepository.findById(6L).get(),
//                                2),
//                        new OrderDetail(
//                                orderRepository.findByOrderId(6L).get(),
//                                menuRepository.findById(6L).get(),
//                                1)
//                );
//        orderDetailRepository.saveAll(orderDetailList);
//
//        List<Review> reviewList =
//                List.of(
//                        new Review(
//                                orderRepository.findByOrderId(2L).get(),
//                                orderRepository.findByOrderId(2L).get().getUser().getUserId(),
//                                "Review Test 1",
//                                5L),
//                        new Review(
//                                orderRepository.findByOrderId(4L).get(),
//                                orderRepository.findByOrderId(4L).get().getUser().getUserId(),
//                                "Review Test 2",
//                                5L),
//                        new Review(
//                                orderRepository.findByOrderId(5L).get(),
//                                orderRepository.findByOrderId(5L).get().getUser().getUserId(),
//                                "Review Test 3",
//                                4L)
//                );
//        reviewRepository.saveAll(reviewList);
//
//        List<PasswordHistory> passwordHistoryList =
//                List.of(
//                        new PasswordHistory(
//                                userRepository.findById(3L).get(),
//                                passwordEncoder.encode("Bqwer1234!")),
//                        new PasswordHistory(
//                                userRepository.findById(3L).get(),
//                                passwordEncoder.encode("Cqwer1234!")),
//                        new PasswordHistory(
//                                userRepository.findById(3L).get(),
//                                passwordEncoder.encode("Dqwer1234!")),
//                        new PasswordHistory(
//                                userRepository.findById(3L).get(),
//                                passwordEncoder.encode("Eqwer1234!")),
//                        new PasswordHistory(
//                                userRepository.findById(4L).get(),
//                                passwordEncoder.encode("Eqwer1234!"))
//                );
//        passwordHistoryRepository.saveAll(passwordHistoryList);
//    }
//
//}
