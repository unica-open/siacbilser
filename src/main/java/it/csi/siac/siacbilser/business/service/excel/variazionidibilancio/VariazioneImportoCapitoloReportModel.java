/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.variazionidibilancio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.model.BILDataDictionary;
import it.csi.siac.siacbilser.model.report.VariazioneImportoCapitoloReport;
import it.csi.siac.siacbilser.model.report.VariazioneImportoCapitoloReportCampiColonne;

/**
 * VariazioneImportoCapitolo.
 *
 * @author Elisa Chiari 
 * @version 1.0.0 - 17/07/2017
 * 
 */
@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class VariazioneImportoCapitoloReportModel implements Serializable {
	
	/**Per la serializzazione */
	private static final long serialVersionUID = 1466271119306069284L;

	private VariazioneImportoCapitoloReportCampiColonne[] intestazioni = VariazioneImportoCapitoloReportCampiColonne.values();
	private List<VariazioneImportoCapitoloReport> variazioniCampi = new ArrayList<VariazioneImportoCapitoloReport>();

	/**
	 * @return the intestazioni
	 */
	public VariazioneImportoCapitoloReportCampiColonne[] getIntestazioni() {
		return intestazioni;
	}

	/**
	 * @param intestazioni the intestazioni to set
	 */
	public void setIntestazioni(VariazioneImportoCapitoloReportCampiColonne[] intestazioni) {
		this.intestazioni = intestazioni;
	}

	/**
	 * @return the variazioniCampi
	 */
	public List<VariazioneImportoCapitoloReport> getVariazioniCampi() {
		return variazioniCampi;
	}

	/**
	 * @param variazioniCampi the variazioniCampi to set
	 */
	public void setVariazioniCampi(List<VariazioneImportoCapitoloReport> variazioniCampi) {
		this.variazioniCampi = variazioniCampi;
	}
	
}
