/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ContoTesoreriaDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;



/**
 * The Class ControllaDisponibilitaCassaContoVincolatoCapitoloService.
 *
 * @author elisa
 * @version 1.0.0 12 gen 2022
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllaDisponibilitaCassaContoVincolatoCapitoloService
		extends CheckedAccountBaseService<ControllaDisponibilitaCassaContoVincolatoCapitolo, ControllaDisponibilitaCassaContoVincolatoCapitoloResponse>{ 
	
//	private CapitoloEntrataGestione capEG;
	
	List<CapitoloEntrataGestione> capitoliEntrataGestione = new ArrayList<CapitoloEntrataGestione>();
	List<CapitoloUscitaGestione> capitoliUscitaGestione = new ArrayList<CapitoloUscitaGestione>();
	
	List<CapitoloEntrataGestione> capitoliEntrataGestioneVincolati = new ArrayList<CapitoloEntrataGestione>();
	List<CapitoloUscitaGestione> capitoliUscitaGestioneVincolati = new ArrayList<CapitoloUscitaGestione>();
	
//	private CapitoloUscitaGestione capUG;
	
	@Autowired
	private CapitoloDad capitoloDad;

	@Autowired
	private ContoTesoreriaDad contoTesoreriaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getContoTesoreria() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto tesoreria"));
		boolean parametriSpesaValidi = isCapitoloValorizzato(req.getCapitoloUscitaGestione()) || isListaIdValida(req.getIdsSubdocumentiSpesa());
		boolean parametriEntrataValidi = isCapitoloValorizzato(req.getCapitoloEntrataGestione()) || isListaIdValida(req.getIdsSubdocumentiEntrata());
		checkCondition(parametriSpesaValidi ^ parametriEntrataValidi, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo o subdocumenti"));
	}
	@Override
	protected void init() {
		super.init();
		capitoloDad.setEnte(ente);
	}
	
	@Transactional
	public ControllaDisponibilitaCassaContoVincolatoCapitoloResponse executeService(ControllaDisponibilitaCassaContoVincolatoCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	@Override
	protected void execute() {	
		caricaCapitoloEntrataGestione();
		caricaCapitoloUscitaGestione();

		controllaCapitoliEntrataGestione();
		controllaCapitoliUscitaGestione();
		
		if(CollectionUtils.isNotEmpty(capitoliEntrataGestioneVincolati)  || CollectionUtils.isNotEmpty(this.capitoliUscitaGestioneVincolati)) {
			ContoTesoreria contoTesoreria = caricaContoTesoreria();
			
			checkContoTesoreria(contoTesoreria);
			
			caricaDisponibilitaPagare(contoTesoreria);
			caricaDisponibilitaIncassare(contoTesoreria);
		}
		
//		res.addInformazione(new Informazione("CRU-CON-001","operazione effettuata con successo."));
	}
	private void checkContoTesoreria(ContoTesoreria contoTesoreria) {
		if(contoTesoreria == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("conto tesoreria"));
		}
		if(!contoTesoreria.isVincolato()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("conto tesoreria " + StringUtils.defaultString(contoTesoreria.getCodice()), "conto non vincolato"));
		}
		
	}
	private void caricaDisponibilitaIncassare(ContoTesoreria contoTesoreria) {
		if(this.capitoliEntrataGestioneVincolati == null || this.capitoliEntrataGestioneVincolati.isEmpty() || contoTesoreria == null) {
			return;
		}
		for (CapitoloEntrataGestione capEG : this.capitoliEntrataGestioneVincolati) {
			BigDecimal disponibilitaIncSottoContoVincolato = capitoloDad.caricaDisponibilitaIncassareSottoContoVincolato(contoTesoreria, capEG);
			//SIAC-8856
			capEG.setListaVincoliUEGestione(capitoloDad.codiceVincolo(capEG,contoTesoreria));
			res.setDisponibilitaContoVincolatoEntrata(disponibilitaIncSottoContoVincolato);
			//SIAC-8856
			if(capEG.getListaVincoliUEGestione().isEmpty()) {
				res.addMessaggio(MessaggioBil.DISPONIBILITA_CAPITOLO_SU_SOTTOCONTO_NON_VINCOLATO.getMessaggio(String.valueOf(capEG.getNumeroCapitolo()), String.valueOf(capEG.getNumeroArticolo()), getChiaveContoTesoreria(contoTesoreria)));
			}else {
				res.addMessaggio(MessaggioBil.DISPONIBILITA_CAPITOLO_SU_SOTTOCONTO_INCASSO.getMessaggio(CollectionUtil.getFirst(capEG.getListaVincoliUEGestione()), getChiaveContoTesoreria(contoTesoreria) + " e' di " + NumberUtil.toImporto(disponibilitaIncSottoContoVincolato)));
			}
		} 
		
	}
	
	private void caricaDisponibilitaPagare(ContoTesoreria contoTesoreria) {
		if(this.capitoliUscitaGestioneVincolati == null || this.capitoliUscitaGestioneVincolati.isEmpty() || contoTesoreria == null) {
			return;
		}
		
		for (CapitoloUscitaGestione capUG : this.capitoliUscitaGestioneVincolati) {
			BigDecimal disponibilitaPagSottoContoVincolato = capitoloDad.caricaDisponibilitaPagareSottoContoVincolato(contoTesoreria, capUG);
			res.setDisponibilitaContoVincolatoSpesa(disponibilitaPagSottoContoVincolato);
			res.addMessaggio(MessaggioBil.DISPONIBILITA_CAPITOLO_SU_SOTTOCONTO.getMessaggio(getChiaveCapitolo(capUG), getChiaveContoTesoreria(contoTesoreria) + " di " + NumberUtil.toImporto(disponibilitaPagSottoContoVincolato)));
		} 
		
		
	}
	private ContoTesoreria caricaContoTesoreria() {
		if(req.getContoTesoreria() == null) {
			return null;
		}
		if(req.getContoTesoreria().getUid() != 0) {
			return contoTesoreriaDad.findByUid(req.getContoTesoreria().getUid());
		}
		//devo ricaricarlo, viene passato solo il codice da front-end!!!!
		if(StringUtils.isNotBlank(req.getContoTesoreria().getCodice())) {
			return contoTesoreriaDad.findContoTesoreriaByEnteCodice(ente, req.getContoTesoreria().getCodice());
		}
		return null;
	}
	
	private void caricaCapitoloEntrataGestione() {
		boolean caricaCapitoloDaSubdoc = req.getIdsSubdocumentiEntrata() != null && !req.getIdsSubdocumentiEntrata().isEmpty();
		if(req.getCapitoloEntrataGestione() == null && !caricaCapitoloDaSubdoc){
			return;
		}
		if(caricaCapitoloDaSubdoc){
			Map<CapitoloEntrataGestione, List<SubdocumentoEntrata>> capitoliFound = capitoloDad.findCapitoliEntrataGestioneBySubdoc(req.getIdsSubdocumentiEntrata());
			this.capitoliEntrataGestione.addAll(capitoliFound.keySet());
			return;
		}
		CapitoloEntrataGestione capitolo = req.getCapitoloEntrataGestione();
		if(capitolo.getUid() == 0) {
			Integer id = caricaIdCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE,capitolo.getAnnoCapitolo(), capitolo.getNumeroCapitolo(), capitolo.getNumeroArticolo(), capitolo.getNumeroUEB());
			capitolo.setUid(id);
		}
		this.capitoliEntrataGestione.add(capitolo);

	}
	
	private void caricaCapitoloUscitaGestione() {
		boolean caricaCapitoloDaSubdoc = req.getIdsSubdocumentiSpesa() != null && !req.getIdsSubdocumentiSpesa().isEmpty();
		if(req.getCapitoloUscitaGestione() == null && !caricaCapitoloDaSubdoc){
			return;
		}
		if(caricaCapitoloDaSubdoc){
			Map<CapitoloUscitaGestione, List<SubdocumentoSpesa>> capitoliFound = capitoloDad.findCapitoliSpesaGestioneBySubdoc(req.getIdsSubdocumentiSpesa());
			this.capitoliUscitaGestione.addAll(capitoliFound.keySet());
			return;
		}
		CapitoloUscitaGestione capitolo = req.getCapitoloUscitaGestione();
		if(capitolo.getUid() == 0) {
			Integer id = caricaIdCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE,capitolo.getAnnoCapitolo(), capitolo.getNumeroCapitolo(), capitolo.getNumeroArticolo(), capitolo.getNumeroUEB());
			capitolo.setUid(id);
		}
		this.capitoliUscitaGestione.add(capitolo);
		
	}
	
	private Integer caricaIdCapitolo(TipoCapitolo tipoCapitolo, Integer anno,  Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB ) {
		if(anno == null || anno.intValue() == 0) {
			anno = req.getAnnoBilancio();
		}
		List<Integer> idCapitolos = capitoloDad.ricercaIdByChiaveLogicaCapitolo(tipoCapitolo, anno, numeroCapitolo, numeroArticolo, numeroUEB);
		if(idCapitolos == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("capitolo"  + getChiaveLogicaCapitolo(tipoCapitolo, anno, numeroCapitolo, numeroArticolo, numeroUEB) ));
		}
		if(idCapitolos.size() > 1) {
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("trovati piu' capitoli corrispondenti a questi dati: " + getChiaveLogicaCapitolo(tipoCapitolo, anno, numeroCapitolo, numeroArticolo, numeroUEB) ));
		}
		Integer idCapitolo = idCapitolos.get(0);
		if(idCapitolo == null || idCapitolo.intValue() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("capitolo" + getChiaveLogicaCapitolo(tipoCapitolo, anno, numeroCapitolo, numeroArticolo, numeroUEB)));
		}
		return idCapitolo;
	}

	private void controllaCapitoliEntrataGestione() {
//		controllaCapitolo(capEG);
		for (CapitoloEntrataGestione capEG : this.capitoliEntrataGestione) {
			if(capEG!= null && capEG.getUid() != 0 && isCapitoloVincolato(capEG)) {
				this.capitoliEntrataGestioneVincolati.add(capEG);
				continue;
			}
			res.addMessaggio(MessaggioBil.DISPONIBILITA_CAPITOLO_SU_SOTTOCONTO.getMessaggio(getChiaveCapitolo(capEG), "non calcolabile, capitolo non vincolato."));
			
		}
	}
	
	private void controllaCapitoliUscitaGestione() {
//		controllaCapitolo(capUG);
		for (CapitoloUscitaGestione capUG : this.capitoliUscitaGestione) {
			if(capUG!= null && capUG.getUid() != 0 && isCapitoloVincolato(capUG)) {
				this.capitoliUscitaGestioneVincolati.add(capUG);
				continue;
			}
			res.addMessaggio(MessaggioBil.DISPONIBILITA_CAPITOLO_SU_SOTTOCONTO.getMessaggio(getChiaveCapitolo(capUG), "non calcolabile, capitolo non vincolato."));
		}
	}
	
	private boolean isCapitoloVincolato(Capitolo<?, ?> cap) {
		if(cap == null || cap.getUid() == 0) {
			return true;
		}
		if (capitoloDad.countVincoliCapitolo(cap, EnumSet.of(StatoOperativo.VALIDO)) == 0) {
			
			return false;
		}
		return true;
	}
	
	
	protected String getChiaveLogicaCapitolo(TipoCapitolo tipoCapitolo, Integer anno,  Integer numeroCapitolo, Integer numeroArticolo, Integer numeroUEB) {
		StringBuilder s = new StringBuilder();
		if(tipoCapitolo != null) {
			s.append(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(tipoCapitolo) ? " entrata "  
					:TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(tipoCapitolo) ? " spesa " : " [] "  );
		}
		s.append(anno!= null? anno : "null").append("/")
			.append(numeroCapitolo!= null? numeroCapitolo : "null").append("/")
			.append(numeroArticolo!= null? numeroArticolo : "null");
		if(isGestioneUEB()) {
			s.append("/").append(numeroUEB != null? numeroUEB : "null");
		}
		return s.toString();
	}

	private boolean isListaIdValida(List<Integer> uids) {
		return CollectionUtils.isNotEmpty(uids);
	}
	
	private boolean isCapitoloValorizzato(Capitolo<?,?> cap) {
		return cap != null && (cap.getUid() != 0 || isChiaveLogicaCapitoloValorizzata(cap));
	}
	
	private boolean isChiaveLogicaCapitoloValorizzata(Capitolo<?,?> cap) {
		return cap != null && cap.getNumeroCapitolo() != null && cap.getNumeroCapitolo().intValue() != 0 
				&& cap.getNumeroArticolo() != null;
	}
	
	private String getChiaveCapitolo(Capitolo<?,?> cap) {
		if(cap == null) {
			return null;
		}
		boolean annoCapitoloValorizzato = cap.getAnnoCapitolo() != null && cap.getAnnoCapitolo().intValue()!= 0;
		StringBuilder chiave = new StringBuilder()
				.append(annoCapitoloValorizzato? cap.getAnnoCapitolo() : "")
				.append(annoCapitoloValorizzato? "/" : "")
				.append(cap.getNumeroCapitolo() != null? cap.getNumeroCapitolo() : "")
				.append(cap.getNumeroCapitolo() != null? "/" : "")
				.append(cap.getNumeroArticolo() != null? cap.getNumeroArticolo() : "")
				;
		return chiave.toString();
	}
	
	private String getChiaveContoTesoreria(ContoTesoreria conto){
		return conto != null? StringUtils.defaultString(conto.getCodice()) : "";
	}
	
}
