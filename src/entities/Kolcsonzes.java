/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "kolcsonzes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kolcsonzes.findAll", query = "SELECT k FROM Kolcsonzes k"),
    @NamedQuery(name = "Kolcsonzes.findById", query = "SELECT k FROM Kolcsonzes k WHERE k.id = :id"),
    @NamedQuery(name = "Kolcsonzes.findByDatum", query = "SELECT k FROM Kolcsonzes k WHERE k.datum = :datum"),
    @NamedQuery(name = "Kolcsonzes.findByHatarido", query = "SELECT k FROM Kolcsonzes k WHERE k.hatarido = :hatarido"),
    @NamedQuery(name = "Kolcsonzes.findByVisszahoz", query = "SELECT k FROM Kolcsonzes k WHERE k.visszahoz = :visszahoz")})
public class Kolcsonzes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Column(name = "hatarido")
    @Temporal(TemporalType.DATE)
    private Date hatarido;
    @Column(name = "visszahoz")
    @Temporal(TemporalType.DATE)
    private Date visszahoz;
    @JoinColumn(name = "leltarszam", referencedColumnName = "leltarszam")
    @ManyToOne
    private Peldany leltarszam;
    @JoinColumn(name = "torzsszam", referencedColumnName = "torzsszam")
    @ManyToOne
    private Kolcsonzo torzsszam;

    public Kolcsonzes() {
    }

    public Kolcsonzes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getHatarido() {
        return hatarido;
    }

    public void setHatarido(Date hatarido) {
        this.hatarido = hatarido;
    }

    public Date getVisszahoz() {
        return visszahoz;
    }

    public void setVisszahoz(Date visszahoz) {
        this.visszahoz = visszahoz;
    }

    public Peldany getLeltarszam() {
        return leltarszam;
    }

    public void setLeltarszam(Peldany leltarszam) {
        this.leltarszam = leltarszam;
    }

    public Kolcsonzo getTorzsszam() {
        return torzsszam;
    }

    public void setTorzsszam(Kolcsonzo torzsszam) {
        this.torzsszam = torzsszam;
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
        if (!(object instanceof Kolcsonzes)) {
            return false;
        }
        Kolcsonzes other = (Kolcsonzes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Kolcsonzes[ id=" + id + " ]";
    }
    
}
