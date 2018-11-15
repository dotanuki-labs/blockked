package io.dotanuki.blockked.domain

sealed class RemoteIntegrationIssue : Throwable() {

    object ClientOrigin : RemoteIntegrationIssue()
    object RemoteSystem : RemoteIntegrationIssue()
    object UnexpectedResponse : RemoteIntegrationIssue()

    override fun toString() = when (this) {
        ClientOrigin -> "Issue originated from client"
        RemoteSystem -> "Issue incoming from server"
        UnexpectedResponse -> "Broken contract"
    }

}