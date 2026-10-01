import java.time.LocalDateTime

class Felino(
    codigo: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno
) : Paciente(codigo, nombre, especie, fechaIngreso, tipoDueno) {

    fun esAtencionGratuita(minutos: Int): Boolean = minutos < MINUTOS_GRATIS

    override fun calcularCostoBase(minutos: Int): Double {
        return if (esAtencionGratuita(minutos)) 0.0 else TARIFA_HORA * minutos / 60.0
    }

    private companion object {
        const val TARIFA_HORA = 9000.0
        const val MINUTOS_GRATIS = 20
    }
}