/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota.MovimentoInventarioHandler;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.ValidaPrimaNotaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.integration.dad.VariazioneCespiteDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidaPrimaNotaCespiteService extends CheckedAccountBaseService<ValidaPrimaNotaCespite, ValidaPrimaNotaCespiteResponse> {

	
	private static final String CODICE_TIPO_RELAZIONE_PRIME_NOTE ="A";
	private PrimaNota primaNotaINV;
	private Bilancio bilancio;
	
	@Autowired
	@Inventario
	private PrimaNotaInvDad primaNotaInvDad;	
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	@Autowired
	private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	@Autowired
	private VariazioneCespiteDad variazioneCespiteDad;
	@Autowired
	private DismissioneCespiteDad dismissioneCespiteDad;
	@Autowired
	private CausaleEPDad causaleEPDad;
	
	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;	
	@Autowired
	private ValidaPrimaNotaService validaPrimaNotaService;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNota(), "prima nota da validare");

	}

	@Override
	protected void init() {
		super.init();
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
		bilancioDad.setEnteEntity(ente);
		ammortamentoAnnuoCespiteDad.setEnte(ente);
		ammortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(ente);
		dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione(loginOperazione);
		variazioneCespiteDad.setEnte(ente);
		causaleEPDad.setEnte(ente);
	}

	@Transactional
	@Override
	public ValidaPrimaNotaCespiteResponse executeService(ValidaPrimaNotaCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {

		//carico il bilancio
		caricaBilancio();
		
		// carico la prima nota che devo validare
		caricaPrimaNotaAmbitoINV();
		
		checkPrimaNotaAmbitoINV();

		// aggiorno lo stato della prima nota inv
		validaPrimaNota(this.primaNotaINV, StatoAccettazionePrimaNotaDefinitiva.INTEGRATO);
				
//		// inserisco la prima nota in ambito FIN
		PrimaNota primaNotaFIN = inserisciPrimaNotaAmbitoFIN();
		
		// effettuo,se necessario, delle operazioni sulle entita' generanti la prima nota INV 
		effettuaOperazioniEntitaCollegata();
	}

	/**
	 * Carica bilancio.
	 */
	private void caricaBilancio() {
		this.bilancio = bilancioDad.getBilancioByAnno(req.getAnnoBilancio().intValue());
		if(this.bilancio == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Bilancio non trovato per anno" + req.getAnnoBilancio()));
		}
	}
	
	/**
	 * Carica prima nota ambito di ambito INV che deve essere validata.
	 */
	private void caricaPrimaNotaAmbitoINV() {
		final String methodName ="caricaPrimaNotaAmbitoINV";
		
		Utility.MDTL.addModelDetails(MovimentoEPModelDetail.MovimentoDettaglioModelDetail, MovimentoEPModelDetail.CausaleEPModelDetail, MovimentoDettaglioModelDetail.ContoModelDetail, MovimentoDettaglioModelDetail.Segno);
		log.debug(methodName, "carico da db la prima nota con uid: " + req.getPrimaNota().getUid());
		this.primaNotaINV = primaNotaInvDad.findPrimaNotaByUid(req.getPrimaNota().getUid(), new PrimaNotaModelDetail[] {PrimaNotaModelDetail.MovimentiEpModelDetail, PrimaNotaModelDetail.Ambito, PrimaNotaModelDetail.Bilancio});
	}

	/**
	 * Check prima nota ambito INV.
	 */
	private void checkPrimaNotaAmbitoINV() {
		final String methodName ="checkPrimaNotaAmbitoINV";
		if(this.primaNotaINV == null || this.primaNotaINV.getUid() == 0) {
			log.debug(methodName, "Nessuna prima nota trovata su base dati.");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("validazione prima nota con uid: " + req.getPrimaNota().getUid(), "prima nota"));
		}
		
		log.logXmlTypeObject(this.primaNotaINV, "prima nota INV");
		
		if(StatoOperativoPrimaNota.ANNULLATO.equals(this.primaNotaINV.getStatoOperativoPrimaNota())) {
			log.debug(methodName, "La prima nota ha stato operativo annullato.");
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("prima nota",StatoOperativoPrimaNota.ANNULLATO.getDescrizione()));
		}
		
		if(!Ambito.AMBITO_INV.equals(this.primaNotaINV.getAmbito())) {
			log.debug(methodName, "La prima nota ha ambito errato o non e' stato possibile caricare il bilancio.");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota", "deve essere di ambito inventario e deve avere un bilancio."));
		}
		
		if(this.primaNotaINV.getListaMovimentiEP() == null || this.primaNotaINV.getListaMovimentiEP().isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("prima nota ambito inventario", "movimenti ep non presenti."));
		}
		
	}
	
	private PrimaNota inserisciPrimaNotaAmbitoFIN() {
		final String methodName = "inserisciPrimaNotaAmbitoFIN";
		InseriscePrimaNota reqIPN = new InseriscePrimaNota();
		reqIPN.setRichiedente(req.getRichiedente());
		reqIPN.setDataOra(new Date());
		reqIPN.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		
		PrimaNota primaNotaFIN =creaPrimaNotaFIN();	
		
		reqIPN.setPrimaNota(primaNotaFIN);
		
		
		
		log.debug(methodName, "Creata prima nota di ambito FIN da inserire.");
		log.logXmlTypeObject(primaNotaFIN, "primaNotaFIN");
		
		InseriscePrimaNotaResponse resIPN = serviceExecutor.executeServiceSuccess(inseriscePrimaNotaService, reqIPN);
		checkServiceResponseFallimento(resIPN);
		
		validaPrimaNota(resIPN.getPrimaNota(), null);
		
		log.debug(methodName, "Inserita prima nota definitiva in ambito FIN: " + resIPN.getPrimaNota().getNumeroRegistrazioneLibroGiornale() + " con uid: " + resIPN.getPrimaNota().getUid());
		return resIPN.getPrimaNota();
	}
	
	/**
	 * Valida prima nota INV.
	 * @param statoAccettazione 
	 */
	private void validaPrimaNota(PrimaNota primaNotaDaValidare, StatoAccettazionePrimaNotaDefinitiva statoAccettazionePrimaNotaDefinitiva) {
		final String methodName = "validaPrimaNotaINV";
		if(primaNotaDaValidare == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile validare la prima nota, prima nota non fornita"));
		}
		ValidaPrimaNota reqVPN = new ValidaPrimaNota();
		reqVPN.setRichiedente(req.getRichiedente());
		reqVPN.setDataOra(new Date());
		reqVPN.setAnnoBilancio(Integer.valueOf(this.bilancio.getAnno()));
		PrimaNota pnreq = new PrimaNota();
		pnreq.setUid(primaNotaDaValidare.getUid());
		pnreq.setAmbito(primaNotaDaValidare.getAmbito());
		pnreq.setStatoAccettazionePrimaNotaDefinitiva(statoAccettazionePrimaNotaDefinitiva);
		reqVPN.setPrimaNota(pnreq);
		ValidaPrimaNotaResponse resVPN = serviceExecutor.executeServiceSuccess(validaPrimaNotaService, reqVPN);
		checkServiceResponseFallimento(resVPN);
		primaNotaDaValidare.setNumeroRegistrazioneLibroGiornale(resVPN.getPrimaNota().getNumeroRegistrazioneLibroGiornale());
	}

	/**
	 * Effettua operazioni entita collegata.
	 */
	private void effettuaOperazioniEntitaCollegata() {
		Class<?> entitaInventarioCollegataAPrimaNota = primaNotaInvDad.ottieniEntitaGenerantePrimaNota(null, null, this.primaNotaINV.getUid());
		MovimentoInventarioHandler<?> movimentoInventarioHandler = MovimentoInventarioHandler.getInstance(serviceExecutor, req.getRichiedente(), ente, entitaInventarioCollegataAPrimaNota.getSimpleName(),loginOperazione);
		movimentoInventarioHandler.caricaMovimento(this.primaNotaINV);
		movimentoInventarioHandler.effettuaOperazioniCollegateADefinizionePrimaNotaInventario(this.primaNotaINV);
	}

	/**
	 * Crea prima nota FIN.
	 *
	 * @return the prima nota
	 */
	private PrimaNota creaPrimaNotaFIN() {
		PrimaNota pn = new PrimaNota();

		pn.setAmbito(Ambito.AMBITO_FIN);
		pn.setBilancio(bilancio);
		pn.setEnte(ente);
		pn.setTipoCausale(TipoCausale.Libera);
		pn.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.DEFINITIVO);
		pn.setDataRegistrazione(new Date());
		pn.setDescrizione("");

		
		for(MovimentoEP movimentoEPINV : this.primaNotaINV.getListaMovimentiEP()) {
			MovimentoEP movimentoEP = copiaMovimentoEP(movimentoEPINV);
			pn.getListaMovimentiEP().add(movimentoEP);
		}
		
		impostaCollegamentoConPrimANotaINV(pn);
		return pn;
	}

	/**
	 * Imposta collegamento con prim A nota INV.
	 *
	 * @param pnFiglia the pn figlia
	 */
	private void impostaCollegamentoConPrimANotaINV(PrimaNota pnFiglia) {
		TipoRelazionePrimaNota tipoRelazionePrimaNota = primaNotaInvDad.findTipoRelazioneByCodice(CODICE_TIPO_RELAZIONE_PRIME_NOTE);
		this.primaNotaINV.setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
		//la metto come prima nota padre in quanto e' padre
		pnFiglia.getListaPrimaNotaPadre().add(this.primaNotaINV);
	}

	/**
	 * @param movimentoEPINV
	 * @return
	 */
	private MovimentoEP copiaMovimentoEP(MovimentoEP movimentoEPINV) {
		MovimentoEP movimentoEP = new MovimentoEP();
		movimentoEP.setCausaleEP(caricaCausaleEPDaPrimaNotaINV(movimentoEPINV.getCausaleEP()));
		movimentoEP.setAmbito(Ambito.AMBITO_FIN);
		movimentoEP.setEnte(ente);

		for (MovimentoDettaglio movimentoDettaglioINV : movimentoEPINV.getListaMovimentoDettaglio()){
			
			MovimentoDettaglio movimentoDettaglio = new MovimentoDettaglio();
			movimentoDettaglio.setAmbito(Ambito.AMBITO_FIN);
			movimentoDettaglio.setConto(movimentoDettaglioINV.getConto());
			movimentoDettaglio.setEnte(ente);
			movimentoDettaglio.setImporto(movimentoDettaglioINV.getImporto());
			movimentoDettaglio.setSegno(movimentoDettaglioINV.getSegno());
			movimentoDettaglio.setNumeroRiga(movimentoDettaglioINV.getNumeroRiga());
			movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglio);
		}
		return movimentoEP;
	}

	/**
	 * Carica causale EP da prima nota INV.
	 *
	 * @param causaleEPINV the causale EPINV
	 * @return the causale EP
	 */
	private CausaleEP caricaCausaleEPDaPrimaNotaINV(CausaleEP causaleEPINV) {
		CausaleEP criteriDiRicerca = new CausaleEP();
		criteriDiRicerca.setCodice(causaleEPINV.getCodice());
		criteriDiRicerca.setAmbito(Ambito.AMBITO_FIN);
		// N.B: queste ricerca puo' essere ittimizzata, ho bisogno solo dell'id
		CausaleEP causaleEP = causaleEPDad.ricercaCausaleEPByCodice(criteriDiRicerca);
		return causaleEP;
	}

}
