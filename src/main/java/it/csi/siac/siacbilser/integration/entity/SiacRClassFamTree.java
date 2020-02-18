/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_class_fam_tree database table.
 * 
 */
@Entity
@Table(name="siac_r_class_fam_tree")
public class SiacRClassFamTree extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The classif classif fam tree id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CLASS_FAM_TREE_CLASSIFCLASSIFFAMTREEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CLASS_FAM_TREE_CLASSIF_CLASSIF_FAM_TREE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CLASS_FAM_TREE_CLASSIFCLASSIFFAMTREEID_GENERATOR")
	@Column(name="classif_classif_fam_tree_id")
	private Integer classifClassifFamTreeId;

	/** The livello. */
	private Integer livello;
	
	/** The ordine. */
	private String ordine;

	

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class figlio. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClassFiglio;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class padre. */
	@ManyToOne
	@JoinColumn(name="classif_id_padre")
	private SiacTClass siacTClassPadre;

	//bi-directional many-to-one association to SiacTClassFamTree
	/** The siac t class fam tree. */
	@ManyToOne
	@JoinColumn(name="classif_fam_tree_id")
	private SiacTClassFamTree siacTClassFamTree;

	/**
	 * Instantiates a new siac r class fam tree.
	 */
	public SiacRClassFamTree() {
	}

	/**
	 * Gets the classif classif fam tree id.
	 *
	 * @return the classif classif fam tree id
	 */
	public Integer getClassifClassifFamTreeId() {
		return this.classifClassifFamTreeId;
	}

	/**
	 * Sets the classif classif fam tree id.
	 *
	 * @param classifClassifFamTreeId the new classif classif fam tree id
	 */
	public void setClassifClassifFamTreeId(Integer classifClassifFamTreeId) {
		this.classifClassifFamTreeId = classifClassifFamTreeId;
	}
/*
	public Integer getClassifIdPadre() {
		return this.classifIdPadre;
	}

	public void setClassifIdPadre(Integer classifIdPadre) {
		this.classifIdPadre = classifIdPadre;
	}
*/


	/**
 * Gets the livello.
 *
 * @return the livello
 */
public Integer getLivello() {
		return this.livello;
	}

	/**
	 * Sets the livello.
	 *
	 * @param livello the new livello
	 */
	public void setLivello(Integer livello) {
		this.livello = livello;
	}



	/**
	 * Gets the ordine.
	 *
	 * @return the ordine
	 */
	public String getOrdine() {
		return this.ordine;
	}

	/**
	 * Sets the ordine.
	 *
	 * @param ordine the new ordine
	 */
	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}





	/**
	 * Gets the siac t class figlio.
	 *
	 * @return the siac t class figlio
	 */
	public SiacTClass getSiacTClassFiglio() {
		return this.siacTClassFiglio;
	}

	/**
	 * Sets the siac t class figlio.
	 *
	 * @param siacTClass1 the new siac t class figlio
	 */
	public void setSiacTClassFiglio(SiacTClass siacTClass1) {
		this.siacTClassFiglio = siacTClass1;
	}

	/**
	 * Gets the siac t class padre.
	 *
	 * @return the siac t class padre
	 */
	public SiacTClass getSiacTClassPadre() {
		return this.siacTClassPadre;
	}

	/**
	 * Sets the siac t class padre.
	 *
	 * @param siacTClass2 the new siac t class padre
	 */
	public void setSiacTClassPadre(SiacTClass siacTClass2) {
		this.siacTClassPadre = siacTClass2;
	}

	/**
	 * Gets the siac t class fam tree.
	 *
	 * @return the siac t class fam tree
	 */
	public SiacTClassFamTree getSiacTClassFamTree() {
		return this.siacTClassFamTree;
	}

	/**
	 * Sets the siac t class fam tree.
	 *
	 * @param siacTClassFamTree the new siac t class fam tree
	 */
	public void setSiacTClassFamTree(SiacTClassFamTree siacTClassFamTree) {
		this.siacTClassFamTree = siacTClassFamTree;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return classifClassifFamTreeId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.classifClassifFamTreeId = uid;
		
	}



}