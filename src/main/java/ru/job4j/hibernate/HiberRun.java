package ru.job4j.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.hibernate.model.Candidate;

public class HiberRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Candidate one = Candidate.of("Alex", "Moscow Bank, 1 years", 210.0f);
            Candidate two = Candidate.of("Nikolay", "SberTech, 3 years", 288.1f);
            Candidate three = Candidate.of("Nikita", "Kaliningrad, 10 years", 355f);

            session.save(one);
            session.save(two);
            session.save(three);

            Query query = session.createQuery("from Candidate");
            for (Object st : query.list()) {
                System.out.println(st);
            }

            Query query1 = session.createQuery("from Candidate c where c.id = :fId");
            query1.setParameter("fId", 1);
            System.out.println(query1.uniqueResult());

            Query query2 = session.createQuery("from Candidate c where c.name = :fName");
            query2.setParameter("fName", "Nikita");
            for (Object st : query2.list()) {
                System.out.println(st);
            }

            session.createQuery("update Candidate c set c.salary = :newSalary, "
                            + "c.experience = :newExperience where c.id = :fId")
                    .setParameter("newSalary", 320f)
                    .setParameter("newExperience", "London City Bank 7 years")
                    .setParameter("fId", 1)
                    .executeUpdate();

            session.createQuery("delete from Candidate where id = :fId")
                    .setParameter("fId", 3)
                    .executeUpdate();

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
