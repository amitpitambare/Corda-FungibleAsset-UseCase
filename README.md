<p align="center">
  <img src="https://www.corda.net/wp-content/uploads/2016/11/fg005_corda_b.png" alt="VBC CordaApp" width="500">
</p>

# Fungible Asset CordaApp


### Steps for Executing Corda Flows from  Interactive Shell
 
###### 1. To create Asset - run below command on SecuritySeller node's console:

	flow start com.asp.cordaapp.seller.flows.CreateAssetFlow assetName: "Abc Equity", purchaseCost: $20000, assetCode: "CUSIP226"

#####    To see state created run below command:
    run vaultQuery contractStateType: com.asp.corda.states.AssetState


###### 2. To create AssetTransfer - run below command again on SecuritySeller node's console:
	
    flow start com.asp.cordaapp.seller.flows.AssetTransferRequestInitiateFlow assetCode: "CUSIP226", assetBuyer: "O=SecurityBuyer,L=New 	York,C=US"

#####    To see AssetTransfer state created run below command again:
    run vaultQuery contractStateType: com.asp.corda.states.AssetTransferState

###### 3. The Buyer party confirm AssetTransfer request received by running below command on counterparty SecurityBuyer node's console:

	flow start com.asp.cordapp.buyer.flows.ConfirmAssetTransferRequestInitiatorFlow linearID: "2e414be2-e448-4b79-8d52-ca6e8ee7fbd0"
