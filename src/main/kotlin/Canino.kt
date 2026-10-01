import java.time.LocalDateTime

class Canino(
    codigo: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno
) : Paciente(codigo, nombre, especie, fechaIngreso, tipoDueno) {

    override fun calcularCostoBase(minutos: Int): Double {
        val monto = TARIFA_HORA * minutos / 60.0
        return if (tipoDueno == TipoDueno.CONVENIO) monto * FACTOR_CONVENIO else monto
    }

    private companion object {
        const val TARIFA_HORA = 12000.0
        const val FACTOR_CONVENIO = 0.80
    }
}