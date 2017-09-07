/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.exceptions.NonexistentEntityException;
import entities.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author user
 */
public class KolcsonzoJpaController implements Serializable {

    public KolcsonzoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kolcsonzo kolcsonzo) throws PreexistingEntityException, Exception {
        if (kolcsonzo.getKolcsonzesCollection() == null) {
            kolcsonzo.setKolcsonzesCollection(new ArrayList<Kolcsonzes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Kolcsonzes> attachedKolcsonzesCollection = new ArrayList<Kolcsonzes>();
            for (Kolcsonzes kolcsonzesCollectionKolcsonzesToAttach : kolcsonzo.getKolcsonzesCollection()) {
                kolcsonzesCollectionKolcsonzesToAttach = em.getReference(kolcsonzesCollectionKolcsonzesToAttach.getClass(), kolcsonzesCollectionKolcsonzesToAttach.getId());
                attachedKolcsonzesCollection.add(kolcsonzesCollectionKolcsonzesToAttach);
            }
            kolcsonzo.setKolcsonzesCollection(attachedKolcsonzesCollection);
            em.persist(kolcsonzo);
            for (Kolcsonzes kolcsonzesCollectionKolcsonzes : kolcsonzo.getKolcsonzesCollection()) {
                Kolcsonzo oldTorzsszamOfKolcsonzesCollectionKolcsonzes = kolcsonzesCollectionKolcsonzes.getTorzsszam();
                kolcsonzesCollectionKolcsonzes.setTorzsszam(kolcsonzo);
                kolcsonzesCollectionKolcsonzes = em.merge(kolcsonzesCollectionKolcsonzes);
                if (oldTorzsszamOfKolcsonzesCollectionKolcsonzes != null) {
                    oldTorzsszamOfKolcsonzesCollectionKolcsonzes.getKolcsonzesCollection().remove(kolcsonzesCollectionKolcsonzes);
                    oldTorzsszamOfKolcsonzesCollectionKolcsonzes = em.merge(oldTorzsszamOfKolcsonzesCollectionKolcsonzes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKolcsonzo(kolcsonzo.getTorzsszam()) != null) {
                throw new PreexistingEntityException("Kolcsonzo " + kolcsonzo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kolcsonzo kolcsonzo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kolcsonzo persistentKolcsonzo = em.find(Kolcsonzo.class, kolcsonzo.getTorzsszam());
            Collection<Kolcsonzes> kolcsonzesCollectionOld = persistentKolcsonzo.getKolcsonzesCollection();
            Collection<Kolcsonzes> kolcsonzesCollectionNew = kolcsonzo.getKolcsonzesCollection();
            Collection<Kolcsonzes> attachedKolcsonzesCollectionNew = new ArrayList<Kolcsonzes>();
            for (Kolcsonzes kolcsonzesCollectionNewKolcsonzesToAttach : kolcsonzesCollectionNew) {
                kolcsonzesCollectionNewKolcsonzesToAttach = em.getReference(kolcsonzesCollectionNewKolcsonzesToAttach.getClass(), kolcsonzesCollectionNewKolcsonzesToAttach.getId());
                attachedKolcsonzesCollectionNew.add(kolcsonzesCollectionNewKolcsonzesToAttach);
            }
            kolcsonzesCollectionNew = attachedKolcsonzesCollectionNew;
            kolcsonzo.setKolcsonzesCollection(kolcsonzesCollectionNew);
            kolcsonzo = em.merge(kolcsonzo);
            for (Kolcsonzes kolcsonzesCollectionOldKolcsonzes : kolcsonzesCollectionOld) {
                if (!kolcsonzesCollectionNew.contains(kolcsonzesCollectionOldKolcsonzes)) {
                    kolcsonzesCollectionOldKolcsonzes.setTorzsszam(null);
                    kolcsonzesCollectionOldKolcsonzes = em.merge(kolcsonzesCollectionOldKolcsonzes);
                }
            }
            for (Kolcsonzes kolcsonzesCollectionNewKolcsonzes : kolcsonzesCollectionNew) {
                if (!kolcsonzesCollectionOld.contains(kolcsonzesCollectionNewKolcsonzes)) {
                    Kolcsonzo oldTorzsszamOfKolcsonzesCollectionNewKolcsonzes = kolcsonzesCollectionNewKolcsonzes.getTorzsszam();
                    kolcsonzesCollectionNewKolcsonzes.setTorzsszam(kolcsonzo);
                    kolcsonzesCollectionNewKolcsonzes = em.merge(kolcsonzesCollectionNewKolcsonzes);
                    if (oldTorzsszamOfKolcsonzesCollectionNewKolcsonzes != null && !oldTorzsszamOfKolcsonzesCollectionNewKolcsonzes.equals(kolcsonzo)) {
                        oldTorzsszamOfKolcsonzesCollectionNewKolcsonzes.getKolcsonzesCollection().remove(kolcsonzesCollectionNewKolcsonzes);
                        oldTorzsszamOfKolcsonzesCollectionNewKolcsonzes = em.merge(oldTorzsszamOfKolcsonzesCollectionNewKolcsonzes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = kolcsonzo.getTorzsszam();
                if (findKolcsonzo(id) == null) {
                    throw new NonexistentEntityException("The kolcsonzo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kolcsonzo kolcsonzo;
            try {
                kolcsonzo = em.getReference(Kolcsonzo.class, id);
                kolcsonzo.getTorzsszam();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kolcsonzo with id " + id + " no longer exists.", enfe);
            }
            Collection<Kolcsonzes> kolcsonzesCollection = kolcsonzo.getKolcsonzesCollection();
            for (Kolcsonzes kolcsonzesCollectionKolcsonzes : kolcsonzesCollection) {
                kolcsonzesCollectionKolcsonzes.setTorzsszam(null);
                kolcsonzesCollectionKolcsonzes = em.merge(kolcsonzesCollectionKolcsonzes);
            }
            em.remove(kolcsonzo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kolcsonzo> findKolcsonzoEntities() {
        return findKolcsonzoEntities(true, -1, -1);
    }

    public List<Kolcsonzo> findKolcsonzoEntities(int maxResults, int firstResult) {
        return findKolcsonzoEntities(false, maxResults, firstResult);
    }

    private List<Kolcsonzo> findKolcsonzoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kolcsonzo.class));
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

    public Kolcsonzo findKolcsonzo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kolcsonzo.class, id);
        } finally {
            em.close();
        }
    }

    public int getKolcsonzoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kolcsonzo> rt = cq.from(Kolcsonzo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
