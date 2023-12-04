/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_reg_movfin database table.
 * 
 */
@Entity
@Table(name="siac_t_reg_movfin")
@NamedQuery(name="SiacTRegMovfin.findAll", query="SELECT s FROM SiacTRegMovfin s")
public class SiacTRegMovfin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_REG_MOVFIN_REGMOVFINID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_REG_MOVFIN_REGMOVFIN_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REG_MOVFIN_REGMOVFINID_GENERATOR")
	@Column(name="regmovfin_id")
	private Integer regmovfinId;

	//bi-directional many-to-one association to SiacREventoRegMovfin
	@OneToMany(mappedBy="siacTRegMovfin", cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacREventoRegMovfin> siacREventoRegMovfins;

	//bi-directional many-to-one association to SiacRRegMovfinStato
	@OneToMany(mappedBy="siacTRegMovfin", cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRRegMovfinStato> siacRRegMovfinStatos;

	//bi-directional many-to-one association to SiacTMovEp
	@OneToMany(mappedBy="siacTRegMovfin", cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTMovEp> siacTMovEps;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id_aggiornato")
	private SiacTClass siacTClass1;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id_iniziale")
	private SiacTClass siacTClass2;
	
	// bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

	public SiacTRegMovfin() {
	}

	public Integer getRegmovfinId() {
		return this.regmovfinId;
	}

	public void setRegmovfinId(Integer regmovfinId) {
		this.regmovfinId = regmovfinId;
	}

	public List<SiacREventoRegMovfin> getSiacREventoRegMovfins() {
		return this.siacREventoRegMovfins;
	}

	public void setSiacREventoRegMovfins(List<SiacREventoRegMovfin> siacREventoRegMovfins) {
		this.siacREventoRegMovfins = siacREventoRegMovfins;
	}

	public SiacREventoRegMovfin addSiacREventoRegMovfin(SiacREventoRegMovfin siacREventoRegMovfin) {
		getSiacREventoRegMovfins().add(siacREventoRegMovfin);
		siacREventoRegMovfin.setSiacTRegMovfin(this);

		return siacREventoRegMovfin;
	}

	public SiacREventoRegMovfin removeSiacREventoRegMovfin(SiacREventoRegMovfin siacREventoRegMovfin) {
		getSiacREventoRegMovfins().remove(siacREventoRegMovfin);
		siacREventoRegMovfin.setSiacTRegMovfin(null);

		return siacREventoRegMovfin;
	}

	public List<SiacRRegMovfinStato> getSiacRRegMovfinStatos() {
		return this.siacRRegMovfinStatos;
	}

	public void setSiacRRegMovfinStatos(List<SiacRRegMovfinStato> siacRRegMovfinStatos) {
		this.siacRRegMovfinStatos = siacRRegMovfinStatos;
	}

	public SiacRRegMovfinStato addSiacRRegMovfinStato(SiacRRegMovfinStato siacRRegMovfinStato) {
		getSiacRRegMovfinStatos().add(siacRRegMovfinStato);
		siacRRegMovfinStato.setSiacTRegMovfin(this);

		return siacRRegMovfinStato;
	}

	public SiacRRegMovfinStato removeSiacRRegMovfinStato(SiacRRegMovfinStato siacRRegMovfinStato) {
		getSiacRRegMovfinStatos().remove(siacRRegMovfinStato);
		siacRRegMovfinStato.setSiacTRegMovfin(null);

		return siacRRegMovfinStato;
	}

	public List<SiacTMovEp> getSiacTMovEps() {
		return this.siacTMovEps;
	}

	public void setSiacTMovEps(List<SiacTMovEp> siacTMovEps) {
		this.siacTMovEps = siacTMovEps;
	}

	public SiacTMovEp addSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().add(siacTMovEp);
		siacTMovEp.setSiacTRegMovfin(this);

		return siacTMovEp;
	}

	public SiacTMovEp removeSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().remove(siacTMovEp);
		siacTMovEp.setSiacTRegMovfin(null);

		return siacTMovEp;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	public SiacTClass getSiacTClass1() {
		return this.siacTClass1;
	}

	public void setSiacTClass1(SiacTClass siacTClass1) {
		this.siacTClass1 = siacTClass1;
	}

	public SiacTClass getSiacTClass2() {
		return this.siacTClass2;
	}

	public void setSiacTClass2(SiacTClass siacTClass2) {
		this.siacTClass2 = siacTClass2;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.regmovfinId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.regmovfinId = uid;
		
	}

}