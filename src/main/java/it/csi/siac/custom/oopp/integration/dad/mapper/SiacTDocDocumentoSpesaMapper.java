/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTAttrHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTDocHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTSubdocHelper;
import it.csi.siac.siacbilser.model.TipoOnereEnum;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.collections.Predicate;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siaccommon.util.number.BigDecimalUtil;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacintegser.model.custom.oopp.DocumentoSpesa;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTDocDocumentoSpesaMapper extends BaseMapper<SiacTDoc, DocumentoSpesa> {

	@Autowired private SiacTSoggettoSoggettoIntegMapper siacTSoggettoSoggettoMapper;
	@Autowired private SiacTOrdineOrdineMapper siacTOrdineOrdineMapper;
	@Autowired private SiacRDocAttrDocumentoSpesaMapper siacRDocAttrDocumentoSpesaMapper;
	@Autowired private SiacDDocStatoStatoMapper siacDDocStatoStatoMapper;
	@Autowired private SiacTOrdinativoMandatoMapper siacTOrdinativoMandatoMapper;

	@Autowired private SiacTDocHelper siacTDocHelper;
	@Autowired private SiacTSubdocHelper siacTSubdocHelper;
	@Autowired private SiacTAttrHelper siacTAttrHelper;
	
	
	@Override
	public void map(SiacTDoc o1, DocumentoSpesa o2) {
		o2.setAnno(o1.getDocAnno());
		o2.setNumero(o1.getDocNumero());
		o2.setStato(siacDDocStatoStatoMapper.map(siacTDocHelper.getSiacDDocStato(o1)));
		o2.setDescrizione(o1.getDocDesc());
		o2.setTipo(o1.getSiacDDocTipo().getDocTipoDesc());
		o2.setImporto(o1.getDocImporto());
		siacRDocAttrDocumentoSpesaMapper.map(siacTDocHelper.getSiacRDocAttrList(o1), o2);
		o2.setImportoNetto(getImportoNetto(o1, o2));
		o2.setSoggetto(siacTSoggettoSoggettoMapper.map(siacTDocHelper.getSiacTSoggetto(o1)));
		o2.setElencoOrdini(siacTOrdineOrdineMapper.map(siacTDocHelper.getSiacTOrdineList(o1)));
		o2.setDataEmissione(o1.getDataCreazione());
		o2.setElencoMandati(siacTOrdinativoMandatoMapper.map(siacTDocHelper.getMandatiNonAnnullati(o1)));
		o2.setElencoCig(getElencoCig(o1));
	}


	private List<String> getElencoCig(SiacTDoc siacTDoc) {

		List<SiacRSubdocAttr> siacRSubdocAttrList = new ArrayList<SiacRSubdocAttr>();

		CollectionUtil.apply(siacTDocHelper.getSiacTSubdocList(siacTDoc), new Predicate<SiacTSubdoc>() {
			@Override
			public void apply(SiacTSubdoc source) {
				CollectionUtil.addAll(siacRSubdocAttrList, siacTSubdocHelper.getSiacRSubdocAttrList(source));	
			}
		});
		
		return 
			CollectionUtil.toList(
				CollectionUtil.distinct(
					CollectionUtil.map(
						CollectionUtil.filter(siacRSubdocAttrList, new Filter<SiacRSubdocAttr> () {
							@Override
							public boolean isAcceptable(SiacRSubdocAttr source) {
								return siacTAttrHelper.checkAttrCode(source.getSiacTAttr(), SiacTAttrEnum.Cig);
							}
						}),
						new Function<SiacRSubdocAttr, String>() {
							@Override
							public String map(SiacRSubdocAttr source) {
								return source.getTesto();
							}
						}
					),
					new Function<String, String> () {
						@Override
						public String map(String source) {
							return source;
						}}
				)
			);
	}


	private BigDecimal getImportoNetto(SiacTDoc siacTDoc, DocumentoSpesa o2) {
		return 
			BigDecimalUtil.sum(					
				BigDecimalUtil.getDefaultZero(o2.getImporto()), 
				BigDecimalUtil.getDefaultZero(o2.getArrotondamento()),
				BigDecimalUtil.getDefaultZero(getImportoTotaleOneri(siacTDoc)),
				BigDecimalUtil.neg(BigDecimalUtil.getDefaultZero(getNoteDiCredito(siacTDoc)))
			); 
	}

	private BigDecimal getImportoTotaleOneri(SiacTDoc siacTDoc) {
		return 
			BigDecimalUtil.sum(	
				CollectionUtil.map(
					CollectionUtil.filter(siacTDoc.getSiacRDocOneres(), 
						new Filter<SiacRDocOnere>() {
							@Override
							public boolean isAcceptable(SiacRDocOnere source) {
								return 
									EntityUtil.isValid(source) && 
									TipoIvaSplitReverse.REVERSE_CHANGE.getCodice().equals(source.getSiacDOnere().getOnereCode()) &&
									TipoOnereEnum.SP.name().equals(source.getSiacDOnere().getSiacDOnereTipo().getOnereTipoCode());
							}
						}
					), 
					new Function<SiacRDocOnere, BigDecimal>() {
						@Override
						public BigDecimal map(SiacRDocOnere source) {
							return BigDecimalUtil.getDefaultZero(source.getImportoCaricoSoggetto());
						}
					}
				)
			);
	}

	private BigDecimal getNoteDiCredito(SiacTDoc siacTDoc) {
		return 
			BigDecimalUtil.sum(
				CollectionUtil.map(
					EntityUtil.getAllValid(siacTDoc.getSiacTSubdocs()), 
					new Function<SiacTSubdoc, BigDecimal>() {
						@Override
						public BigDecimal map(SiacTSubdoc source) {
							return BigDecimalUtil.getDefaultZero(source.getSubdocImportoDaDedurre());
						}
					}
				)
			);
	}
}

