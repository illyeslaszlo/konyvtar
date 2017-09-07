/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.exceptions.NonexistentEntityException;
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
public class KonyvadatJpaController implements Serializable {

    public KonyvadatJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Konyvadat konyvadat) {
        if (konyvadat.getTargyszoCollection() == null) {
            konyvadat.setTargyszoCollection(new ArrayList<Targyszo>());
        }
        if (konyvadat.getPeldanyCollection() == null) {
            konyvadat.setPeldanyCollection(new ArrayList<Peldany>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Targyszo> attachedTargyszoCollection = new ArrayList<Targyszo>();
            for (Targyszo targyszoCollectionTargyszoToAttach : konyvadat.getTargyszoCollection()) {
                targyszoCollectionTargyszoToAttach = em.getReference(targyszoCollectionTargyszoToAttach.getClass(), targyszoCollectionTargyszoToAttach.getId());
                attachedTargyszoCollection.add(targyszoCollectionTargyszoToAttach);
            }
            konyvadat.setTargyszoCollection(attachedTargyszoCollection);
            Collection<Peldany> attachedPeldanyCollection = new ArrayList<Peldany>();
            for (Peldany peldanyCollectionPeldanyToAttach : konyvadat.getPeldanyCollection()) {
                peldanyCollectionPeldanyToAttach = em.getReference(peldanyCollectionPeldanyToAttach.getClass(), peldanyCollectionPeldanyToAttach.getLeltarszam());
                attachedPeldanyCollection.add(peldanyCollectionPeldanyToAttach);
            }
            konyvadat.setPeldanyCollection(attachedPeldanyCollection);
            em.persist(konyvadat);
            for (Targyszo targyszoCollectionTargyszo : konyvadat.getTargyszoCollection()) {
                targyszoCollectionTargyszo.getKonyvadatCollection().add(konyvadat);
                targyszoCollectionTargyszo = em.merge(targyszoCollectionTargyszo);
            }
            for (Peldany peldanyCollectionPeldany : konyvadat.getPeldanyCollection()) {
                Konyvadat oldKonyvadatIdOfPeldanyCollectionPeldany = peldanyCollectionPeldany.getKonyvadatId();
                peldanyCollectionPeldany.setKonyvadatId(konyvadat);
                peldanyCollectionPeldany = em.merge(peldanyCollectionPeldany);
                if (oldKonyvadatIdOfPeldanyCollectionPeldany != null) {
                    oldKonyvadatIdOfPeldanyCollectionPeldany.getPeldanyCollection().remove(peldanyCollectionPeldany);
                    oldKonyvadatIdOfPeldanyCollectionPeldany = em.merge(oldKonyvadatIdOfPeldanyCollectionPeldany);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Konyvadat konyvadat) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Konyvadat persistentKonyvadat = em.find(Konyvadat.class, konyvadat.getId());
            Collection<Targyszo> targyszoCollectionOld = persistentKonyvadat.getTargyszoCollection();
            Collection<Targyszo> targyszoCollectionNew = konyvadat.getTargyszoCollection();
            Collection<Peldany> peldanyCollectionOld = persistentKonyvadat.getPeldanyCollection();
            Collection<Peldany> peldanyCollectionNew = konyvadat.getPeldanyCollection();
            Collection<Targyszo> attachedTargyszoCollectionNew = new ArrayList<Targyszo>();
            for (Targyszo targyszoCollectionNewTargyszoToAttach : targyszoCollectionNew) {
                targyszoCollectionNewTargyszoToAttach = em.getReference(targyszoCollectionNewTargyszoToAttach.getClass(), targyszoCollectionNewTargyszoToAttach.getId());
                attachedTargyszoCollectionNew.add(targyszoCollectionNewTargyszoToAttach);
            }
            targyszoCollectionNew = attachedTargyszoCollectionNew;
            konyvadat.setTargyszoCollection(targyszoCollectionNew);
            Collection<Peldany> attachedPeldanyCollectionNew = new ArrayList<Peldany>();
            for (Peldany peldanyCollectionNewPeldanyToAttach : peldanyCollectionNew) {
                peldanyCollectionNewPeldanyToAttach = em.getReference(peldanyCollectionNewPeldanyToAttach.getClass(), peldanyCollectionNewPeldanyToAttach.getLeltarszam());
                attachedPeldanyCollectionNew.add(peldanyCollectionNewPeldanyToAttach);
            }
            peldanyCollectionNew = attachedPeldanyCollectionNew;
            konyvadat.setPeldanyCollection(peldanyCollectionNew);
            konyvadat = em.merge(konyvadat);
            for (Targyszo targyszoCollectionOldTargyszo : targyszoCollectionOld) {
                if (!targyszoCollectionNew.contains(targyszoCollectionOldTargyszo)) {
                    targyszoCollectionOldTargyszo.getKonyvadatCollection().remove(konyvadat);
                    targyszoCollectionOldTargyszo = em.merge(targyszoCollectionOldTargyszo);
                }
            }
            for (Targyszo targyszoCollectionNewTargyszo : targyszoCollectionNew) {
                if (!targyszoCollectionOld.contains(targyszoCollectionNewTargyszo)) {
                    targyszoCollectionNewTargyszo.getKonyvadatCollection().add(konyvadat);
                    targyszoCollectionNewTargyszo = em.merge(targyszoCollectionNewTargyszo);
                }
            }
            for (Peldany peldanyCollectionOldPeldany : peldanyCollectionOld) {
                if (!peldanyCollectionNew.contains(peldanyCollectionOldPeldany)) {
                    peldanyCollectionOldPeldany.setKonyvadatId(null);
                    peldanyCollectionOldPeldany = em.merge(peldanyCollectionOldPeldany);
                }
            }
            for (Peldany peldanyCollectionNewPeldany : peldanyCollectionNew) {
                if (!peldanyCollectionOld.contains(peldanyCollectionNewPeldany)) {
                    Konyvadat oldKonyvadatIdOfPeldanyCollectionNewPeldany = peldanyCollectionNewPeldany.getKonyvadatId();
                    peldanyCollectionNewPeldany.setKonyvadatId(konyvadat);
                    peldanyCollectionNewPeldany = em.merge(peldanyCollectionNewPeldany);
                    if (oldKonyvadatIdOfPeldanyCollectionNewPeldany != null && !oldKonyvadatIdOfPeldanyCollectionNewPeldany.equals(konyvadat)) {
                        oldKonyvadatIdOfPeldanyCollectionNewPeldany.getPeldanyCollection().remove(peldanyCollectionNewPeldany);
                        oldKonyvadatIdOfPeldanyCollectionNewPeldany = em.merge(oldKonyvadatIdOfPeldanyCollectionNewPeldany);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = konyvadat.getId();
                if (findKonyvadat(id) == null) {
                    throw new NonexistentEntityException("The konyvadat with id " + id + " no longer exists.");
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
            Konyvadat konyvadat;
            try {
                konyvadat = em.getReference(Konyvadat.class, id);
                konyvadat.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The konyvadat with id " + id + " no longer exists.", enfe);
            }
            Collection<Targyszo> targyszoCollection = konyvadat.getTargyszoCollection();
            for (Targyszo targyszoCollectionTargyszo : targyszoCollection) {
                targyszoCollectionTargyszo.getKonyvadatCollection().remove(konyvadat);
                targyszoCollectionTargyszo = em.merge(targyszoCollectionTargyszo);
            }
            Collection<Peldany> peldanyCollection = konyvadat.getPeldanyCollection();
            for (Peldany peldanyCollectionPeldany : peldanyCollection) {
                peldanyCollectionPeldany.setKonyvadatId(null);
                peldanyCollectionPeldany = em.merge(peldanyCollectionPeldany);
            }
            em.remove(konyvadat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Konyvadat> findKonyvadatEntities() {
        return findKonyvadatEntities(true, -1, -1);
    }

    public List<Konyvadat> findKonyvadatEntities(int maxResults, int firstResult) {
        return findKonyvadatEntities(false, maxResults, firstResult);
    }

    private List<Konyvadat> findKonyvadatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Konyvadat.class));
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

    public Konyvadat findKonyvadat(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Konyvadat.class, id);
        } finally {
            em.close();
        }
    }

    public int getKonyvadatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Konyvadat> rt = cq.from(Konyvadat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
