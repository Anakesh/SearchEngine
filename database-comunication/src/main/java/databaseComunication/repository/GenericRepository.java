package databaseComunication.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Pavel on 26.01.2019.
 */
public class GenericRepository<E> implements IRepository<E> {

    protected final SessionFactory sessionFactory;
    private Class<E> entityClass;
    private String idTableName;

    public GenericRepository(SessionFactory sessionFactory, Class<E> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
//        this.idTableName = Arrays.stream(entityClass.getFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get().getName();
        this.idTableName = "id";
    }

    public void add(E entity) {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.merge(entity);
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
    }

    public List<E> readAll() {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        List<E> entityList;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> entityCriteria =  criteriaBuilder.createQuery(entityClass);
            Root<E> entityRoot = entityCriteria.from(entityClass);
            entityCriteria.select(entityRoot);
            entityList = entityManager.createQuery(entityCriteria).getResultList();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return entityList;
    }

    public E firstById(){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        E object;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> entityCriteria =  criteriaBuilder.createQuery(entityClass);
            Root<E> entityRoot = entityCriteria.from(entityClass);
            entityCriteria.select(entityRoot);
//            entityCriteria.where(criteriaBuilder.function("least", Boolean.class,entityRoot.get(idTableName))).;
            object = entityManager.createQuery(entityCriteria).getSingleResult();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return object;
//        List<E> all = readAll();
//        return all.get(0);

    }

    public E getById(Long entityId){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        E pageToCrawl;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> entityCriteria =  criteriaBuilder.createQuery(entityClass);
            Root<E> entityRoot = entityCriteria.from(entityClass);
            entityCriteria.select(entityRoot);
            entityCriteria.where(criteriaBuilder.equal(entityRoot.get(idTableName),entityId));
            pageToCrawl = entityManager.createQuery(entityCriteria).getSingleResult();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return pageToCrawl;
    }

    public void deleteById(Long entityId) {
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaDelete<E> deleteEntityCriteria = criteriaBuilder.createCriteriaDelete(entityClass);
            Root<E> entityRoot = deleteEntityCriteria.from(entityClass);
            deleteEntityCriteria.where(criteriaBuilder.equal(entityRoot.get(idTableName),entityId));
            entityManager.createQuery(deleteEntityCriteria).executeUpdate();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
    }

    public Long count(){
        Session session = getSession();
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = null;
        Long count = -1L;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> entityCriteria =  criteriaBuilder.createQuery(Long.class);
            Root<E> entityRoot = entityCriteria.from(entityClass);
            entityCriteria.select(criteriaBuilder.count(entityRoot));
            count = entityManager.createQuery(entityCriteria).getSingleResult();
            transaction.commit();
        } catch (RollbackException ex){
            if(transaction!=null) transaction.rollback();
            ex.printStackTrace();
            throw ex;
        }finally {
            entityManager.close();
            session.close();
        }
        return count;
    }

    protected Session getSession(){
        Session session = null;
        synchronized (sessionFactory){
             session = sessionFactory.openSession();
        }
        return session;
    }

    public boolean isEmpty() {
        return count()<=0;
    }
}
