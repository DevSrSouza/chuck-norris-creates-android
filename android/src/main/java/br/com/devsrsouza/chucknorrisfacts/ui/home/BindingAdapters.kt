package br.com.devsrsouza.chucknorrisfacts.ui.home

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.model.UIState
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.model.mainCategoryOrNull
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import com.google.android.material.chip.Chip

@BindingAdapter("app:visibleIf")
fun View.visibleIf(value: Boolean) {
    visibility = if(value) View.VISIBLE else View.GONE
}

@BindingAdapter("app:factMainCategoryOrUncategorized")
fun Chip.factMainCategoryOrUncategorized(fact: Fact) {
    val category = fact.mainCategoryOrNull

    if(category != null) {
        setText(category)
    } else {
        setText(R.string.uncategorized_fact)
    }
}

@BindingAdapter("app:textStateError")
fun TextView.textStateError(state: UIState<*>) {
    when(state) {
        is UIState.NetworkNotAvailable -> setText(R.string.network_not_available)
        is UIState.Error -> setText(R.string.search_error)
        is UIState.NetworkClientError -> setText(state.message)
    }
}