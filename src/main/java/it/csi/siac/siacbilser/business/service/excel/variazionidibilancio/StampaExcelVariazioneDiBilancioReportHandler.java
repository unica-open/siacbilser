/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.variazionidibilancio;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelBaseBytesHandler;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.report.ReportColumn;
import it.csi.siac.siacbilser.model.report.VariazioneImportoCapitoloReport;
import it.csi.siac.siacbilser.model.report.VariazioneImportoCapitoloReportCampiColonne;

/**
 * Report handler per la stampa dell'allegato atto.
 *
 * @author Elisa Chiari
 * @version 1.0.0 - 17/07/2017
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaExcelVariazioneDiBilancioReportHandler extends ExcelBaseBytesHandler<VariazioneImportoCapitoloReportModel> {
	
	@Autowired
	private VariazioniDad variazioniDad;
	
	private List<Object[]> dettagli;
	
	private VariazioneImportoCapitolo variazioneImportoCapitolo;
	private DecimalFormat decimalFormat;

	@Override
	protected void init(){
		variazioniDad.setEnte(getEnte());
		setHeaderTitles(VariazioneImportoCapitoloReportCampiColonne.getColonne());
		super.init();
		
		// SIAC-5130: per la formattazione degli importi
		decimalFormat = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);
		decimalFormat.setParseBigDecimal(true);
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setMaximumFractionDigits(2);
	}
	
	@Override
	protected void addRows() {
		int rowCountToAdd = getRowCount();
		for (VariazioneImportoCapitoloReport variazioneImportoCapitoloReport : reportModel.getVariazioniCampi()) {
			Map<VariazioneImportoCapitoloReportCampiColonne, Object> campiColonne = variazioneImportoCapitoloReport.getCampi();
			
			Row row = getSheet().createRow(rowCountToAdd++);
			int cellCount = 0;
			for (ReportColumn intestazione : getHeaderTitles()) {
				VariazioneImportoCapitoloReportCampiColonne campiColonneReportVariazione = VariazioneImportoCapitoloReportCampiColonne.valueOf(intestazione.name());
				Object colonna = campiColonne.get(campiColonneReportVariazione);
				Cell cell = row.createCell(cellCount++);
				setCellValueAndStyle(cell, campiColonneReportVariazione, colonna);
			}
		}
		
		setRowCount(rowCountToAdd);
	}

	@Override
	protected void elaborateData() {
		// Carico i dati tramite una function
		log.info("elaborateData", "ottieni dettagli");
		ottieniDettagli();
		// A partire dall'oggetto ottenuto, popolo i dati
		log.info("elaborateData", "prima di  popola righe");
		popolaMappaRighe();
	}

	/**
	 * Ottiene i dettagli della variazione
	 */
	private void ottieniDettagli(){
		dettagli = variazioniDad.findAllDettagliVariazioneImportoCapitoloByUidVariazione(variazioneImportoCapitolo.getUid());
	}
	
	/**
	 * Popola handler.
	 *
	 * @param result the result
	 */
	private void popolaMappaRighe(){
		List<VariazioneImportoCapitoloReport> variazioniImportoCapitoloReport = new ArrayList<VariazioneImportoCapitoloReport>();
		
		for (Object[] o : dettagli) {
			if(log.isDebugEnabled()) {
				log.info("popolaMappaRighe", Arrays.toString(o));
			}
			
			VariazioneImportoCapitoloReport riga = new VariazioneImportoCapitoloReport();
			
			riga.setVariazioneNum((Integer) o[35]);
			// SIAC-6883
//			riga.setVariazioneAnno((String) o[36]);
			
			riga.setStatoVariazione((String) o[0]);
			riga.setAnnoCapitolo((String) o[1]);
			riga.setNumeroCapitolo((String) o[2]);
			riga.setNumeroArticolo((String) o[3]);
			riga.setTipoCapitolo((String) o[5]);
			riga.setDescrizioneCapitolo((String) o[6]);
			// Classificatori spesa
			riga.setMissioneCapitolo((String) o[8]);
			riga.setProgrammaCapitolo((String) o[9]);
			riga.setTitoloCapitoloSpesa((String) o[10]);
			riga.setMacroaggregatoCapitolo((String) o[11]);
			// Classificatori entrata
			riga.setTitoloCapitoloEntrata((String) o[12]);
			riga.setTipologiaCapitoloEntrata((String) o[13]);
			riga.setCategoriaTipologiaTitoloCapitoloEntrata((String) o[14]);
			//SIAC-6468
			riga.setTipologiaFinanziamento((String) o[33]);
			riga.setStrutturaAmministrativaResponsabile((String) o[34]);

			// Stanziamenti capitolo
			riga.setStanziamentoCapitolo((BigDecimal) o[24]);
			riga.setStanziamentoResiduoCapitolo((BigDecimal) o[25]);
			riga.setStanziamentoCassaCapitolo((BigDecimal) o[26]);
			riga.setStanziamentoCapitoloAnno1((BigDecimal) o[27]);
			riga.setStanziamentoCapitoloAnno2((BigDecimal) o[30]);
			// Stanziamenti variazione
			riga.setStanziamentoVariazione((BigDecimal) o[15]);
			riga.setStanziamentoResiduoVariazione((BigDecimal) o[16]);
			riga.setStanziamentoCassaVariazione((BigDecimal) o[17]);
			riga.setStanziamentoVariazioneAnno1((BigDecimal) o[18]);
			riga.setStanziamentoVariazioneAnno2((BigDecimal) o[21]);
			variazioniImportoCapitoloReport.add(riga);
		}
		reportModel.setVariazioniCampi(variazioniImportoCapitoloReport);
	}
	
	@Override
	protected Object[] instantiateHeaderParameters() {
		int annoBilancio = variazioneImportoCapitolo.getBilancio().getAnno();
		return new Object[] {
				Integer.valueOf(annoBilancio),
				Integer.valueOf(annoBilancio + 1),
				Integer.valueOf(annoBilancio + 2)
		};
	}
	
	/**
	 * @return the variazioneImportoCapitolo
	 */
	public VariazioneImportoCapitolo getVariazioneImportoCapitolo() {
		return variazioneImportoCapitolo;
	}

	/**
	 * @param variazioneImportoCapitolo the variazioneImportoCapitolo to set
	 */
	public void setVariazioneImportoCapitolo(VariazioneImportoCapitolo variazioneImportoCapitolo) {
		this.variazioneImportoCapitolo = variazioneImportoCapitolo;
	}

}
