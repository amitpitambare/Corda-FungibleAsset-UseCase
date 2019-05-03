package com.asp.corda.schemas;

import java.util.Currency;

import javax.persistence.Entity;

import com.google.common.collect.ImmutableList;

import net.corda.core.contracts.Amount;
import net.corda.core.identity.AbstractParty;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

public class AssetSchemaV1 extends MappedSchema {
	public AssetSchemaV1() {
        super(AssetSchemaV1.class, 1, ImmutableList.of(PersistentAsset.class));
        
        
    }
	
	
	@Entity
	public static class PersistentAsset extends PersistentState {
		
		 String  owner;
		 String assetName;
		 String purchaseCost;
		 String assetCode;
		 
		 
		 
		public PersistentAsset() {
			//super();
		}



		public PersistentAsset(String owner, String assetName, String purchaseCost, String assetCode) {
			
			this.owner = owner;
			this.assetName = assetName;
			this.purchaseCost = purchaseCost;
			this.assetCode = assetCode;
		}
		
		
		
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getAssetName() {
			return assetName;
		}
		public void setAssetName(String assetName) {
			this.assetName = assetName;
		}
		public String getPurchaseCost() {
			return purchaseCost;
		}
		public void setPurchaseCost(String purchaseCost) {
			this.purchaseCost = purchaseCost;
		}
		public String getAssetCode() {
			return assetCode;
		}
		public void setAssetCode(String assetCode) {
			this.assetCode = assetCode;
		}
		
		
	}
	
	
}
