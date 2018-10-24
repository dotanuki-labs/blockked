package io.dotanuki.blockked.domain

sealed class NetworkingIssue : Throwable() {

    object HostUnreachable : NetworkingIssue()
    object OperationTimeout : NetworkingIssue()
    object ConnectionSpike : NetworkingIssue()

    override fun toString() = when (this) {
        HostUnreachable -> "Cannot reach remote host"
        OperationTimeout -> "Networking operation timed out"
        ConnectionSpike -> "In-flight networking operation broke-up"
    }

}