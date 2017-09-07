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
public class PeldanyJpaController implements Serializable {

    public PeldanyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Peldany peldany) throws PreexistingEntityException, Exception {
        if (peldany.getKolcsonzesCollection() == null) {
            peldany.setKolcsonzesCollection(new ArrayList<Kolcsonzes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Konyvadat konyvadatId = peldany.getKonyvadatId();
            if (konyvadatId != null) {
                konyvadatId = em.getReference(konyvadatId.getClass(), konyvadatId.getId());
                peldany.setKonyvadatId(konyvadatId);
            }
            Collection<Kolcsonzes> attachedKolcsonzesCollection = new ArrayList<Kolcsonzes>();
            for (Kolcsonzes kolcsonzesCollectionKolcsonzesToAttach : peldany.getKolcsonzesCollection()) {
                kolcsonzesCollectionKolcsonzesToAttach = em.getReference(kolcsonzesCollectionKolcsonzesToAttach.getClass(), kolcsonzesCollectionKolcsonzesToAttach.getId());
                attachedKolcsonzesCollection.add(kolcsonzesCollectionKolcsonzesToAttach);
            }
            peldany.setKolcsonzesCollection(attachedKolcsonzesCollection);
            em.persist(peldany);
            if (konyvadatId != null) {
                konyvadatId.getPeldanyCollection().add(peldany);
                konyvadatId = em.merge(konyvadatId);
            }
            for (Kolcsonzes kolcsonzesCollectionKolcsonzes : peldany.getKolcsonzesCollection()) {
                Peldany oldLeltarszamOfKolcsonzesCollectionKolcsonzes = kolcsonzesCollectionKolcsonzes.getLeltarszam();
                kolcsonzesCollectionKolcsonzes.setLeltarszam(peldany);
                kolcsonzesCollectionKolcsonzes = em.merge(kolcsonzesCollectionKolcsonzes);
                if (oldLeltarszamOfKolcsonzesCollectionKolcsonzes != null) {
                    oldLeltarszamOfKolcsonzesCollectionKolcsonzes.getKolcsonzesCollection().remove(kolcsonzesCollectionKolcsonzes);
                    oldLeltarszamOfKolcsonzesCollectionKolcsonzes = em.merge(oldLeltarszamOfKolcsonzesCollectionKolcsonzes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPeldany(peldany.getLeltarszam()) != null) {
                throw new PreexistingEntityException("Peldany " + peldany + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Peldany peldany) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peldany persistentPeldany = em.find(Peldany.class, peldany.getLeltarszam());
            Konyvadat konyvadatIdOld = persistentPeldany.getKonyvadatId();
            Konyvadat konyvadatIdNew = peldany.getKonyvadatId();
            Collection<Kolcsonzes> kolcsonzesCollectionOld = persistentPeldany.getKolcsonzesCollection();
            Collection<Kolcsonzes> kolcsonzesCollectionNew = peldany.getKolcsonzesCollection();
            if (konyvadatIdNew != null) {
                konyvadatIdNew = em.getReference(konyvadatIdNew.getClass(), konyvadatIdNew.getId());
                peldany.setKonyvadatId(konyvadatIdNew);
            }
            Collection<Kolcsonzes> attachedKolcsonzesCollectionNew = new ArrayList<Kolcsonzes>();
            for (Kolcsonzes kolcsonzesCollectionNewKolcsonzesToAttach : kolcsonzesCollectionNew) {
                kolcsonzesCollectionNewKolcsonzesToAttach = em.getReference(kolcsonzesCollectionNewKolcsonzesToAttach.getClass(), kolcsonzesCollectionNewKolcsonzesToAttach.getId());
                attachedKolcsonzesCollectionNew.add(kolcsonzesCollectionNewKolcsonzesToAttach);
            }
            kolcsonzesCollectionNew = attachedKolcsonzesCollectionNew;
            peldany.setKolcsonzesCollection(kolcsonzesCollectionNew);
            peldany = em.merge(peldany);
            if (konyvadatIdOld != null && !konyvadatIdOld.equals(konyvadatIdNew)) {
                konyvadatIdOld.getPeldanyCollection().remove(peldany);
                konyvadatIdOld = em.merge(konyvadatIdOld);
            }
            if (konyvadatIdNew != null && !konyvadatIdNew.equals(konyvadatIdOld)) {
                konyvadatIdNew.getPeldanyCollection().add(peldany);
                konyvadatIdNew = em.merge(konyvadatIdNew);
            }
            for (Kolcsonzes kolcsonzesCollectionOldKolcsonzes : kolcsonzesCollectionOld) {
                if (!kolcsonzesCollectionNew.contains(kolcsonzesCollectionOldKolcsonzes)) {
                    kolcsonzesCollectionOldKolcsonzes.setLeltarszam(null);
                    kolcsonzesCollectionOldKolcsonzes = em.merge(kolcsonzesCollectionOldKolcsonzes);
                }
            }
            for (Kolcsonzes kolcsonzesCollectionNewKolcsonzes : kolcsonzesCollectionNew) {
                if (!kolcsonzesCollectionOld.contains(kolcsonzesCollectionNewKolcsonzes)) {
                    Peldany oldLeltarszamOfKolcsonzesCollectionNewKolcsonzes = kolcsonzesCollectionNewKolcsonzes.getLeltarszam();
                    kolcsonzesCollectionNewKolcsonzes.setLeltarszam(peldany);
                    kolcsonzesCollectionNewKolcsonzes = em.merge(kolcsonzesCollectionNewKolcsonzes);
                    if (oldLeltarszamOfKolcsonzesCollectionNewKolcsonzes != null && !oldLeltarszamOfKolcsonzesCollectionNewKolcsonzes.equals(peldany)) {
                        oldLeltarszamOfKolcsonzesCollectionNewKolcsonzes.getKolcsonzesCollection().remove(kolcsonzesCollectionNewKolcsonzes);
                        oldLeltarszamOfKolcsonzesCollectionNewKolcsonzes = em.merge(oldLeltarszamOfKolcsonzesCollectionNewKolcsonzes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = peldany.getLeltarszam();
                if (findPeldany(id) == null) {
                    throw new NonexistentEntityException("The peldany with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peldany peldany;
            try {
                peldany = em.getReference(Peldany.class, id);
                peldany.getLeltarszam();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peldany with id " + id + " no longer exists.", enfe);
            }
            Konyvadat konyvadatId = peldany.getKonyvadatId();
            if (konyvadatId != null) {
                konyvadatId.getPeldanyCollection().remove(peldany);
                konyvadatId = em.merge(konyvadatId);
            }
            Collection<Kolcsonzes> kolcsonzesCollection = peldany.getKolcsonzesCollection();
            for (Kolcsonzes kolcsonzesCollectionKolcsonzes : kolcsonzesCollection) {
                kolcsonzesCollectionKolcsonzes.setLeltarszam(null);
                kolcsonzesCollectionKolcsonzes = em.merge(kolcsonzesCollectionKolcsonzes);
            }
            em.remove(peldany);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Peldany> findPeldanyEntities() {
        return findPeldanyEntities(true, -1, -1);
    }

    public List<Peldany> findPeldanyEntities(int maxResults, int firstResult) {
        return findPeldanyEntities(false, maxResults, firstResult);
    }

    private List<Peldany> findPeldanyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Peldany.class));
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

    public Peldany findPeldany(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Peldany.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeldanyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Peldany> rt = cq.from(Peldany.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
