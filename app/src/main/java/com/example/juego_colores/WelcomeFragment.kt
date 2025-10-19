/*Fragment centrado en la ventana inicial o de bienvenida al usuario
AUTOR: Renzo Murillo Alvarez
Fecha Creación: 16/10/2025
* */

package com.example.juego_colores

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnJugar = view.findViewById<Button>(R.id.btnJugar)

        btnJugar.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment    )
        }
    }


}