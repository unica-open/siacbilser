/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompMacroTipoEnum;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siacfinser.integration.dao.movgest.ModificaMovimentoGestioneSpesaCollegataDao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.entitymapping.converter.base.ConvertersFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaFinModelDetail;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ModificaMovimentoGestioneSpesaCollegataDad extends ExtendedBaseFinDad {
	
	@Autowired
	private ModificaMovimentoGestioneSpesaCollegataDao modificaMovimentoGestioneSpesaCollegataDao;

	public List<ModificaMovimentoGestioneSpesaCollegata> ricercaModulareModificaMovimentoGestioneSpesaCollegata(Accertamento accertamento, 
			ModificaMovimentoGestioneEntrata modifica, boolean escludiModificheEntrataAnnullate, ModelDetailEnum[] modelDetails) {
		// 1- chiamata al Dao, ottengo List<SiacRMovgestTsDetModFin> a fronte di accertamento, ente, mod_id (facoltativa)
		List<SiacRMovgestTsDetModFin> siacRMovgestTsDetMods = modificaMovimentoGestioneSpesaCollegataDao
				.trovaModificaMovimentoGestioneCollegateAdAccertamento(
					accertamento.getUid(),
					modifica != null && modifica.getUid() != 0 ? modifica.getUid() : null,
							//SIAC-8609
							escludiModificheEntrataAnnullate
				);
		
		//chiamata a DOZER!! con il mapping "SiacRMovgestTsDetMod_ModiicaMovimentoGestioneSpesaCollegata_ModelDetail"
		return convertiLista(siacRMovgestTsDetMods, ModificaMovimentoGestioneSpesaCollegata.class, 
				FinMapId.ModificaMovimentoGestioneSpesaCollegata_SiacRMovgestTsDetModFin_ModelDetail, ConvertersFin.byModelDetails(modelDetails));
	}
	
	public List<ModificaMovimentoGestioneSpesa> caricaModificheMovimentoGestionePerDatiDefaultVincoloEsplicito(Integer uidAccertamento){
		List<SiacTModificaFin> siacTModificheFins = modificaMovimentoGestioneSpesaCollegataDao
				.caricaModificheMovimentoGestionePerDatiDefaultVincoloEsplicito(uidAccertamento);
		return convertiLista(siacTModificheFins, ModificaMovimentoGestioneSpesa.class, 
				FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa,
				ConvertersFin.byModelDetails(
					ModificaMovimentoGestioneSpesaFinModelDetail.Impegno,
					ModificaMovimentoGestioneSpesaFinModelDetail.DatiModificaImportoSpesa
				));
	}
	
	public List<ModificaMovimentoGestioneSpesa> caricaModificheMovimentoGestionePerDatiDefaultVincoloImplicito(Integer uidCapitolo, Integer uidEnteProprietario) {
		 List<SiacTModificaFin> siacTModificheFins = null;
		 siacTModificheFins = modificaMovimentoGestioneSpesaCollegataDao.caricaModificheMovimentoGestionePerDatiDefaultVincoloImplicito(uidCapitolo, 
				 Arrays.asList(SiacDBilElemDetCompMacroTipoEnum.FPV.getCodice(), SiacDBilElemDetCompMacroTipoEnum.AVANZO.getCodice()), 
				 uidEnteProprietario);
		return convertiLista(siacTModificheFins, ModificaMovimentoGestioneSpesa.class, 
				FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa,
				ConvertersFin.byModelDetails(
					ModificaMovimentoGestioneSpesaFinModelDetail.Impegno,
					ModificaMovimentoGestioneSpesaFinModelDetail.DatiModificaImportoSpesa
				));
	}

	public List<BigDecimal> caricaImportiModificaSpesaCollegataDefault(Integer uidModifica, Integer uidAccertamento,  boolean vincoloEsplicito) {
		//SIAC-8409
		return modificaMovimentoGestioneSpesaCollegataDao.caricaImportiModificaSpesaCollegataDefault(uidModifica,uidAccertamento);
	}

	public BigDecimal caricaImportoResiduoCollegare(int uid) {
		BigDecimal importoResiduo = modificaMovimentoGestioneSpesaCollegataDao.caricaImportoResiduoCollegare(uid);
		return importoResiduo != null ? importoResiduo : BigDecimal.ZERO;
	}

	public BigDecimal caricaImportoMassimoCollegabileDefault(Integer uidModifica, Integer uidAccertamento) {
		BigDecimal importoMaxCollegabile = modificaMovimentoGestioneSpesaCollegataDao.caricaImportoMassimoCollegabileDefault(uidModifica, uidAccertamento);
		return importoMaxCollegabile != null ? importoMaxCollegabile : BigDecimal.ZERO;
	}

	/**
	 * Restituisco l'importo in assenza di una corrispondenza con la SiacRMovgestTsDetModFin
	 * @param listSiacRMovgestTsDetModFin 
	 * @param <ModificaMovimentoGestioneSpesa> modificaMovimentoGestioneSpesa
	 * @return <BigDecimal> importoResiduoCollegare
	 */
//	private BigDecimal getImportoResiduoCollgare(List<SiacRMovgestTsDetModFin> listSiacRMovgestTsDetModFin, ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		//lascio questo codice come promemoria ma di fatto questo caso non si dovrebbe mai prendere
		//il minimo tra tutti gli importi residui in quanto viene sempre passato l'importo a ZERO 
////		BigDecimal importoResiduoCollegare = BigDecimal.ZERO;
////		if (importoResiduoCollegare.abs().compareTo(BigDecimal.ZERO) >0 ) {
////			//prendo il minimo tra tutti gli importi residui
////			if (importoResiduoCollegare.abs().compareTo(siacRMovgestTsDetModFin.getMovgestTsModImpoResiduo().abs()) > 0 ) {
////				importoResiduoCollegare = siacRMovgestTsDetModFin.getMovgestTsModImpoResiduo();
////			} 
////		}else {
////			importoResiduoCollegare = siacRMovgestTsDetModFin.getMovgestTsModImpoResiduo();
////		}
//		return CollectionUtils.isNotEmpty(listSiacRMovgestTsDetModFin) ? 
//				(listSiacRMovgestTsDetModFin.get(0).getMovgestTsModImpoResiduo().abs().compareTo(BigDecimal.ZERO) > 0 ?
//						listSiacRMovgestTsDetModFin.get(0).getMovgestTsModImpoResiduo().abs() : BigDecimal.ZERO) : modificaMovimentoGestioneSpesa.getImportoOld().abs();
//	}

	/**
	 * Controllo che l'importo del vincolo sia maggiore del residuo associato alla modifica di spesa collegata 
	 * altrimenti assumiamo l'importo residuo come importo massimo collegabile
	 * @param <SiacTMovgestTsDetModFin> siacTMovgestTsDetModFin
	 * @param <ModificaMovimentoGestioneSpesaCollegata> modificaDiSpesaCollegata
	 * @return <BigDecimal> importoMaxCollegabile
	 */
//	private BigDecimal getImportoMaxCollegabile(SiacTMovgestTsDetModFin siacTMovgestTsDetModFin,
//			ModificaMovimentoGestioneSpesaCollegata modificaDiSpesaCollegata) {
//		BigDecimal importoMaxCollegabile = getImportoMaxCollegabile(
//					modificaDiSpesaCollegata.getImportoResiduoCollegare(), 
//					siacTMovgestTsDetModFin.getSiacTMovgestT()
//						.getSiacRMovgestTsB().get(0).getMovgestTsImporto()
//				);
//		return importoMaxCollegabile != null ? importoMaxCollegabile : BigDecimal.ZERO;
//	}

//	private BigDecimal getImportoMaxCollegabile(BigDecimal ImportoResiduo, BigDecimal importoVincolo) {
//		return ImportoResiduo.compareTo(importoVincolo) <= 0 ? ImportoResiduo : importoVincolo;
//	}

}