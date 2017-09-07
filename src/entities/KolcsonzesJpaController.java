/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.exceptions.NonexistentEntityException;
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
public class KolcsonzesJpaController implements Serializable {

    public KolcsonzesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kolcsonzes kolcsonzes) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peldany leltarszam = kolcsonzes.getLeltarszam();
            if (leltarszam != null) {
                leltarszam = em.getReference(leltarszam.getClass(), leltarszam.getLeltarszam());
                kolcsonzes.setLeltarszam(leltarszam);
            }
            Kolcsonzo torzsszam = kolcsonzes.getTorzsszam();
            if (torzsszam != null) {
                torzsszam = em.getReference(torzsszam.getClass(), torzsszam.getTorzsszam());
                kolcsonzes.setTorzsszam(torzsszam);
            }
            em.persist(kolcsonzes);
            if (leltarszam != null) {
                leltarszam.getKolcsonzesCollection().add(kolcsonzes);
                leltarszam = em.merge(leltarszam);
            }
            if (torzsszam != null) {
                torzsszam.getKolcsonzesCollection().add(kolcsonzes);
                torzsszam = em.merge(torzsszam);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kolcsonzes kolcsonzes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kolcsonzes persistentKolcsonzes = em.find(Kolcsonzes.class, kolcsonzes.getId());
            Peldany leltarszamOld = persistentKolcsonzes.getLeltarszam();
            Peldany leltarszamNew = kolcsonzes.getLeltarszam();
            Kolcsonzo torzsszamOld = persistentKolcsonzes.getTorzsszam();
            Kolcsonzo torzsszamNew = kolcsonzes.getTorzsszam();
            if (leltarszamNew != null) {
                leltarszamNew = em.getReference(leltarszamNew.getClass(), leltarszamNew.getLeltarszam());
                kolcsonzes.setLeltarszam(leltarszamNew);
            }
            if (torzsszamNew != null) {
                torzsszamNew = em.getReference(torzsszamNew.getClass(), torzsszamNew.getTorzsszam());
                kolcsonzes.setTorzsszam(torzsszamNew);
            }
            kolcsonzes = em.merge(kolcsonzes);
            if (leltarszamOld != null && !leltarszamOld.equals(leltarszamNew)) {
                leltarszamOld.getKolcsonzesCollection().remove(kolcsonzes);
                leltarszamOld = em.merge(leltarszamOld);
            }
            if (leltarszamNew != null && !leltarszamNew.equals(leltarszamOld)) {
                leltarszamNew.getKolcsonzesCollection().add(kolcsonzes);
                leltarszamNew = em.merge(leltarszamNew);
            }
            if (torzsszamOld != null && !torzsszamOld.equals(torzsszamNew)) {
                torzsszamOld.getKolcsonzesCollection().remove(kolcsonzes);
                torzsszamOld = em.merge(torzsszamOld);
            }
            if (torzsszamNew != null && !torzsszamNew.equals(torzsszamOld)) {
                torzsszamNew.getKolcsonzesCollection().add(kolcsonzes);
                torzsszamNew = em.merge(torzsszamNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = kolcsonzes.getId();
                if (findKolcsonzes(id) == null) {
                    throw new NonexistentEntityException("The kolcsonzes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kolcsonzes kolcsonzes;
            try {
                kolcsonzes = em.getReference(Kolcsonzes.class, id);
                kolcsonzes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kolcsonzes with id " + id + " no longer exists.", enfe);
            }
            Peldany leltarszam = kolcsonzes.getLeltarszam();
            if (leltarszam != null) {
                leltarszam.getKolcsonzesCollection().remove(kolcsonzes);
                leltarszam = em.merge(leltarszam);
            }
            Kolcsonzo torzsszam = kolcsonzes.getTorzsszam();
            if (torzsszam != null) {
                torzsszam.getKolcsonzesCollection().remove(kolcsonzes);
                torzsszam = em.merge(torzsszam);
            }
            em.remove(kolcsonzes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kolcsonzes> findKolcsonzesEntities() {
        return findKolcsonzesEntities(true, -1, -1);
    }

    public List<Kolcsonzes> findKolcsonzesEntities(int maxResults, int firstResult) {
        return findKolcsonzesEntities(false, maxResults, firstResult);
    }

    private List<Kolcsonzes> findKolcsonzesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kolcsonzes.class));
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

    public Kolcsonzes findKolcsonzes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kolcsonzes.class, id);
        } finally {
            em.close();
        }
    }

    public int getKolcsonzesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kolcsonzes> rt = cq.from(Kolcsonzes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
