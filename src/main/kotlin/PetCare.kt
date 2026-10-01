import kotlinx.coroutines.delay

class PetCare {

    val nombre = "PetCare"

    val boxes: List<Box> = List(CAPACIDAD) { indice -> Box(indice + 1) }

    private val _historial: MutableList<Ticket> = mutableListOf()
    val historial: List<Ticket> get() = _historial

    private val _recaudacionPorTipo: MutableMap<String, Double> = mutableMapOf()
    val recaudacionPorTipo: Map<String, Double> get() = _recaudacionPorTipo

    var recaudacionTotal: Double = 0.0
        private set

    suspend fun registrarEntrada(paciente: Paciente) {
        if (buscarBoxPorCodigo(paciente.codigo) != null) {
            throw PacienteDuplicadoException(
                "El paciente ${paciente.codigo} ya se encuentra registrado."
            )
        }

        val box = boxes.firstOrNull { it.estado is EstadoBox.Libre }
            ?: throw SistemaLlenoException(
                "El sistema está lleno. No se puede registrar al paciente ${paciente.codigo}."
            )

        box.estado = EstadoBox.EnProceso("Registrando entrada")
        println("Box ${box.numero}: registrando entrada de ${paciente.nombre}...")

        delay(TIEMPO_ENTRADA_MS)

        box.estado = EstadoBox.EnAtencion(paciente)
        println("Entrada registrada: ${paciente.detalle()} -> Box ${box.numero}")
    }

    suspend fun registrarSalida(codigo: String, minutos: Int) {
        Validador.validarCodigo(codigo)

        val box = buscarBoxPorCodigo(codigo)
            ?: throw PacienteNoEncontradoException(
                "No se encontró un paciente con código $codigo."
            )

        val paciente = (box.estado as EstadoBox.EnAtencion).paciente

        box.estado = EstadoBox.EnProceso("Calculando tarifa")
        println("Box ${box.numero}: calculando tarifa de ${paciente.nombre}...")

        try {
            delay(TIEMPO_SALIDA_MS)

            val monto = CalculadoraTarifas.calcular(paciente, minutos)

            val ticket = Ticket(
                numero = _historial.size + 1,
                tipoPaciente = paciente::class.simpleName ?: "Desconocido",
                codigo = paciente.codigo,
                minutos = minutos,
                monto = monto,
                tipoDueno = paciente.tipoDueno
            )

            _historial.add(ticket)
            recaudacionTotal += monto
            _recaudacionPorTipo[ticket.tipoPaciente] =
                (_recaudacionPorTipo[ticket.tipoPaciente] ?: 0.0) + monto

            box.estado = EstadoBox.Libre

            println(
                "Salida registrada: ${paciente.nombre} (${paciente.codigo}). " +
                        "Ticket #${ticket.numero} - Monto: $${"%.0f".format(ticket.monto)}"
            )
        } catch (e: Exception) {
            box.estado = EstadoBox.EnAtencion(paciente)
            throw e
        }
    }

    fun inhabilitarBox(numero: Int, motivo: String) {
        val box = boxes.firstOrNull { it.numero == numero } ?: return

        if (box.estado is EstadoBox.Libre) {
            box.estado = EstadoBox.FueraDeServicio(motivo)
            println("Box $numero fuera de servicio: $motivo")
        } else {
            println("No se puede inhabilitar el box $numero: no está libre.")
        }
    }

    fun boxesDisponibles(): Int = boxes.count { it.estado is EstadoBox.Libre }

    private fun buscarBoxPorCodigo(codigo: String): Box? {
        return boxes.firstOrNull { box ->
            when (val estado = box.estado) {
                is EstadoBox.EnAtencion -> estado.paciente.codigo.equals(codigo, ignoreCase = true)
                is EstadoBox.Libre -> false
                is EstadoBox.EnProceso -> false
                is EstadoBox.FueraDeServicio -> false
            }
        }
    }

    private companion object {
        const val CAPACIDAD = 10
        const val TIEMPO_ENTRADA_MS = 3000L
        const val TIEMPO_SALIDA_MS = 6500L
    }
}