package com.example.juego_colores

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ResultFragment : Fragment(R.layout.fragment_result) {
    private lateinit var txtPuntajeFinal : TextView
    private lateinit var txtMensajePuntaje: TextView
    private lateinit var btnJugardeNuevo: Button

    // Para SharedPreferences (mejor puntaje básico)
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        inicializarVistas(view)

        // Configurar SharedPreferences para el mejor puntaje
        configurarSharedPreferences()

        // Obtener puntaje y mostrar resultados
        mostrarResultados()

        // Configurar botones
        configurarBotones()
    }
    private fun inicializarVistas(view: View) {
        txtPuntajeFinal = view.findViewById(R.id.txtPuntajeFinal)
        txtMensajePuntaje = view.findViewById(R.id.txtMensajePuntaje)
        btnJugardeNuevo = view.findViewById(R.id.btnJugardeNuevo)
    }

    private fun configurarSharedPreferences() {
        sharedPreferences = requireContext().getSharedPreferences("juego_prefs", Context.MODE_PRIVATE)
    }
    private fun mostrarResultados() {
        // Obtener puntaje actual del Bundle
        val puntajeActual = arguments?.getInt("puntaje", 0) ?: 0

        // Mostrar puntaje actual
        txtPuntajeFinal.text = "Puntaje: $puntajeActual"

        // Mostrar mensaje según el puntaje
        val mensaje = when (puntajeActual) {
            in 0..2 -> "¡Sigue practicando! 💪"
            in 3..4 -> "¡Buen trabajo! 👍"
            in 5..7 -> "¡Excelente! 🎯"
            else -> "¡Increíble! 🏆"
        }
        txtMensajePuntaje.text = mensaje

        // Guardar mejor puntaje si es récord
        val mejorPuntaje = sharedPreferences.getInt("mejor_puntaje", 0)
        if (puntajeActual > mejorPuntaje) {
            // ¡Nuevo récord!
            sharedPreferences.edit().putInt("mejor_puntaje", puntajeActual).apply()
            txtPuntajeFinal.text = "🏆 NUEVO RÉCORD: $puntajeActual"
            txtPuntajeFinal.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
            // Mostrar Toast de felicitación
            Toast.makeText(requireContext(), "¡Nuevo récord establecido!", Toast.LENGTH_LONG).show()
        } else {
            // Mostrar mejor puntaje también
            txtMensajePuntaje.text = "$mensaje\nMejor puntaje: $mejorPuntaje"
        }
    }

    private fun configurarBotones() {
        btnJugardeNuevo.setOnClickListener {
            // Volver al WelcomeFragment (limpiando el back stack)
            findNavController().popBackStack(R.id.welcomeFragment, false)
        }
    }

}