package io.dotanuki.blockked.domain

sealed class BlockchainInfoIntegrationIssue : Throwable() {

    object ClientOrigin : BlockchainInfoIntegrationIssue()
    object RemoteSystem : BlockchainInfoIntegrationIssue()
    object UnexpectedResponse : BlockchainInfoIntegrationIssue()
    object AbsentFromCache : BlockchainInfoIntegrationIssue()

    override fun toString() = when (this) {
        ClientOrigin -> "Issue originated from client"
        RemoteSystem -> "Issue incoming from server"
        UnexpectedResponse -> "Broken contract"
        AbsentFromCache -> "Cache miss"
    }

}