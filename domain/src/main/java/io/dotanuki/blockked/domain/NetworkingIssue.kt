package io.dotanuki.blockked.domain

sealed class NetworkingIssue : Throwable() {

    object HostUnreachable : NetworkingIssue()
    object OperationTimeout : NetworkingIssue()
    object ConnectionSpike : NetworkingIssue()

}