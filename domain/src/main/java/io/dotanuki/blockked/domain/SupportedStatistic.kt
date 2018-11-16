package io.dotanuki.blockked.domain

sealed class SupportedStatistic {

    object AverageMarketPrice : SupportedStatistic()
    object MarketCapitalization : SupportedStatistic()
    object TotalBitcoins : SupportedStatistic()
    object TradeVolume : SupportedStatistic()

    object BlockchainSize : SupportedStatistic()
    object AverageBlockSize : SupportedStatistic()
    object OrphanBlocks : SupportedStatistic()
    object TransactionsPerBlock : SupportedStatistic()
    object TransactionConfirmationTime : SupportedStatistic()

    object HashRate : SupportedStatistic()
    object Difficulty : SupportedStatistic()
    object TotalTransactionFee : SupportedStatistic()
    object PercentualCostOfTransaction : SupportedStatistic()
    object CostPerTransaction : SupportedStatistic()

    object TransactionsPerDay : SupportedStatistic()
    object MemoryPoolSize : SupportedStatistic()
    object OutputVolume : SupportedStatistic()
    object EstimatedTransactionsVolume : SupportedStatistic()

    override fun toString() = when (this) {
        AverageMarketPrice -> "market-price"
        MarketCapitalization -> "market-cap"
        TotalBitcoins -> "total-bitcoins"
        TradeVolume -> "trade-volume"
        BlockchainSize -> "blocks-size"
        AverageBlockSize -> "avg-block-size"
        OrphanBlocks -> "n-orphaned-blocks"
        TransactionsPerBlock -> "n-transactions-per-block"
        TransactionConfirmationTime -> "median-confirmation-time"
        HashRate -> "hash-rate"
        Difficulty -> "difficulty"
        TotalTransactionFee -> "transaction-fees-usd"
        PercentualCostOfTransaction -> "cost-per-transaction-percent"
        CostPerTransaction -> "cost-per-transaction"
        TransactionsPerDay -> "n-transactions"
        MemoryPoolSize -> "mempool-size"
        OutputVolume -> "output-volume"
        EstimatedTransactionsVolume -> "estimated-transaction-volume-usd"
    }

    companion object {
        val ALL = listOf(
            SupportedStatistic.AverageMarketPrice,
            MarketCapitalization,
            TotalBitcoins,
            TradeVolume,
            BlockchainSize,
            AverageBlockSize,
            OrphanBlocks,
            TransactionsPerBlock,
            TransactionConfirmationTime,
            HashRate,
            Difficulty,
            TotalTransactionFee,
            PercentualCostOfTransaction,
            CostPerTransaction,
            TransactionsPerDay,
            MemoryPoolSize,
            OutputVolume,
            EstimatedTransactionsVolume
        )
    }
}