package br.com.devsrsouza.chucknorrisfacts.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.databinding.FragmentHomeBinding
import br.com.devsrsouza.chucknorrisfacts.util.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}