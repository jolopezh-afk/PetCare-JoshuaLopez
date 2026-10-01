data class Ticket(
    val numero: Int,
    val tipoPaciente: String,
    val codigo: String,
    val minutos: Int,
    val monto: Double,
    val tipoDueno: TipoDueno
)