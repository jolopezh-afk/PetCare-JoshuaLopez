import java.time.LocalDateTime

class Exotico(
    codigo: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno,
    val esSilvestre: Boolean
) : Paciente(codigo, nombre, especie, fechaIngreso, tipoDueno) {

    override fun calcularCostoBase(minutos: Int): Double {
        val monto = TARIFA_HORA * minutos / 60.0
        return if (esSilvestre) monto * FACTOR_SILVESTRE else monto
    }

    override fun detalle(): String {
        return super.detalle() + " | Silvestre: ${if (esSilvestre) "Sí" else "No"}"
    }

    private companion object {
        const val TARIFA_HORA = 20000.0
        const val FACTOR_SILVESTRE = 1.30
    }
}