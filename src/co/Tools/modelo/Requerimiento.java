/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.modelo;

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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mery Evelyn Ceron
 */
@Entity
@Table(name = "requerimiento", catalog = "tools", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Requerimiento.findAll", query = "SELECT r FROM Requerimiento r"),
    @NamedQuery(name = "Requerimiento.findById", query = "SELECT r FROM Requerimiento r WHERE r.id = :id"),
    @NamedQuery(name = "Requerimiento.findByProd", query = "SELECT r FROM Requerimiento r WHERE r.prod = :prod"),
    @NamedQuery(name = "Requerimiento.findByEstado", query = "SELECT r FROM Requerimiento r WHERE r.estado = :estado"),
    @NamedQuery(name = "Requerimiento.findByFechaRadicado", query = "SELECT r FROM Requerimiento r WHERE r.fechaRadicado = :fechaRadicado"),
    @NamedQuery(name = "Requerimiento.findByFechaUltimoMovimiento", query = "SELECT r FROM Requerimiento r WHERE r.fechaUltimoMovimiento = :fechaUltimoMovimiento"),
    @NamedQuery(name = "Requerimiento.findByDescripcion", query = "SELECT r FROM Requerimiento r WHERE r.descripcion = :descripcion")})
public class Requerimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "prod", nullable = false, length = 100)
    private String prod;
    @Basic(optional = false)
    @Column(name = "estado", nullable = false, length = 100)
    private String estado;
    @Column(name = "fecha_radicado")
    @Temporal(TemporalType.DATE)
    private Date fechaRadicado;
    @Column(name = "fecha_ultimo_movimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimoMovimiento;
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    @JoinColumn(name = "id_estrategia", referencedColumnName = "id")
    @ManyToOne
    private Estrategia idEstrategia;

    public Requerimiento() {
    }

    public Requerimiento(Integer id) {
        this.id = id;
    }

    public Requerimiento(Integer id, String prod, String estado) {
        this.id = id;
        this.prod = prod;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaRadicado() {
        return fechaRadicado;
    }

    public void setFechaRadicado(Date fechaRadicado) {
        this.fechaRadicado = fechaRadicado;
    }

    public Date getFechaUltimoMovimiento() {
        return fechaUltimoMovimiento;
    }

    public void setFechaUltimoMovimiento(Date fechaUltimoMovimiento) {
        this.fechaUltimoMovimiento = fechaUltimoMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Requerimiento)) {
            return false;
        }
        Requerimiento other = (Requerimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.Tools.modelo.Requerimiento[ id=" + id + " ]";
    }
    
}
