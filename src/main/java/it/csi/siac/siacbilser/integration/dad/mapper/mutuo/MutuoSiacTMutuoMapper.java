/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.attoamministrativo.AttoAmministrativoSiacTAttoAmmMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.contotesoreria.ContotesoreriaSiacDContoTesoreriaMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.ente.EnteSiacTEnteProprietarioMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.soggetto.SoggettoSiacTSoggettoMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommonser.util.mapper.EntitaExtSiacTBaseExtMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MutuoSiacTMutuoMapper extends EntitaExtSiacTBaseExtMapper<Mutuo, SiacTMutuo> {
	
	@Autowired private EnteSiacTEnteProprietarioMapper enteSiacTEnteProprietarioMapper;
	@Autowired private StatoMutuoSiacDMutuoStatoMapper statoMutuoSiacDMutuoStatoMapper;
	@Autowired private PeriodoRimborsoMutuoSiacDMutuoPeriodoRimborsoMapper periodoRimborsoMutuoSiacDMutuoPeriodoRimborsoMapper;
	@Autowired private TipoTassoMutuoSiacDMutuoTipoTassoMapper tipoTassoMutuoSiacDMutuoTipoTassoMapper;
	@Autowired private ContotesoreriaSiacDContoTesoreriaMapper contotesoreriaSiacDContoTesoreriaMapper;
	@Autowired private AttoAmministrativoSiacTAttoAmmMapper attoAmministrativoSiacTAttoAmmMapper;
	@Autowired private SoggettoSiacTSoggettoMapper soggettoSiacTSoggettoMapper;
	
/*
	private List<VariazioneMutuo> elencoVariazioni;
	private List<PianoAmmortamentoMutuo> elencoPianiAmmortamento;

 */
	
	@Override
	public void map(Mutuo s, SiacTMutuo d) {
		super.map(s, d);
		d.setMutuoNumero(s.getNumero());
		d.setMutuoOggetto(s.getOggetto());
		d.setMutuoDataAtto(s.getDataAtto());
		d.setMutuoSommaIniziale(s.getSommaMutuataIniziale());
		d.setMutuoSommaEffettiva(s.getSommaMutuataEffettiva());
		d.setMutuoTasso(s.getTassoInteresse());
		d.setMutuoTassoEuribor(s.getTassoInteresseEuribor());
		d.setMutuoTassoSpread(s.getTassoInteresseSpread());
		d.setMutuoPreAmmortamento(s.getPreammortamento());
		d.setMutuoDurataAnni(s.getDurataAnni());
		d.setMutuoAnnoInizio(s.getAnnoInizio());
		d.setMutuoAnnoFine(s.getAnnoFine());
		d.setMutuoDataScadenzaPrimaRata(s.getScadenzaPrimaRata());
		d.setMutuoAnnualita(s.getAnnualita());
		
		d.setSiacTEnteProprietario(enteSiacTEnteProprietarioMapper.map(s.getEnte()));
		d.setSiacDMutuoStato(statoMutuoSiacDMutuoStatoMapper.map(s.getStatoMutuo(), s.getEnte().getUid()));
		d.setSiacDMutuoPeriodoRimborso(periodoRimborsoMutuoSiacDMutuoPeriodoRimborsoMapper.map(s.getPeriodoRimborso()));
		d.setSiacDMutuoTipoTasso(tipoTassoMutuoSiacDMutuoTipoTassoMapper.map(s.getTipoTasso(), s.getEnte().getUid()));
		d.setSiacDContotesoreria(contotesoreriaSiacDContoTesoreriaMapper.map(s.getContoTesoreria()));
		d.setSiacTAttoAmm(attoAmministrativoSiacTAttoAmmMapper.map(s.getAttoAmministrativo()));
		d.setSiacTSoggetto(soggettoSiacTSoggettoMapper.map(s.getSoggetto()));
	}
}
