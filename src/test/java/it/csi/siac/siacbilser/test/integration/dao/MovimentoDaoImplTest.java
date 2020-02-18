/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.integration.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.dad.MovimentoDad;
import it.csi.siac.siacbilser.integration.dao.MovimentoDao;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

public class MovimentoDaoImplTest extends BaseJunit4TestCase {
	@Autowired
	private MovimentoDao movimentoDao;
	@Autowired
	private MovimentoDad movimentoDad;
	
	
	@Test
	public void ricercaSinteticaMovimentoDao(){
		final String methodName = "ricercaSinteticaMovimentoDao";
		Calendar cal = Calendar.getInstance();
		
		cal.set(2017, Calendar.JANUARY, 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date movtDataInizio = cal.getTime();
		
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		Date movtDataFine = cal.getTime();
		
		Integer cassaeconId = Integer.valueOf(2);
		Integer enteProprietarioId = Integer.valueOf(1);
		Integer bilId = Integer.valueOf(16);
		
		Pageable pageable = new PageRequest(0, 50);
		Page<SiacTMovimento> siacTMovimentos = movimentoDao.ricercaSinteticaMovimentoStampa(movtDataInizio, movtDataFine, cassaeconId, enteProprietarioId, bilId, pageable);
		
		for(SiacTMovimento stm : siacTMovimentos) {
			log.debug(methodName, stm.getMovtId() + " ==> " + stm.getMovtNumero());
		}
	}
	
	@Test
	public void totaleRicercaSinteticaMovimentoDao(){
		final String methodName = "totaleRicercaSinteticaMovimentoDao";
		Calendar cal = Calendar.getInstance();
		
		cal.set(2017, Calendar.JANUARY, 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date movtDataInizio = cal.getTime();
		
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		Date movtDataFine = cal.getTime();
		
		Integer cassaeconId = Integer.valueOf(2);
		Integer enteProprietarioId = Integer.valueOf(1);
		Integer bilId = Integer.valueOf(16);
		
		BigDecimal totale = movimentoDao.totaleRicercaSinteticaMovimentoStampa(movtDataInizio, movtDataFine, cassaeconId, enteProprietarioId, bilId, null);
		
		log.info(methodName, "Totale: " + totale);
	}

	@Test
	public void ricercaSinteticaMovimentoDad(){
		final String methodName = "ricercaSinteticaMovimentoDad";
		Calendar cal = Calendar.getInstance();
		
		cal.set(2017, Calendar.JANUARY, 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date dataPeriodoInizio = cal.getTime();
		
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		Date dataPeriodoFine = cal.getTime();
		
		Integer cassaEconomaleUid = Integer.valueOf(2);
		Integer enteProprietarioId = Integer.valueOf(1);
		Integer bilancioId = Integer.valueOf(16);
		
		ParametriPaginazione parametriPaginazione = getParametriPaginazione(0, 50);
		ListaPaginata<Movimento> movimenti = movimentoDad.findByPeriodoMovimentoCassaEconId(dataPeriodoInizio, dataPeriodoFine, cassaEconomaleUid, enteProprietarioId, bilancioId, parametriPaginazione);
		
		for(Movimento m : movimenti) {
			log.debug(methodName, m.getNumeroMovimento());
		}
		log.debug(methodName, "Pagina corrente: " + movimenti.getPaginaCorrente());
		log.debug(methodName, "Totale elementi: " + movimenti.getTotaleElementi());
		log.debug(methodName, "Totale pagine: " + movimenti.getTotalePagine());
	}
	
	@Test
	public void totaleRicercaSinteticaMovimentoDad(){
		final String methodName = "totaleRicercaSinteticaMovimentoDad";
		Calendar cal = Calendar.getInstance();
		
		cal.set(2017, Calendar.JANUARY, 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date movtDataInizio = cal.getTime();
		
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		Date movtDataFine = cal.getTime();
		
		Integer cassaeconId = Integer.valueOf(2);
		Integer enteProprietarioId = Integer.valueOf(1);
		Integer bilId = Integer.valueOf(16);
		
		BigDecimal totale = movimentoDad.findTotaleByPeriodoMovimentoCassaEconId(movtDataInizio, movtDataFine, cassaeconId, enteProprietarioId, bilId,null);
		
		log.info(methodName, "Totale: " + totale);
	}
}
