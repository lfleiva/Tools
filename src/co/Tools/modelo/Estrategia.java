/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.modelo;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mery Evelyn Ceron
 */
@Entity
@Table(name = "estrategia", catalog = "tools", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"codigo"}),
    @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estrategia.findAll", query = "SELECT e FROM Estrategia e"),
    @NamedQuery(name = "Estrategia.findById", query = "SELECT e FROM Estrategia e WHERE e.id = :id"),
    @NamedQuery(name = "Estrategia.findByCodigo", query = "SELECT e FROM Estrategia e WHERE e.codigo = :codigo"),
    @NamedQuery(name = "Estrategia.findByNombre", query = "SELECT e FROM Estrategia e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Estrategia.findByMultientidad", query = "SELECT e FROM Estrategia e WHERE e.multientidad = :multientidad"),
    @NamedQuery(name = "Estrategia.findByComplejidad", query = "SELECT e FROM Estrategia e WHERE e.complejidad = :complejidad"),
    @NamedQuery(name = "Estrategia.findByDescripcion", query = "SELECT e FROM Estrategia e WHERE e.descripcion = :descripcion")})
public class Estrategia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "codigo", nullable = false, length = 100)
    private String codigo;
    @Basic(optional = false)
    @Column(name = "nombre", nullable = false, length = 500)
    private String nombre;
    @Column(name = "multientidad")
    private Boolean multientidad;
    @Column(name = "complejidad", length = 100)
    private String complejidad;
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    @OneToMany(mappedBy = "idEstrategia")
    private List<Migracion> migracionList;
    @JoinColumn(name = "id_entidad", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Entidad idEntidad;
    @OneToMany(mappedBy = "idEstrategia")
    private List<Transacciones> transaccionesList;
    @OneToMany(mappedBy = "idEstrategia")
    private List<Requerimiento> requerimientoList;

    public Estrategia() {
    }

    public Estrategia(Integer id) {
        this.id = id;
    }

    public Estrategia(Integer id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getMultientidad() {
        return multientidad;
    }

    public void setMultientidad(Boolean multientidad) {
        this.multientidad = multientidad;
    }

    public String getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(String complejidad) {
        this.complejidad = complejidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Migracion> getMigracionList() {
        return migracionList;
    }

    public void setMigracionList(List<Migracion> migracionList) {
        this.migracionList = migracionList;
    }

    public Entidad getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Entidad idEntidad) {
        this.idEntidad = idEntidad;
    }

    @XmlTransient
    public List<Transacciones> getTransaccionesList() {
        return transaccionesList;
    }

    public void setTransaccionesList(List<Transacciones> transaccionesList) {
        this.transaccionesList = transaccionesList;
    }

    @XmlTransient
    public List<Requerimiento> getRequerimientoList() {
        return requerimientoList;
    }

    public void setRequerimientoList(List<Requerimiento> requerimientoList) {
        this.requerimientoList = requerimientoList;
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
        if (!(object instanceof Estrategia)) {
            return false;
        }
        Estrategia other = (Estrategia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.Tools.modelo.Estrategia[ id=" + id + " ]";
    }
    
}
