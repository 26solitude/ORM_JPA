package org.example.orm_jpa.ch05.model2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.Date;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("orm_jpa");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1. 회원 생성 및 저장
            Member member = new Member();
            member.setName("조장호");
            member.setCity("대구");
            member.setStreet("상인역 5번 출구");
            member.setZipcode("970519");
            em.persist(member);

            // 2. 상품 생성 및 저장
            Item item = new Item();
            item.setName("포테이토피자");
            item.setPrice(14900);
            item.setStockQuantity(10);
            em.persist(item);

            // 3. 주문 생성 및 저장
            Order order = new Order();
            order.setMember(member); // 양방향 관계 설정
            order.setOrderDate(new Date());
            order.setStatus(OrderStatus.ORDER);
            em.persist(order);

            // 4. 주문 항목(OrderItem) 추가 및 저장
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setOrderPrice(2000000);
            orderItem.setCount(1);
            em.persist(orderItem);

            // 5. 주문에 주문 항목 추가 (양방향 연관관계 메서드 사용)
            order.addOrderItem(orderItem);

            // 6. 트랜잭션 커밋
            tx.commit();

            // 7. 저장된 주문 조회
            findOrder(em, order.getId());

            // 8. 주문한 회원 객체 그래프 탐색
            findMemberByOrder(em, order.getId());

            // 9. 주문한 상품 객체 그래프 탐색
            findItemByOrder(em, order.getId());

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 🔹 주문을 통해 회원 조회 (객체 그래프 탐색)
    public static void findMemberByOrder(EntityManager em, Long orderId) {
        System.out.println("\n=== 주문한 회원 조회 (객체 그래프 탐색) ===");
        Order order = em.find(Order.class, orderId);
        if (order == null) {
            System.out.println("주문을 찾을 수 없습니다.");
            return;
        }

        Member member = order.getMember(); // 주문 → 회원 탐색
        System.out.println("주문 ID: " + order.getId());
        System.out.println("주문한 회원 이름: " + member.getName());
        System.out.println("회원 주소: " + member.getCity() + ", " + member.getStreet() + ", " + member.getZipcode());
    }

    // 🔹 주문을 통해 상품 조회 (객체 그래프 탐색)
    public static void findItemByOrder(EntityManager em, Long orderId) {
        System.out.println("\n=== 주문한 상품 조회 (객체 그래프 탐색) ===");
        Order order = em.find(Order.class, orderId);
        if (order == null) {
            System.out.println("주문을 찾을 수 없습니다.");
            return;
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = orderItem.getItem(); // 주문 → 주문항목 → 상품 탐색
            System.out.println("주문 ID: " + order.getId());
            System.out.println("상품명: " + item.getName());
            System.out.println("상품 가격: " + item.getPrice());
            System.out.println("주문 개수: " + orderItem.getCount());
        }
    }

    // 🔹 주문 조회 함수
    public static void findOrder(EntityManager em, Long orderId) {
        System.out.println("\n=== 주문 조회 ===");
        Order order = em.find(Order.class, orderId);
        if (order == null) {
            System.out.println("주문을 찾을 수 없습니다.");
            return;
        }

        Member member = order.getMember();
        System.out.println("주문 ID: " + order.getId());
        System.out.println("주문한 회원: " + member.getName());
        System.out.println("주문 상태: " + order.getStatus());
        System.out.println("주문 날짜: " + order.getOrderDate());

        System.out.println("=== 주문 상품 목록 ===");
        for (OrderItem orderItem : order.getOrderItems()) {
            System.out.println("- 상품명: " + orderItem.getItem().getName());
            System.out.println("  주문 가격: " + orderItem.getOrderPrice());
            System.out.println("  주문 개수: " + orderItem.getCount());
        }
    }
}
