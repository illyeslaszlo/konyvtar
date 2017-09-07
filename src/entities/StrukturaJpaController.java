/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.exceptions.NonexistentEntityException;
import entities.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author user
 */
public class StrukturaJpaController implements Serializable {

    public StrukturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Struktura struktura) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Targyszo szuloId = struktura.getSzuloId();
            if (szuloId != null) {
                szuloId = em.getReference(szuloId.getClass(), szuloId.getId());
                struktura.setSzuloId(szuloId);
            }
            Targyszo targyszo = struktura.getTargyszo();
            if (targyszo != null) {
                targyszo = em.getReference(targyszo.getClass(), targyszo.getId());
                struktura.setTargyszo(targyszo);
            }
            em.persist(struktura);
            if (szuloId != null) {
                szuloId.getStrukturaCollection().add(struktura);
                szuloId = em.merge(szuloId);
            }
            if (targyszo != null) {
                targyszo.getStrukturaCollection().add(struktura);
                targyszo = em.merge(targyszo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStruktura(struktura.getGyerekId()) != null) {
                throw new PreexistingEntityException("Struktura " + struktura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Struktura struktura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Struktura persistentStruktura = em.find(Struktura.class, struktura.getGyerekId());
            Targyszo szuloIdOld = persistentStruktura.getSzuloId();
            Targyszo szuloIdNew = struktura.getSzuloId();
            Targyszo targyszoOld = persistentStruktura.getTargyszo();
            Targyszo targyszoNew = struktura.getTargyszo();
            if (szuloIdNew != null) {
                szuloIdNew = em.getReference(szuloIdNew.getClass(), szuloIdNew.getId());
                struktura.setSzuloId(szuloIdNew);
            }
            if (targyszoNew != null) {
                targyszoNew = em.getReference(targyszoNew.getClass(), targyszoNew.getId());
                struktura.setTargyszo(targyszoNew);
            }
            struktura = em.merge(struktura);
            if (szuloIdOld != null && !szuloIdOld.equals(szuloIdNew)) {
                szuloIdOld.getStrukturaCollection().remove(struktura);
                szuloIdOld = em.merge(szuloIdOld);
            }
            if (szuloIdNew != null && !szuloIdNew.equals(szuloIdOld)) {
                szuloIdNew.getStrukturaCollection().add(struktura);
                szuloIdNew = em.merge(szuloIdNew);
            }
            if (targyszoOld != null && !targyszoOld.equals(targyszoNew)) {
                targyszoOld.getStrukturaCollection().remove(struktura);
                targyszoOld = em.merge(targyszoOld);
            }
            if (targyszoNew != null && !targyszoNew.equals(targyszoOld)) {
                targyszoNew.getStrukturaCollection().add(struktura);
                targyszoNew = em.merge(targyszoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = struktura.getGyerekId();
                if (findStruktura(id) == null) {
                    throw new NonexistentEntityException("The struktura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Struktura struktura;
            try {
                struktura = em.getReference(Struktura.class, id);
                struktura.getGyerekId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The struktura with id " + id + " no longer exists.", enfe);
            }
            Targyszo szuloId = struktura.getSzuloId();
            if (szuloId != null) {
                szuloId.getStrukturaCollection().remove(struktura);
                szuloId = em.merge(szuloId);
            }
            Targyszo targyszo = struktura.getTargyszo();
            if (targyszo != null) {
                targyszo.getStrukturaCollection().remove(struktura);
                targyszo = em.merge(targyszo);
            }
            em.remove(struktura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Struktura> findStrukturaEntities() {
        return findStrukturaEntities(true, -1, -1);
    }

    public List<Struktura> findStrukturaEntities(int maxResults, int firstResult) {
        return findStrukturaEntities(false, maxResults, firstResult);
    }

    private List<Struktura> findStrukturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Struktura.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Struktura findStruktura(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Struktura.class, id);
        } finally {
            em.close();
        }
    }

    public int getStrukturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Struktura> rt = cq.from(Struktura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
