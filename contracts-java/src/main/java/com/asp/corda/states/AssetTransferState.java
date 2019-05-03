package com.asp.corda.states;

import java.util.Arrays;
import java.util.List;


import com.asp.corda.schemas.AssetTransferSchemaV1;
import com.google.common.collect.ImmutableList;

import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

public class AssetTransferState implements ContractState ,LinearState,QueryableState{
	
	private final AssetState asset;
	private final Party assetSeller;
	private final  Party assetBuyer;
	
	private final  RequestStatus status;
	private final UniqueIdentifier linearId;
	 
	 
	 

	public AssetTransferState(AssetState asset, Party assetSeller, Party assetBuyer,
			RequestStatus status, UniqueIdentifier linearId) {
		//super();
		this.asset = asset;
		this.assetSeller = assetSeller;
		this.assetBuyer = assetBuyer;
		this.status = status;
		this.linearId = linearId;
	}

	@Override
	public List<AbstractParty> getParticipants() {
		// TODO Auto-generated method stub
		return Arrays.asList(assetSeller,assetBuyer);
	}

	@Override
	public PersistentState generateMappedObject(MappedSchema schema) {
		if (schema instanceof AssetTransferSchemaV1) {
            return new AssetTransferSchemaV1.PersistentAssetTransfer(
                    this.asset.getAssetName(),
                    this.asset.getAssetCode(),
                    this.assetSeller,
                    this.assetBuyer,
                    this.status.getStatus(),
                    this.linearId.toString()
                    );
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
	}

	@Override
	public Iterable<MappedSchema> supportedSchemas() {
		// TODO Auto-generated method stub
		return ImmutableList.of(new AssetTransferSchemaV1());
	}


	
	 @Override 
	 public UniqueIdentifier getLinearId() {
		 return linearId;
		 }

	public AssetState getAsset() {
		return asset;
	}

	public Party getAssetSeller() {
		return assetSeller;
	}

	public Party getAssetBuyer() {
		return assetBuyer;
	}

	public RequestStatus getStatus() {
		return status;
	}

}
