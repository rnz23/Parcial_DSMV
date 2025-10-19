package com.example.juego_colores

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import android.widget.Button
import android.widget.Toast



class GameFragment : Fragment(R.layout.fragment_game) {
    private var puntaje = 0
    private var tiempoRestante = 30
    private lateinit var txtPuntaje: TextView
    private lateinit var txtTiempo: TextView
    private lateinit var txtColorObjetivo: TextView
    private lateinit var txtColorAdivinar: TextView

    private val colores = listOf("ROJO", "VERDE", "AZUL","MORADO", "NARANJA")
    private var colorActual = ""

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        inicializarVistas(view)
        configurarBotones(view)
        iniciarJuego()
    }

    private fun inicializarVistas(view: View){
        txtPuntaje = view.findViewById(R.id.txtPuntaje)
        txtTiempo= view.findViewById(R.id.txtTiempo)
        txtColorAdivinar = view.findViewById<TextView>(R.id.txtColorAdivinar)
    }

    private fun configurarBotones(view: View) {
        // Configurar listeners para todos los botones de colores
        view.findViewById<Button>(R.id.btnRojo).setOnClickListener { verificarRespuesta("ROJO") }
        view.findViewById<Button>(R.id.btnVerde).setOnClickListener { verificarRespuesta("VERDE") }
        view.findViewById<Button>(R.id.btnAzul).setOnClickListener { verificarRespuesta("AZUL") }
        view.findViewById<Button>(R.id.btnMorado).setOnClickListener { verificarRespuesta("MORADO") }
        view.findViewById<Button>(R.id.btnNaranja).setOnClickListener { verificarRespuesta("NARANJA") }
    }

    private fun iniciarJuego() {
        generarNuevoColor()
        actualizarUI()

        // Por ahora sin temporizador, lo agregaremos después
        Toast.makeText(requireContext(), "¡El juego ha comenzado!", Toast.LENGTH_SHORT).show()
    }

    private fun generarNuevoColor() {
        colorActual = colores.random()
        txtColorObjetivo.text = colorActual

        // También cambiar el color de fondo del TextView
        when (colorActual) {
            "ROJO" -> txtColorObjetivo.setBackgroundColor(requireContext().getColor(android.R.color.holo_red_light))
            "VERDE" -> txtColorObjetivo.setBackgroundColor(requireContext().getColor(android.R.color.holo_green_light))
            "AZUL" -> txtColorObjetivo.setBackgroundColor(requireContext().getColor(android.R.color.holo_blue_light))
            "MORADO" -> txtColorObjetivo.setBackgroundColor(requireContext().getColor(android.R.color.holo_purple))
            "NARANJA" -> txtColorObjetivo.setBackgroundColor(requireContext().getColor(android.R.color.holo_orange_dark))
        }
    }

    private fun verificarRespuesta(colorSeleccionado: String) {
        if (colorSeleccionado == colorActual) {
            // Acierto
            puntaje++
            Toast.makeText(requireContext(), "¡Correcto! +1 punto", Toast.LENGTH_SHORT).show()
        } else {
            // Error
            Toast.makeText(requireContext(), "Incorrecto. Era $colorActual", Toast.LENGTH_SHORT).show()
        }
        generarNuevoColor()
        actualizarUI()

        // Por ahora, navegar a resultados después de 5 puntos (para probar)
        if (puntaje >= 5) {
            terminarJuego()
        }
    }

    private fun actualizarUI() {
        txtPuntaje.text = "Puntaje: $puntaje"
        txtTiempo.text = "Tiempo: ${tiempoRestante}s"
    }


    private fun terminarJuego() {
        // Navegar al fragment de resultados con el puntaje
        val bundle = Bundle().apply {
            putInt("puntaje", puntaje)
        }
        findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundle)
    }


}