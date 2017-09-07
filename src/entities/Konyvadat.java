/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "konyvadat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Konyvadat.findAll", query = "SELECT k FROM Konyvadat k"),
    @NamedQuery(name = "Konyvadat.findById", query = "SELECT k FROM Konyvadat k WHERE k.id = :id"),
    @NamedQuery(name = "Konyvadat.findByCim", query = "SELECT k FROM Konyvadat k WHERE k.cim = :cim"),
    @NamedQuery(name = "Konyvadat.findBySzerzo", query = "SELECT k FROM Konyvadat k WHERE k.szerzo = :szerzo"),
    @NamedQuery(name = "Konyvadat.findByKiado", query = "SELECT k FROM Konyvadat k WHERE k.kiado = :kiado"),
    @NamedQuery(name = "Konyvadat.findByMegjEv", query = "SELECT k FROM Konyvadat k WHERE k.megjEv = :megjEv"),
    @NamedQuery(name = "Konyvadat.findByNyelv", query = "SELECT k FROM Konyvadat k WHERE k.nyelv = :nyelv"),
    @NamedQuery(name = "Konyvadat.findByIsbn", query = "SELECT k FROM Konyvadat k WHERE k.isbn = :isbn"),
    @NamedQuery(name = "Konyvadat.findByPolc", query = "SELECT k FROM Konyvadat k WHERE k.polc = :polc")})
public class Konyvadat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cim")
    private String cim;
    @Column(name = "szerzo")
    private String szerzo;
    @Column(name = "kiado")
    private String kiado;
    @Column(name = "megj_ev")
    private Short megjEv;
    @Column(name = "nyelv")
    private String nyelv;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "polc")
    private String polc;
    @JoinTable(name = "ktsz", joinColumns = {
        @JoinColumn(name = "konyvadat_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "targyszo_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Targyszo> targyszoCollection;
    @OneToMany(mappedBy = "konyvadatId")
    private Collection<Peldany> peldanyCollection;

    public Konyvadat() {
    }

    public Konyvadat(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getSzerzo() {
        return szerzo;
    }

    public void setSzerzo(String szerzo) {
        this.szerzo = szerzo;
    }

    public String getKiado() {
        return kiado;
    }

    public void setKiado(String kiado) {
        this.kiado = kiado;
    }

    public Short getMegjEv() {
        return megjEv;
    }

    public void setMegjEv(Short megjEv) {
        this.megjEv = megjEv;
    }

    public String getNyelv() {
        return nyelv;
    }

    public void setNyelv(String nyelv) {
        this.nyelv = nyelv;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPolc() {
        return polc;
    }

    public void setPolc(String polc) {
        this.polc = polc;
    }

    @XmlTransient
    public Collection<Targyszo> getTargyszoCollection() {
        return targyszoCollection;
    }

    public void setTargyszoCollection(Collection<Targyszo> targyszoCollection) {
        this.targyszoCollection = targyszoCollection;
    }

    @XmlTransient
    public Collection<Peldany> getPeldanyCollection() {
        return peldanyCollection;
    }

    public void setPeldanyCollection(Collection<Peldany> peldanyCollection) {
        this.peldanyCollection = peldanyCollection;
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
        if (!(object instanceof Konyvadat)) {
            return false;
        }
        Konyvadat other = (Konyvadat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Konyvadat[ id=" + id + " ]";
    }
    
}
