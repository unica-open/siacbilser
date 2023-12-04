/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaAutomaticaService.MovimentoDettaglioConContoTipoOperazione;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoGruppoDocumento;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * MovimentoHandler delle Note di credito.
 * 
 * @author Domenico
 */
public class SubdocumentoSpesaSubdocumentoSpesaMovimentoHandler extends SubdocumentoSubdocumentoMovimentoHandler<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa> {

	public SubdocumentoSpesaSubdocumentoSpesaMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		
		Entita movimentoCollegato = registrazioneMovFin.getMovimentoCollegato(); 
		
		SubdocumentoSpesa sub = (SubdocumentoSpesa)movimentoCollegato;
		RicercaDettaglioQuotaSpesaResponse resRDQS = documentoServiceCallGroup.ricercaDettaglioQuotaSpesa(sub);
		sub = resRDQS.getSubdocumentoSpesa();
		
		RicercaDettaglioDocumentoSpesaResponse resRDDS = documentoServiceCallGroup.ricercaDettaglioDocumentoSpesaCached(sub.getDocumento());
		DocumentoSpesa documento = resRDDS.getDocumento();
		sub.setDocumento(documento); //a questo punto il documento ha tutti i subdocumentiIva convertiti con SiacTSubdocIva_SubdocumentoIva_Base (che ha già le Aliquote e quindi dovrebbe bastarmi)
		
		registrazioneMovFin.setMovimentoCollegato(sub);
		
		List<DocumentoSpesa> listaDocumentiSpesaFiglioByTipoGruppo = documento.getListaDocumentiSpesaFiglioByTipoGruppo(TipoGruppoDocumento.NOTA_DI_CREDITO);
		if(listaDocumentiSpesaFiglioByTipoGruppo.isEmpty() || listaDocumentiSpesaFiglioByTipoGruppo.size()>1){
			throw new BusinessException("Attenzione! Viene gestito solamente il caso in cui e' presente esattamente una e una sola nota di credito associata al documento.");
		}
		
		Entita movimento = registrazioneMovFin.getMovimento();
		SubdocumentoSpesa subNota = (SubdocumentoSpesa)movimento;
		RicercaDettaglioQuotaSpesaResponse resRDQSN = documentoServiceCallGroup.ricercaDettaglioQuotaSpesa(subNota);
		subNota = resRDQSN.getSubdocumentoSpesa();
		
		RicercaDettaglioDocumentoSpesaResponse resRDDSN = documentoServiceCallGroup.ricercaDettaglioDocumentoSpesaCached(subNota.getDocumento());
		DocumentoSpesa nota = resRDDSN.getDocumento();
		subNota.setDocumento(nota);
		 
		registrazioneMovFin.setMovimento(subNota); //Documento NotaCredito associato alla quota NotaCredito
		
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		// dati gia' presenti.
		
	}
	
	@Override
	protected void setImportoInElementoScrittura(BigDecimal imponibile, BigDecimal imposta, MovimentoDettaglioConContoTipoOperazione movimentoDettaglio) {
		ContoTipoOperazione contoTipoOperazione = movimentoDettaglio.getContoTipoOperazione();
		String methodName = "";
		
		// JIRA-3559: Inverso rispetto al subdoc di stesso segno
		if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())) {
			log.debug(methodName, "segno dare, ha importo pari a imponibile + imposta");
			// L'avere ha importo pari a imponibile + imposta
			movimentoDettaglio.setImporto(imponibile.add(imposta).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode()));
		} else {
			log.debug(methodName, "segno avere");
			if(OperazioneTipoImporto.IMPONIBILE.equals(contoTipoOperazione.getOperazioneTipoImporto())) {
				log.debug(methodName, "tipo imponibile : importo=imponibile");
				movimentoDettaglio.setImporto(imponibile);
			} else if(OperazioneTipoImporto.IMPOSTA.equals(contoTipoOperazione.getOperazioneTipoImporto())) {
				log.debug(methodName, "tipo imposta, importo = imposta");
				movimentoDettaglio.setImporto(imposta);
			} else if(OperazioneTipoImporto.LORDO.equals(contoTipoOperazione.getOperazioneTipoImporto())) {
				log.debug(methodName, "tipo lordo, importo = imponibile+imposta");
				movimentoDettaglio.setImporto(imponibile.add(imposta).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode()));
			}
		}
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimentoCollegato();
		SubdocumentoSpesa subdoc = (SubdocumentoSpesa)movimento;
		return subdoc.getImpegno().getCapitoloUscitaGestione();
	}
	
	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		SubdocumentoSpesa subdoc = (SubdocumentoSpesa)registrazioneMovFin.getMovimento();
		return subdoc.getImporto();
	}

}
