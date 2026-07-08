package com.cowlsly.simplesettings.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View

/**
 * Short UI feedback for page-turner buttons — click tone + haptic on press.
 */
class SoundEffects(context: Context) {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 60)

    fun playPageTurn() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 80)
    }

    fun playPress() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
    }

    fun playDenied() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 120)
    }

    fun hapticPress(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
    }

    fun release() {
        toneGenerator.release()
    }
}