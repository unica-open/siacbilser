/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.gestione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelRow;
import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelSheet;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaGestioneDad;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioGestioneExcelReportHandler 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler {
	
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaGestioneDad accantonamentoFondiDubbiaEsigibilitaGestioneDad;
	
	@Override
	protected ExcelSheet<? extends BaseExcelRow>[] instantiateExcelSheets() {
		List<Object[]> dettagli = accantonamentoFondiDubbiaEsigibilitaGestioneDad.findDettagliAccantonamentoByUidAttributiBilancio(
				accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getUid());
		Object[] dettaglio0 = dettagli.get(0);

		return ArrayUtil.toArray(
				new IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelSheet(dettaglio0[0], dettaglio0[1],
						dettaglio0[2], dettaglio0[3], dettaglio0[4], dettaglio0[5], dettaglio0[6], dettaglio0[28]),
				new AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioGestioneExcelSheet(
						accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getQuinquennioRiferimento().intValue(),
						accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getBilancio().getAnno(),
						dettagli));
	}
}
