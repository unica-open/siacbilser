/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc.datioperazione;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPccDebitoStatoEnum;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.tesoro.fatture.ContabilizzazioneTipo;
import it.tesoro.fatture.ListaContabilizzazioneTipo;
import it.tesoro.fatture.NaturaSpesaContabiliTipo;
import it.tesoro.fatture.OperazioneContabilizzazioneTipo;
import it.tesoro.fatture.StatoDebitoTipo;
import it.tesoro.fatture.StrutturaDatiOperazioneTipo;

/**
 * Popola la struttura dati operazione per l'operazione CO: Contabilizzazione
 * 
 *  @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaStrutturaDatiOperazioneCO extends PopolaStrutturaDatiOperazioneCOCPBase {

	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;

	public PopolaStrutturaDatiOperazioneCO(RegistroComunicazioniPCC registrazione) {
		super(registrazione);
	}

	
	@Override
	public StrutturaDatiOperazioneTipo popolaStrutturaDatiOperazione() {
		SubdocumentoSpesa subdocumentoSpesa = registrazione.getSubdocumentoSpesa();
		Impegno impegno = subdocumentoSpesa.getImpegno();
		CapitoloUscitaGestione capitolo = impegno!=null?impegno.getCapitoloUscitaGestione():null;
		
		
		StrutturaDatiOperazioneTipo strutturaDatiOperazione = new StrutturaDatiOperazioneTipo();
		
		
		ListaContabilizzazioneTipo listaContabilizzazione = new ListaContabilizzazioneTipo();
		ContabilizzazioneTipo contabilizzazione = new ContabilizzazioneTipo();
		
		BigDecimal importoImponibile = subdocumentoSpesa.getImportoNotNull().subtract(subdocumentoSpesa.getImportoSplitReverseNotNull()).setScale(2, RoundingMode.HALF_UP);
		contabilizzazione.setImportoMovimento(importoImponibile);
		
		
		contabilizzazione.setCapitoliSpesa(StringUtils.abbreviate(getDescrizioneCapitolo(capitolo), 100));
		
		contabilizzazione.setDescrizione(StringUtils.abbreviate(subdocumentoSpesa.getDescrizione(), 100));
		
		contabilizzazione.setEstremiImpegno(StringUtils.abbreviate(getEstremiImpegno(impegno),50));
		
		OperazioneContabilizzazioneTipo operazione = new OperazioneContabilizzazioneTipo();
		
		//stato debito e' obbligatorio
		if(registrazione.getStatoDebito() == null || StringUtils.isBlank(registrazione.getStatoDebito().getCodice())){
			throw new BusinessException("La Causale PCC per l'operazione Contabilizzazione (CO) deve essere valorizzata. RegistroComunicazioniPCC con uid: "+registrazione.getUid());
		}
		
		StatoDebito.Value statoDebitoValue = SiacDPccDebitoStatoEnum.byCodice(registrazione.getStatoDebito().getCodice()).getStatoDebitoValue();
		statoDebitoValue.getStatoDebitoTipo();
		operazione.setStatoDebito(statoDebitoValue.getStatoDebitoTipo());
		
		contabilizzazione.setNaturaSpesa(getNaturaSpesa(capitolo, operazione.getStatoDebito()));
		
		contabilizzazione.setCodiceCIG(getCIG(subdocumentoSpesa, operazione.getStatoDebito()));
		contabilizzazione.setCodiceCUP(getCUP(subdocumentoSpesa, operazione.getStatoDebito()));
		
		
		//CausalePCC e' facoltativo
		if(registrazione.getCausalePCC()!=null && StringUtils.isNotBlank(registrazione.getCausalePCC().getCodice())){
			operazione.setCausale(registrazione.getCausalePCC().getCodice());
		}
		
		contabilizzazione.setOperazione(operazione);
		
		listaContabilizzazione.getContabilizzazione().add(contabilizzazione);
		
		strutturaDatiOperazione.setListaContabilizzazione(listaContabilizzazione);
		
		return strutturaDatiOperazione;
	}


	/**
	 * Ottine la natura spesa a partire dal capitolo:
	 * Se lo Stato del debito è uno dei seguenti: LIQ, LIQdaSOSP, LIQdaNL, SOSPdaLIQ e NLdaLIQ specificare:
	 * <ul>
	 * <li>CO = se il capitolo ha titolo 1</li>
	 * <li>CA = se il capitolo ha titolo 2</li>
	 * </ul>
	 * NA = negli altri casi.
	 * 
	 * @param statoDebitoTipo 
	 * 
	 */
	private NaturaSpesaContabiliTipo getNaturaSpesa(CapitoloUscitaGestione cap, StatoDebitoTipo statoDebitoTipo) {
		final String methodName = "getNaturaSpesa";
		
		if(cap==null){
			return NaturaSpesaContabiliTipo.NA;
		}
		
		if(!StatoDebitoTipo.LIQ.equals(statoDebitoTipo)){
			//Risposta di PCC: E255-Per gli stati diversi da LIQ impostare il campo natura spesa a NA
			return NaturaSpesaContabiliTipo.NA;
		}
		
		//Occhio sarebbe formalmente piu' corretto questo:
//		if(!EnumSet.of(StatoDebito.Value.ImportoLiquidato, //LIQ
//				StatoDebito.Value.CambioStatoDaSospesoALiquidato, //LIQdaSOSP
//				StatoDebito.Value.CambioStatoDaNonLiquidabileALiquidato, //LIQdaNL
//				StatoDebito.Value.CambioStatoDaLiquidatoASospeso, //SOSPdaLIQ
//				StatoDebito.Value.CambioStatoDaLiquidatoANonLiquidabile  //NLdaLIQ
//				).contains(statoDebitoTipo)){
//			return NaturaSpesaContabiliTipo.NA;
//		}
		
		Macroaggregato macroaggregato = capitoloUscitaGestioneDad.ricercaClassificatoreMacroaggregato(cap);
		cap.setMacroaggregato(macroaggregato);
		
		TitoloSpesa titoloSpesa = capitoloUscitaGestioneDad.ricercaClassificatoreTitoloSpesa(cap, macroaggregato);
		cap.setTitoloSpesa(titoloSpesa);
		
		String codiceTitoloSpesa = titoloSpesa!=null?titoloSpesa.getCodice():null;
		log.debug(methodName, "codice titoloSpesa ottenuto: "+ codiceTitoloSpesa);
		
		NaturaSpesaContabiliTipo result; 
		if(codiceTitoloSpesa!= null && codiceTitoloSpesa.startsWith("1")){
			result =  NaturaSpesaContabiliTipo.CO;
		} else if (codiceTitoloSpesa!= null && codiceTitoloSpesa.startsWith("2")){
			result = NaturaSpesaContabiliTipo.CA;
		} else {
			result = NaturaSpesaContabiliTipo.NA;
		}
		
		log.debug(methodName, "returning: " + result + " per titoloSpesa: "+codiceTitoloSpesa);
		return result;
	}
	
}
