/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "peldany")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Peldany.findAll", query = "SELECT p FROM Peldany p"),
    @NamedQuery(name = "Peldany.findByLeltarszam", query = "SELECT p FROM Peldany p WHERE p.leltarszam = :leltarszam"),
    @NamedQuery(name = "Peldany.findByEto", query = "SELECT p FROM Peldany p WHERE p.eto = :eto"),
    @NamedQuery(name = "Peldany.findByKolcsonozheto", query = "SELECT p FROM Peldany p WHERE p.kolcsonozheto = :kolcsonozheto"),
    @NamedQuery(name = "Peldany.findByElerheto", query = "SELECT p FROM Peldany p WHERE p.elerheto = :elerheto"),
    @NamedQuery(name = "Peldany.findByLejarat", query = "SELECT p FROM Peldany p WHERE p.lejarat = :lejarat")})
public class Peldany implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "leltarszam")
    private String leltarszam;
    @Column(name = "ETO")
    private String eto;
    @Column(name = "kolcsonozheto")
    private Boolean kolcsonozheto;
    @Column(name = "elerheto")
    private Boolean elerheto;
    @Column(name = "lejarat")
    @Temporal(TemporalType.DATE)
    private Date lejarat;
    @OneToMany(mappedBy = "leltarszam")
    private Collection<Kolcsonzes> kolcsonzesCollection;
    @JoinColumn(name = "konyvadat_id", referencedColumnName = "id")
    @ManyToOne
    private Konyvadat konyvadatId;

    public Peldany() {
    }

    public Peldany(String leltarszam) {
        this.leltarszam = leltarszam;
    }

    public String getLeltarszam() {
        return leltarszam;
    }

    public void setLeltarszam(String leltarszam) {
        this.leltarszam = leltarszam;
    }

    public String getEto() {
        return eto;
    }

    public void setEto(String eto) {
        this.eto = eto;
    }

    public Boolean getKolcsonozheto() {
        return kolcsonozheto;
    }

    public void setKolcsonozheto(Boolean kolcsonozheto) {
        this.kolcsonozheto = kolcsonozheto;
    }

    public Boolean getElerheto() {
        return elerheto;
    }

    public void setElerheto(Boolean elerheto) {
        this.elerheto = elerheto;
    }

    public Date getLejarat() {
        return lejarat;
    }

    public void setLejarat(Date lejarat) {
        this.lejarat = lejarat;
    }

    @XmlTransient
    public Collection<Kolcsonzes> getKolcsonzesCollection() {
        return kolcsonzesCollection;
    }

    public void setKolcsonzesCollection(Collection<Kolcsonzes> kolcsonzesCollection) {
        this.kolcsonzesCollection = kolcsonzesCollection;
    }

    public Konyvadat getKonyvadatId() {
        return konyvadatId;
    }

    public void setKonyvadatId(Konyvadat konyvadatId) {
        this.konyvadatId = konyvadatId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (leltarszam != null ? leltarszam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Peldany)) {
            return false;
        }
        Peldany other = (Peldany) object;
        if ((this.leltarszam == null && other.leltarszam != null) || (this.leltarszam != null && !this.leltarszam.equals(other.leltarszam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Peldany[ leltarszam=" + leltarszam + " ]";
    }
    
}
