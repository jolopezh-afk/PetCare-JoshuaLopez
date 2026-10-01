object Reporte {

    fun mostrarBoxesDisponibles(petCare: PetCare) {
        val disponibles = petCare.boxes
            .filter { it.estado is EstadoBox.Libre }
            .map { it.numero }

        println("\n=== BOXES DISPONIBLES ===")

        if (disponibles.isEmpty()) {
            println("No hay boxes disponibles.")
        } else {
            println("Boxes disponibles: ${disponibles.joinToString(", ")}")
        }
    }

    fun mostrarPacientesConvenio(petCare: PetCare) {
        val tickets = petCare.historial
            .filter { it.tipoDueno == TipoDueno.CONVENIO }

        println("\n=== PACIENTES CON CONVENIO ===")

        if (tickets.isEmpty()) {
            println("No hay pacientes con convenio en el historial.")
        } else {
            tickets.forEach { ticket ->
                println("Código: ${ticket.codigo} | Tipo: ${ticket.tipoPaciente}")
            }
        }
    }

    fun mostrarPromedioIngresos(petCare: PetCare) {
        println("\n=== PROMEDIO DE INGRESOS POR PACIENTE ===")

        if (petCare.historial.isEmpty()) {
            println("No hay pacientes registrados en el historial.")
        } else {
            println("Promedio: $${"%.0f".format(calcularPromedio(petCare))}")
        }
    }

    fun mostrarCodigosFinalizados(petCare: PetCare) {
        val codigos = petCare.historial.map { it.codigo }

        println("\n=== CÓDIGOS DE PACIENTES FINALIZADOS ===")

        if (codigos.isEmpty()) {
            println("No hay pacientes finalizados.")
        } else {
            codigos.forEach { println(it) }
        }
    }

    fun mostrarPacienteMayorUso(petCare: PetCare) {
        val ticket = petCare.historial.maxByOrNull { it.minutos }

        println("\n=== PACIENTE CON MAYOR TIEMPO DE USO ===")

        if (ticket == null) {
            println("No hay pacientes finalizados.")
        } else {
            println("Código: ${ticket.codigo} | Tiempo: ${ticket.minutos} minutos")
        }
    }

    fun mostrarRecaudacionPorTipo(petCare: PetCare) {
        println("\n=== RECAUDACIÓN POR TIPO DE PACIENTE ===")

        if (petCare.recaudacionPorTipo.isEmpty()) {
            println("No hay recaudación registrada.")
        } else {
            petCare.recaudacionPorTipo.forEach { (tipo, monto) ->
                println("$tipo: $${"%.0f".format(monto)}")
            }
        }
    }

    fun mostrarResumenCierre(petCare: PetCare) {
        println("\n========================================")
        println("           REPORTE DE CIERRE")
        println("              ${petCare.nombre}")
        println("========================================")

        println("\n--- TICKETS ---")

        if (petCare.historial.isEmpty()) {
            println("No hay tickets registrados.")
        } else {
            petCare.historial.forEach { ticket ->
                println(
                    "Ticket #${ticket.numero} | " +
                            "Tipo: ${ticket.tipoPaciente} | " +
                            "Código: ${ticket.codigo} | " +
                            "Tiempo: ${ticket.minutos} min | " +
                            "Monto: $${"%.0f".format(ticket.monto)}"
                )
            }
        }

        val cantidadPacientes = petCare.historial.size

        val tipoMayorRecaudacion = petCare.recaudacionPorTipo
            .maxByOrNull { it.value }
            ?.key
            ?: "Sin datos"

        println("\n--- RESUMEN ---")
        println("Total recaudado: $${"%.0f".format(petCare.historial.sumOf { it.monto })}")
        println("Cantidad de pacientes: $cantidadPacientes")
        println("Promedio por paciente: $${"%.0f".format(calcularPromedio(petCare))}")
        println("Tipo con mayor recaudación: $tipoMayorRecaudacion")
        println("Boxes disponibles al cierre: ${petCare.boxesDisponibles()}")
    }

    private fun calcularPromedio(petCare: PetCare): Double {
        return if (petCare.historial.isEmpty()) 0.0
        else petCare.historial.map { it.monto }.average()
    }

    fun mostrarEstadoBoxes(petCare: PetCare) {
        println("\n=== ESTADO DE LOS BOXES ===")

        petCare.boxes.forEach { box ->
            val texto = when (val estado = box.estado) {
                is EstadoBox.Libre -> "Libre"
                is EstadoBox.EnAtencion ->
                    "En atención: ${estado.paciente.nombre} (${estado.paciente.codigo})"
                is EstadoBox.EnProceso -> "En proceso: ${estado.motivo}"
                is EstadoBox.FueraDeServicio -> "Fuera de servicio: ${estado.motivo}"
            }
            println("Box ${box.numero}: $texto")
        }
    }
}