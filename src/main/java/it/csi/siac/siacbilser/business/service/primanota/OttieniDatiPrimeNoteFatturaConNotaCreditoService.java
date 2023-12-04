/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCredito;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCreditoResponse;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Ricerca sintetica di una PrimaNota
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OttieniDatiPrimeNoteFatturaConNotaCreditoService extends CheckedAccountBaseService<OttieniDatiPrimeNoteFatturaConNotaCredito, OttieniDatiPrimeNoteFatturaConNotaCreditoResponse> {
	
	@Autowired 
	@Inventario
	private PrimaNotaInvDad primaNotaInvDad;
	
	@Autowired
	private BilancioDad bilancioDad;
	
	private PrimaNota primaNotaInput;
	private Evento evento;
	private TipoCausale tipoCausale;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNota(), "prima nota");
		primaNotaInput = req.getPrimaNota();
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public OttieniDatiPrimeNoteFatturaConNotaCreditoResponse executeService(OttieniDatiPrimeNoteFatturaConNotaCredito serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		Utility.MDTL.addModelDetails(DocumentoSpesaModelDetail.DocumentiCollegati, DocumentoSpesaModelDetail.TipoDocumento);
		primaNotaInvDad.setEnte(ente);
		bilancioDad.setEnteEntity(ente);
	}
	
	@Override
	protected void execute() {
		
		caricaTipoCausale();
		
		if(!TipoCausale.Integrata.equals(this.tipoCausale)) {
			return;
		}
		
		caricaEvento();
		
		if(!"DS".equals(evento.getTipoEvento().getCodice())) {
			return;
		}
		
		DocumentoSpesa documento = caricaDocumentoCheHaGeneratoLaPrimaNota();
		if(documento == null || documento.getTipoDocumento() == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile reperire il documento che ha generato la prima nota."));
		}
		
		impostaInResponseDatiNotaCredito(documento);
		
		impostaInResponseDatiFattura(documento);
		
		impostaInResponseDocumentoGenerante(documento);
	}
	
	private void impostaInResponseDocumentoGenerante(DocumentoSpesa documento) {
		documento.setListaDocumentiSpesaFiglio(null);
		documento.setListaDocumentiSpesaPadre(null);
		res.setDocumentoSpesaCheHaGeneratoLaPrimaNota(documento);
	}


	private void caricaTipoCausale() {
		this.tipoCausale = primaNotaInvDad.findTipoCausalePrimaNota(req.getPrimaNota().getUid());
		if(this.tipoCausale == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile caricare il tipo causale della prima nota."));
		}
		res.setTipoCausale(tipoCausale);
	}


	private void caricaEvento() {
		this.evento = primaNotaInvDad.caricaEventoPrimaNotaIntegrata(primaNotaInput);
		if(this.evento == null || this.evento.getTipoEvento() == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile reperire l'evento della prima nota."));
		}
		res.setEventoPrimaNota(this.evento);
	}


	private void impostaInResponseDatiFattura(DocumentoSpesa documento) {
		final String methodName = "impostaDatiFattura";
		if(!documento.getTipoDocumento().isFattura()) {
			//il documento che ha generato la prima nota della request non e' una fattura Non devo impostare valori qui.
			return;
		}
		
		
		if(documento.getListaDocumentiSpesaFiglio() == null || documento.getListaDocumentiSpesaFiglio().isEmpty()) {
			//la nota di credito non e' collegata a fatture: esco.
			log.debug(methodName, "la fattura che ha generato la prima nota non risulta essere collegata a note credito. Esco.");
			return;
		}
		List<PrimaNota> primeNoteGenerateDalleNoteDiCredito = caricaPrimeNoteDocumenti(documento.getListaDocumentiSpesaFiglio());
		// i documenti di tipo FAT hanno come prime note spesa figlio documenti che possono essere solo note credito (da analisi)
		res.setNoteCreditoCollegateAFattura(documento.getListaDocumentiSpesaFiglio());
		res.setPrimeNoteGenerateDallaFattura(Arrays.asList(req.getPrimaNota()));
		res.setPrimeNoteGenerateDalleNoteDiCredito(primeNoteGenerateDalleNoteDiCredito);
	}


	private void impostaInResponseDatiNotaCredito(DocumentoSpesa documento) {
		final String methodName = "impostaDatiNotaCredito";
		if(!documento.getTipoDocumento().isNotaCredito()) {
			//il documento che ha generato la prima nota della request non e' una nota di credito. Non devo impostare valori.
			return;
		}
		//il documento che ha generato la prima nota della request e' di tipo nota di credito. Imposto la prima nota  della request nel campo corrispondente in response
		res.setPrimeNoteGenerateDalleNoteDiCredito(Arrays.asList(req.getPrimaNota()));
		if(documento.getListaDocumentiSpesaPadre() == null || documento.getListaDocumentiSpesaPadre().isEmpty()) {
			//la nota di credito non e' collegata a fatture: esco.
			log.debug(methodName, "la nota di credito che ha generato la prima nota non risulta essere collegata a documenti di spesa. Esco.");
			return;
		}
		
		res.setFattureCollegateANotaDiCredito(documento.getListaDocumentiSpesaPadre());
		List<PrimaNota> primeNoteGenerateDalleFattureCollegate = caricaPrimeNoteDocumenti(documento.getListaDocumentiSpesaPadre());
		res.setPrimeNoteGenerateDallaFattura(primeNoteGenerateDalleFattureCollegate);
		return;
		
	}


	/**
	 * @param docs
	 */
	private List<PrimaNota> caricaPrimeNoteDocumenti(List<DocumentoSpesa> docs) {
		final String methodName="caricaPrimeNoteDocumenti";
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(0, Integer.MAX_VALUE);
		List<Integer> uidsDocumento = creaListaUidDocumento(docs);
		Bilancio bilancio = caricaBilancio();
		ListaPaginata<PrimaNota> primeNote = null;
		try {
			primeNote = primaNotaInvDad.ricercaSinteticaPrimeNoteIntegrateRegistroA(bilancio, new PrimaNota(), null, null, null, null, null, null, null, Arrays.asList(StatoOperativoPrimaNota.PROVVISORIO, StatoOperativoPrimaNota.DEFINITIVO), null, "S", Arrays.asList(evento.getTipoEvento()), null, null, null, null, null, null, null, null, uidsDocumento, null, null, null, null, parametriPaginazione, PrimaNotaModelDetail.PrimaNotaInventario);
		} catch (DadException e) {
			log.error(methodName, "errore durante l'elaborazione del dad");
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore durante la ricerca della prima nota nel DAD: " + e.getMessage()));
		}
		
		return primeNote;
	}


	private List<Integer> creaListaUidDocumento(List<DocumentoSpesa> listaDocumentiSpesaPadre) {
		 List<Integer> uids = new ArrayList<Integer>();
		 for (DocumentoSpesa ds : listaDocumentiSpesaPadre) {
			if(ds != null && ds.getUid() != 0) {
				uids.add(Integer.valueOf(ds.getUid()));
			}
		}
		return uids;
	}


	/**
	 * Carica bilancio.
	 */
	private Bilancio caricaBilancio() {
		Bilancio bilancio =  bilancioDad.getBilancioByAnno(req.getAnnoBilancio());
		if(bilancio == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile reperire il bilancio."));
		}
		return bilancio;
	}


	/**
	 * @return
	 */
	private DocumentoSpesa caricaDocumentoCheHaGeneratoLaPrimaNota() {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(0, 1);
		SubdocumentoSpesaModelDetail[] subdocModelDetail = new SubdocumentoSpesaModelDetail[]{SubdocumentoSpesaModelDetail.DocPadreModelDetail};
		ListaPaginata<Entita> entitas = primaNotaInvDad.ottieniEntitaCollegatePrimaNota(primaNotaInput, TipoCollegamento.SUBDOCUMENTO_SPESA, subdocModelDetail, parametriPaginazione);
		if(entitas == null || entitas.getTotaleElementi() == 0) {
			return null;
		}
		SubdocumentoSpesa ss = (SubdocumentoSpesa) entitas.get(0);
		
		DocumentoSpesa documento = ss.getDocumento();
		res.setDocumentoSpesaCheHaGeneratoLaPrimaNota(documento);
		return documento;
	}
	

}
