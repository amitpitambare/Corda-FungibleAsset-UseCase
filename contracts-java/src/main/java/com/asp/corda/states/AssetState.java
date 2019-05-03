package com.asp.corda.states;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.asp.corda.contracts.AssetContract;
import com.asp.corda.schemas.AssetSchemaV1;
import com.google.common.collect.ImmutableList;

import net.corda.core.contracts.Amount;
import net.corda.core.contracts.CommandAndState;
import net.corda.core.contracts.OwnableState;
import net.corda.core.crypto.NullKeys;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.AnonymousParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

public class AssetState implements OwnableState,QueryableState{

	// Party ownerName; 
	 
	 private AbstractParty owner;
	 String assetName;
	 Amount<Currency> purchaseCost;
	 String assetCode;
	 
	 public AssetState() {
	    }  // For serialization
	
	public AssetState(AbstractParty owner, String assetName, Amount<Currency> purchaseCost, String assetCode) {
		
		 this.owner = owner;
		this.assetName = assetName;
		this.purchaseCost = purchaseCost;
		this.assetCode = assetCode;
	}

	
	  public AssetState copy() {
	        return new AssetState( this.owner, this.assetName,this.purchaseCost, this.assetCode);
	    }

	    public AssetState withoutOwner() {
	        return new AssetState(new AnonymousParty(NullKeys.NullPublicKey.INSTANCE),this.assetName,this.purchaseCost, this.assetCode);
	    }

	    @NotNull
	    @Override
	    public CommandAndState withNewOwner(@NotNull AbstractParty newOwner) {
	        return new CommandAndState(new AssetContract.Commands.Transfer(), new AssetState( newOwner,this.assetName, this.purchaseCost, this.assetCode));
	    }
	    
	    
	@Override
	public List<AbstractParty> getParticipants() {
		// TODO Auto-generated method stub
		return Arrays.asList(owner);
	}

	
	public AbstractParty getOwner() {
		
		return owner;
	}

	

	

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Amount<Currency> getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(Amount<Currency> purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	@Override
	public PersistentState generateMappedObject(MappedSchema schema) {
		if (schema instanceof AssetSchemaV1) {
            return new AssetSchemaV1.PersistentAsset(
                    this.owner.nameOrNull().toString(),
                    this.assetName,
                    String.valueOf(this.purchaseCost.getQuantity()),
                    this.assetCode );
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
	}

	@Override
	public Iterable<MappedSchema> supportedSchemas() {
		// TODO Auto-generated method stub
		 return ImmutableList.of(new AssetSchemaV1());
	}

}
