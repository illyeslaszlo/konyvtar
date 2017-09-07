/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "targyszo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Targyszo.findAll", query = "SELECT t FROM Targyszo t"),
    @NamedQuery(name = "Targyszo.findById", query = "SELECT t FROM Targyszo t WHERE t.id = :id"),
    @NamedQuery(name = "Targyszo.findByNev", query = "SELECT t FROM Targyszo t WHERE t.nev = :nev")})
public class Targyszo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Column(name = "nev")
    private String nev;
    @ManyToMany(mappedBy = "targyszoCollection")
    private Collection<Konyvadat> konyvadatCollection;
    @OneToMany(mappedBy = "szuloId")
    private Collection<Struktura> strukturaCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "targyszo")
    private Struktura struktura;

    public Targyszo() {
    }

    public Targyszo(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    @XmlTransient
    public Collection<Konyvadat> getKonyvadatCollection() {
        return konyvadatCollection;
    }

    public void setKonyvadatCollection(Collection<Konyvadat> konyvadatCollection) {
        this.konyvadatCollection = konyvadatCollection;
    }

    @XmlTransient
    public Collection<Struktura> getStrukturaCollection() {
        return strukturaCollection;
    }

    public void setStrukturaCollection(Collection<Struktura> strukturaCollection) {
        this.strukturaCollection = strukturaCollection;
    }

    public Struktura getStruktura() {
        return struktura;
    }

    public void setStruktura(Struktura struktura) {
        this.struktura = struktura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Targyszo)) {
            return false;
        }
        Targyszo other = (Targyszo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Targyszo[ id=" + id + " ]";
    }
    
}
