/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_ambito database table.
 * 
 */
@Entity
@Table(name="siac_d_ambito")
public class SiacDAmbito extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ambito id. */
	@Id
	@SequenceGenerator(name="SIAC_D_AMBITO_AMBITOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_AMBITO_AMBITO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_AMBITO_AMBITOID_GENERATOR")
	@Column(name="ambito_id")
	private Integer ambitoId;

	/** The ambito code. */
	@Column(name="ambito_code")
	private String ambitoCode;

	/** The ambito desc. */
	@Column(name="ambito_desc")
	private String ambitoDesc;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggettos. */
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTSoggetto> siacTSoggettos;
	
	/** The siac t pdce contos. */
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTPdceConto> siacTPdceContos;
	
	/** The siac t pdce fam trees. */
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTPdceFamTree> siacTPdceFamTrees;
	
	/** The siac d pdce fams. */
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacDPdceFam> siacDPdceFams;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacDCespitiCategoria> siacDCespitiCategorias;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTCausaleEp> siacTCausaleEps;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTPrimaNotaProgressivogiornaleNum> siacTPrimaNotaProgressivogiornaleNums;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTMovEp> siacTMovEps;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTPrimaNotaNum> siacTPrimaNotaNums;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTRegMovfin> siacTRegMovfins;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTPrimaNota> siacTPrimaNotas;
	
	@OneToMany(mappedBy="siacDAmbito")
	private List<SiacTMovEpNum> siacTMovEpNums;

	/**
	 * Instantiates a new siac d ambito.
	 */
	public SiacDAmbito() {
	}

	/**
	 * Gets the ambito id.
	 *
	 * @return the ambito id
	 */
	public Integer getAmbitoId() {
		return this.ambitoId;
	}

	/**
	 * Sets the ambito id.
	 *
	 * @param ambitoId the new ambito id
	 */
	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	/**
	 * Gets the ambito code.
	 *
	 * @return the ambito code
	 */
	public String getAmbitoCode() {
		return this.ambitoCode;
	}

	/**
	 * Sets the ambito code.
	 *
	 * @param ambitoCode the new ambito code
	 */
	public void setAmbitoCode(String ambitoCode) {
		this.ambitoCode = ambitoCode;
	}

	/**
	 * Gets the ambito desc.
	 *
	 * @return the ambito desc
	 */
	public String getAmbitoDesc() {
		return this.ambitoDesc;
	}

	/**
	 * Sets the ambito desc.
	 *
	 * @param ambitoDesc the new ambito desc
	 */
	public void setAmbitoDesc(String ambitoDesc) {
		this.ambitoDesc = ambitoDesc;
	}

	/**
	 * Gets the siac t soggettos.
	 *
	 * @return the siac t soggettos
	 */
	public List<SiacTSoggetto> getSiacTSoggettos() {
		return this.siacTSoggettos;
	}

	/**
	 * Sets the siac t soggettos.
	 *
	 * @param siacTSoggettos the new siac t soggettos
	 */
	public void setSiacTSoggettos(List<SiacTSoggetto> siacTSoggettos) {
		this.siacTSoggettos = siacTSoggettos;
	}

	/**
	 * Adds the siac t soggetto.
	 *
	 * @param siacTSoggetto the siac t soggetto
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto addSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		getSiacTSoggettos().add(siacTSoggetto);
		siacTSoggetto.setSiacDAmbito(this);

		return siacTSoggetto;
	}

	/**
	 * Removes the siac t soggetto.
	 *
	 * @param siacTSoggetto the siac t soggetto
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto removeSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		getSiacTSoggettos().remove(siacTSoggetto);
		siacTSoggetto.setSiacDAmbito(null);

		return siacTSoggetto;
	}
	
	/**
	 * @return the siacTPdceContos
	 */
	public List<SiacTPdceConto> getSiacTPdceContos() {
		return siacTPdceContos;
	}

	/**
	 * @param siacTPdceContos the siacTPdceContos to set
	 */
	public void setSiacTPdceContos(List<SiacTPdceConto> siacTPdceContos) {
		this.siacTPdceContos = siacTPdceContos;
	}
	
	public SiacTPdceConto addSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().add(siacTPdceConto);
		siacTPdceConto.setSiacDAmbito(this);

		return siacTPdceConto;
	}
	
	public SiacTPdceConto removeSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().remove(siacTPdceConto);
		siacTPdceConto.setSiacDAmbito(null);

		return siacTPdceConto;
	}

	/**
	 * @return the siacTPdceFamTrees
	 */
	public List<SiacTPdceFamTree> getSiacTPdceFamTrees() {
		return siacTPdceFamTrees;
	}

	/**
	 * @param siacTPdceFamTrees the siacTPdceFamTrees to set
	 */
	public void setSiacTPdceFamTrees(List<SiacTPdceFamTree> siacTPdceFamTrees) {
		this.siacTPdceFamTrees = siacTPdceFamTrees;
	}
	
	public SiacTPdceFamTree addSiacTPdceFamTree(SiacTPdceFamTree siacTPdceFamTree) {
		getSiacTPdceFamTrees().add(siacTPdceFamTree);
		siacTPdceFamTree.setSiacDAmbito(this);

		return siacTPdceFamTree;
	}
	
	public SiacTPdceFamTree removeSiacTPdceFamTree(SiacTPdceFamTree siacTPdceFamTree) {
		getSiacTPdceFamTrees().remove(siacTPdceFamTree);
		siacTPdceFamTree.setSiacDAmbito(null);

		return siacTPdceFamTree;
	}

	/**
	 * @return the siacDPdceFams
	 */
	public List<SiacDPdceFam> getSiacDPdceFams() {
		return siacDPdceFams;
	}

	/**
	 * @param siacDPdceFams the siacDPdceFams to set
	 */
	public void setSiacDPdceFams(List<SiacDPdceFam> siacDPdceFams) {
		this.siacDPdceFams = siacDPdceFams;
	}
	
	public SiacDPdceFam addSiacDPdceFam(SiacDPdceFam siacDPdceFam) {
		getSiacDPdceFams().add(siacDPdceFam);
		siacDPdceFam.setSiacDAmbito(this);

		return siacDPdceFam;
	}
	
	public SiacDPdceFam removeSiacDPdceFam(SiacDPdceFam siacDPdceFam) {
		getSiacDPdceFams().remove(siacDPdceFam);
		siacDPdceFam.setSiacDAmbito(null);

		return siacDPdceFam;
	}
	
	public List<SiacDCespitiCategoria> getSiacDCespitiCategorias() {
		return siacDCespitiCategorias;
	}

	public void setSiacDCespitiCategorias(List<SiacDCespitiCategoria> siacDCespitiCategorias) {
		this.siacDCespitiCategorias = siacDCespitiCategorias;
	}
	
	public SiacDCespitiCategoria addSiacDCespitiCategoria(SiacDCespitiCategoria siacDCespitiCategoria) {
		getSiacDCespitiCategorias().add(siacDCespitiCategoria);
		siacDCespitiCategoria.setSiacDAmbito(this);

		return siacDCespitiCategoria;
	}
	
	public SiacDCespitiCategoria removeSiacDCespitiCategoria(SiacDCespitiCategoria siacDCespitiCategoria) {
		getSiacDCespitiCategorias().remove(siacDCespitiCategoria);
		siacDCespitiCategoria.setSiacDAmbito(null);

		return siacDCespitiCategoria;
	}
	
	public List<SiacTCausaleEp> getSiacTCausaleEps() {
		return siacTCausaleEps;
	}

	public void setSiacTCausaleEps(List<SiacTCausaleEp> siacTCausaleEps) {
		this.siacTCausaleEps = siacTCausaleEps;
	}
	
	public SiacTCausaleEp addSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		getSiacTCausaleEps().add(siacTCausaleEp);
		siacTCausaleEp.setSiacDAmbito(this);

		return siacTCausaleEp;
	}
	
	public SiacTCausaleEp removeSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		getSiacTCausaleEps().remove(siacTCausaleEp);
		siacTCausaleEp.setSiacDAmbito(null);

		return siacTCausaleEp;
	}
	
	public List<SiacTPrimaNotaProgressivogiornaleNum> getSiacTPrimaNotaProgressivogiornaleNums() {
		return siacTPrimaNotaProgressivogiornaleNums;
	}

	public void setSiacTPrimaNotaProgressivogiornaleNums(List<SiacTPrimaNotaProgressivogiornaleNum> siacTPrimaNotaProgressivogiornaleNums) {
		this.siacTPrimaNotaProgressivogiornaleNums = siacTPrimaNotaProgressivogiornaleNums;
	}
	
	public SiacTPrimaNotaProgressivogiornaleNum addSiacTPrimaNotaProgressivogiornaleNum(SiacTPrimaNotaProgressivogiornaleNum siacTPrimaNotaProgressivogiornaleNum) {
		getSiacTPrimaNotaProgressivogiornaleNums().add(siacTPrimaNotaProgressivogiornaleNum);
		siacTPrimaNotaProgressivogiornaleNum.setSiacDAmbito(this);

		return siacTPrimaNotaProgressivogiornaleNum;
	}
	
	public SiacTPrimaNotaProgressivogiornaleNum removeSiacTPrimaNotaProgressivogiornaleNum(SiacTPrimaNotaProgressivogiornaleNum siacTPrimaNotaProgressivogiornaleNum) {
		getSiacTPrimaNotaProgressivogiornaleNums().remove(siacTPrimaNotaProgressivogiornaleNum);
		siacTPrimaNotaProgressivogiornaleNum.setSiacDAmbito(null);

		return siacTPrimaNotaProgressivogiornaleNum;
	}
	
	/**
	 * @return the siacTMovEps
	 */
	public List<SiacTMovEp> getSiacTMovEps() {
		return siacTMovEps;
	}

	/**
	 * @param siacTMovEps the siacTMovEps to set
	 */
	public void setSiacTMovEps(List<SiacTMovEp> siacTMovEps) {
		this.siacTMovEps = siacTMovEps;
	}
	
	public SiacTMovEp addSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().add(siacTMovEp);
		siacTMovEp.setSiacDAmbito(this);

		return siacTMovEp;
	}
	
	public SiacTMovEp removeSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().remove(siacTMovEp);
		siacTMovEp.setSiacDAmbito(null);

		return siacTMovEp;
	}
	
	public List<SiacTPrimaNotaNum> getSiacTPrimaNotaNums() {
		return siacTPrimaNotaNums;
	}

	public void setSiacTPrimaNotaNums(List<SiacTPrimaNotaNum> siacTPrimaNotaNums) {
		this.siacTPrimaNotaNums = siacTPrimaNotaNums;
	}
	
	public SiacTPrimaNotaNum addSiacTPrimaNotaNum(SiacTPrimaNotaNum siacTPrimaNotaNum) {
		getSiacTPrimaNotaNums().add(siacTPrimaNotaNum);
		siacTPrimaNotaNum.setSiacDAmbito(this);

		return siacTPrimaNotaNum;
	}
	
	public SiacTPrimaNotaNum removeSiacTPrimaNotaNum(SiacTPrimaNotaNum siacTPrimaNotaNum) {
		getSiacTPrimaNotaNums().remove(siacTPrimaNotaNum);
		siacTPrimaNotaNum.setSiacDAmbito(null);

		return siacTPrimaNotaNum;
	}
	
	public List<SiacTRegMovfin> getSiacTRegMovfins() {
		return siacTRegMovfins;
	}

	public void setSiacTRegMovfins(List<SiacTRegMovfin> siacTRegMovfins) {
		this.siacTRegMovfins = siacTRegMovfins;
	}
	
	public SiacTRegMovfin addSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		getSiacTRegMovfins().add(siacTRegMovfin);
		siacTRegMovfin.setSiacDAmbito(this);

		return siacTRegMovfin;
	}
	
	public SiacTRegMovfin removeSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		getSiacTRegMovfins().remove(siacTRegMovfin);
		siacTRegMovfin.setSiacDAmbito(null);

		return siacTRegMovfin;
	}
	
	public List<SiacTPrimaNota> getSiacTPrimaNotas() {
		return siacTPrimaNotas;
	}

	public void setSiacTPrimaNotas(List<SiacTPrimaNota> siacTPrimaNotas) {
		this.siacTPrimaNotas = siacTPrimaNotas;
	}
	
	public SiacTPrimaNota addSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		getSiacTPrimaNotas().add(siacTPrimaNota);
		siacTPrimaNota.setSiacDAmbito(this);

		return siacTPrimaNota;
	}
	
	public SiacTPrimaNota removeSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		getSiacTPrimaNotas().remove(siacTPrimaNota);
		siacTPrimaNota.setSiacDAmbito(null);

		return siacTPrimaNota;
	}
	
	public List<SiacTMovEpNum> getSiacTMovEpNums() {
		return siacTMovEpNums;
	}

	public void setSiacTMovEpNums(List<SiacTMovEpNum> siacTMovEpNums) {
		this.siacTMovEpNums = siacTMovEpNums;
	}
	
	public SiacTMovEpNum addSiacTMovEpNum(SiacTMovEpNum siacTMovEpNum) {
		getSiacTMovEpNums().add(siacTMovEpNum);
		siacTMovEpNum.setSiacDAmbito(this);

		return siacTMovEpNum;
	}
	
	public SiacTMovEpNum removeSiacTMovEpNum(SiacTMovEpNum siacTMovEpNum) {
		getSiacTMovEpNums().remove(siacTMovEpNum);
		siacTMovEpNum.setSiacDAmbito(null);

		return siacTMovEpNum;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ambitoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ambitoId = uid;
		
	}

}