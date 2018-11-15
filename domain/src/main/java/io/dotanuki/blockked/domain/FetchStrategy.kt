package io.dotanuki.blockked.domain

sealed class FetchStrategy {

    object FromPrevious : FetchStrategy()
    object ForceUpdate : FetchStrategy()

}