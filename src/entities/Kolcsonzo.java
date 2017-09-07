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
import javax.persistence.Id;
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
@Table(name = "kolcsonzo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kolcsonzo.findAll", query = "SELECT k FROM Kolcsonzo k"),
    @NamedQuery(name = "Kolcsonzo.findByTorzsszam", query = "SELECT k FROM Kolcsonzo k WHERE k.torzsszam = :torzsszam"),
    @NamedQuery(name = "Kolcsonzo.findByCsaladnev", query = "SELECT k FROM Kolcsonzo k WHERE k.csaladnev = :csaladnev"),
    @NamedQuery(name = "Kolcsonzo.findBySzemelynev", query = "SELECT k FROM Kolcsonzo k WHERE k.szemelynev = :szemelynev"),
    @NamedQuery(name = "Kolcsonzo.findByEmail", query = "SELECT k FROM Kolcsonzo k WHERE k.email = :email"),
    @NamedQuery(name = "Kolcsonzo.findByMegye", query = "SELECT k FROM Kolcsonzo k WHERE k.megye = :megye"),
    @NamedQuery(name = "Kolcsonzo.findByHelyseg", query = "SELECT k FROM Kolcsonzo k WHERE k.helyseg = :helyseg"),
    @NamedQuery(name = "Kolcsonzo.findByUtca", query = "SELECT k FROM Kolcsonzo k WHERE k.utca = :utca"),
    @NamedQuery(name = "Kolcsonzo.findBySzam", query = "SELECT k FROM Kolcsonzo k WHERE k.szam = :szam"),
    @NamedQuery(name = "Kolcsonzo.findByCnp", query = "SELECT k FROM Kolcsonzo k WHERE k.cnp = :cnp")})
public class Kolcsonzo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "torzsszam")
    private Integer torzsszam;
    @Column(name = "csaladnev")
    private String csaladnev;
    @Column(name = "szemelynev")
    private String szemelynev;
    @Column(name = "email")
    private String email;
    @Column(name = "megye")
    private String megye;
    @Column(name = "helyseg")
    private String helyseg;
    @Column(name = "utca")
    private String utca;
    @Column(name = "szam")
    private String szam;
    @Column(name = "CNP")
    private String cnp;
    @OneToMany(mappedBy = "torzsszam")
    private Collection<Kolcsonzes> kolcsonzesCollection;

    public Kolcsonzo() {
    }

    public Kolcsonzo(Integer torzsszam) {
        this.torzsszam = torzsszam;
    }

    public Integer getTorzsszam() {
        return torzsszam;
    }

    public void setTorzsszam(Integer torzsszam) {
        this.torzsszam = torzsszam;
    }

    public String getCsaladnev() {
        return csaladnev;
    }

    public void setCsaladnev(String csaladnev) {
        this.csaladnev = csaladnev;
    }

    public String getSzemelynev() {
        return szemelynev;
    }

    public void setSzemelynev(String szemelynev) {
        this.szemelynev = szemelynev;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMegye() {
        return megye;
    }

    public void setMegye(String megye) {
        this.megye = megye;
    }

    public String getHelyseg() {
        return helyseg;
    }

    public void setHelyseg(String helyseg) {
        this.helyseg = helyseg;
    }

    public String getUtca() {
        return utca;
    }

    public void setUtca(String utca) {
        this.utca = utca;
    }

    public String getSzam() {
        return szam;
    }

    public void setSzam(String szam) {
        this.szam = szam;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @XmlTransient
    public Collection<Kolcsonzes> getKolcsonzesCollection() {
        return kolcsonzesCollection;
    }

    public void setKolcsonzesCollection(Collection<Kolcsonzes> kolcsonzesCollection) {
        this.kolcsonzesCollection = kolcsonzesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (torzsszam != null ? torzsszam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kolcsonzo)) {
            return false;
        }
        Kolcsonzo other = (Kolcsonzo) object;
        if ((this.torzsszam == null && other.torzsszam != null) || (this.torzsszam != null && !this.torzsszam.equals(other.torzsszam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Kolcsonzo[ torzsszam=" + torzsszam + " ]";
    }
    
}
