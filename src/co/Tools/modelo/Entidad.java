/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Luis Fernando Leiva
 */
@Entity
@Table(name = "entidad", catalog = "tools", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nit"})
    , @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entidad.findAll", query = "SELECT e FROM Entidad e")
    , @NamedQuery(name = "Entidad.findById", query = "SELECT e FROM Entidad e WHERE e.id = :id")
    , @NamedQuery(name = "Entidad.findByNit", query = "SELECT e FROM Entidad e WHERE e.nit = :nit")
    , @NamedQuery(name = "Entidad.findByNombre", query = "SELECT e FROM Entidad e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Entidad.findByAutonomo", query = "SELECT e FROM Entidad e WHERE e.autonomo = :autonomo")
    , @NamedQuery(name = "Entidad.findByDescripcion", query = "SELECT e FROM Entidad e WHERE e.descripcion = :descripcion")})
public class Entidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nit", nullable = false, length = 100)
    private String nit;
    @Basic(optional = false)
    @Column(name = "nombre", nullable = false, length = 500)
    private String nombre;
    @Column(name = "autonomo")
    private Boolean autonomo;
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEntidad")
    private List<Estrategia> estrategiaList;

    public Entidad() {
    }

    public Entidad(Integer id) {
        this.id = id;
    }

    public Entidad(Integer id, String nit, String nombre) {
        this.id = id;
        this.nit = nit;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getAutonomo() {
        return autonomo;
    }

    public void setAutonomo(Boolean autonomo) {
        this.autonomo = autonomo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Estrategia> getEstrategiaList() {
        return estrategiaList;
    }

    public void setEstrategiaList(List<Estrategia> estrategiaList) {
        this.estrategiaList = estrategiaList;
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
        if (!(object instanceof Entidad)) {
            return false;
        }
        Entidad other = (Entidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.Tools.modelo.Entidad[ id=" + id + " ]";
    }
    
}
