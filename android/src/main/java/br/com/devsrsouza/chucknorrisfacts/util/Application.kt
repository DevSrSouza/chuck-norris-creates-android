package br.com.devsrsouza.chucknorrisfacts.util

import androidx.fragment.app.Fragment
import br.com.devsrsouza.chucknorrisfacts.ChuckNorrisFactsApplication

fun Fragment.requireChuckNorrisFactsApplication(): ChuckNorrisFactsApplication {
    return requireContext().applicationContext as ChuckNorrisFactsApplication
}