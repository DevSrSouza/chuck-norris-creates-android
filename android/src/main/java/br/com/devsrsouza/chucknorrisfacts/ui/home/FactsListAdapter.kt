package br.com.devsrsouza.chucknorrisfacts.ui.home

import br.com.devsrsouza.chucknorrisfacts.databinding.ChuckNorrisFactCardItemBinding
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.util.KotlinListAdapter

typealias FactListAdapter = KotlinListAdapter<Fact, ChuckNorrisFactCardItemBinding>

fun FactListAdapter(
    homeViewModel: HomeViewModel
): FactListAdapter = KotlinListAdapter(
        inflateBinding = { inflater, viewGroup, _ -> ChuckNorrisFactCardItemBinding.inflate(inflater, viewGroup, false) },
        getViewFromBinding = { binding -> binding.root },
        onBind = { fact, binding, _ ->
            binding.fact = fact
            binding.homeViewModel = homeViewModel
            binding.executePendingBindings()
        },
        isDataClassItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }
)