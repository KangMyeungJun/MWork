package com.mwork.main.querydsl;

import static com.mwork.main.entity.test.QTeam.*;
import com.mwork.main.entity.test.Team;
import com.mwork.main.entity.test.TestEntity;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.mwork.main.entity.test.QTestEntity.testEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class TestQuery {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory factory;

    @BeforeEach
    public void before() {
        factory = new JPAQueryFactory(em);
        TestEntity entity = new TestEntity();
        TestEntity entity2 = new TestEntity();
        TestEntity entity3 = new TestEntity();
        TestEntity entity4 = new TestEntity();
        TestEntity entity5 = new TestEntity();

        entity.setName("test1");
        entity2.setName("test2");
        entity3.setName("test3");
        entity4.setName("test4");
        entity5.setName("test5");

        Team team1 = new Team();
        Team team2 = new Team();
        Team team3 = new Team();
        Team team4 = new Team();
        Team team5 = new Team();

        team1.setName("ATeam");
        team2.setName("BTeam");
        team3.setName("CTeam");
        team4.setName("DTeam");
        team5.setName("ETeam");

        em.persist(team1);
        em.persist(team2);
        em.persist(team3);
        em.persist(team4);
        em.persist(team5);

        entity.setTeam(team1);
        entity2.setTeam(team2);
        entity3.setTeam(team3);
        entity4.setTeam(team4);
        entity5.setTeam(team5);

        em.persist(entity);
        em.persist(entity2);
        em.persist(entity3);
        em.persist(entity4);
        em.persist(entity5);

        em.flush();
        em.clear();
    }

    @Test
    void testQuery() {
        TestEntity entity = new TestEntity();
        em.persist(entity);

        TestEntity result = factory.selectFrom(testEntity).fetchOne();

        assertThat(result).isEqualTo(entity);
        assertThat(result.getId()).isEqualTo(entity.getId());

    }

    @Test
    void queryTest2() {
        TestEntity newEntity = new TestEntity();
        newEntity.setName("test");
        em.persist(newEntity);
        em.flush();
        em.clear();


        TestEntity result = factory
                .select(testEntity)
                .from(testEntity)
                .where(testEntity.name.eq("test"))
                .fetchOne();
        assertThat(result.getName()).isEqualTo(newEntity.getName());

    }

    @Test
    void testWhere() {
        List<TestEntity> entityList = factory.selectFrom(testEntity)
                .where(testEntity.name.eq("test1")
                        .or(testEntity.name.eq("test2")))
                .fetch();

        assertThat(entityList.size()).isEqualTo(2);
    }

    @Test
    void testOnJoin() {
        List<Tuple> aTeam = factory
                .select(testEntity, team)
                .from(testEntity)
                .join(testEntity.team(), team).on(team.name.eq("ATeam"))
                .fetch();
        for (Tuple tuple : aTeam) {
            System.out.println("tuple = " + tuple.get(testEntity).getName());
            System.out.println("tuple = " + tuple.get(team).getName());
        }
    }

    @Test
    void testOnJoin2() {
        List<Tuple> fetch = factory
                .select(testEntity, team)
                .from(testEntity)
                .join(team).on(testEntity.team().name.eq(team.name))
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple.get(testEntity).getName());
            System.out.println("tuple = " + tuple.get(team).getName());
        }
    }



}
