/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.exceptions.IllegalOrphanException;
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
public class TargyszoJpaController implements Serializable {

    public TargyszoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Targyszo targyszo) {
        if (targyszo.getKonyvadatCollection() == null) {
            targyszo.setKonyvadatCollection(new ArrayList<Konyvadat>());
        }
        if (targyszo.getStrukturaCollection() == null) {
            targyszo.setStrukturaCollection(new ArrayList<Struktura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Struktura struktura = targyszo.getStruktura();
            if (struktura != null) {
                struktura = em.getReference(struktura.getClass(), struktura.getGyerekId());
                targyszo.setStruktura(struktura);
            }
            Collection<Konyvadat> attachedKonyvadatCollection = new ArrayList<Konyvadat>();
            for (Konyvadat konyvadatCollectionKonyvadatToAttach : targyszo.getKonyvadatCollection()) {
                konyvadatCollectionKonyvadatToAttach = em.getReference(konyvadatCollectionKonyvadatToAttach.getClass(), konyvadatCollectionKonyvadatToAttach.getId());
                attachedKonyvadatCollection.add(konyvadatCollectionKonyvadatToAttach);
            }
            targyszo.setKonyvadatCollection(attachedKonyvadatCollection);
            Collection<Struktura> attachedStrukturaCollection = new ArrayList<Struktura>();
            for (Struktura strukturaCollectionStrukturaToAttach : targyszo.getStrukturaCollection()) {
                strukturaCollectionStrukturaToAttach = em.getReference(strukturaCollectionStrukturaToAttach.getClass(), strukturaCollectionStrukturaToAttach.getGyerekId());
                attachedStrukturaCollection.add(strukturaCollectionStrukturaToAttach);
            }
            targyszo.setStrukturaCollection(attachedStrukturaCollection);
            em.persist(targyszo);
            if (struktura != null) {
                Targyszo oldTargyszoOfStruktura = struktura.getTargyszo();
                if (oldTargyszoOfStruktura != null) {
                    oldTargyszoOfStruktura.setStruktura(null);
                    oldTargyszoOfStruktura = em.merge(oldTargyszoOfStruktura);
                }
                struktura.setTargyszo(targyszo);
                struktura = em.merge(struktura);
            }
            for (Konyvadat konyvadatCollectionKonyvadat : targyszo.getKonyvadatCollection()) {
                konyvadatCollectionKonyvadat.getTargyszoCollection().add(targyszo);
                konyvadatCollectionKonyvadat = em.merge(konyvadatCollectionKonyvadat);
            }
            for (Struktura strukturaCollectionStruktura : targyszo.getStrukturaCollection()) {
                Targyszo oldSzuloIdOfStrukturaCollectionStruktura = strukturaCollectionStruktura.getSzuloId();
                strukturaCollectionStruktura.setSzuloId(targyszo);
                strukturaCollectionStruktura = em.merge(strukturaCollectionStruktura);
                if (oldSzuloIdOfStrukturaCollectionStruktura != null) {
                    oldSzuloIdOfStrukturaCollectionStruktura.getStrukturaCollection().remove(strukturaCollectionStruktura);
                    oldSzuloIdOfStrukturaCollectionStruktura = em.merge(oldSzuloIdOfStrukturaCollectionStruktura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Targyszo targyszo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Targyszo persistentTargyszo = em.find(Targyszo.class, targyszo.getId());
            Struktura strukturaOld = persistentTargyszo.getStruktura();
            Struktura strukturaNew = targyszo.getStruktura();
            Collection<Konyvadat> konyvadatCollectionOld = persistentTargyszo.getKonyvadatCollection();
            Collection<Konyvadat> konyvadatCollectionNew = targyszo.getKonyvadatCollection();
            Collection<Struktura> strukturaCollectionOld = persistentTargyszo.getStrukturaCollection();
            Collection<Struktura> strukturaCollectionNew = targyszo.getStrukturaCollection();
            List<String> illegalOrphanMessages = null;
            if (strukturaOld != null && !strukturaOld.equals(strukturaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Struktura " + strukturaOld + " since its targyszo field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (strukturaNew != null) {
                strukturaNew = em.getReference(strukturaNew.getClass(), strukturaNew.getGyerekId());
                targyszo.setStruktura(strukturaNew);
            }
            Collection<Konyvadat> attachedKonyvadatCollectionNew = new ArrayList<Konyvadat>();
            for (Konyvadat konyvadatCollectionNewKonyvadatToAttach : konyvadatCollectionNew) {
                konyvadatCollectionNewKonyvadatToAttach = em.getReference(konyvadatCollectionNewKonyvadatToAttach.getClass(), konyvadatCollectionNewKonyvadatToAttach.getId());
                attachedKonyvadatCollectionNew.add(konyvadatCollectionNewKonyvadatToAttach);
            }
            konyvadatCollectionNew = attachedKonyvadatCollectionNew;
            targyszo.setKonyvadatCollection(konyvadatCollectionNew);
            Collection<Struktura> attachedStrukturaCollectionNew = new ArrayList<Struktura>();
            for (Struktura strukturaCollectionNewStrukturaToAttach : strukturaCollectionNew) {
                strukturaCollectionNewStrukturaToAttach = em.getReference(strukturaCollectionNewStrukturaToAttach.getClass(), strukturaCollectionNewStrukturaToAttach.getGyerekId());
                attachedStrukturaCollectionNew.add(strukturaCollectionNewStrukturaToAttach);
            }
            strukturaCollectionNew = attachedStrukturaCollectionNew;
            targyszo.setStrukturaCollection(strukturaCollectionNew);
            targyszo = em.merge(targyszo);
            if (strukturaNew != null && !strukturaNew.equals(strukturaOld)) {
                Targyszo oldTargyszoOfStruktura = strukturaNew.getTargyszo();
                if (oldTargyszoOfStruktura != null) {
                    oldTargyszoOfStruktura.setStruktura(null);
                    oldTargyszoOfStruktura = em.merge(oldTargyszoOfStruktura);
                }
                strukturaNew.setTargyszo(targyszo);
                strukturaNew = em.merge(strukturaNew);
            }
            for (Konyvadat konyvadatCollectionOldKonyvadat : konyvadatCollectionOld) {
                if (!konyvadatCollectionNew.contains(konyvadatCollectionOldKonyvadat)) {
                    konyvadatCollectionOldKonyvadat.getTargyszoCollection().remove(targyszo);
                    konyvadatCollectionOldKonyvadat = em.merge(konyvadatCollectionOldKonyvadat);
                }
            }
            for (Konyvadat konyvadatCollectionNewKonyvadat : konyvadatCollectionNew) {
                if (!konyvadatCollectionOld.contains(konyvadatCollectionNewKonyvadat)) {
                    konyvadatCollectionNewKonyvadat.getTargyszoCollection().add(targyszo);
                    konyvadatCollectionNewKonyvadat = em.merge(konyvadatCollectionNewKonyvadat);
                }
            }
            for (Struktura strukturaCollectionOldStruktura : strukturaCollectionOld) {
                if (!strukturaCollectionNew.contains(strukturaCollectionOldStruktura)) {
                    strukturaCollectionOldStruktura.setSzuloId(null);
                    strukturaCollectionOldStruktura = em.merge(strukturaCollectionOldStruktura);
                }
            }
            for (Struktura strukturaCollectionNewStruktura : strukturaCollectionNew) {
                if (!strukturaCollectionOld.contains(strukturaCollectionNewStruktura)) {
                    Targyszo oldSzuloIdOfStrukturaCollectionNewStruktura = strukturaCollectionNewStruktura.getSzuloId();
                    strukturaCollectionNewStruktura.setSzuloId(targyszo);
                    strukturaCollectionNewStruktura = em.merge(strukturaCollectionNewStruktura);
                    if (oldSzuloIdOfStrukturaCollectionNewStruktura != null && !oldSzuloIdOfStrukturaCollectionNewStruktura.equals(targyszo)) {
                        oldSzuloIdOfStrukturaCollectionNewStruktura.getStrukturaCollection().remove(strukturaCollectionNewStruktura);
                        oldSzuloIdOfStrukturaCollectionNewStruktura = em.merge(oldSzuloIdOfStrukturaCollectionNewStruktura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = targyszo.getId();
                if (findTargyszo(id) == null) {
                    throw new NonexistentEntityException("The targyszo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Targyszo targyszo;
            try {
                targyszo = em.getReference(Targyszo.class, id);
                targyszo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The targyszo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Struktura strukturaOrphanCheck = targyszo.getStruktura();
            if (strukturaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Targyszo (" + targyszo + ") cannot be destroyed since the Struktura " + strukturaOrphanCheck + " in its struktura field has a non-nullable targyszo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Konyvadat> konyvadatCollection = targyszo.getKonyvadatCollection();
            for (Konyvadat konyvadatCollectionKonyvadat : konyvadatCollection) {
                konyvadatCollectionKonyvadat.getTargyszoCollection().remove(targyszo);
                konyvadatCollectionKonyvadat = em.merge(konyvadatCollectionKonyvadat);
            }
            Collection<Struktura> strukturaCollection = targyszo.getStrukturaCollection();
            for (Struktura strukturaCollectionStruktura : strukturaCollection) {
                strukturaCollectionStruktura.setSzuloId(null);
                strukturaCollectionStruktura = em.merge(strukturaCollectionStruktura);
            }
            em.remove(targyszo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Targyszo> findTargyszoEntities() {
        return findTargyszoEntities(true, -1, -1);
    }

    public List<Targyszo> findTargyszoEntities(int maxResults, int firstResult) {
        return findTargyszoEntities(false, maxResults, firstResult);
    }

    private List<Targyszo> findTargyszoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Targyszo.class));
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

    public Targyszo findTargyszo(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Targyszo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTargyszoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Targyszo> rt = cq.from(Targyszo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
