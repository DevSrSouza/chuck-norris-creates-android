package br.com.devsrsouza.chucknorrisfacts.ui

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.model.UIState
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.model.mainCategoryOrNull
import com.google.android.material.chip.Chip

@BindingAdapter("app:visibleIf")
fun View.visibleIf(value: Boolean) {
    visibility = if(value) View.VISIBLE else View.GONE
}

@BindingAdapter("app:factMainCategoryOrUncategorized")
fun TextView.factMainCategoryOrUncategorized(fact: Fact) {
    val category = fact.mainCategoryOrNull

    if(category != null) {
        setText(category)
    } else {
        setText(R.string.uncategorized_fact)
    }
}

@BindingAdapter("app:textStateError")
fun TextView.textStateError(state: UIState<*>?) {
    when(state) {
        is UIState.NetworkNotAvailable -> setText(R.string.network_not_available)
        is UIState.Error -> setText(R.string.search_error)
        is UIState.NetworkClientError -> setText(state.message)
    }
}

@BindingAdapter("app:visibleWhenIsNotEmpty")
fun View.visibleIfContainsResult(state: UIState<List<Fact>>?) {
    visibleIf(state is UIState.Success && state.value.isNotEmpty())
}

@BindingAdapter("app:visibleWhenIsEmpty")
fun View.visibleIfDoesNotContainsResult(state: UIState<List<Fact>>?) {
    visibleIf(state is UIState.Success && state.value.isEmpty())
}

@BindingAdapter("app:factTextWithAdaptedSize")
fun TextView.factTextWithAdaptedSize(text: String) {
    val charForSmall = resources.getInteger(R.integer.fact_char_count_for_small_font)

    setTextSize(
        TypedValue.COMPLEX_UNIT_PX,
        resources.getDimension(
            if(text.length >= charForSmall)
                R.dimen.fact_small_font_size
            else R.dimen.fact_big_font_size
        )
    )
}