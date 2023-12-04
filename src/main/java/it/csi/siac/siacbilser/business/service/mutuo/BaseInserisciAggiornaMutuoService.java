/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.utility.mutuo.MutuoUtil;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseInserisciAggiornaMutuoServiceRequest;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseInserisciAggiornaMutuoServiceResponse;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dad.PeriodoRimborsoMutuoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import it.csi.siac.siaccommon.util.date.DateUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public abstract class BaseInserisciAggiornaMutuoService<BIAMSREQ extends BaseInserisciAggiornaMutuoServiceRequest, BIAMSRES extends BaseInserisciAggiornaMutuoServiceResponse> 
	extends BaseMutuoService<BIAMSREQ, BIAMSRES> {

	@Autowired private SoggettoDad soggettoDad;
	@Autowired private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired private PeriodoRimborsoMutuoDad periodoRimborsoMutuoDad;
	
	@Override
	@Transactional
	public BIAMSRES executeService(BIAMSREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(mutuo.getTipoTasso(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipoTasso"));
		checkNotNull(mutuo.getDataAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dataAtto"));
		checkNotNull(mutuo.getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("attoAmministrativo"));
		checkNotNull(mutuo.getAttoAmministrativo().getTipoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipoAtto"));
		checkCondition(mutuo.getAttoAmministrativo().getUid()!= 0 || 
				mutuo.getAttoAmministrativo().getAnno()!=0 && mutuo.getAttoAmministrativo().getNumero()!=0 && (mutuo.getAttoAmministrativo().getTipoAtto().getUid()!= 0 || StringUtils.isNotBlank(mutuo.getAttoAmministrativo().getTipoAtto().getCodice())) , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave atto amministrativo"), true);
		checkCondition(StringUtils.isNotBlank(mutuo.getOggetto()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("oggetto"), true);
		checkNotNull(mutuo.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto"));
		checkCondition( mutuo.getSoggetto().getUid()!= 0 || StringUtils.isNotBlank(mutuo.getSoggetto().getCodiceSoggetto()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave soggetto"), true);
		checkEntita(mutuo.getContoTesoreria(), "contoTesoreria");
		checkCondition(NumberUtil.isValidAndGreaterThanZero(mutuo.getDurataAnni()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("durataAnni"), true);
		checkCondition(mutuo.getDurataAnni().compareTo(99)<=0, ErroreCore.FORMATO_NON_VALIDO.getErrore("durataAnni", "inserire massimo 2 cifre"), true);
		checkCondition(NumberUtil.isValidAndGreaterThanZero(mutuo.getAnnoInizio()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoInizio"), true);
		checkCondition(mutuo.getAnnoInizio().compareTo(9999)<=0, ErroreCore.FORMATO_NON_VALIDO.getErrore("annoInizio", "inserire massimo 4 cifre"), true);
		checkCondition(NumberUtil.isValidAndGreaterThanZero(mutuo.getAnnoFine()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoFine"), true);
		checkCondition(mutuo.getPeriodoRimborso().getUid()!= 0 || 
				StringUtils.isNotBlank(mutuo.getPeriodoRimborso().getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave periodo rimborso"), true);
		checkNotNull(mutuo.getScadenzaPrimaRata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("scadenzaPrimaRata"));
		checkImporto(mutuo.getSommaMutuataIniziale(), "sommaMutuataIniziale");
		
		checkCondition(
				!TipoTassoMutuo.Fisso.equals(mutuo.getTipoTasso()) || (mutuo.getTassoInteresse() != null && mutuo.getTassoInteresse().compareTo(BigDecimal.ZERO)>=0),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("tassoInteresse", "deve essere uguale o maggiore di 0"), true);
		checkCondition(
				!TipoTassoMutuo.Variabile.equals(mutuo.getTipoTasso()) || (mutuo.getTassoInteresse() != null && mutuo.getTassoInteresse().compareTo(BigDecimal.ZERO)>0),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("tassoInteresse","deve essere maggiore di 0"), true);
		checkCondition(
				!TipoTassoMutuo.Fisso.equals(mutuo.getTipoTasso()) || mutuo.getTassoInteresseEuribor() == null,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("tassoInteresseEuribor","deve essere NULL"), true);
		checkCondition(
				!TipoTassoMutuo.Fisso.equals(mutuo.getTipoTasso()) || mutuo.getTassoInteresseSpread() == null,
						ErroreCore.VALORE_NON_CONSENTITO.getErrore("tassoInteresseSpread",": deve essere NULL"), true);
		checkCondition(
				!TipoTassoMutuo.Variabile.equals(mutuo.getTipoTasso()) || (mutuo.getTassoInteresseEuribor() != null && mutuo.getTassoInteresseEuribor().compareTo(BigDecimal.ZERO)>0),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("tassoInteresseEuribor", "deve essere maggiore di 0"), true);
		checkCondition(
				!TipoTassoMutuo.Variabile.equals(mutuo.getTipoTasso()) || (mutuo.getTassoInteresseSpread() != null && mutuo.getTassoInteresseSpread().compareTo(BigDecimal.ZERO)>0),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("tassoInteresseSpread", "deve essere maggiore di 0"), true);
		checkCondition(
				!TipoTassoMutuo.Variabile.equals(mutuo.getTipoTasso()) || (mutuo.getTassoInteresseEuribor().add(mutuo.getTassoInteresseSpread()).compareTo(mutuo.getTassoInteresse())==0),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("tassoInteresse","il valore deve essere pari alla somma di tassoInteresseEuribor e tassoInteresseSpread"), true);
		checkImporto(mutuo.getAnnualita(), "annualita");
		checkNotNull(mutuo.getStatoMutuo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("statoMutuo"));
		
	}
	
	
	@Override
	protected void init() {
		super.init();
		mutuo.setEnte(ente);
		mutuo.setLoginOperazione(loginOperazione);
		mutuo.setLoginModifica(loginOperazione);
		soggettoDad.setEnte(ente);
		attoAmministrativoDad.setEnte(ente);
	}

	@Override
	protected void execute() {
		super.execute();
		checkScadenzaPrimaRata();
		checkPeriodoRimborso();
		checkSoggetto();
		checkAttoAmministrativo();
		checkAnnoFine();
	}

	private void checkScadenzaPrimaRata() {
		int annoScadenzaPrimaRata = DateUtil.getYear(mutuo.getScadenzaPrimaRata());
		
		checkBusinessCondition(annoScadenzaPrimaRata == mutuo.getAnnoInizio() || annoScadenzaPrimaRata == mutuo.getAnnoInizio()+1, ErroreCore.VALORE_NON_CONSENTITO.getErrore("scadenzaPrimaRata", "Data non compatibile con l’anno di inizio del mutuo" ));
	}
	
	private void checkPeriodoRimborso() {
		PeriodoRimborsoMutuo periodoRimborsoMutuo =  periodoRimborsoMutuoDad.findPeriodoRimborsoByKey(mutuo.getPeriodoRimborso());
		checkBusinessCondition(periodoRimborsoMutuo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("periodoRimborso", mutuo.getPeriodoRimborso().getCodice()) );
		checkBusinessCondition(periodoRimborsoMutuo.isEntitaValida(), ErroreCore.VALORE_NON_VALIDO.getErrore("periodoRimborso", mutuo.getPeriodoRimborso().getCodice()));
		mutuo.setPeriodoRimborso(periodoRimborsoMutuo);
	}
	
	private void checkAttoAmministrativo() {
		if (mutuo.getAttoAmministrativo().getUid() == 0) {
			AttoAmministrativo attoAmministrativo =  attoAmministrativoDad.findAttoAmministrativoByLogicKey(mutuo.getAttoAmministrativo());
			checkBusinessCondition(attoAmministrativo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("attoAmministrativo", mutuo.getAttoAmministrativo().getDescrizioneCompleta()) );
			checkBusinessCondition(attoAmministrativo.isEntitaValida(), ErroreCore.VALORE_NON_VALIDO.getErrore("attoAmministrativo", attoAmministrativo.getDescrizioneCompleta()));
			mutuo.setAttoAmministrativo(attoAmministrativo);
		}
	}

	private void checkSoggetto() {
		Soggetto soggetto = soggettoDad.findSoggetoByKey(mutuo.getSoggetto());
		
		checkBusinessCondition(soggetto != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("soggetto"));
		checkBusinessCondition(soggetto.isEntitaValida(), ErroreFin.SOGGETTO_NON_VALIDO.getErrore(soggetto.getCodiceSoggetto()));
		checkBusinessCondition(soggetto.isFlagIstitutoDiCredito(), ErroreBil.SOGGETTO_NON_ISTITUTO_CREDITO.getErrore(soggetto.getCodiceSoggetto()));
		mutuo.setSoggetto(soggetto);
	}

	private void checkAnnoFine() {
		Integer annoScadenzaUltimaRata = DateUtil.getYear(DateUtils.addMonths(mutuo.getScadenzaPrimaRata(), mutuo.getDurataAnni()*12 - mutuo.getPeriodoRimborso().getNumeroMesi()));

		checkBusinessCondition(annoScadenzaUltimaRata.compareTo(mutuo.getAnnoFine())==0, 
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("annoFine", String.format(": deve essere coerente con periodoRimborso e scadenzaPrimaRata (%d)", annoScadenzaUltimaRata)));
	}
}
