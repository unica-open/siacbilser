/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * The Class AggiornamentoMassivoCapitoloBaseService.
 */
public abstract class AggiornamentoMassivoCapitoloBaseService<REQ extends ServiceRequest,RES extends ServiceResponse, CAP extends Capitolo<?, ?>> extends CheckedAccountBaseService<REQ, RES> {
	
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	protected static final int RISULTATI_PAGINA_REMOTE = 100;
	
	@Autowired
	private ControllaClassificatoriModificabiliCapitoloService controllaClassificatoriModificabiliCapitoloService;
	@Autowired
	private ControllaAttributiModificabiliCapitoloService controllaAttributiModificabiliCapitoloService;
	
	
	// La modificabilita' e' da leggersi in senso contrario
	protected Map<TipologiaClassificatore, Integer> classificatoriNonModificabili;
	// La modificabilita' e' da leggersi in senso contrario
	protected Map<TipologiaAttributo, Object> attributiNonModificabili;
	// Capitoli con stesso numero capitolo TODO: come utilizzarlo?
	protected long stessoNumeroCap;
	// Capitoli con stesso numero capitolo e numero articolo TODO: come utilizzarlo?
	protected long stessoNumeroCapArt;
	
	// Solo di utilita'
	protected Bilancio bilancio;
	protected Integer numeroCapitolo;
	protected Integer numeroArticolo;
	protected TipoCapitolo tipoCapitolo;
	
	protected void ottieniClassificatoriNonAggiornabili() {
		ControllaClassificatoriModificabiliCapitolo ccmcReq = new ControllaClassificatoriModificabiliCapitolo();
		
		ccmcReq.setBilancio(bilancio);
		ccmcReq.setDataOra(new Date());
		ccmcReq.setEnte(ente);
		ccmcReq.setModalitaAggiornamento(true);
		ccmcReq.setRichiedente(req.getRichiedente());
		
		ccmcReq.setNumeroCapitolo(numeroCapitolo);
		ccmcReq.setNumeroArticolo(numeroArticolo);
		ccmcReq.setTipoCapitolo(tipoCapitolo);
		
		ControllaClassificatoriModificabiliCapitoloResponse ccmcRes = serviceExecutor.executeServiceSuccess(ControllaClassificatoriModificabiliCapitoloService.class, ccmcReq);
		classificatoriNonModificabili = ccmcRes.getClassificatoriNonModificabili();
		
		stessoNumeroCap = ccmcRes.getStessoNumCap() != null ? ccmcRes.getStessoNumCap().longValue() : 0L;
		stessoNumeroCapArt = ccmcRes.getStessoNumCapArt() != null ? ccmcRes.getStessoNumCapArt().longValue() : 0L;
	}

	protected void ottieniAttributiNonAggiornabili() {
		ControllaAttributiModificabiliCapitolo camcReq = new ControllaAttributiModificabiliCapitolo();
		
		camcReq.setBilancio(bilancio);
		camcReq.setDataOra(new Date());
		camcReq.setEnte(ente);
		camcReq.setModalitaAggiornamento(true);
		camcReq.setRichiedente(req.getRichiedente());
		
		camcReq.setNumeroCapitolo(numeroCapitolo);
		camcReq.setNumeroArticolo(numeroArticolo);
		camcReq.setTipoCapitolo(tipoCapitolo);
		
		ControllaAttributiModificabiliCapitoloResponse ccmcRes = serviceExecutor.executeServiceSuccess(ControllaAttributiModificabiliCapitoloService.class, camcReq);
		attributiNonModificabili = ccmcRes.getAttributiNonModificabili();
	}

	/**
	 * Travasa dati modificabili.
	 *
	 * @param capitoloModificato the capitolo modificato
	 * @param capitoloCorrente the capitolo corrente
	 */
	protected void travasaDatiModificabili(CAP capitoloModificato, CAP capitoloCorrente) {
		
		// Classificatori
		capitoloCorrente.setElementoPianoDeiConti(travasaClassificatoreModificabile(capitoloModificato.getElementoPianoDeiConti(), capitoloCorrente.getElementoPianoDeiConti(),
				TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V));
		capitoloCorrente.setStrutturaAmministrativoContabile(travasaClassificatoreModificabile(capitoloModificato.getStrutturaAmministrativoContabile(), capitoloCorrente.getStrutturaAmministrativoContabile(),
				TipologiaClassificatore.CDC, TipologiaClassificatore.CDR));
		capitoloCorrente.setTipoFinanziamento(travasaClassificatoreModificabile(capitoloModificato.getTipoFinanziamento(), capitoloCorrente.getTipoFinanziamento(),
				TipologiaClassificatore.TIPO_FINANZIAMENTO));
		capitoloCorrente.setTipoFondo(travasaClassificatoreModificabile(capitoloModificato.getTipoFondo(), capitoloCorrente.getTipoFondo(),
				TipologiaClassificatore.TIPO_FONDO));
		
		// Classificatori generici
		travasaClassificatoriGenerici(capitoloModificato, capitoloCorrente);
		
		// Attributi
		
		capitoloCorrente.setFlagRilevanteIva(travasaAttributoModificabile(capitoloModificato.getFlagRilevanteIva(), capitoloCorrente.getFlagRilevanteIva(),
				TipologiaAttributo.FLAG_RILEVANTE_IVA));
		capitoloCorrente.setNote(travasaAttributoModificabile(capitoloModificato.getNote(), capitoloCorrente.getNote(),
				TipologiaAttributo.NOTE));
		capitoloCorrente.setFlagImpegnabile(travasaAttributoModificabile(capitoloModificato.getFlagImpegnabile(), capitoloCorrente.getFlagImpegnabile(),
				TipologiaAttributo.FLAG_IMPEGNABILE));
		//task-55
		capitoloCorrente.setFlagNonInserireAllegatoA1(travasaAttributoModificabile(capitoloModificato.getFlagNonInserireAllegatoA1(), capitoloCorrente.getFlagNonInserireAllegatoA1(),
				TipologiaAttributo.FLAG_NON_INSERIRE_ALLEGATO_A1));
		
		// Non aggiornabili
		capitoloCorrente.setDataAnnullamento(capitoloCorrente.getDataAnnullamento());
		capitoloCorrente.setDataFineValidita(capitoloCorrente.getDataFineValidita());
		capitoloCorrente.setDisponibilitaVariare(capitoloCorrente.getDisponibilitaVariare());
		capitoloCorrente.setFondoPluriennaleVinc(capitoloCorrente.getFondoPluriennaleVinc());
		capitoloCorrente.setFondoPluriennaleVincPrec(capitoloCorrente.getFondoPluriennaleVincPrec());
//		capitoloCorrente.setStatoOperativoElementoDiBilancio(capitoloModificato.getStatoOperativoElementoDiBilancio());
		
		// Sempre da aggiornare
		capitoloCorrente.setDescrizioneArticolo(capitoloCorrente.getDescrizioneArticolo());
		//capitoloCorrente.setCategoriaCapitolo(capitoloModificato.getCategoriaCapitolo());
		
		travasaDescrizioneCapitolo(capitoloModificato,capitoloCorrente);
	}
	
	protected void travasaClassificatoriGenerici(CAP capitoloModificato, CAP capitoloCorrente) {
		final String methodName = "travasaClassificatoriGenerici";
		
		List<ClassificatoreGenerico> classificatoriGenerici = new ArrayList<ClassificatoreGenerico>();
		classificatoriGenerici.addAll(capitoloCorrente.getClassificatoriGenerici());
		classificatoriGenerici.addAll(capitoloModificato.getClassificatoriGenerici());
		
		Map<TipologiaClassificatore, ClassificatoreGenerico> classificatoriDaTravasare = new HashMap<TipologiaClassificatore, ClassificatoreGenerico>();
		
		for(ClassificatoreGenerico cgList : classificatoriGenerici) {
			TipologiaClassificatore tc;
			TipoClassificatore tipoClassificatore;
			if(cgList == null || (tipoClassificatore = cgList.getTipoClassificatore()) == null || tipoClassificatore.getCodice() == null
					|| (tc = TipologiaClassificatore.fromCodice(tipoClassificatore.getCodice())) == null
					|| classificatoriDaTravasare.containsKey(tc)) {
				continue;
			}
			
			ClassificatoreGenerico cgCorrente = findClassificatoreGenericoByCodiceTipoClassificatore(capitoloCorrente, tipoClassificatore.getCodice());
			ClassificatoreGenerico cgModificato = findClassificatoreGenericoByCodiceTipoClassificatore(capitoloModificato, tipoClassificatore.getCodice());
			
			ClassificatoreGenerico cgDaTravasare = travasaClassificatoreModificabile(cgModificato, cgCorrente, tc);
			if(cgDaTravasare != null) {
				log.debug(methodName, "Imposto il classificatore generico per il tipo " + tc);
				classificatoriDaTravasare.put(tc, cgDaTravasare);
			}
		}
		
		capitoloCorrente.setClassificatoriGenerici(new ArrayList<ClassificatoreGenerico>(classificatoriDaTravasare.values()));
	}


	protected ClassificatoreGenerico findClassificatoreGenericoByCodiceTipoClassificatore(CAP capitolo, String codice) {
		for(ClassificatoreGenerico cg : capitolo.getClassificatoriGenerici()) {
			if(cg != null && cg.getTipoClassificatore() != null && codice.equals(cg.getTipoClassificatore().getCodice())) {
				return cg;
			}
		}
		return null;
	}


	protected <C extends Codifica> C travasaClassificatoreModificabile(C classificatoreModificato, C classificatoreCorrente, TipologiaClassificatore... tipologieClassificatori) {
		final String methodName = "travasaClassificatoreModificabile";
		// Se ho un solo valore, allora posso modificare cio' che voglio
		if(stessoNumeroCapArt == 1L) {
			log.debug(methodName, "Un unico capitolo: travaso il classificatore corrispondente ai tipi " + Arrays.toString(tipologieClassificatori));
			return classificatoreModificato;
		}
		for(TipologiaClassificatore tc : tipologieClassificatori) {
			if(classificatoriNonModificabili.containsKey(tc)) {
				log.debug(methodName, "Non modificabile puntualmente: travaso il classificatore corrispondente al tipo " + tc);
				return classificatoreModificato;
			}
		}
		log.debug(methodName, "Mantengo il classificatore originale per i tipi " + Arrays.toString(tipologieClassificatori));
		return classificatoreCorrente;
	}

	protected <A extends Object> A travasaAttributoModificabile(A attributoModificato, A attributoCorrente, TipologiaAttributo... tipologiaAttributo) {
		final String methodName = "travasaAttributoModificabile";
		// Se ho un solo valore, allora posso modificare cio' che voglio
		if(stessoNumeroCapArt == 1L) {
			log.debug(methodName, "Un unico capitolo: travaso l'attributo corrispondente ai tipi " + Arrays.toString(tipologiaAttributo));
			return attributoModificato;
		}
		for(TipologiaAttributo ta : tipologiaAttributo) {
			if(attributiNonModificabili.containsKey(ta)) {
				log.debug(methodName, "Non modificabile puntualmente: travaso l'attributo corrispondente al tipo " + ta);
				return attributoModificato;
			}
		}
		log.debug(methodName, "Mantengo l'attributo originale per i tipi " + Arrays.toString(tipologiaAttributo));
		return attributoCorrente;
	}
	
	private void travasaDescrizioneCapitolo(CAP capitoloModificato, CAP capitoloCorrente) {
		final String methodName = "travasaDescrizioneCapitolo";
		// Posso modificarlo se ho un unico capitolo o se tutti i capitoli sono con pari articolo
		if(stessoNumeroCap == 1 || stessoNumeroCap == stessoNumeroCapArt) {
			log.debug(methodName, "Travaso la descrizione del capitolo");
			capitoloCorrente.setDescrizione(capitoloModificato.getDescrizione());
		}
	}
	
}
