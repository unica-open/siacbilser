/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio.report.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelReportHandler;
import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VariazioneBilancioExcelReportHandler extends BaseExcelReportHandler {
	
	@Autowired
	private VariazioniDad variazioniDad;
	
	private VariazioneImportoCapitolo variazioneImportoCapitolo;

	@Override
	protected void init(){
		variazioniDad.setEnte(getEnte());
		super.init();
	}

	
	@Override
	protected ExcelSheet<VariazioneImportoCapitoloExcelRow>[] instantiateExcelSheets() {
		return ArrayUtil.toArray(
				new VariazioneBilancioExcelSheet(
					variazioneImportoCapitolo.getBilancio().getAnno(), 
					variazioniDad.findAllDettagliVariazioneImportoCapitoloByUidVariazione(variazioneImportoCapitolo.getUid())));
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
