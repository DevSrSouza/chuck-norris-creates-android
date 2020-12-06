package br.com.devsrsouza.chucknorrisfact.util.share

import br.com.devsrsouza.chucknorrisfacts.util.share.ContentShare

class FakeContentShare : ContentShare {

    var textToBeShared: String? = null
        private set

    override fun shareText(text: String) {
        textToBeShared = text
    }

    fun reset() {
        textToBeShared = null
    }

}