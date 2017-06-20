/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.control;

import co.Tools.modelo.Estrategia;
import co.Tools.modelo.Requerimiento;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Luis Fernando Leiva
 */
public class AccesoDatos {
    
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ToolsPU");
    private EntityManager entityManager;

    public AccesoDatos() {
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public <E> E persistirActualizar(E objeto) {

        entityManager = null;

        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            objeto = entityManager.merge(objeto);

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        } 

        return objeto;
    }

    public <E> List<E> persistirActualizar(List<E> listaObjeto) {

        List<E> listaRetorno = new ArrayList<E>();

        entityManager = null;

        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            for (E objeto : listaObjeto) {
                objeto = entityManager.merge(entityManager.contains(objeto) ? objeto : entityManager.merge(objeto));
                listaRetorno.add(objeto);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return listaRetorno;
    }

    public <E> E consultar(Class<E> clase, Object idObjeto) {

        entityManager = null;

        try {

            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            E objeto = entityManager.find(clase, idObjeto);

            entityManager.getTransaction().commit();

            return objeto;

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public <E> List<E> consultarTodos(Class<E> clase, boolean all, int maxResults, int firstResult) {
        entityManager = null;

        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
            cq.select(cq.from(clase));
            Query q = entityManager.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public <E> List<E> consultarObjeto(Class<E> clase, Object objeto) {
        entityManager = null;
        List<E> listaObjetos = new ArrayList<E>();

        try {

            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            List<String> listaAtributos = new ArrayList<String>();
            List<Object> listaValores = new ArrayList<Object>();
            String condiciones = "";

            // Armar las condiciones de los primitivos del bean
            condiciones = this.doConstruirCondiciones(objeto, listaAtributos, listaValores, "");

            // Obtener la clase y nombre del atributo marcado como Id para JPA
            Class<? extends Object> claseId = getClaseAtributoId(objeto);
            String nombreAtributoId = getNombreAtributoId(objeto);

            // Si el id de la no es un tipo basico
            if (!claseId.equals(Boolean.class) && !claseId.equals(Byte.class) && !claseId.equals(Short.class) && !claseId.equals(Integer.class) && !claseId.equals(Long.class) && !claseId.equals(Float.class) && !claseId.equals(Double.class) && !claseId.equals(String.class) && !claseId.equals(Character.class)) {

                // Obtener el valor del atributo que es el Id
                Object retorno = null;
                try {
                    // Obtener el metodo del getter del id del objeto
                    Method metodoGetId = objeto.getClass().getMethod("get" + nombreAtributoId.substring(0, 1).toUpperCase() + nombreAtributoId.substring(1), new Class[]{});
                    retorno = metodoGetId.invoke(objeto, new Object[]{});
                    if (retorno != null) {
                        // Armar las condiciones de los primitivos de la llave compuesta
                        condiciones += (condiciones.length() > 0 ? " AND " : "") + this.doConstruirCondiciones(retorno, listaAtributos, listaValores, nombreAtributoId);
                    }
                } catch (NoSuchMethodException e) {

                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
            }

            // Ensamblar la consulta
            StringBuilder strConsulta = new StringBuilder();
            strConsulta.append("SELECT objeto FROM ").append(objeto.getClass().getName()).append(" AS objeto WHERE ");                       
            
            if (condiciones != null && !condiciones.trim().equals("")) {                                               
                // Agregar las condiciones a la consulta
                strConsulta.append(condiciones);
            }

            // Crear el query
            TypedQuery<E> consulta = entityManager.createQuery(strConsulta.toString(), clase);

            //consulta.setParameter("vigencia", vigencia);

            // Asignar los atributos al query
            Iterator<String> iteradorListaPropiedades = listaAtributos.iterator();
            for (Object parametro : listaValores) {
                consulta.setParameter(iteradorListaPropiedades.next(), parametro);
            }

            // Ejecutar el query y devolver resultados
            listaObjetos = consulta.getResultList();

        } catch (PersistenceException e) {
            System.out.println(e);
        } catch (RuntimeException e) {
            System.out.println(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return listaObjetos;
    }

    public <E> List<E> consultarTodos(Class<E> clase) {
        entityManager = null;

        try {

            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            // Ensamblar la consulta
            StringBuilder strConsulta = new StringBuilder();
            strConsulta.append("SELECT objeto FROM ").append(clase.getName()).append(" AS objeto");
            // Ejecutar la consulta y devolver el resultado
            TypedQuery<E> consulta = entityManager.createQuery(strConsulta.toString(), clase);
            //consulta.setParameter("vigencia", vigencia);
            List<E> listaObjetos = consulta.getResultList();
            return listaObjetos;

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public <E> List<E> consultarTodos(E objeto, Class<E> clase) {

        entityManager = null;
        List<E> listaObjetos = new ArrayList<E>();

        try {

            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            List<String> listaAtributos = new ArrayList<String>();
            List<Object> listaValores = new ArrayList<Object>();
            String condiciones = "";

            // Armar las condiciones de los primitivos del bean
            condiciones = this.doConstruirCondiciones(objeto, listaAtributos, listaValores, "");

            // Obtener la clase y nombre del atributo marcado como Id para JPA
            Class<? extends Object> claseId = getClaseAtributoId(objeto);
            String nombreAtributoId = getNombreAtributoId(objeto);

            // Si el id de la no es un tipo basico
            if (!claseId.equals(Boolean.class) && !claseId.equals(Byte.class) && !claseId.equals(Short.class) && !claseId.equals(Integer.class) && !claseId.equals(Long.class) && !claseId.equals(Float.class) && !claseId.equals(Double.class) && !claseId.equals(String.class) && !claseId.equals(Character.class)) {

                // Obtener el valor del atributo que es el Id
                Object retorno = null;
                try {
                    // Obtener el metodo del getter del id del objeto
                    Method metodoGetId = objeto.getClass().getMethod("get" + nombreAtributoId.substring(0, 1).toUpperCase() + nombreAtributoId.substring(1), new Class[]{});
                    retorno = metodoGetId.invoke(objeto, new Object[]{});
                    if (retorno != null) {
                        // Armar las condiciones de los primitivos de la llave compuesta
                        condiciones += (condiciones.length() > 0 ? " AND " : "") + this.doConstruirCondiciones(retorno, listaAtributos, listaValores, nombreAtributoId);
                    }
                } catch (NoSuchMethodException e) {

                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
            }

            // Ensamblar la consulta
            StringBuilder strConsulta = new StringBuilder();
            strConsulta.append("SELECT objeto FROM ").append(objeto.getClass().getName()).append(" AS objeto");
            if (condiciones != null && !condiciones.trim().equals("")) {
                // Agregar las condiciones a la consulta
                strConsulta.append(" WHERE " + condiciones);
            }

            // Crear el query
            TypedQuery<E> consulta = entityManager.createQuery(strConsulta.toString(), clase);

            // Asignar los atributos al query
            Iterator<String> iteradorListaPropiedades = listaAtributos.iterator();
            for (Object parametro : listaValores) {
                consulta.setParameter(iteradorListaPropiedades.next(), parametro);
            }

            // Ejecutar el query y devolver resultados
            listaObjetos = consulta.getResultList();

        } catch (PersistenceException e) {
            System.out.println(e);
        } catch (RuntimeException e) {
            System.out.println(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return listaObjetos;
    }

    private String doConstruirCondiciones(Object objeto, List<String> listaAtributos, List<Object> listaValores, String prefijo) {
        Boolean hayCondiciones = false;
        String condiciones = "";
        Method[] listaMetodos = objeto.getClass().getMethods();
        // Recorrer los metodos de la clase
        for (Method metodo : listaMetodos) {
            // Obtener el nombre de la propiedad
            String atributo = metodo.getName().replace("get", "").substring(0, 1).toLowerCase() + metodo.getName().replace("get", "").substring(1);
            // Verificar si el metodo corresponde a un getter
            if (metodo.getName().substring(0, 3).equals("get")) {                
                // Verificar que el getter corresponda a un tipo primitivo o sus
                // correspondientes wrappers
                if (metodo.getReturnType().equals(Boolean.class) || metodo.getReturnType().equals(Byte.class) || metodo.getReturnType().equals(Short.class) || metodo.getReturnType().equals(Integer.class) || metodo.getReturnType().equals(Long.class) || metodo.getReturnType().equals(Float.class) || metodo.getReturnType().equals(Double.class) || metodo.getReturnType().equals(String.class) || metodo.getReturnType().equals(Character.class) || metodo.getReturnType().equals(BigDecimal.class)) {                    
                    // Obtener el valor del metodo                    
                    Object retorno = null;
                    try {
                        retorno = metodo.invoke(objeto, new Object[]{});
                        if (retorno != null) {
                            listaAtributos.add(atributo);
                            // Agregar la condición al query
                            condiciones += " AND " + (prefijo != null && !prefijo.equals("") ? prefijo + "." : "") + atributo + " = :" + atributo;
                            // Agregar el parametro a la lista de parametros
                            listaValores.add(retorno);
                            hayCondiciones = true;
                        }
                    } catch (IllegalAccessException e) {

                    } catch (InvocationTargetException e) {

                    }
                }
            }
        }

        // Si se agregaron condiciones quitar el primer AND
        if (hayCondiciones) {
            condiciones = condiciones.substring(5);
        }

        return condiciones;
    }

    public static <E> Class<? extends Object> getClaseAtributoId(E objeto) {
        Field[] listaAtributos = objeto.getClass().getDeclaredFields();
        for (Field atributo : listaAtributos) {
            for (Annotation anotacion : atributo.getAnnotations()) {
                if (anotacion.annotationType().equals(EmbeddedId.class) || anotacion.annotationType().equals(Id.class)) {
                    return atributo.getType();
                }
            }
        }
        return null;
    }

    public static <E> String getNombreAtributoId(E objeto) {
        Field[] listaAtributos = objeto.getClass().getDeclaredFields();
        for (Field atributo : listaAtributos) {
            for (Annotation anotacion : atributo.getAnnotations()) {
                if (anotacion.annotationType().equals(EmbeddedId.class) || anotacion.annotationType().equals(Id.class)) {
                    return atributo.getName();
                }
            }
        }
        return null;
    }

    public <E> List<E> consultarHSQL(String HSQLQuery, Map<String, Object> mapaParametros, Class<E> clase, Integer posicionInicio, Integer numeroRegistros) {

        entityManager = null;

        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            TypedQuery<E> query = entityManager.createQuery(HSQLQuery, clase);
            for (Map.Entry<String, Object> e : mapaParametros.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (posicionInicio != null && posicionInicio > 0) {
                query.setFirstResult(posicionInicio);
            }

            if (numeroRegistros != null && numeroRegistros > 0) {
                query.setMaxResults(numeroRegistros);
            }

            return query.getResultList();

        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    
    public List<Requerimiento> consultarRequerimientos(Estrategia estrategia) {
        List<Requerimiento> listaRequerimientos = new ArrayList<Requerimiento>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT R FROM Requerimiento R ");
        sql.append("WHERE R.idEstrategia = :estrategia ");
        sql.append("ORDER BY R.fechaRadicado ASC");

        Map<String, Object> mapParametros = new HashMap<String, Object>();
        mapParametros.put("estrategia", estrategia);

        listaRequerimientos = consultarHSQL(sql.toString(), mapParametros, Requerimiento.class, 0, 0);

        if (listaRequerimientos != null && !listaRequerimientos.isEmpty()) {
            return listaRequerimientos;
        } else {
            return null;
        }
    }

    public Integer eliminacionRegistros(String consulta, Map<String, Object> mapaParametros) {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery(consulta);

            for (Map.Entry<String, Object> e : mapaParametros.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }

            return query.executeUpdate();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public <E> Boolean eliminarRegistro(E objeto) {
        
        try {
            entityManager = null;    
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();                
            entityManager.remove(objeto);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return true;
    }

    
    public void eliminarRegistro(String entidad, String condicion, String valor) {
        entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createNativeQuery("DELETE FROM " + entidad + " WHERE " + condicion + " = " + valor);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
