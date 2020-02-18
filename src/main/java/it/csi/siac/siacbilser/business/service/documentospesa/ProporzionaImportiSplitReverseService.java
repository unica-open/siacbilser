/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverseResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;

/**
 * Attiva le registrazioni contabili (GEN e PCC) impostando il FlagContabilizzaGenPcc=TRUE sul documento di spesa.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProporzionaImportiSplitReverseService extends CheckedAccountBaseService<ProporzionaImportiSplitReverse, ProporzionaImportiSplitReverseResponse> {
	
	private static final BigDecimal CENTO = BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
	
	@Autowired
	private OnereSpesaDad onereSpesaDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	
	private DocumentoSpesa doc;
	private DettaglioOnere dettaglioOnere;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoSpesa(), "documento spesa");
		doc = req.getDocumentoSpesa();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(ente);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public ProporzionaImportiSplitReverseResponse executeService(ProporzionaImportiSplitReverse serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<DettaglioOnere> listaOneri = onereSpesaDad.findOneryByIdDocumento(doc.getUid());
		determinaTipoOnere(listaOneri);
		List<SubdocumentoSpesa> quote = subdocumentoSpesaDad.findSubdocumentiSpesaBaseByIdDocumento(doc.getUid());
		DocumentoSpesa documento =  documentoSpesaDad.findDocumentoSpesaByIdModelDetail(doc.getUid(),new DocumentoSpesaModelDetail[]{});
		BigDecimal importoDoc = documento.getImporto();
		int indexQuota = 1;
		BigDecimal totaleSplitGiaAssegnato = BigDecimal.ZERO;
		for(SubdocumentoSpesa subdoc: quote){
			subdocumentoSpesaDad.aggiornaTipoSplitQuota(subdoc,dettaglioOnere.getTipoOnere().getTipoIvaSplitReverse());
			BigDecimal importoSplitQuota = determinaImportoSplitQuota(subdoc.getImportoNotNull(), indexQuota == quote.size(), totaleSplitGiaAssegnato, importoDoc);
			subdocumentoSpesaDad.aggiornaImportoSplitQuota(subdoc, importoSplitQuota);
			subdoc.setImportoSplitReverse(importoSplitQuota);
			totaleSplitGiaAssegnato = totaleSplitGiaAssegnato.add(importoSplitQuota);
			indexQuota++;
		}
		
		doc.setListaSubdocumenti(quote);
		res.setDocumentoSpesa(doc);
		
	}


	/**
	 * Determina la ripartizione dell'iva sulle singole quote secondo questa regola:
	 * <br>                            
	 *                            
	 * CoefficienteDiriporto = importoACaricoSoggetto /  importo documento    
	 * <br>           (33/200)*40             
	 *            x= (Importo a carico soggetto/Importo) * importoQuota  
	 *             <br>              
	 *  ImportoSplit = importoQuota * coefficiente di riporto
	 *  <br>
	 *  Nel caso in cui ci siano pi&ugrave; oneri, l'importo a carico del soggetto deve essere  la somma degli importi a carico del soggetto
	 *  
	 *  <br>
	 *   L'ultima quota deve essere invece pari a importoACaricoDelsoggetto - totaleSplitgi&agrave; assegnato.
	 * 
	 * @param importoQuota
	 * @param isUltimaQuota
	 * @param totaleSplitGiaAssegnato
	 * @return
	 */
	private BigDecimal determinaImportoSplitQuota(BigDecimal importoQuota, boolean isUltimaQuota,  BigDecimal totaleSplitGiaAssegnato, BigDecimal importoDoc) {
		String methodName = "determinaImportoSplitQuota";
		
		BigDecimal totaleSplitAncoraDaAsseganere = dettaglioOnere.getImportoCaricoSoggetto().subtract(totaleSplitGiaAssegnato);
		
		if(totaleSplitGiaAssegnato.compareTo(dettaglioOnere.getImportoCaricoSoggetto()) >= 0){
			log.debug(methodName, "l'importo dell'onere e' gia' stato coperto dalle quote precerdenti, restituisco ZERO");
			return BigDecimal.ZERO;
		}
		
		if(isUltimaQuota){
			log.debug(methodName, "sto lavorando sull'ulitma quota, restituuisco il totaleSplitAncoraDaAsseganere: " + totaleSplitAncoraDaAsseganere);
			return totaleSplitAncoraDaAsseganere;
		}
		
		BigDecimal multiplicand = dettaglioOnere.getImportoCaricoSoggetto().divide(importoDoc, MathContext.DECIMAL128);
		BigDecimal result = importoQuota.multiply(multiplicand).setScale(2, RoundingMode.HALF_DOWN);
		
		if(result.compareTo(totaleSplitAncoraDaAsseganere) >= 0){
			log.debug(methodName, "result: " + totaleSplitAncoraDaAsseganere);
			return totaleSplitAncoraDaAsseganere;
		}else{
			log.debug(methodName, "result: " + result);
			return result;
		}
		
	}

	private void determinaTipoOnere(List<DettaglioOnere> listaOneri) {
		String methodName = "determinaTipoOnere" ;
		int oneriSplit = 0;
		for(DettaglioOnere onere : listaOneri){
			NaturaOnere natura = onere.getTipoOnere().getNaturaOnere();
			TipoIvaSplitReverse tipo = onere.getTipoOnere().getTipoIvaSplitReverse();
			log.debug(methodName, "natura spliReverse? " + natura.isSplitReverse());
			log.debug(methodName, "tipo: " + tipo);
			if(natura.isSplitReverse() && (tipo.equals(TipoIvaSplitReverse.SPLIT_COMMERCIALE) || tipo.equals(TipoIvaSplitReverse.SPLIT_ISTITUZIONALE))){
				dettaglioOnere = onere;
				oneriSplit++;
			}
		}
		log.debug(methodName, "numero di oneri compatibili trovati: " + oneriSplit);
		if(oneriSplit != 1){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("condizioni per il calcolo degli importi iva non soddisfatte"));
		}
	}
		
	
	/*
	 * Se un documento ha associato un'unico onere di natura SPLIT-REVERSE e con TipoSplitReverse di tipo SPLIT (o istituzionale oppure commerciale) ed è possibile effettuare  l'aggiornamento di tutte le sue quote (vedasi condizioni par 2.12.7)  effettuare queste operazioni sulle quote:
		- aggiornare tutte le quote del documento mettendo l'importo split sulla singola quota = (importo quota-(importo quota*100/(100+aliquota dell'unico split associato a livello di documento));
		- aggiornare tutte le quote del documento con il TipoSplitReverse = al TipoSplitReverse dell’unico onere del documento;
		- sull'ultima quota se necessario dovrà essere fatto l'arrotondamento e quindi portare il totale degli importi split di tutte le quote = importo dell'imposta dell'onere di tipo split presente a livello di documento.
	 * 
	 */
}
