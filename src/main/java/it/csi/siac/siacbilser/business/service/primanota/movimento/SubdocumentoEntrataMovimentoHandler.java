/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaAutomaticaService.MovimentoDettaglioConContoTipoOperazione;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * SubdocumentoEntrataMovimentoHandler.
 * 
 * @author Domenico
 * 
 */
public class SubdocumentoEntrataMovimentoHandler extends SubdocumentoMovimentoHandler<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata> {

	public SubdocumentoEntrataMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		
		Entita movimento = registrazioneMovFin.getMovimento(); 
		
		SubdocumentoEntrata sub = (SubdocumentoEntrata)movimento;
		RicercaDettaglioQuotaEntrataResponse resRDQS = documentoServiceCallGroup.ricercaDettaglioQuotaEntrata(sub);
		sub = resRDQS.getSubdocumentoEntrata();
		registrazioneMovFin.setMovimento(sub);
		
		RicercaDettaglioDocumentoEntrataResponse resRDDS = documentoServiceCallGroup.ricercaDettaglioDocumentoEntrataCached(sub.getDocumento());
		DocumentoEntrata documento = resRDDS.getDocumento();
		sub.setDocumento(documento); //a questo punto il documento ha tutti i subdocumentiIva convertiti con SiacTSubdocIva_SubdocumentoIva_Base (che ha già le Aliquote e quindi dovrebbe bastarmi)
		
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		//in questo caso i dati necessari sono già presenti
	}
	
	@Override
	protected void setImportoInElementoScrittura(BigDecimal imponibile, BigDecimal imposta, MovimentoDettaglioConContoTipoOperazione movimentoDettaglio, TipoDocumento tipoDocumento) {
		ContoTipoOperazione contoTipoOperazione = movimentoDettaglio.getContoTipoOperazione();
		
		if((OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno()) && !tipoDocumento.isNotaCredito())
				|| (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno()) && tipoDocumento.isNotaCredito())) {
			// Il dare ha importo pari a imponibile + imposta
			movimentoDettaglio.setImporto(imponibile.add(imposta).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode()));
		} else {
			if(OperazioneTipoImporto.IMPONIBILE.equals(contoTipoOperazione.getOperazioneTipoImporto())) {
				movimentoDettaglio.setImporto(imponibile);
			} else if(OperazioneTipoImporto.IMPOSTA.equals(contoTipoOperazione.getOperazioneTipoImporto())) {
				movimentoDettaglio.setImporto(imposta);
			} else if(OperazioneTipoImporto.LORDO.equals(contoTipoOperazione.getOperazioneTipoImporto())) {
				movimentoDettaglio.setImporto(imponibile.add(imposta).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode()));
			}
		}
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		SubdocumentoEntrata subdoc = (SubdocumentoEntrata) movimento;
		return subdoc.getAccertamento().getCapitoloEntrataGestione();
		
	}

	
	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		SubdocumentoEntrata subdoc = (SubdocumentoEntrata)registrazioneMovFin.getMovimento();
		return subdoc.getImporto();
	}
	
}
