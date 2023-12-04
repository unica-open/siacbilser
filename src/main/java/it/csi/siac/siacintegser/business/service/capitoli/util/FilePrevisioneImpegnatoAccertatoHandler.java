/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.capitoli.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;


public class FilePrevisioneImpegnatoAccertatoHandler
{
	public FilePrevisioneImpegnatoAccertatoHandler() {
		
	}
	
	List<Integer> listaDistintiAnniPresenti = new ArrayList<Integer>();
	
	public List<PrevisioneImpegnatoAccertato> readElencoPrevisioneImpegnatoAccertatoDaFile(byte[] fileBytes)
			throws InvalidFormatException, IOException
	{
		List<PrevisioneImpegnatoAccertato> elencoPrevisioneImpegnatoAccertato = new ArrayList<PrevisioneImpegnatoAccertato>();

		ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
		Workbook wb = null;
		try
		{
			wb = WorkbookFactory.create(bais);
		}
		catch (Throwable e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Sheet sheet = wb.getSheetAt(0);

		boolean atEnd = false;
		int i = 1;

		while (!atEnd && i < sheet.getPhysicalNumberOfRows())
		{
			PrevisioneImpegnatoAccertato previsioneImpegnatoAccertato = mapRowToPrevisioneImpenatoAccertato(sheet.getRow(i++));

			if (previsioneImpegnatoAccertato != null) {
				elencoPrevisioneImpegnatoAccertato.add(previsioneImpegnatoAccertato);
			}else {
				atEnd = true;
			}
		}

		return elencoPrevisioneImpegnatoAccertato;
	}

	private PrevisioneImpegnatoAccertato mapRowToPrevisioneImpenatoAccertato(Row row)
	{
		if (row != null)
		{
			PrevisioneImpegnatoAccertato previsioneImpegnatoAccertatoSuCapitolo = new PrevisioneImpegnatoAccertato();

			int c = 0;
			//primoCampo: Entrata o Spesa
			String type = (String) getStringCellValue(row, c++);
			if("E".equals(type)) {
				previsioneImpegnatoAccertatoSuCapitolo.setCapitoloEntrataGestione(new CapitoloEntrataGestione());
			}else {
				previsioneImpegnatoAccertatoSuCapitolo.setCapitoloUscitaGestione(new CapitoloUscitaGestione());
			}
			Integer annoCapitolo = (int) getNumericCellValue(row, c++);
			Integer numeroCapitolo = (int) getNumericCellValue(row, c++);
			
			//la riga non e' popolata, considero il file finito?
			if(StringUtils.isBlank(type) && (annoCapitolo == null || annoCapitolo.intValue() == 0)  && (numeroCapitolo == null || numeroCapitolo.intValue() == 0)) {
				return null;
			}
			
			previsioneImpegnatoAccertatoSuCapitolo.getCapitolo().setAnnoCapitolo(annoCapitolo);
			previsioneImpegnatoAccertatoSuCapitolo.getCapitolo().setNumeroCapitolo(numeroCapitolo);
			previsioneImpegnatoAccertatoSuCapitolo.getCapitolo().setNumeroArticolo((int) getNumericCellValue(row, c++));
//			previsioneImpegnatoAccertatoSuCapitolo.getCapitolo().setNumeroUEB((int) getNumericCellValue(row, c++));
			
			if(annoCapitolo != null && !listaDistintiAnniPresenti.contains(annoCapitolo)) {
				listaDistintiAnniPresenti.add(annoCapitolo);
			}
			
			return previsioneImpegnatoAccertatoSuCapitolo;
		}

		return null;
	}

	public List<Integer> getAnniEsercizioPresentiSuExcel(){
		return this.listaDistintiAnniPresenti;
	}
	
	private double getNumericCellValue(Row row, int col)
	{
		try {
			Cell cell = row.getCell(col);
	
			return cell.getNumericCellValue();
		} catch (Exception e) {
			throw new RuntimeException(String.format("%s (colonna %d)", e.getMessage(), col + 1));
		}
	}

	private String getStringCellValue(Row row, int i)
	{
		Cell cell = row.getCell(i);

		return cell.getStringCellValue();
	}

}
