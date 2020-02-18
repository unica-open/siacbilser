/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.limiteimpegnabile.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacintegser.business.service.limiteimpegnabile.model.CapitoloLimiteImpegnabile;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileLimiteImpegnabileHandler
{
	public List<CapitoloLimiteImpegnabile> readElencoCapitoliLimiteImpegnabile(byte[] fileBytes)
			throws InvalidFormatException, IOException
	{
		List<CapitoloLimiteImpegnabile> elencoCapitoliLimiteImpegnabile = new ArrayList<CapitoloLimiteImpegnabile>();

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
			CapitoloLimiteImpegnabile capitoloLimiteImpegnabile = mapRowToCapitoloLimiteImpegnabile(sheet.getRow(i++));

			if (capitoloLimiteImpegnabile != null)
				elencoCapitoliLimiteImpegnabile.add(capitoloLimiteImpegnabile);
			else
				atEnd = true;
		}

		return elencoCapitoliLimiteImpegnabile;
	}

	private CapitoloLimiteImpegnabile mapRowToCapitoloLimiteImpegnabile(Row row)
	{
		if (row != null)
		{
			CapitoloLimiteImpegnabile capitoloLimiteImpegnabile = new CapitoloLimiteImpegnabile();

			int c = 0;

			capitoloLimiteImpegnabile.setAnno((int) getNumericCellValue(row, c++));
			capitoloLimiteImpegnabile.setNumeroCapitolo(String.valueOf((int) getNumericCellValue(row, c++)));
			capitoloLimiteImpegnabile.setNumeroArticolo(String.valueOf((int) getNumericCellValue(row, c++)));
			capitoloLimiteImpegnabile.setNumeroUeb(String.valueOf((int) getNumericCellValue(row, c++)));
			capitoloLimiteImpegnabile.setImportoAnno(
					BigDecimal.valueOf(getNumericCellValue(row, c++)).setScale(2, BigDecimal.ROUND_HALF_UP));
			capitoloLimiteImpegnabile.setImportoAnno1(
					BigDecimal.valueOf(getNumericCellValue(row, c++)).setScale(2, BigDecimal.ROUND_HALF_UP));
			capitoloLimiteImpegnabile.setImportoAnno2(
					BigDecimal.valueOf(getNumericCellValue(row, c++)).setScale(2, BigDecimal.ROUND_HALF_UP));

			return capitoloLimiteImpegnabile;
		}

		return null;
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
