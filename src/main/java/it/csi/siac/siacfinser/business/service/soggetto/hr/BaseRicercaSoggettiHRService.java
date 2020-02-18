/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto.hr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.hr.HRServiceDelegate;
import it.csi.siac.siaccommon.util.date.DateConverter;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneDataType;

public abstract class BaseRicercaSoggettiHRService<REQ extends ServiceRequest, RES extends ServiceResponse> extends
		AbstractBaseService<REQ, RES>
{
	@Autowired
	protected HRServiceDelegate hrServiceDelegate;

	@Autowired
	protected GenericService genericService;

	protected Soggetto toSoggetto(MissioneDataType missioneDataType)
	{
		Soggetto soggetto = new Soggetto();

		soggetto.setMatricola(String.valueOf(missioneDataType.getExMatricola()));
		soggetto.setCognome(missioneDataType.getDipentente());
		soggetto.setNome(missioneDataType.getDipentente());
		soggetto.setDenominazione(missioneDataType.getDipentente());
		soggetto.setDataNascita(stringToDate(missioneDataType.getDataNascita()));
		ComuneNascita comuneNascita = new ComuneNascita();
		comuneNascita.setDescrizione(missioneDataType.getComuneNascita());
		soggetto.setComuneNascita(comuneNascita);

		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = new ModalitaPagamentoSoggetto();
		ModalitaAccreditoSoggetto modalitaAccreditoSoggetto = new ModalitaAccreditoSoggetto();

		if (StringUtils.isNotBlank(missioneDataType.getIbanDip())
				|| StringUtils.isNotBlank(missioneDataType.getBicCod())
				|| StringUtils.isNotBlank(missioneDataType.getContoCorrente()))
		{
			modalitaAccreditoSoggetto.setCodice("CCB");
			modalitaAccreditoSoggetto.setDescrizione("Conto Corrente Bancario");

			modalitaPagamentoSoggetto.setBic(missioneDataType.getBicCod());
			modalitaPagamentoSoggetto.setContoCorrente(missioneDataType.getContoCorrente());
			modalitaPagamentoSoggetto.setIban(missioneDataType.getIbanDip());
		}
		else
		{
			modalitaAccreditoSoggetto.setCodice("CON");
			modalitaAccreditoSoggetto.setDescrizione("Contanti");
		}

		modalitaPagamentoSoggetto.setModalitaAccreditoSoggetto(modalitaAccreditoSoggetto);
		modalitaPagamentoSoggetto.setContoCorrente(missioneDataType.getContoCorrente());

		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
		modalitaPagamentoList.add(modalitaPagamentoSoggetto);
		soggetto.setModalitaPagamentoList(modalitaPagamentoList);

		if ("S".equals(missioneDataType.getBlocco()))
			soggetto.setStatoOperativo(StatoOperativoAnagrafica.BLOCCATO);
		else
			soggetto.setStatoOperativo(StatoOperativoAnagrafica.VALIDO);

		List<String> tmp = new ArrayList<String>();
		if (StringUtils.isNotBlank(missioneDataType.getUnitaOrg()))
			tmp.add(missioneDataType.getUnitaOrg());

		if (StringUtils.isNotBlank(missioneDataType.getUorDescr()))
			tmp.add(missioneDataType.getUorDescr());

		if (!tmp.isEmpty())
			soggetto.setNote(StringUtils.join(tmp, " - "));

		return soggetto;
	}

	private Date stringToDate(String dateString)
	{
		final String HR_DATE_PATTERN = "dd-MM-yyyy";

		return DateConverter.convertFromString(dateString, HR_DATE_PATTERN);
	}

}
