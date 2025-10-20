package com.example.juego_colores

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment

import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import android.widget.Button
import android.widget.Toast
import kotlin.math.max


class GameFragment : Fragment(R.layout.fragment_game) {
    private var puntaje = 0
    private var intentosTotales = 0
    private val maxIntentos = 5
    private lateinit var txtPuntaje: TextView
    private lateinit var txtTiempo: TextView
    private lateinit var txtColorAdivinar: TextView
    private lateinit var txtIntentos: TextView
    private lateinit var soundManager: SoundManager
    private var temporizadorColor: CountDownTimer? = null

    private var tiempoPorColor = 3000L // 3 segundos por color

    private val colores = listOf("ROJO", "VERDE", "AZUL","MORADO", "NARANJA")
    private var colorActual = ""

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        soundManager= SoundManager(requireContext())
        inicializarVistas(view)
        configurarBotones(view)
        iniciarJuego()
    }

    override fun onDestroyView(){
        super.onDestroyView()
        temporizadorColor?.cancel()
        soundManager.release()
    }

    private fun inicializarVistas(view: View){
        txtPuntaje = view.findViewById(R.id.txtPuntaje)
        txtTiempo= view.findViewById(R.id.txtTiempo)
        txtColorAdivinar = view.findViewById<TextView>(R.id.txtColorAdivinar)
        txtIntentos = view.findViewById(R.id.txtTiempo)
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
        intentosTotales=0
        puntaje=0
        generarNuevoColor()
        actualizarUI()

        // Por ahora sin temporizador, lo agregaremos después
        Toast.makeText(requireContext(), "¡El juego ha comenzado!", Toast.LENGTH_SHORT).show()
    }

    private fun generarNuevoColor() {
        temporizadorColor?.cancel()
        colorActual = colores.random()
        txtColorAdivinar.text = colorActual

        // También cambiar el color de fondo del TextView
        when (colorActual) {
            "ROJO" -> txtColorAdivinar.setBackgroundColor(requireContext().getColor(android.R.color.holo_red_light))
            "VERDE" -> txtColorAdivinar.setBackgroundColor(requireContext().getColor(android.R.color.holo_green_light))
            "AZUL" -> txtColorAdivinar.setBackgroundColor(requireContext().getColor(android.R.color.holo_blue_light))
            "MORADO" -> txtColorAdivinar.setBackgroundColor(requireContext().getColor(android.R.color.holo_purple))
            "NARANJA" -> txtColorAdivinar.setBackgroundColor(requireContext().getColor(android.R.color.holo_orange_dark))
        }
        iniciarTemporizadorColor()
    }

    private fun iniciarTemporizadorColor() {
        temporizadorColor = object : CountDownTimer(tiempoPorColor, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = (millisUntilFinished / 1000).toInt()
                txtTiempo.text = "Tiempo: ${segundosRestantes}s"
            }
            override fun onFinish() {

                println("❌ Tiempo agotado para color: $colorActual")
                soundManager.playSound(R.raw.error_sound)
                Toast.makeText(requireContext(), "¡Tiempo agotado! Era $colorActual", Toast.LENGTH_SHORT).show()

               intentosTotales++
                if(intentosTotales>= maxIntentos){
                    terminarJuego()
                }else{
                    generarNuevoColor()
                    actualizarUI()
                }

            }
        }.start()
    }

    private fun verificarRespuesta(colorSeleccionado: String) {
        temporizadorColor?.cancel()
        intentosTotales++
        if (colorSeleccionado == colorActual) {
            // Acierto
            puntaje++
            soundManager.playSound(R.raw.acierto_sound)
            Toast.makeText(requireContext(), "¡Correcto! +1 punto", Toast.LENGTH_SHORT).show()
        } else {
            // Error
            soundManager.playSound(R.raw.error_sound)
            Toast.makeText(requireContext(), "Incorrecto. Era $colorActual", Toast.LENGTH_SHORT).show()
        }
        if(intentosTotales >= maxIntentos){
            soundManager.playSound(R.raw.fin_juego_positivo)
            terminarJuego()
        }else{
            generarNuevoColor()
            actualizarUI()
        }
    }

    private fun actualizarUI() {
        txtPuntaje.text = "Puntaje: $puntaje"
        val intentosRestantes = maxIntentos - intentosTotales
        txtTiempo.text ="Intentos: $intentosRestantes/$maxIntentos"
    }


    private fun terminarJuego() {
        // Navegar al fragment de resultados con el puntaje
        val bundle = Bundle().apply {
            putInt("puntaje", puntaje)
        }

        val mensajeFinal = when {
            puntaje == maxIntentos -> "¡PERFECTO! "
            puntaje >= maxIntentos - 1 -> "¡Excelente! "
            puntaje >= maxIntentos - 2 -> "¡Buen trabajo! "
            else -> "¡Sigue practicando! "
        }
        findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundle)
    }


}