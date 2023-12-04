/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfinser.integration.dao.soggetto.ModalitaPagamentoDao;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ModalitaPagamentoDad extends BaseDadImpl
{
	@Autowired
	private ModalitaPagamentoDao modalitaPagamentoDao;

	public Integer trovaModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte)
	{
		return modalitaPagamentoDao.findModalitaPagamentoContoCorrente(idSoggetto, iban, idEnte);
	}

	public int inserisciModalitaPagamentoContoCorrente(Integer idSoggetto, String iban, Integer idEnte, String login)
	{
		return modalitaPagamentoDao.insertModalitaPagamentoContoCorrente(idSoggetto, iban, idEnte, login);
	}

	public Integer trovaModalitaPagamentoContanti(Integer idSoggetto, TipologiaGestioneLivelli tipologiaGestioneLivelli, Integer idEnte)
	{
		Integer accreditoTipoId = modalitaPagamentoDao.findAccreditoTipoIdByGestioneLivello(idEnte,
				tipologiaGestioneLivelli.getCodice());
		
		return modalitaPagamentoDao.findModalitaPagamentoByAccreditoTipoId(idSoggetto, idEnte, accreditoTipoId);
	}

	public Integer inserisciModalitaPagamentoContanti(Soggetto soggetto, Integer idEnte, String login)
	{
		return modalitaPagamentoDao.insertModalitaPagamentoContanti(soggetto.getUid(), soggetto.getDenominazione(),
				soggetto.getCodiceFiscale(), idEnte, login);
	}

	public Integer trovaModalitaPagamentoGiroFondi(Integer idSoggetto, String numeroConto, Integer idEnte)
	{
		Integer accreditoTipoId = modalitaPagamentoDao.findAccreditoTipoIdByGestioneLivello(idEnte,
				TipologiaGestioneLivelli.ACCREDITO_GIRO_FONDI.getCodice());
		
		return modalitaPagamentoDao.findModalitaPagamentoGiroFondi(idSoggetto, numeroConto, idEnte, accreditoTipoId);
	}
	
	public Integer inserisciModalitaPagamentoGiroFondi(Integer idSoggetto, String numeroConto, Integer idEnte, String login)
	{
		return modalitaPagamentoDao.insertModalitaPagamentoGiroFondi(idSoggetto, numeroConto, idEnte, login);
	}
}
