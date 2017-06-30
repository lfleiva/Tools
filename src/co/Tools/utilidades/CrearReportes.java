/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.utilidades;

import co.Tools.control.AccesoDatos;
import co.Tools.modelo.Entidad;
import co.Tools.modelo.Estrategia;
import co.Tools.modelo.Migracion;
import co.Tools.modelo.Requerimiento;
import co.Tools.modelo.Transacciones;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Fernando Leiva
 */
public class CrearReportes {

    private final int DIAS_SEMANA = 8;
    private final int DIAS_QUINCENAL = 15;
    private final int DIAS_MENSUAL = 30;
    private final int DIAS_BIMENSUAL = 60;
    private final int DIAS_TRIMESTRAL = 90;
    private final int DIAS_SEMESTRAL = 180;
    private final int DIAS_ANUAL = 360;

    AccesoDatos accesoDatos;

    public CrearReportes() {
        pesoComplejidad("BAJA");
        crearReporteMigracion();
    }

    private void crearReporteMigracion() {
        accesoDatos = new AccesoDatos();
        List<Transacciones> listaTransacciones = new ArrayList<Transacciones>();
        listaTransacciones = accesoDatos.consultarTodos(Transacciones.class);

        if (!listaTransacciones.isEmpty()) {
            for (Transacciones transacciones : listaTransacciones) {
                Migracion migracion = new Migracion();
                migracion.setIdEstrategia(transacciones.getIdEstrategia());
                migracion.setCodigoEstrategia(transacciones.getIdEstrategia().getCodigo());
                migracion.setNombreEstrategia(transacciones.getIdEstrategia().getNombre());
                migracion.setMultientidad(transacciones.getIdEstrategia().getMultientidad() ? "SI" : "NO");       
                migracion.setNitEntidad(transacciones.getNit());
                migracion.setNombreEntidad(transacciones.getEntidad());
                migracion.setAutonomo(validarClienteAutonomo(transacciones.getNit()));
                migracion.setComplejidad(transacciones.getIdEstrategia().getComplejidad());
                migracion.setTransacciones(transacciones.getTransacciones());

                List<Requerimiento> listaRequerimientos = new ArrayList<Requerimiento>();
                listaRequerimientos = consultarRequerimientos(transacciones.getIdEstrategia());

                int requerimientos2016 = 0;
                int requerimientos2017 = 0;
                if (listaRequerimientos != null && !listaRequerimientos.isEmpty()) {
                    migracion.setFrecuencia(frecuenciaRequerimientos(listaRequerimientos));
                    for (Requerimiento requerimiento : listaRequerimientos) {
                        Calendar fechaCal = new GregorianCalendar();
                        fechaCal.setTime(requerimiento.getFechaRadicado());
                        int anio = fechaCal.get(Calendar.YEAR);
                        if (anio == 2016) {
                            ++requerimientos2016;
                        } else if (anio == 2017) {
                            ++requerimientos2017;
                        }
                    }
                    migracion.setCq2016(requerimientos2016);
                    migracion.setCq2017(requerimientos2017);

                    migracion.setFechaUltimoCambio(listaRequerimientos.get(listaRequerimientos.size() - 1).getFechaRadicado());                    
                    
                    if(migracion.getComplejidad() != null) {
                        migracion.setComplejidadPeso(pesoComplejidad(migracion.getComplejidad()));
                    } else {
                        migracion.setComplejidadPeso(0);
                    }
                    
                    migracion.setFrecuenciaPeso(pesoFrecuencia(migracion.getFrecuencia()));                    
                    migracion.setTransaccionesPeso(pesoTransacciones(migracion.getTransacciones()));
                    migracion.setPeso(migracion.getFrecuenciaPeso() + migracion.getComplejidadPeso() + migracion.getTransaccionesPeso());

                    accesoDatos.persistirActualizar(migracion);
                } else {
                    migracion.setFrecuencia("NA");
                    migracion.setCq2016(requerimientos2016);
                    migracion.setCq2017(requerimientos2017);
                    accesoDatos.persistirActualizar(migracion);
                }
            }
        }
    }

    private List<Requerimiento> consultarRequerimientos(Estrategia estrategia) {
        accesoDatos = new AccesoDatos();
        List<Requerimiento> listaRequerimientos = new ArrayList<Requerimiento>();
        Requerimiento requerimiento = new Requerimiento();
        requerimiento.setIdEstrategia(estrategia);
        listaRequerimientos = accesoDatos.consultarRequerimientos(estrategia);

        if (listaRequerimientos != null && !listaRequerimientos.isEmpty()) {
            return listaRequerimientos;
        } else {
            return null;
        }
    }

    private String frecuenciaRequerimientos(List<Requerimiento> listaRequerimientos) {
        String frecuencia = "NA";
        int tamanoLista = listaRequerimientos.size();
        int diasDiferencia = 0;
        int dias = 0;

        for (int i = 0; i < tamanoLista; i++) {
            if (i + 1 < tamanoLista) {
                Requerimiento requerimiento = new Requerimiento();
                Requerimiento requerimientoSgte = new Requerimiento();
                requerimiento = listaRequerimientos.get(i);
                requerimientoSgte = listaRequerimientos.get(i + 1);

                diasDiferencia = diasDiferencia + (int) ((requerimientoSgte.getFechaRadicado().getTime() - requerimiento.getFechaRadicado().getTime()) / 86400000);
                System.out.println("dias de diferencia = " + diasDiferencia);
            }
        }

        float promedio = diasDiferencia / tamanoLista;

        Calendar fechaActual = new GregorianCalendar();
        fechaActual = Calendar.getInstance();
        Calendar fechaUltimoRequerimiento = new GregorianCalendar();
        fechaUltimoRequerimiento.setTime(listaRequerimientos.get(tamanoLista - 1).getFechaRadicado());

        int diasDiferenciaFechaActual = (int) ((fechaActual.getTime().getTime() - fechaUltimoRequerimiento.getTime().getTime()) / 86400000);

        if (promedio > 0) {
            if (promedio < diasDiferenciaFechaActual) {
                dias = diasDiferenciaFechaActual;
            } else {
                dias = (int) promedio;
            }

            if (dias < DIAS_QUINCENAL) {
                frecuencia = "SEMANAL";
            } else if (dias >= DIAS_QUINCENAL && dias < DIAS_MENSUAL) {
                frecuencia = "QUINCENAL";
            } else if (dias >= DIAS_MENSUAL && dias < DIAS_BIMENSUAL) {
                frecuencia = "MENSUAL";
            } else if (dias >= DIAS_BIMENSUAL && dias < DIAS_TRIMESTRAL) {
                frecuencia = "BIMENSUAL";
            } else if (dias >= DIAS_TRIMESTRAL && dias < DIAS_SEMESTRAL) {
                frecuencia = "TRIMESTRAL";
            } else if (dias >= DIAS_SEMESTRAL && dias < DIAS_ANUAL) {
                frecuencia = "SEMESTRAL";
            } else if (dias >= DIAS_ANUAL) {
                frecuencia = "ANUAL O MAYOR";
            }
        } else {
            frecuencia = "NA";
        }

        return frecuencia;
    }
    
    private int pesoComplejidad(String complejidad) {
        int peso = 0;
        
        try {
            Properties p = new Properties();
            p.load(new FileReader("src/co/Tools/utilidades/matriz_pesos.properties"));
            
            switch (complejidad) {
                case "BAJA":
                    peso = Integer.parseInt(p.getProperty("COMPLEJIDAD_BAJA"));
                    break;
                case "MEDIA":
                    peso = Integer.parseInt(p.getProperty("COMPLEJIDAD_MEDIA"));
                    break;            
                case "ALTA":
                    peso = Integer.parseInt(p.getProperty("COMPLEJIDAD_ALTA"));
                    break;
                default:
                    break;
            }
            
            System.out.println(complejidad + " = " + peso);            
        } catch (IOException ex) {
            Logger.getLogger(CrearReportes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return peso;
    }
    
    private int pesoFrecuencia(String frecuencia) {
        int peso = 0;
        
        try {
            Properties p = new Properties();
            p.load(new FileReader("src/co/Tools/utilidades/matriz_pesos.properties"));
            
            switch (frecuencia) {
                case "SEMANAL":
                    peso = Integer.parseInt(p.getProperty("FRECUENCIA_SEMANAL"));
                    break;
                case "QUINCENAL":
                    peso = Integer.parseInt(p.getProperty("FRECUENCIA_QUINCENAL"));
                    break;            
                case "MENSUAL":
                    peso = Integer.parseInt(p.getProperty("FRECUENCIA_MENSUAL"));
                    break;
                case "BIMENSUAL":
                    peso = Integer.parseInt(p.getProperty("FRECUENCIA_BIMENSUAL"));
                    break;
                case "TRIMESTRAL":
                    peso = Integer.parseInt(p.getProperty("FRECUENCIA_TRIMESTRAL"));
                    break;
                case "SEMESTRAL":
                    peso = Integer.parseInt(p.getProperty("FRECUENCIA_SEMESTRAL"));
                    break;
                case "ANUAL O MAYOR":
                    peso = Integer.parseInt(p.getProperty("FRECUENCIA_ANUAL"));
                    break;
                case "NA":
                    peso = Integer.parseInt(p.getProperty("SIN_CAMBIOS"));
                    break;
                default:
                    break;
            }
            
            System.out.println(frecuencia + " = " + peso);            
        } catch (IOException ex) {
            Logger.getLogger(CrearReportes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return peso;
    }
    
    private int pesoTransacciones(int transacciones) {
        int peso = 0;
        
        try {
            Properties p = new Properties();
            p.load(new FileReader("src/co/Tools/utilidades/matriz_pesos.properties"));
            int baja = Integer.parseInt(p.getProperty("NUMERO_BAJA"));
            int media = Integer.parseInt(p.getProperty("NUMERO_MEDIA"));
            
            if(transacciones <= baja) {
                peso = Integer.parseInt(p.getProperty("TRANSACCIONES_BAJA"));
            } else if(transacciones > baja && transacciones <= media) {
                peso = Integer.parseInt(p.getProperty("TRANSACCIONES_MEDIA"));
            } else if(transacciones > media) {
                peso = Integer.parseInt(p.getProperty("TRANSACCIONES_ALTA"));
            }            
            
            System.out.println(transacciones + " = " + peso);            
        } catch (IOException ex) {
            Logger.getLogger(CrearReportes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return peso;
    }
    
    private String validarClienteAutonomo(String nit) {
        accesoDatos = new AccesoDatos();
        List<Entidad> listaEntidad = new ArrayList<Entidad>();
        Entidad entidad = new Entidad();        
        
        entidad.setNit(nit);
        listaEntidad = accesoDatos.consultarObjeto(Entidad.class, entidad);
        
        if(!listaEntidad.isEmpty()) {
            entidad = listaEntidad.get(0);            
            
            if(entidad.getAutonomo()) {
                return "SI";
            } else {
                return "NO";
            }
        } else {
            return "NO";
        }
    }
}
