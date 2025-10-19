package com.example.juego_colores

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast

class SoundManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    fun playSound(soundRes: Int) {
        try {
            // Liberar el sonido anterior si está reproduciéndose
            mediaPlayer?.release()

            // Crear y reproducir nuevo sonido
            mediaPlayer = MediaPlayer.create(context, soundRes)
            mediaPlayer?.setOnCompletionListener {
                it.release()
            }
            mediaPlayer?.start()

        } catch (e: Exception) {
            Toast.makeText(context, "Error reproduciendo sonido", Toast.LENGTH_SHORT).show()
        }
    }
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}