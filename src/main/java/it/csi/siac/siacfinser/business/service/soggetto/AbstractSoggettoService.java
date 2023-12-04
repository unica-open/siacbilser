/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public abstract class AbstractSoggettoService<REQ extends ServiceRequest, RES extends ServiceResponse> extends AbstractBaseService<REQ, RES> {
	
	@Autowired
	DocumentoSpesaService documentoSpesaService;
	
	@Autowired
	DocumentoEntrataService documentoEntrataService;
	
	protected List<Errore> controlliDipendenzaEntita(Integer idSoggetto, Ente ente, Richiedente richiedente, Operazione operazione ){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		listaErrori = soggettoDad.controlliEsistenzaLegamiSoggettoEntita(idSoggetto, ente, operazione);
		
		if(operazione.equals(Operazione.ANNULLA)){
			Soggetto soggettoToCheck = new Soggetto();
			soggettoToCheck.setUid(idSoggetto);

			// verifico la presenza di documenti di SPESA in stato EMESSO collegati al soggetto
			RicercaSinteticaDocumentoSpesa ricercaSinteticaDocumentoSpesa = new RicercaSinteticaDocumentoSpesa();
			
			DocumentoSpesa documentoSpesa = new DocumentoSpesa();
			documentoSpesa.setEnte(ente);
			documentoSpesa.setStatoOperativoDocumento(StatoOperativoDocumento.EMESSO);
			documentoSpesa.setSoggetto(soggettoToCheck);

			ricercaSinteticaDocumentoSpesa.setParametriPaginazione(new ParametriPaginazione());
			ricercaSinteticaDocumentoSpesa.setRichiedente(richiedente);
			ricercaSinteticaDocumentoSpesa.setDocumentoSpesa(documentoSpesa);

			RicercaSinteticaDocumentoSpesaResponse ricercaSinteticaDocumentoSpesaResponse = documentoSpesaService.ricercaSinteticaDocumentoSpesa(ricercaSinteticaDocumentoSpesa);

			if(ricercaSinteticaDocumentoSpesaResponse.isFallimento()){
				listaErrori = ricercaSinteticaDocumentoSpesaResponse.getErrori();
			} else {
				if(ricercaSinteticaDocumentoSpesaResponse.getDocumenti()!=null && ricercaSinteticaDocumentoSpesaResponse.getDocumenti().size()>0){
					listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore("", "documenti spesa"));
				}					
			}

			// verifico la presenza di documenti di ENTRATA in stato EMESSO collegati al soggetto
			RicercaSinteticaDocumentoEntrata ricercaSinteticaDocumentoEntrata = new RicercaSinteticaDocumentoEntrata();
			
			DocumentoEntrata documentoEntrata = new DocumentoEntrata();
			documentoEntrata.setEnte(ente);
			documentoEntrata.setStatoOperativoDocumento(StatoOperativoDocumento.EMESSO);
			documentoEntrata.setSoggetto(soggettoToCheck);

			ricercaSinteticaDocumentoEntrata.setParametriPaginazione(new ParametriPaginazione());
			ricercaSinteticaDocumentoEntrata.setRichiedente(richiedente);
			ricercaSinteticaDocumentoEntrata.setDocumentoEntrata(documentoEntrata);

			RicercaSinteticaDocumentoEntrataResponse ricercaSinteticaDocumentoEntrataResponse = documentoEntrataService.ricercaSinteticaDocumentoEntrata(ricercaSinteticaDocumentoEntrata);

			if(ricercaSinteticaDocumentoEntrataResponse.isFallimento()){
				listaErrori = ricercaSinteticaDocumentoEntrataResponse.getErrori();
			} else {
				if(ricercaSinteticaDocumentoEntrataResponse.getDocumenti()!=null && ricercaSinteticaDocumentoEntrataResponse.getDocumenti().size()>0){
					listaErrori.add(ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore("", "documenti entrata"));
				}					
			}
		}
		
		return listaErrori;
	}

	protected Soggetto completaInformazioni(Soggetto sogg,String codiceAmbito, Integer idEnte,String codiceSoggetto,
			boolean isIncludeModif, Richiedente richiedente,DatiOperazioneDto datiOperazioneDto){
		String methodName = "completaInformazioni";
		List<SedeSecondariaSoggetto> listaSediSecondarie = null;
		List<ModalitaPagamentoSoggetto> listaModPag = null ;
		if(sogg!=null){
			//2 Lista SediSecondarie
			log.debug(methodName, "- chiamo ricercaSediSecondarie()");	
			boolean isDecentrato = soggettoDad.isDecentrato(richiedente.getAccount());
			listaSediSecondarie = soggettoDad.ricercaSediSecondarie(idEnte, sogg.getUid(), isDecentrato, isIncludeModif,datiOperazioneDto);
			//3 Lista ModalitaPagamento
			log.debug(methodName, "chiamo ricercaModalitaPagamento()");			
			listaModPag = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte, sogg.getUid(), "Soggetto", isIncludeModif);
			
			if (listaSediSecondarie != null && listaSediSecondarie.size() > 0) {
				for (SedeSecondariaSoggetto currentSedeSecondaria : listaSediSecondarie) {
					List<ModalitaPagamentoSoggetto> listaModPagSupportSedi = soggettoDad.ricercaModalitaPagamento(codiceAmbito, idEnte, currentSedeSecondaria.getUid(), currentSedeSecondaria.getDenominazione(), isIncludeModif);
					if (listaModPagSupportSedi != null && listaModPagSupportSedi.size() > 0) {
						if (listaModPag == null) {
							listaModPag = new ArrayList<ModalitaPagamentoSoggetto>();
						}
						for (ModalitaPagamentoSoggetto currentModPagSede : listaModPagSupportSedi) {
							listaModPag.add(currentModPagSede);
						}
					}
				}
			}
			
			//COMPONGO LA DESCRIZIONE ARRICCHITA TRAMITE IL METODO CENTRALIZZATO:
			listaModPag = modalitaPagamentoSoggettoHelper.componiDescrizioneArricchita(listaModPag, null, idEnte);
			//
			
			sogg.setSediSecondarie(listaSediSecondarie);
			sogg.setModalitaPagamentoList(listaModPag);
		}
		return sogg;
	}
}
