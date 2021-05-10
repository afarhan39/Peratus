package my.farhan.peratus

data class Peratus(
    val label: String,
    val multiplier: Double,
    val resultProfit: String,
    val resultLoss: String
)

enum class PeratusType{
    Profit, Loss
}