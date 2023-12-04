/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc.datioperazione;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.tesoro.fatture.MandatoPagamentoTipo;
import it.tesoro.fatture.NaturaSpesaTipo;
import it.tesoro.fatture.PagamentoTipo;
import it.tesoro.fatture.StrutturaDatiOperazioneTipo;

/**
 * Popola la struttura dati operazione per l'operazione CP: Comunicazione pagamento
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaStrutturaDatiOperazioneCP extends PopolaStrutturaDatiOperazioneCOCPBase {

	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private SubdocumentoDad subdocumentoDad;

	public PopolaStrutturaDatiOperazioneCP(RegistroComunicazioniPCC registrazione) {
		super(registrazione);
	}

	@Override
	public StrutturaDatiOperazioneTipo popolaStrutturaDatiOperazione() {
		SubdocumentoSpesa subdocumentoSpesa = registrazione.getSubdocumentoSpesa();
		DocumentoSpesa documentoSpesa = registrazione.getDocumentoSpesa();
		
		Impegno impegno = subdocumentoSpesa.getImpegno();
		CapitoloUscitaGestione capitolo = impegno!=null?impegno.getCapitoloUscitaGestione():null;

		StrutturaDatiOperazioneTipo strutturaDatiOperazione = new StrutturaDatiOperazioneTipo();
		
		PagamentoTipo pagamento = new PagamentoTipo();
		
		pagamento.setCapitoliSpesa(StringUtils.abbreviate(getDescrizioneCapitolo(capitolo),100));
		
		pagamento.setCodiceCIG(getCIG(subdocumentoSpesa, null));
		pagamento.setCodiceCUP(getCUP(subdocumentoSpesa, null));
		
		pagamento.setDescrizione(StringUtils.abbreviate(subdocumentoSpesa.getDescrizione(),100));
		
		pagamento.setEstremiImpegno(StringUtils.abbreviate(getEstremiImpegno(impegno),50));
		
		pagamento.setIdFiscaleIVABeneficiario(getIdFiscaleIva(documentoSpesa.getSoggetto())); 
		
		// SIAC-5873: togliere l'importo split-reverse dall'importo trasmesso a PCC
		// Nota: il dato e' stato tolto qua si' da evitare trattamenti dati sulle registrazioni gia' esistenti
		BigDecimal importoSplitReverseSubdocumento = subdocumentoDad.getImportoSplitReverseSubdocumento(subdocumentoSpesa);
		// L'importo split/reverse puo' essere null. Per sicurezza, lo forzo in tal caso a zero (per evitare impatti su altre funzionalita')
		if(importoSplitReverseSubdocumento == null) {
			importoSplitReverseSubdocumento = BigDecimal.ZERO;
		}
		BigDecimal importoQuietanza = registrazione.getImportoQuietanza();
		if(importoQuietanza == null) {
			importoQuietanza = BigDecimal.ZERO;
		}
		importoQuietanza = importoQuietanza.subtract(importoSplitReverseSubdocumento);
		
		pagamento.setImportoPagato(importoQuietanza);
		
		MandatoPagamentoTipo mandatoPagamento = new MandatoPagamentoTipo();
		mandatoPagamento.setNumero(""+registrazione.getNumeroOrdinativo());
		
		GregorianCalendar dataEmissioneOrdinativo = new GregorianCalendar();
		dataEmissioneOrdinativo.setTime(registrazione.getDataEmissioneOrdinativo());
		mandatoPagamento.setDataMandatoPagamento(dataEmissioneOrdinativo);
		
		pagamento.setMandatoPagamento(mandatoPagamento);
		
		pagamento.setNaturaSpesa(getNaturaSpesa(capitolo));
		
		strutturaDatiOperazione.setPagamento(pagamento);
		
		return strutturaDatiOperazione;
	}

	
	
	/**
	 * Ottine la natura spesa a partire dal capitolo:
	 * 
	 * CO = se il capitolo ha titolo 1
	 * CA = se il capitolo ha titolo 2
	 * null = negli altri casi
	 * 
	 */
	private NaturaSpesaTipo getNaturaSpesa(CapitoloUscitaGestione cap) {
		final String methodName = "getNaturaSpesa";
		if (cap==null){
			return null;
		}
		Macroaggregato macroaggregato = capitoloUscitaGestioneDad.ricercaClassificatoreMacroaggregato(cap);
		cap.setMacroaggregato(macroaggregato);
		
		TitoloSpesa titoloSpesa = capitoloUscitaGestioneDad.ricercaClassificatoreTitoloSpesa(cap, macroaggregato);
		cap.setTitoloSpesa(titoloSpesa);
		
		String codiceTitoloSpesa = titoloSpesa!=null?titoloSpesa.getCodice():null;
		log.debug(methodName, "codice titoloSpesa ottenuto: "+ codiceTitoloSpesa);
		
		NaturaSpesaTipo result;
		if(codiceTitoloSpesa!= null && codiceTitoloSpesa.startsWith("1")){
			result =  NaturaSpesaTipo.CO;
		} else if (codiceTitoloSpesa!= null && codiceTitoloSpesa.startsWith("2")){
			result = NaturaSpesaTipo.CA;
		} else {
			log.error(methodName,  "Impossibile determinare la naturaSpesa per il capitolo con uid: "+cap.getUid() + " per titoloSpesa: "+codiceTitoloSpesa);
			result = null;
		}
		
		log.debug(methodName, "returning: " + result + " per titoloSpesa: "+codiceTitoloSpesa);
		return result;
	}

}
