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
@Table(name = "migracion", catalog = "tools", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Migracion.findAll", query = "SELECT m FROM Migracion m"),
    @NamedQuery(name = "Migracion.findById", query = "SELECT m FROM Migracion m WHERE m.id = :id"),
    @NamedQuery(name = "Migracion.findByCq2016", query = "SELECT m FROM Migracion m WHERE m.cq2016 = :cq2016"),
    @NamedQuery(name = "Migracion.findByCq2017", query = "SELECT m FROM Migracion m WHERE m.cq2017 = :cq2017"),
    @NamedQuery(name = "Migracion.findByFechaUltimoCambio", query = "SELECT m FROM Migracion m WHERE m.fechaUltimoCambio = :fechaUltimoCambio"),
    @NamedQuery(name = "Migracion.findByFrecuencia", query = "SELECT m FROM Migracion m WHERE m.frecuencia = :frecuencia"),
    @NamedQuery(name = "Migracion.findByComplejidad", query = "SELECT m FROM Migracion m WHERE m.complejidad = :complejidad"),
    @NamedQuery(name = "Migracion.findByTransacciones", query = "SELECT m FROM Migracion m WHERE m.transacciones = :transacciones"),
    @NamedQuery(name = "Migracion.findByFrecuenciaPeso", query = "SELECT m FROM Migracion m WHERE m.frecuenciaPeso = :frecuenciaPeso"),
    @NamedQuery(name = "Migracion.findByComplejidadPeso", query = "SELECT m FROM Migracion m WHERE m.complejidadPeso = :complejidadPeso"),
    @NamedQuery(name = "Migracion.findByTransaccionesPeso", query = "SELECT m FROM Migracion m WHERE m.transaccionesPeso = :transaccionesPeso"),
    @NamedQuery(name = "Migracion.findByPeso", query = "SELECT m FROM Migracion m WHERE m.peso = :peso")})
public class Migracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "cq_2016")
    private Integer cq2016;
    @Column(name = "cq_2017")
    private Integer cq2017;
    @Column(name = "fecha_ultimo_cambio")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimoCambio;
    @Column(name = "frecuencia", length = 100)
    private String frecuencia;
    @Column(name = "complejidad", length = 100)
    private String complejidad;
    @Column(name = "transacciones")
    private Integer transacciones;
    @Column(name = "frecuencia_peso")
    private Integer frecuenciaPeso;
    @Column(name = "complejidad_peso")
    private Integer complejidadPeso;
    @Column(name = "transacciones_peso")
    private Integer transaccionesPeso;
    @Column(name = "peso")
    private Integer peso;
    @JoinColumn(name = "id_estrategia", referencedColumnName = "id")
    @ManyToOne
    private Estrategia idEstrategia;

    public Migracion() {
    }

    public Migracion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCq2016() {
        return cq2016;
    }

    public void setCq2016(Integer cq2016) {
        this.cq2016 = cq2016;
    }

    public Integer getCq2017() {
        return cq2017;
    }

    public void setCq2017(Integer cq2017) {
        this.cq2017 = cq2017;
    }

    public Date getFechaUltimoCambio() {
        return fechaUltimoCambio;
    }

    public void setFechaUltimoCambio(Date fechaUltimoCambio) {
        this.fechaUltimoCambio = fechaUltimoCambio;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(String complejidad) {
        this.complejidad = complejidad;
    }

    public Integer getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(Integer transacciones) {
        this.transacciones = transacciones;
    }

    public Integer getFrecuenciaPeso() {
        return frecuenciaPeso;
    }

    public void setFrecuenciaPeso(Integer frecuenciaPeso) {
        this.frecuenciaPeso = frecuenciaPeso;
    }

    public Integer getComplejidadPeso() {
        return complejidadPeso;
    }

    public void setComplejidadPeso(Integer complejidadPeso) {
        this.complejidadPeso = complejidadPeso;
    }

    public Integer getTransaccionesPeso() {
        return transaccionesPeso;
    }

    public void setTransaccionesPeso(Integer transaccionesPeso) {
        this.transaccionesPeso = transaccionesPeso;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
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
        if (!(object instanceof Migracion)) {
            return false;
        }
        Migracion other = (Migracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.Tools.modelo.Migracion[ id=" + id + " ]";
    }
    
}
