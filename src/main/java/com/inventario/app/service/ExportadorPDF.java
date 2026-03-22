package com.inventario.app.service;

import com.inventario.app.model.Movimientos;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
public class ExportadorPDF {

public void exportar(HttpServletResponse response, List<Movimientos> lista, Date inicio, Date fin) throws IOException {
    
    // Creamos un formateador para que no salga con la hora y sea legible
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
    String fechaTextoInicio = (inicio != null) ? sdf.format(inicio) : "N/A";
    String fechaTextoFin = (fin != null) ? sdf.format(fin) : "N/A";

    Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
    PdfWriter.getInstance(documento, response.getOutputStream());
    documento.open();


        // --- COLORES EXACTOS DEL DISEÑO ---
        Color azulCorporativo = new Color(0, 56, 118); // Navy blue para cabeceras
        Color grisCebra = new Color(242, 242, 242);     // Light gray para filas alternas
        Color azulTitulo = new Color(0, 86, 179);     // Blue para el título

        // --- FUENTES ---
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, azulTitulo);
        Font fontFiltro = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font fontCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);

        // --- 1. CABECERA: LOGO Y TÍTULO ---
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.setWidthPercentage(100);
        
        // Carga el logo desde src/main/resources/static/images/logo.png
        URL logoUrl = getClass().getResource("/static/images/logo.png");
        if (logoUrl != null) {
            Image imageLogo = Image.getInstance(logoUrl);
            imageLogo.scaleToFit(180f, 60f); // Escalar logo
            
            PdfPCell cellLogo = new PdfPCell(imageLogo);
            cellLogo.setBorder(Rectangle.NO_BORDER);
            cellLogo.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellLogo.setPaddingBottom(15f);
            tableHeader.addCell(cellLogo);
        } else {
            // Si no encuentra el logo, avisa en consola pero no detiene el PDF
            System.err.println("ERROR: No se pudo cargar el logo en static/images/logo.png");
        }
        documento.add(tableHeader);

        // Título del Reporte
        Paragraph pTitulo = new Paragraph("REPORTE DE MOVIMIENTOS", fontTitulo);
        pTitulo.setAlignment(Element.ALIGN_LEFT);
        pTitulo.setSpacingAfter(15f);
        documento.add(pTitulo);

        // --- 2. INFORMACIÓN DE FILTROS ---
PdfPTable tableFiltros = new PdfPTable(2);
tableFiltros.setWidthPercentage(50f);
tableFiltros.setHorizontalAlignment(Element.ALIGN_LEFT);

String empName = "Todos los empleados";
if (!lista.isEmpty() && lista.get(0).getIdEmpleado() != null) {
    empName = lista.get(0).getIdEmpleado().getNombre() + " " + lista.get(0).getIdEmpleado().getApellidos();
}

// Fila Empleado
tableFiltros.addCell(new PdfPCell(new Phrase("Empleado:", fontFiltro)) {{ setBorder(Rectangle.NO_BORDER); }});
tableFiltros.addCell(new PdfPCell(new Phrase(empName, fontFiltro)) {{ setBorder(Rectangle.NO_BORDER); setPaddingLeft(10f); }});

// Fila Fechas (AQUÍ USAMOS LAS VARIABLES NUEVAS)
tableFiltros.addCell(new PdfPCell(new Phrase("Rango de Fechas:", fontFiltro)) {{ setBorder(Rectangle.NO_BORDER); }});
tableFiltros.addCell(new PdfPCell(new Phrase(fechaTextoInicio + " - " + fechaTextoFin, fontFiltro)) {{ setBorder(Rectangle.NO_BORDER); setPaddingLeft(10f); }});

tableFiltros.setSpacingAfter(20f);
documento.add(tableFiltros);

        // --- 3. TABLA DE DATOS (Estilo Navy/Cebra) ---
        PdfPTable tabla = new PdfPTable(4); 
        tabla.setWidthPercentage(100);
        // Anchos proporcionales exactos del diseño
        tabla.setWidths(new float[] {1.5f, 3.2f, 3.2f, 2.1f});

        // CABECERA (Azul Corporativo)
        String[] titulos = {"FECHA", "TRABAJADOR", "ARTÍCULO", "CANTIDAD"};
        
        for (String t : titulos) {
            PdfPCell cell = new PdfPCell(new Phrase(t, fontCabecera));
            cell.setBackgroundColor(azulCorporativo); 
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER); // Quitar bordes para limpieza
            tabla.addCell(cell);
        }

        // CUERPO (Efecto Cebra)
        int contadorFilas = 0;
        for (Movimientos m : lista) {
            
            // Determinar color de fondo
            Color colorFondo = (contadorFilas % 2 != 0) ? grisCebra : Color.WHITE;
            
            // Celda Fecha
            PdfPCell c1 = new PdfPCell(new Phrase(sdf.format(m.getFecha()), fontCuerpo));
            c1.setBackgroundColor(colorFondo);
            c1.setPadding(8);
            c1.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(c1);
            
            // Celda Trabajador
            String emp = (m.getIdEmpleado() != null) ? m.getIdEmpleado().getNombre() + " " + m.getIdEmpleado().getApellidos() : "N/A";
            PdfPCell c2 = new PdfPCell(new Phrase(emp, fontCuerpo));
            c2.setBackgroundColor(colorFondo);
            c2.setPadding(8);
            c2.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(c2);
            
            // Celda Artículo
            String art = (m.getIdArticulo() != null) ? m.getIdArticulo().getNombre() : "N/A";
            PdfPCell c3 = new PdfPCell(new Phrase(art, fontCuerpo));
            c3.setBackgroundColor(colorFondo);
            c3.setPadding(8);
            c3.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(c3);
            
            // Celda Cantidad (Centrada)
            PdfPCell c4 = new PdfPCell(new Phrase(String.valueOf(m.getCantidad()), fontCuerpo));
            c4.setBackgroundColor(colorFondo);
            c4.setPadding(8);
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(c4);
            
            contadorFilas++;
        }

        documento.add(tabla);
        // --- 5. SECCIÓN DE FIRMA (Abajo a la derecha) ---
        // Creamos una tabla de 2 columnas para empujar el cuadro a la derecha
        PdfPTable tablaFirma = new PdfPTable(2);
        tablaFirma.setWidthPercentage(100);
        tablaFirma.setSpacingBefore(50f); // Espacio después de la tabla de datos

        // Columna izquierda vacía (para hacer espacio)
        PdfPCell celdaVacia = new PdfPCell(new Phrase(""));
        celdaVacia.setBorder(Rectangle.NO_BORDER);
        tablaFirma.addCell(celdaVacia);

        // Columna derecha con el cuadro de firma
        Paragraph pFirma = new Paragraph();
        pFirma.add(new Chunk("__________________________\n", fontCuerpo));
        pFirma.add(new Chunk("Firma del Trabajador", fontFiltro)); // Usamos fontFiltro para que resalte en negrita
        
        PdfPCell celdaFirma = new PdfPCell(pFirma);
        celdaFirma.setBorder(Rectangle.NO_BORDER);
        celdaFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaFirma.setVerticalAlignment(Element.ALIGN_BOTTOM);
        tablaFirma.addCell(celdaFirma);

        documento.add(tablaFirma);

      
        // --- 4. PIE DE PÁGINA ---
        Paragraph fechaGen = new Paragraph("\nGenerado el: " + new Date(), fontFooter);
        fechaGen.setAlignment(Element.ALIGN_RIGHT);
        documento.add(fechaGen);

        Paragraph footerLine = new Paragraph("Sistema de Gestión de Almacén © 2026 | CALSOMATU Naval e Industrial", fontFooter);
        footerLine.setAlignment(Element.ALIGN_CENTER);
        footerLine.setSpacingBefore(15f);
        documento.add(footerLine);

        documento.close();
    }
}