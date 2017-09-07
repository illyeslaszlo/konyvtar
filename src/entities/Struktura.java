/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "struktura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Struktura.findAll", query = "SELECT s FROM Struktura s"),
    @NamedQuery(name = "Struktura.findByGyerekId", query = "SELECT s FROM Struktura s WHERE s.gyerekId = :gyerekId")})
public class Struktura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "gyerek_id")
    private Short gyerekId;
    @JoinColumn(name = "szulo_id", referencedColumnName = "id")
    @ManyToOne
    private Targyszo szuloId;
    @JoinColumn(name = "gyerek_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Targyszo targyszo;

    public Struktura() {
    }

    public Struktura(Short gyerekId) {
        this.gyerekId = gyerekId;
    }

    public Short getGyerekId() {
        return gyerekId;
    }

    public void setGyerekId(Short gyerekId) {
        this.gyerekId = gyerekId;
    }

    public Targyszo getSzuloId() {
        return szuloId;
    }

    public void setSzuloId(Targyszo szuloId) {
        this.szuloId = szuloId;
    }

    public Targyszo getTargyszo() {
        return targyszo;
    }

    public void setTargyszo(Targyszo targyszo) {
        this.targyszo = targyszo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gyerekId != null ? gyerekId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Struktura)) {
            return false;
        }
        Struktura other = (Struktura) object;
        if ((this.gyerekId == null && other.gyerekId != null) || (this.gyerekId != null && !this.gyerekId.equals(other.gyerekId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Struktura[ gyerekId=" + gyerekId + " ]";
    }
    
}
