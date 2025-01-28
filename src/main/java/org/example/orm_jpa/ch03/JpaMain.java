package org.example.orm_jpa.ch03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.orm_jpa.ch02.Member;


public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("orm_jpa");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();
            logic(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(EntityManager em) {

        Member memberA = new Member();
        memberA.setId("memberA");
        memberA.setUsername("회원A");

        Member memberB = new Member();
        memberB.setId("memberB");
        memberB.setUsername("회원B");

        em.persist(memberA);
        em.persist(memberB);

        Member A = em.find(Member.class, "memberA");
        System.out.println("findMember = " + A.getUsername() + ", id = " + A.getId());

        Member B = em.find(Member.class, "memberB");
        System.out.println("findMember = " + B.getUsername() + ", id = " + B.getId());

        Member C = em.find(Member.class, "memberB");
        System.out.println(B == C);
    }
}
