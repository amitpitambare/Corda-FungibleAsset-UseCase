package com.asp.corda.schemas;

import javax.persistence.Entity;

import com.google.common.collect.ImmutableList;


import net.corda.core.identity.AbstractParty;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

public class AssetTransferSchemaV1  extends MappedSchema{
	
	
	public AssetTransferSchemaV1() {
        super(AssetTransferSchemaV1.class, 1, ImmutableList.of(PersistentAssetTransfer.class));
        
        
    }
	
	@Entity
    public static class PersistentAssetTransfer extends PersistentState{
		String assetName;
		String assetCode;
		AbstractParty assetSeller;
		AbstractParty assetBuyer;
		
		String  status;
		String linearId;
		
		
		
		public PersistentAssetTransfer(String assetName, String assetCode, AbstractParty assetSeller,
				AbstractParty assetBuyer, String status, String linearId) {
		//	super();
			this.assetName = assetName;
			this.assetCode = assetCode;
			this.assetSeller = assetSeller;
			this.assetBuyer = assetBuyer;
			this.status = status;
			this.linearId = linearId;
		}
		
		public PersistentAssetTransfer(){};
		
		
		public String getAssetName() {
			return assetName;
		}
		public void setAssetName(String assetName) {
			this.assetName = assetName;
		}
		public String getAssetCode() {
			return assetCode;
		}
		public void setAssetCode(String assetCode) {
			this.assetCode = assetCode;
		}
		public AbstractParty getAssetSeller() {
			return assetSeller;
		}
		public void setAssetSeller(AbstractParty assetSeller) {
			this.assetSeller = assetSeller;
		}
		public AbstractParty getAssetBuyer() {
			return assetBuyer;
		}
		public void setAssetBuyer(AbstractParty assetBuyer) {
			this.assetBuyer = assetBuyer;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getLinearId() {
			return linearId;
		}
		public void setLinearId(String linearId) {
			this.linearId = linearId;
		}
	}
	
	
	
	
}
