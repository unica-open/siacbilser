/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfinser.integration.dao.saldo.SaldoDao;
import it.csi.siac.siacfinser.integration.dao.saldo.SiacTCbpiSaldoAddebitoRepository;
import it.csi.siac.siacfinser.integration.dao.saldo.SiacTCbpiSaldoRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTCbpiSaldoAddebitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCbpiSaldoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.saldo.AddebitoContoCorrente;
import it.csi.siac.siacfinser.model.saldo.VociContoCorrente;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SaldoDad extends AbstractFinDad
{
	@Autowired
	private SiacTCbpiSaldoRepository siacTCbpiSaldoRepository;

	@Autowired
	private SiacTCbpiSaldoAddebitoRepository  siacTCbpiSaldoAddebitoRepository;

	@Autowired
	private SaldoDao saldoDao;

	@Autowired
	private SiacTBilRepository siacTBilRepository;

	public VociContoCorrente leggiVociContoCorrente(Integer enteId, Integer anno, Integer idClassifContoCorrente, Date dataInizio, Date dataFine)
	{
		SiacTCbpiSaldoFin siacTCbpiSaldoFin = siacTCbpiSaldoRepository.readSaldoByContoCorrenteAndAnno(
				enteId, 
				readBilancio(anno, enteId),
				idClassifContoCorrente);

		if (siacTCbpiSaldoFin == null)
			return null;

		siacTCbpiSaldoFin.setAddebiti(siacTCbpiSaldoAddebitoRepository.readAddebitiSaldo(siacTCbpiSaldoFin,
				dataInizio != null? dataInizio : new GregorianCalendar(1900, 0, 1).getTime(), 
				dataFine != null ? DateUtils.addDays(dataFine, 1) : new GregorianCalendar(9999, 0, 1).getTime()));
		
		return map(siacTCbpiSaldoFin, VociContoCorrente.class, FinMapId.SiacTCbpiSaldoFin_VociContoCorrente);
	}

	private SiacTBil readBilancio(Integer anno, Integer enteId)
	{
		return siacTBilRepository.getSiacTBilByAnno(anno.toString(), enteId, Periodo.ANNO.getCodice());
	}

	public void aggiornaSaldoIniziale(VociContoCorrente vociContoCorrente, Ente ente, String loginOperazione)
	{
		SiacTCbpiSaldoFin siacTCbpiSaldo = map(vociContoCorrente, SiacTCbpiSaldoFin.class, FinMapId.SiacTCbpiSaldoFin_VociContoCorrente);

		Date now = new Date();

		siacTCbpiSaldo.setBilancio(readBilancio(vociContoCorrente.getAnno(), ente.getUid()));
		siacTCbpiSaldo.setDataModifica(now);
		siacTCbpiSaldo.setDataInizioValidita(now);

		if (siacTCbpiSaldo.getUid() == null || siacTCbpiSaldo.getUid() == 0)
			siacTCbpiSaldo.setDataCreazione(now);

		siacTCbpiSaldo.setSiacTEnteProprietario(map(ente, SiacTEnteProprietarioFin.class));
		siacTCbpiSaldo.setLoginOperazione(loginOperazione);

		siacTCbpiSaldoRepository.saveAndFlush(siacTCbpiSaldo);
	}

	public void aggiornaAddebiti(List<AddebitoContoCorrente> elencoAddebiti, Ente ente, String loginOperazione)
	{
		for (AddebitoContoCorrente addebitoContoCorrente : elencoAddebiti)
		{
			SiacTCbpiSaldoAddebitoFin siacTCbpiSaldoAddebito = map(addebitoContoCorrente, SiacTCbpiSaldoAddebitoFin.class,
					FinMapId.SiacTCbpiSaldoAddebitoFin_AddebitoContoCorrente);

			Date now = new Date();

			siacTCbpiSaldoAddebito.setDataModifica(now);
			siacTCbpiSaldoAddebito.setDataInizioValidita(now);

			if (siacTCbpiSaldoAddebito.getUid() == null || siacTCbpiSaldoAddebito.getUid() == 0)
				siacTCbpiSaldoAddebito.setDataCreazione(now);

			siacTCbpiSaldoAddebito.setSiacTEnteProprietario(map(ente, SiacTEnteProprietarioFin.class));
			siacTCbpiSaldoAddebito.setLoginOperazione(loginOperazione);

			siacTCbpiSaldoAddebitoRepository.saveAndFlush(siacTCbpiSaldoAddebito);
		}
	}

	public BigDecimal calcolaSaldoCassaData(Integer idClassifConto, Date data, Integer anno, Ente ente)
	{
		return saldoDao.leggiSommaImportiPredocumentiEntrataDataCompetenza(idClassifConto, data, anno, ente.getUid())
				.add(saldoDao.leggiSaldoSpesePrelievi(idClassifConto, data, anno, ente.getUid()));
	}
}
