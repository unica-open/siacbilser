/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto.hr;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneDataType;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSoggettiHRService extends BaseRicercaSoggettiHRService<RicercaSoggetti, RicercaSoggettiResponse>
{

	@Override
	protected void execute()
	{
		List<Soggetto> soggetti = new ArrayList<Soggetto>();

		List<MissioneDataType> listMissioneDataType = null;

		ParametroRicercaSoggetto parametroRicercaSoggetto = req.getParametroRicercaSoggetto();

		if (parametroRicercaSoggetto.getMatricola() != null)
			listMissioneDataType = hrServiceDelegate.viDipendentiMatr(parametroRicercaSoggetto.getMatricola());
		else if (parametroRicercaSoggetto.getDenominazione() != null)
			listMissioneDataType = hrServiceDelegate.viDipendentiCogn(parametroRicercaSoggetto.getDenominazione());

		if (listMissioneDataType != null)
			for (MissioneDataType missioneDataType : listMissioneDataType)
				soggetti.add(toSoggetto(missioneDataType));

		res.setSoggetti(soggetti);
	}
}
