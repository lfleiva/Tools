/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.modelo;

import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "transacciones", catalog = "tools", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transacciones.findAll", query = "SELECT t FROM Transacciones t")
    , @NamedQuery(name = "Transacciones.findById", query = "SELECT t FROM Transacciones t WHERE t.id = :id")
    , @NamedQuery(name = "Transacciones.findByNit", query = "SELECT t FROM Transacciones t WHERE t.nit = :nit")
    , @NamedQuery(name = "Transacciones.findByEntidad", query = "SELECT t FROM Transacciones t WHERE t.entidad = :entidad")
    , @NamedQuery(name = "Transacciones.findByTransacciones", query = "SELECT t FROM Transacciones t WHERE t.transacciones = :transacciones")
    , @NamedQuery(name = "Transacciones.findByAnio", query = "SELECT t FROM Transacciones t WHERE t.anio = :anio")})
public class Transacciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nit", length = 100)
    private String nit;
    @Column(name = "entidad", length = 500)
    private String entidad;
    @Basic(optional = false)
    @Column(name = "transacciones", nullable = false)
    private int transacciones;
    @Basic(optional = false)
    @Column(name = "anio", nullable = false, length = 100)
    private String anio;
    @JoinColumn(name = "id_estrategia", referencedColumnName = "id")
    @ManyToOne
    private Estrategia idEstrategia;

    public Transacciones() {
    }

    public Transacciones(Integer id) {
        this.id = id;
    }

    public Transacciones(Integer id, int transacciones, String anio) {
        this.id = id;
        this.transacciones = transacciones;
        this.anio = anio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public int getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(int transacciones) {
        this.transacciones = transacciones;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Estrategia getIdEstrategia() {
        return idEstrategia;
    }

    public void setIdEstrategia(Estrategia idEstrategia) {
        this.idEstrategia = idEstrategia;
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
        if (!(object instanceof Transacciones)) {
            return false;
        }
        Transacciones other = (Transacciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.Tools.modelo.Transacciones[ id=" + id + " ]";
    }
    
}
