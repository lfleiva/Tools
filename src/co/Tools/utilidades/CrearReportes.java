/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.utilidades;

import co.Tools.control.AccesoDatos;
import co.Tools.modelo.Estrategia;
import co.Tools.modelo.Migracion;
import co.Tools.modelo.Requerimiento;
import co.Tools.modelo.TransaccionesEstrategia;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
        crearPrueba();
    }

    private void crearPrueba() {
        accesoDatos = new AccesoDatos();
        List<TransaccionesEstrategia> listaTransacciones = new ArrayList<TransaccionesEstrategia>();
        listaTransacciones = accesoDatos.consultarTodos(TransaccionesEstrategia.class);

        if (!listaTransacciones.isEmpty()) {
            for (TransaccionesEstrategia transacciones : listaTransacciones) {
                Migracion migracion = new Migracion();
                migracion.setIdEstrategia(transacciones.getIdEstrategia());
                migracion.setComplejidad(transacciones.getIdEstrategia().getComplejidad());
                migracion.setTransacciones(transacciones.getTransacciones());

                List<Requerimiento> listaRequerimientos = new ArrayList<Requerimiento>();
                listaRequerimientos = consultarRequerimientos(transacciones.getIdEstrategia());

                int requerimientos2016 = 0;
                int requerimientos2017 = 0;
                if (listaRequerimientos != null && !listaRequerimientos.isEmpty()) {
                    migracion.setFrecuenciaCambios(frecuenciaRequerimientos(listaRequerimientos));
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
                    
                    accesoDatos.persistirActualizar(migracion);
                } else {
                    migracion.setFrecuenciaCambios("NA");
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
            frecuencia = "ANUAL";
        }      

        return frecuencia;
    }
}
