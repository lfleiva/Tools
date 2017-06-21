/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.Tools.utilidades;

import co.Tools.control.AccesoDatos;
import co.Tools.modelo.Entidad;
import co.Tools.modelo.Estrategia;
import co.Tools.modelo.Requerimiento;
import co.Tools.modelo.TransaccionesEstrategia;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Mery Evelyn Ceron
 */
public class leerExcel {

    private final String URL_RESOURCE = "src/recursos/Transacciones.xls";

    AccesoDatos accesoDatos;

    public leerExcel() {
        CrearReportes reporte = new CrearReportes();
    }

    private void extraerEstrategia() {
        accesoDatos = new AccesoDatos();
        Iterator<Row> rowIterator = null;
        rowIterator = leerFilasExcel();

        Row row;
        int numFila = 0;
        // Recorremos todas las filas para mostrar el contenido de cada celda
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            numFila = numFila + 1;
            // Obtenemos el iterator que permite recorres todas las celdas de una fila
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell celda;
            Estrategia estrategia = new Estrategia();
            boolean registrarEstrategia = false;
            while (cellIterator.hasNext() && numFila > 1) {
                celda = cellIterator.next();
                String dato = tipoCelda(celda);

                if (celda.getColumnIndex() == 0) {
                    System.out.println(dato);
                    estrategia.setCodigo(dato);
                }

                if (celda.getColumnIndex() == 1) {
                    System.out.println(dato);
                    estrategia.setNombre(dato);
                }

                if (celda.getColumnIndex() == 3) {
                    System.out.println(dato);
                    estrategia.setIdEntidad(consultarEntidad(dato));
                }

                registrarEstrategia = true;
            }

            if (registrarEstrategia) {
                estrategia = accesoDatos.persistirActualizar(estrategia);
            }
        }
    }

    private void extraerRequerimientos() {
        accesoDatos = new AccesoDatos();
        Iterator<Row> rowIterator = null;
        rowIterator = leerFilasExcel();

        Row row;
        int numFila = 0;
        // Recorremos todas las filas para mostrar el contenido de cada celda
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            numFila = numFila + 1;
            // Obtenemos el iterator que permite recorres todas las celdas de una fila
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell celda;
            Requerimiento requerimiento = new Requerimiento();
            boolean registrarRequerimiento = false;
            while (cellIterator.hasNext() && numFila > 1) {
                celda = cellIterator.next();
                String dato = tipoCelda(celda);

                if (celda.getColumnIndex() == 0) {
                    System.out.println(dato);
                    requerimiento.setProd(dato);
                }

                if (celda.getColumnIndex() == 1) {
                    System.out.println(dato);
                    requerimiento.setEstado(dato);
                }

                if (celda.getColumnIndex() == 6) {
                    if (dato.isEmpty()) {
                        requerimiento.setIdEstrategia(null);
                    } else {
                        requerimiento.setIdEstrategia(consultarEstrategia(dato));
                    }
                }

                if (celda.getColumnIndex() == 9) {
                    System.out.println(dato);
                    requerimiento.setFechaRadicado(convierteFecha(dato));
                }
                
                if (celda.getColumnIndex() == 10) {
                    System.out.println(dato);
                    requerimiento.setFechaUltimoMovimiento(convierteFecha(dato));
                }

                registrarRequerimiento = true;
            }

            if (registrarRequerimiento && requerimiento.getIdEstrategia() != null) {
                requerimiento = accesoDatos.persistirActualizar(requerimiento);
            }
        }
    }

    private void extraerTransacciones() {
        accesoDatos = new AccesoDatos();
        Iterator<Row> rowIterator = null;
        rowIterator = leerFilasExcel();

        Row row;
        int numFila = 0;
        // Recorremos todas las filas para mostrar el contenido de cada celda
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            numFila = numFila + 1;
            // Obtenemos el iterator que permite recorres todas las celdas de una fila
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell celda;
            TransaccionesEstrategia transacciones = new TransaccionesEstrategia();
            boolean registrarTransaccion = false;
            while (cellIterator.hasNext() && numFila > 1) {
                celda = cellIterator.next();
                String dato = tipoCelda(celda);

                if (celda.getColumnIndex() == 0) {
                    System.out.println(dato);
                    transacciones.setIdEstrategia(consultarEstrategia(dato));
                }

                if (celda.getColumnIndex() == 4) {
                    System.out.println(dato);
                    transacciones.setAnio(dato);
                }

                if (celda.getColumnIndex() == 5) {
                    System.out.println(dato);
                    transacciones.setTransacciones(Integer.parseInt(dato));
                }

                registrarTransaccion = true;
            }

            if (registrarTransaccion && transacciones.getIdEstrategia() != null) {
                transacciones = accesoDatos.persistirActualizar(transacciones);
            }
        }
    }

    private void asignarComplejidad() {
        accesoDatos = new AccesoDatos();
        Iterator<Row> rowIterator = null;
        rowIterator = leerFilasExcel();

        Row row;
        int numFila = 0;
        // Recorremos todas las filas para mostrar el contenido de cada celda
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            numFila = numFila + 1;
            // Obtenemos el iterator que permite recorres todas las celdas de una fila
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell celda;
            Estrategia estrategia = new Estrategia();
            boolean registrarEstrategia = false;
            while (cellIterator.hasNext() && numFila > 1) {
                celda = cellIterator.next();
                String dato = tipoCelda(celda);

                if (celda.getColumnIndex() == 0) {
                    System.out.println(dato);
                    estrategia = consultarEstrategia(dato);
                }

                if (celda.getColumnIndex() == 6 && estrategia != null) {
                    System.out.println(dato);
                    if (!dato.isEmpty()) {
                        estrategia.setComplejidad(dato);
                    } else {
                        estrategia.setComplejidad("NA");
                    }
                }

                registrarEstrategia = true;
            }

            if (registrarEstrategia && estrategia != null) {
                estrategia = accesoDatos.persistirActualizar(estrategia);
            }
        }
    }

    private Entidad consultarEntidad(String nitentidad) {
        accesoDatos = new AccesoDatos();
        List<Entidad> listaEntidades = new ArrayList<Entidad>();
        Entidad entidad = new Entidad();
        entidad.setNit(nitentidad);
        listaEntidades = accesoDatos.consultarObjeto(Entidad.class, entidad);

        if (!listaEntidades.isEmpty()) {
            entidad = listaEntidades.get(0);
            return entidad;
        } else {
            return null;
        }
    }

    private Estrategia consultarEstrategia(String codigoEstrategia) {
        accesoDatos = new AccesoDatos();
        List<Estrategia> listaEstrategias = new ArrayList<Estrategia>();
        Estrategia estrategia = new Estrategia();
        estrategia.setCodigo(codigoEstrategia);
        listaEstrategias = accesoDatos.consultarObjeto(Estrategia.class, estrategia);

        if (!listaEstrategias.isEmpty()) {
            estrategia = listaEstrategias.get(0);
            return estrategia;
        } else {
            return null;
        }
    }

    private Iterator leerFilasExcel() {
        FileInputStream file = null;
        Iterator<Row> rowIterator = null;
        try {
            file = new FileInputStream(new File(URL_RESOURCE));
            // Crear el objeto que tendra el libro de Excel
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            /*
            * Obtenemos la primera pestaña a la que se quiera procesar indicando el indice.
            * Una vez obtenida la hoja excel con las filas que se quieren leer obtenemos el iterator
            * que nos permite recorrer cada una de las filas que contiene.
             */
            HSSFSheet sheet = workbook.getSheetAt(0);
            rowIterator = sheet.iterator();
            workbook.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(leerExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(leerExcel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(leerExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rowIterator;
    }

    public String tipoCelda(Cell c) {
        // Dependiendo del formato de la celda el valor se debe mostrar
        // como String, Fecha, boolean, entero...

        String dato = "";

        switch (c.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(c)) {
                    dato = c.getDateCellValue().toString();
                }
                BigDecimal numero = new BigDecimal(c.getNumericCellValue());
                dato = numero.toString();
                break;
            case Cell.CELL_TYPE_STRING:
                dato = c.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                dato = c.getBooleanCellValue() + "";
                break;
        }

        return dato;
    }

    public Date convierteFecha(String dato) {

        String[] regiones = dato.split(" ");

        System.out.println(regiones[0]);
        System.out.println(regiones[2]);
        System.out.println(regiones[4]);

        int dia = Integer.parseInt(regiones[0]);
        int mes = convertirTexto(regiones[2]);
        int anio = Integer.parseInt(regiones[4]);

        @SuppressWarnings("deprecation")
        Date fecha = new Date(anio - 1900, mes, dia);

        return fecha;
    }

    public Date convierteAnioFecha(String dato) {
        Date fecha = new Date(Integer.parseInt(dato), 0, 0);
        return fecha;
    }

    public int convertirTexto(String s) {
        int mes = 0;

        switch (s) {
            case "enero":
                mes = 0;
                break;

            case "febrero":
                mes = 1;
                break;

            case "marzo":
                mes = 2;
                break;

            case "abril":
                mes = 3;
                break;

            case "mayo":
                mes = 4;
                break;

            case "junio":
                mes = 5;
                break;

            case "julio":
                mes = 6;
                break;

            case "agosto":
                mes = 7;
                break;

            case "septiembre":
                mes = 8;
                break;

            case "octubre":
                mes = 9;
                break;

            case "noviembre":
                mes = 10;
                break;

            case "diciembre":
                mes = 11;
                break;

            default:
                break;
        }

        return mes;
    }
}
