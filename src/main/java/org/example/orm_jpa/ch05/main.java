package org.example.orm_jpa.ch05;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("orm_jpa");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();
//            testSave(em);
//            queryLogicJoin(em);
//            updateRelation(em);

//            test순수한객체_양방향(em);
//            testORM_양방향(em);
            testORM_양방향_리팩토링(em);

            biDirection(em);

            tx.commit();

//            queryLogicJoin(em);
            biDirection(em);

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void testSave(EntityManager em) {
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);
    }

    public static void queryLogicJoin(EntityManager em) {

        String jpql = "select m from Member m join m.team t where " + "t.name=:teamName";

        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        for (Member member : resultList) {
            System.out.println("[query] member.username=" + member.getUsername());
        }
    }

    public static void updateRelation(EntityManager em) {

        Team team2 = new Team("team2", "팀2");
        em.persist(team2);

        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }

    public static void deleteRelation(EntityManager em){
        Member member1 = em.find(Member.class, "memeber1");
        member1.setTeam(null);
    }

    public static void biDirection(EntityManager em){
        Team team = em.find(Team.class, "team1");

        List<Member> members = team.getMembers();

        for (Member member : members){
            System.out.println("member.username = " + member.getUsername());
        }
    }
    
    public static void test순수한객체_양방향(EntityManager em){

        Team team1 = new Team("team1", "팀1");
        Member member1 = new Member("member1", "회원1");
        Member member2 = new Member("member2", "회원2");
        
        member1.setTeam(team1);
        team1.getMembers().add(member1);

        member2.setTeam(team1);
        team1.getMembers().add(member2);

        List<Member> members = team1.getMembers();
        System.out.println("members.size() = " + members.size());
    }

    public static void testORM_양방향(EntityManager em){

        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");

        member1.setTeam(team1);
        team1.getMembers().add(member1);
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");

        member2.setTeam(team1);
        team1.getMembers().add(member2);
        em.persist(member2);

        List<Member> members = team1.getMembers();
        System.out.println("members.size() = " + members.size());
    }

    public static void testORM_양방향_리팩토링(EntityManager em){

        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);

        List<Member> members = team1.getMembers();
        System.out.println("members.size() = " + members.size());
    }

}
